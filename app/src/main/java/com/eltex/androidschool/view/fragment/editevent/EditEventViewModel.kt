package com.eltex.androidschool.view.fragment.editevent

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.repository.EventRepository
import com.eltex.androidschool.view.common.Status
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class EditEventViewModel(
    private val eventRepository: EventRepository,
    eventId: Long,
) : ViewModel() {
    val disposable = CompositeDisposable()

    val state: StateFlow<EditEventState>
        field = MutableStateFlow(EditEventState())

    init {
        getEvent(eventId)
    }

    fun setAttachment(uri: Uri) {
        state.update {
            it.copy(
                event = state.value.event.copy(
                    attachment = Attachment(
                        url = uri.toString(),
                        type = AttachmentType.IMAGE,
                    )
                )
            )
        }
    }

    fun setText(text: String) {
        state.update {
            it.copy(
                event = state.value.event.copy(
                    content = text,
                )
            )
        }
    }

    fun setLink(text: String) {
        state.update {
            it.copy(
                event = state.value.event.copy(
                    link = text,
                )
            )
        }
    }

    private fun getEvent(id: Long) {
        eventRepository.getEvents().subscribeBy(
            onSuccess = { event ->
                state.update {
                    it.copy(
                        event = event.filter { it.id == id }.first(),
                        status = Status.Idle,
                    )
                }

            },
            onError = { throwable ->
                state.update {
                    it.copy(
                        status = Status.Error(throwable),
                    )
                }

            }
        ).addTo(disposable)
    }

    fun editEvent() {
        eventRepository.saveEvent(
            event = state.value.event,
        ).subscribeBy(
            onSuccess = { event ->
                state.update {
                    it.copy(
                        event = event,
                        status = Status.Idle,
                    )
                }
            },
            onError = { throwable ->
                state.update {
                    it.copy(
                        status = Status.Error(throwable),
                    )
                }
            },
        )
            .addTo(disposable)
    }

    override fun onCleared() {
        disposable.dispose()
    }
}