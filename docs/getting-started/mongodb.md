# MongoDB

Suppose you have the Book entity.

```kotlin
@Serializable
class Book(
    val id: String,
    val title: String,
    val author: String,
    val genre: String
)
```

And you need to filter by the author and the title. You can create an instance of the Criteria class and define the filters with the values.

```kotlin
val criteria = Criteria(
    filters = listOf(
        Filter("title", "Kotlin", FilterOperator.CONTAINS),
        Filter("author", "Svetlana Isakova", FilterOperator.EQUALS)
    )
)
```

This instance of the Criteria class will filter by books with title that contains the "Kotlin" word and by the author "Svetlana Isakova"

To perform a query using this criteria you will need to create a MongoDB connection with the [Koltin coroutine MongoDB driver](https://www.mongodb.com/docs/drivers/kotlin/coroutine/current/).

```kotlin
val client = MongoClient.create("mongodb://root:root@localhost:27017")
val database = client.getDatabase("book_store")
```

Get the _books_ collection and apply the criteria to a `FindFlow` with a `CriteriaMongoDbFindFlowAdapter` instance.

```kotlin
val collection = database.getCollection<Book>("books")
val findFlow = collection.find()
CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)
val books = findFlow.toList() // get all query results
```

If you have a `FieldMap` you can add it into the `CriteriaMongoDbFindFlowAdapter` constructor.

```kotlin
val fieldMap = mapOf(
    // this will indicate the field author_name points to author field
    "author_name" to "author"
)
// ...
CriteriaMongoDbFindFlowAdapter(fieldMap).apply(criteria, findFlow)
```
