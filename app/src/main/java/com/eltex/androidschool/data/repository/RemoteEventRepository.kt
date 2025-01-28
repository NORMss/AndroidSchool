package com.eltex.androidschool.data.repository

import com.eltex.androidschool.data.remote.api.EventApi
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.repository.EventRepository

/**
 * [RemoteEventRepository] is a concrete implementation of [EventRepository] that fetches event data from a remote source.
 * It utilizes the [EventApi] to perform network requests and retrieve event-related information.
 *
 * @property eventApi The [EventApi] instance used to communicate with the remote event data source.
 */
class RemoteEventRepository(
    private val eventApi: EventApi,
) : EventRepository {
    /**
     * Retrieves a list of events from the remote data source.
     *
     * This function suspends execution while fetching the event data from the
     * `eventApi`. It returns a list of [Event] objects representing the events
     * retrieved.
     *
     * @return A list of [Event] objects representing the fetched events.
     * @throws Exception if there's an issue during the network request or data processing.
     */
    override suspend fun getEvents(): List<Event> {
        return eventApi.getEvents()
    }

    /**
     * Retrieves a list of events that are newer than the event with the given ID.
     *
     * This function fetches events from the remote data source (likely an API) that have an ID
     * greater than the provided [id].  It's assumed that event IDs are monotonically increasing,
     * meaning that a newer event will always have a higher ID than an older event.
     *
     * @param id The ID of the event to compare against. Events with IDs greater than this value will be returned.
     * @return A list of [Event] objects that are newer than the event with the given ID.
     * @throws Exception if there's an error during the network request or data parsing. Specific exception types depend on the underlying `eventApi`.
     * @throws retrofit2.HttpException if the server returns an error HTTP code.
     * @throws java.io.IOException if a network or I/O error occurs.
     */
    override suspend fun getEventsNewer(id: Long): List<Event> {
        return eventApi.getEventsNewer(id)
    }

    /**
     * Retrieves a list of events that occurred before a specified event ID.
     *
     * This function fetches a list of `Event` objects from the underlying data source (e.g., an API or database)
     * that have an ID less than the provided `id`. The number of events returned is limited by the `count` parameter.
     *
     * @param id The ID of the event to use as the upper bound for the event retrieval. Only events with IDs *less than* this value will be returned.
     * @param count The maximum number of events to retrieve.
     * @return A list of `Event` objects that occurred before the event with the specified ID, up to the given count.
     * @throws Exception If there is an error during the retrieval process (e.g., network error, data parsing error). The specific exception type will depend on the implementation of `eventApi.getEventsBefore`.
     */
    override suspend fun getEventsBefore(
        id: Long,
        count: Int
    ): List<Event> {
        return eventApi.getEventsBefore(id, count)
    }

    /**
     * Retrieves a list of events that occurred after a specific event ID.
     *
     * @param id The ID of the event to start retrieving events after.
     *           Events with IDs greater than this will be included in the result.
     * @param count The maximum number of events to retrieve.
     * @return A list of [Event] objects that occurred after the specified event ID,
     *         up to the specified count.
     * @throws Exception if there is an error during the API call.
     */
    override suspend fun getEventsAfter(
        id: Long,
        count: Int
    ): List<Event> {
        return eventApi.getEventsAfter(id, count)
    }

    /**
     * Retrieves the latest events from the data source.
     *
     * This function fetches a specified number of the most recent events.
     *
     * @param count The maximum number of events to retrieve. Must be a positive integer.
     * @return A list of [Event] objects representing the latest events. The list may be empty if no events are found,
     *         but will not be null. The order of the events in the list is typically from most recent to least recent.
     * @throws Exception if an error occurs during the API call or data retrieval. Specific exceptions may vary
     *         depending on the implementation of `eventApi.getEventsLatest()`.
     * @throws IllegalArgumentException if the provided `count` is not a positive integer.
     */
    override suspend fun getEventsLatest(count: Int): List<Event> {
        return eventApi.getEventsLatest(count)
    }

    /**
     * Toggles the like status of an event with the specified ID.
     *
     * This function sends a request to the server to either like or unlike an event based on the `isLiked` parameter.
     * If `isLiked` is true, it will unlike the event; if `isLiked` is false, it will like the event.
     *
     * @param id The unique identifier of the event to be liked or unliked.
     * @param isLiked A boolean indicating the desired state:
     *                - `true`:  indicates that the event should be unliked.
     *                - `false`: indicates that the event should be liked.
     * @return An [Event] object representing the updated event state after the like/unlike operation.
     * @throws Exception if there's an error during the API call.
     */
    override suspend fun likeById(
        id: Long,
        isLiked: Boolean,
    ): Event {
        return when (isLiked) {
            true -> eventApi.unlikeById(id)
            false -> eventApi.likeById(id)
        }
    }

    /**
     * Repository function to handle user participation in an event by its ID.
     *
     * This function allows a user to either participate in or unparticipate from an event
     * based on the provided `isParticipated` flag. It interacts with the `EventApi`
     * to perform the necessary actions.
     *
     * @param id The ID of the event to participate in or unparticipate from.
     * @param isParticipated A boolean flag indicating the desired action:
     *                       - `true`: The user wants to unparticipate from the event.
     *                       - `false`: The user wants to participate in the event.
     * @return An [Event] object representing the event after the participation status has been updated.
     * @throws Exception if there's an error during the network request. The specific exception
     *                   depends on the underlying `EventApi` implementation.
     */
    override suspend fun participateById(
        id: Long,
        isParticipated: Boolean,
    ): Event {
        return when (isParticipated) {
            true -> eventApi.unparticipantById(id)
            false -> eventApi.participantById(id)
        }
    }

    /**
     * Saves an event to the backend.
     *
     * This function takes an [Event] object and sends it to the server via the
     * [eventApi] for persistence. It then returns the possibly modified [Event]
     * object as returned by the server. This modification may include updating
     * the event with an ID generated by the server.
     *
     * @param event The [Event] object to be saved.
     * @return The [Event] object as returned by the server after successful saving.
     * @throws Exception if any error occurs during the saving process, such as network issues or server errors.
     */
    override suspend fun saveEvent(event: Event): Event {
        return eventApi.save(event)
    }

    /**
     * Deletes an event by its unique identifier.
     *
     * This function utilizes the `eventApi` to perform the deletion operation.
     * It is a suspend function, meaning it can be paused and resumed,
     * making it suitable for asynchronous operations, such as network calls.
     *
     * @param id The unique identifier of the event to be deleted.
     * @throws Exception if there is an error during the deletion process. The specific exception
     *                   will depend on the underlying implementation of `eventApi`.
     */
    override suspend fun deleteById(id: Long) {
        return eventApi.deleteById(id)
    }
}