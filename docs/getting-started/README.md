# Getting started

Suppose you have a relational database for saving books, and there exists the Books table with the following schema:

| Column        | Type     |
| ------------- | -------- |
| title         | String   |
| author        | String   |
| genre         | String   |
| release\_date | DateTime |

To filter books by the author _Svetlana Isakova_ we need to create a `Criteria` instance adding a filter by the `author` field.

```kotlin
val criteria = criteria {
    filter("author", "Svetlana Isakova", FilterOperator.EQUALS)
}
```

If we want to sort the result to get the most recent released books we can add an `orderBy` clause.

```kotlin
val criteria = criteria {
    // ...
    orderBy("release_date", Order.DESC)
}
```

Also we can define a `FieldMap` for hide the real field names by other one. For example, we can filter by `author` field referencing it as `author_name`. (See more about `FieldMap` on [next steps](./#next-steps))

```kotlin
val fieldMap = mapOf(
    // this will indicate the field author_name points to author field
    "author_name" to "author"
)

val criteria = criteria {
   filter("author_name", "Svetlana Isakova", FilterOperator.EQUALS)
}
```

To run a query using our `Criteria` we need to adapt it to our favorite database driver or ORM.

### Next steps

* [Adaptar criteria a MongoDB](mongodb.md)
* [Adaptar criteria a Spring Data JPA](spring-data-jpa.md)
