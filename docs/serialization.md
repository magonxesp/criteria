# Serialization

Serializing the criteria instance can be useful for transferring the criteria data.

There are many use cases:

* Appending criteria on a URL query string parameter.
* Writing criteria to a file.
* Saving criteria to the database.

### Base 64

The criteria can be serialized to Base64 format.

```kotlin
import io.github.magonxesp.criteria.infrastructure.serialization.encodeToBase64

val criteria = criteria {
   filter("author_name", "Svetlana Isakova", FilterOperator.EQUALS)
}

// serialize to Base64
val base64 = criteria.encodeToBase64()
```

The resulting Base64 representation will be as follows:

```
eyJmaWx0ZXJzIjpbeyJmaWVsZCI6ImF1dGhvcl9uYW1lIiwidmFsdWUiOiJTdmV0bGFuYSBJc2Frb3ZhIiwib3BlcmF0b3IiOiI9In1dLCJvcmRlckJ5IjpbXSwicGFnaW5hdGlvbiI6eyJwYWdlIjoxLCJzaXplIjpudWxsfX0=
```

Also, a Base64 encoded criteria can be deserialized.

```kotlin
import io.github.magonxesp.criteria.infrastructure.serialization.decodeCriteriaFromBase64

val criteria = "<base64 string>".decodeCriteriaFromBase64()
```

### JSON

The criteria can be serialized to JSON format, which serves as the initial serialization method before Base64 encoding.

```kotlin
import io.github.magonxesp.criteria.infrastructure.serialization.serializeToJson

val criteria = criteria {
   filter("author_name", "Svetlana Isakova", FilterOperator.EQUALS)
}

// serialize to JSON
val json = criteria.serializeToJson()
```

The resulting JSON representation will be as follows:

```json
{
  "filters": [
    {
      "field": "author_name",
      "value": "Svetlana Isakova",
      "operator": "="
    }
  ],
  "orderBy": [],
  "pagination": {
    "page": 1,
    "size": null
  }
}
```

And for deserialize the JSON-encoded criteria, we can use the `deserializeCriteriaFromJson()` extension function.

```kotlin
import io.github.magonxesp.criteria.infrastructure.serialization.deserializeCriteriaFromJson

val criteria = "<json string>".deserializeCriteriaFromJson()
```
