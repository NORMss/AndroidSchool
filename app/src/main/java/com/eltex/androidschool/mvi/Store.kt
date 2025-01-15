package com.eltex.androidschool.mvi

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

class Store<State, Message, Effect>(
    private val reducer: Reducer<State, Message, Effect>,
    private val effectHandler: EffectHandler<Effect, Message>,
    private val initMessages: Set<Message> = emptySet(),
    initState: State,
) {
    val state: StateFlow<State>
        field = MutableStateFlow(initState)

    private val messages = MutableSharedFlow<Message>(extraBufferCapacity = 64)
    private val effects = MutableSharedFlow<Effect>(extraBufferCapacity = 64)

    fun accept(message: Message) {
        messages.tryEmit(message)
    }

    suspend fun connect() = coroutineScope {
        launch {
            effectHandler.connect(effects)
                .collect(messages::tryEmit)
        }
        launch {
            listOf(
                initMessages.asFlow(),
                messages,
            )
                .merge()
                .map { message ->
                    reducer.reduce(state.value, message)
                }
                .collect {
                    state.value = it.newState
                    it.effects.forEach(effects::tryEmit)
                }
        }
    }
}