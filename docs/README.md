# Introduction

The Criteria design pattern helps us to maintain a single form to perform search queries on our database.

It helps to implement the repository design pattern, avoiding to create a bunch of methods to create the same query with different filters.

This implementation is adapted for the following drivers or ORMs:

* [Koltin coroutine MongoDB driver](https://www.mongodb.com/docs/drivers/kotlin/coroutine/current/)
* [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

### Instalation

Add the `criteria-core` dependency on your `build.gradle.kts`.

```kotlin
implementation("io.github.magonxesp:criteria-core:0.1.0")
```

For **Spring Boot and** **Spring Data JPA** add the spring-boot dependency.

```kotlin
implementation("io.github.magonxesp:criteria-spring-boot:0.1.0")
```
