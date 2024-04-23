# Introduccion

El patron Criteria es un patron de diseño que nos ayuda a desacoplarnos de las consultas que hacemos en los motores de base de datos, drivers o librerias.

Esta implementacion del Criteria dispone de las siguientes implementaciones:

* [Koltin coroutine MongoDB driver](https://www.mongodb.com/docs/drivers/kotlin/coroutine/current/)
* [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

### Instalacion

Añade la dependencia del artefacto `criteria-core` en tu `build.gradle.kts`

```kotlin
implementation("io.github.magonxesp:criteria-core:0.0.3")
```

Si usas **Spring Boot** y **Spring Data JPA** tienes que añadir a parte el artefacto de spring-boot

```kotlin
implementation("io.github.magonxesp:criteria-spring-boot:0.0.3")
```
