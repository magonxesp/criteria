# Primeros pasos

### Uso basico

Imagina que tienes una base de datos relacional donde guardas libros. Y existe la tabla books con la siguiente estructura.

| Column        | Type     |
| ------------- | -------- |
| title         | String   |
| author        | String   |
| genre         | String   |
| release\_date | DateTime |

Para poder filtrar por libros que son del autor "Svetlana Isakova" tenemos que crear una instancia de `Criteria` añadiendo un filtro por el campo `author`

```kotlin
val criteria = criteria {
    filter("author", "Svetlana Isakova", FilterOperator.EQUALS)
}
```

Si quisieramos ordenar los resultados para que aparezcan primero los libros que se han publicado mas recientemente tendriamos que añadir un criterio de ordenacion.

```kotlin
val criteria = criteria {
    // ...
    orderBy("release_date", Order.DESC)
}
```

Para ejecutar una consulta compuesta con nuestro criteria necesitamos un adaptador que nos ayude a traducir lo que hemos definido a nuestro motor de base de datos preferido o ORM preferido.

### Siguientes pasos

* [Adaptar criteria a MongoDB](mongodb.md)
* [Adaptar criteria a Spring Data JPA](spring-data-jpa.md)
