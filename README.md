# Project AndroidSchool 

**Part 19-files**
## Demo Program
<details>
  <summary>Video</summary>

  https://github.com/user-attachments/assets/a0e0e724-2cb9-47eb-9191-c473dc7421cb

</details>

## Example of application directory and files
```
com.eltex.androidschool
├───cache
│   └───image_cache
│           50...a0.0
│           50...a0.1
│           journal
│
├───code_cache
└───files
    │   events_file.json
    │   profileInstalled
    │
    └───datastore
            event_config.preferences_pb
```
event_config.preferences_pb
```
1 {
  1: "next_id"
  2 {
    4: 3
  }
}
```

events_file.json
```
[
    {
        "id": 1,
        "authorId": 1000,
        "author": "Sergey Bezborodov",
        "authorJob": "Junior Android Developer",
        "authorAvatar": "https://avatars.githubusercontent.com/u/47896309?v=4",
        "content": "Присоединяйтесь к встрече мобильных разработчиков. Будем дискутировать на тему развития KMP за прошедший год 2024.",
        "datetime": "2024-11-22T16:05:38.121717Z",
        "published": "2024-11-22T16:05:38.122037Z",
        "coords": {
            "lat": 54.9833,
            "long": 82.8964
        },
        "type": "ONLINE",
        "likeOwnerIds": [],
        "likedByMe": false,
        "speakerIds": [],
        "participantsIds": [],
        "participatedByMe": true,
        "attachment": null,
        "link": "https://github.com/NORMss",
        "users": []
    },
    {
        "id": 3,
        "authorId": 1000,
        "author": "Sergey Bezborodov",
        "authorJob": "Junior Android Developer",
        "authorAvatar": "https://avatars.githubusercontent.com/u/47896309?v=4",
        "content": "В конце года, подведём итоги на офлайн всетрче ",
        "datetime": "2024-11-22T16:06:02.855007Z",
        "published": "2024-11-22T16:06:02.855192Z",
        "coords": {
            "lat": 54.9833,
            "long": 82.8964
        },
        "type": "ONLINE",
        "likeOwnerIds": [],
        "likedByMe": true,
        "speakerIds": [],
        "participantsIds": [],
        "participatedByMe": false,
        "attachment": null,
        "link": "https://github.com/NORMss",
        "users": []
    }
]
```
