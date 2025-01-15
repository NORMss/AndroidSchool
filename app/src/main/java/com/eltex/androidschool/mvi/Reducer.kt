package com.eltex.androidschool.mvi

interface Reducer<State, Message, Effect> {
    fun reduce(old: State, message: Message): ReducerResult<State, Effect>
}