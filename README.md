# Project AndroidSchool 

**Part 21-room**
## Demo Program
<details>
  <summary>Video</summary>

  https://github.com/user-attachments/assets/c3eb3f1e-e996-4736-9428-16b75ba26896
  
</details>

## Database Migration in Room

This section demonstrates the step-by-step process of evolving the `EVENT_TABLE` in Room through database migrations.

---

### **Version 1: Initial Structure**

The initial structure of the `EVENT_TABLE` in version 1:

| **event_table**  |  
|-------------------|  
| `id`             |  
| `authorId`       |  
| ...              |  
| `users`          |  

---

### **Version 2: Adding the `views` Column**

To add the `views` column with a default value of `0`:

```kotlin
@ColumnInfo("views", defaultValue = "0")
val views: Int
```

The migration was handled using **autoMigration**:

[.../data/local/AppDb](https://github.com/NORMss/AndroidSchool/blob/5cdaf5a0b15fb0081982a1ed15745c2b3d950707/app/src/main/java/com/eltex/androidschool/data/local/AppDb.kt#L28)

```kotlin
autoMigrations = [
    AutoMigration(
        from = 1, to = 2
    )
]
```

The updated structure of the `EVENT_TABLE` in version 2:

| **event_table**  |  
|-------------------|  
| `id`             |  
| `authorId`       |  
| ...              |  
| `users`          |  
| `views`          |  

---

### **Version 3: Removing the `views` Column**

To remove the `views` column, a custom migration specification was created:

```kotlin
@DeleteColumn.Entries(
    DeleteColumn(tableName = EVENT_TABLE, columnName = "views")
)
class DeleteViewFromEvent : AutoMigrationSpec
```

The migration was configured as follows:

[.../data/local/AppDb](https://github.com/NORMss/AndroidSchool/blob/5cdaf5a0b15fb0081982a1ed15745c2b3d950707/app/src/main/java/com/eltex/androidschool/data/local/AppDb.kt#L31)

```kotlin
autoMigrations = [
    AutoMigration(
        from = 2, to = 3,
        spec = AppDb.DeleteViewFromEvent::class
    )
]
```

The final structure of the `EVENT_TABLE` in version 3:

| **event_table**  |  
|-------------------|  
| `id`             |  
| `authorId`       |  
| ...              |  
| `users`          |  


## Architecture

![image](https://github.com/user-attachments/assets/941b8f28-2965-4392-903d-8bbdd7016177)

*Figure 1. Diagram of Room library architecture.*
