package com.eltex.androidschool.mvi

class Store<State, Message, Effect>(
    private val reducer: Reducer<State, Message, Effect>,
    private val effectHandler: EffectHandler<Effect, Message>,
    private val initMessages: Set<Message> = emptySet(),
    initState: State,
) {
}