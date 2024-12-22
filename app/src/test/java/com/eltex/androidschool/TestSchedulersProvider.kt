package com.eltex.androidschool

import com.eltex.androidschool.domain.rx.SchedulersProvider
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

object TestSchedulersProvider : SchedulersProvider {
    override val computation: Scheduler = Schedulers.trampoline()
    override val io: Scheduler = Schedulers.trampoline()
    override val mainThread: Scheduler = Schedulers.trampoline()
}