# Project AndroidSchool 

**Part 28-mvi**

## **Warning**

Before building the project, add the secret key for accessing the API to [secrets.properties](./secrets.properties) in the root of the project

```kotlin
API_KEY = "your-key"
```

## Optimization Demo


https://github.com/user-attachments/assets/6cc81de2-41ed-4ab4-abbf-415494053ef7



## MVI (Model-View-Intent)
This project is an Android application implementing the MVI (Model-View-Intent) architectural pattern using the following main components:
* State: Current screen/feature state
<img width="1333" alt="MVI_STATE_ALPHA" src="https://github.com/user-attachments/assets/15c860d0-7069-494a-b475-d5ebdd31f9a3" />

* Reducer: The logic for changing the application state based on incoming messages(intents).
* Effect: Command to Model
* Message(Intent): user/server intent

### Example of working with Reducer

```kotlin
fun reducer(state: State, message: Message): State {
    return when (message) {
        is EventMessage.NextPageLoaded -> ReducerResult(
            when (val messageResult = message.result) {
                is Either.Left -> {
                    old.copy(
                        status = EventStatus.NextPageError(messageResult.value),
                    )
                }

                is Either.Right -> {
                    val updatedPosts = old.events + messageResult.value.map(mapper::map)
                    old.copy(
                        events = updatedPosts,
                        status = EventStatus.Idle,
                    )
                }
            }
        )

        EventMessage.LoadNextPage -> ReducerResult(
            old.copy(
                status = EventStatus.NextPageLoading
            ),
            EventEffect.LoadNextPage(old.events.last().id, PAGE_SIZE),
        )
    }
}
```
