# Project AndroidSchool 

**Part 30-photo**

## **Warning**

Before building the project, add the secret key for accessing the API to ```secrets.properties``` in the root of the project

```kotlin
API_KEY = "your-key"
USER_TOKEN = "your-token-authorization"
```
## Demo Program
<details>
  <summary>Video</summary>
  

https://github.com/user-attachments/assets/e3615828-6104-4c68-9638-7f6285a65d4f


</details>

## Logic of photo creation and display
The [multipart/form-data](https://en.wikipedia.org/wiki/MIME#form-data) content type was used to upload the photo to the server.

Created an interface for uploading a file with the `@Multipart` annotation. After successful upload, the file `URL` is returned
<details>
  <summary>api code example</summary>
  
```kotlin
@Multipart
@POST("api/media")
suspend fun upload(@Part file: MultipartBody.Part): Media
```

</details>

Adapt the repository to upload a file to the server and receive a link to the source for further retrieval of the uploaded image

<details>
  <summary>repository code example</summary>
  
```kotlin
override suspend fun saveEvent(
        id: Long,
        content: String,
        link: String?,
        datetime: Instant,
        fileModel: FileModel?
    ): Event {
        val attachment = fileModel?.let {
            val media = upload(it)
            Attachment(media.url, it.type)
        }

        val event = Event(
            id = id,
            content = content,
            datetime = datetime,
            attachment = attachment,
            link = link,
        )

        return eventApi.save(event)
    }

private suspend fun upload(fileModel: FileModel): Media {
        return mediaApi.upload(
            MultipartBody.Part.createFormData(
                "file",
                "file",
                withContext(Dispatchers.IO) {
                    requireNotNull(contentResolver.openInputStream(fileModel.uri)).use {
                        it.readBytes()
                    }
                        .toRequestBody()
                },
            )
        )
    }
```

</details>

To load an image from the server, we use the [Coil](https://coil-kt.github.io/coil/) library.

<details>
  <summary>viewholder code example</summary>

```kotlin
binding.contentImage.load(event.attachment.url) {
                    crossfade(true)
                    placeholder(R.drawable.image_loading)
                    error(R.drawable.image_error)
                }
```

</details>
