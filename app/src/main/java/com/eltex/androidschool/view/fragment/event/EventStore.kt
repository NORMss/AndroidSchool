package com.eltex.androidschool.view.fragment.event

import com.eltex.androidschool.mvi.Store

typealias EventStore = Store<EventState, EventMessage, EventEffect>