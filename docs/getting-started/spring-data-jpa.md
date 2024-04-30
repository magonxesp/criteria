# Spring Data JPA

Suppose you have the Book entity using Spring Boot Data JPA.

<pre class="language-kotlin"><code class="lang-kotlin"><strong>@Entity
</strong><strong>class Book(
</strong><strong>    @Id
</strong>    val id: Int,
    val title: String,
    val author: String
)
</code></pre>

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

To perform a query using this criteria you will need to implement on our JPA Repository the `JpaSpecificationExecutor<T>` interface for allow to execute queries through the spring `Specification<T>` interface.

```kotlin
@Repository
interface BookRepository : CrudRepository<Book, Long>, JpaSpecificationExecutor<Book>
```

Now we can autowire `BookRepository` and execute the query with our `criteria` instance.

```kotlin
@Autowired
lateinit var bookRepository: BookRepository

val adapter = CriteriaJPASpecificationAdapter()
val result = adapter.apply(criteria, bookRepository)
// The result variable contains the collection of Book instances
```

### How to add joins on criteria <a href="#how-to-add-joins-on-criteria" id="how-to-add-joins-on-criteria"></a>

Now imagine you have the `BookAuthor` entity that is related with the `Book` entity with a `OneToMany` relationship

```kotlin
@Entity
class Book(
    //...
    @ManyToOne
    @JoinColumn(name = "author_id")
    var author: BookAuthor,
)

@Entity
class BookAuthor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var name: String,
    val birthDate: Instant,

    @OneToMany(mappedBy = "author")
    val books: MutableList<Book> = mutableListOf(),
)
```

We can add a join from the `Book` entity to `BookAuthor` entity for create a criteria that filters by the author name. For this we need to create a join map.

The join map is simple, you need to define a `Map` with a key that is the relation name and as value the join definition.

```kotlin
mapOf(
    "<relation name>" to join<Book, BookAuthor>("<property that has the relation on Book entity>", JoinType.INNER) // The join definition
)
```

As the last example we need to call the `adapt` method from `CriteriaJPASpecificationAdapter` and add the join map on the `adapt` method

```kotlin
@Autowired
lateinit var bookRepository: BookRepository

val criteria = Criteria(
    filters = listOf(
        // We use the relation notation as filter name, first the relation name defined
        // as the key on the join map and the related field name before the "."
        // NOTE: the related field name is a property name of BookAuthor entity
        Filter("author.name", "Svetlana Isakova", FilterOperator.EQUALS)
    )
)

val adapter = CriteriaJPASpecificationAdapter()
val result = adapter.apply(criteria, bookRepository) {
    mapOf(
        "author" to join<Book, BookAuthor>("author", JoinType.INNER)
    )
}
// The result variable contains the collection of filtered Book instances
```

If you have a `FieldMap` you can add it into the `CriteriaJPASpecificationAdapter` constructor.

```kotlin
val fieldMap = mapOf(
    // this will indicate the field author_name points to author.name related field
    "author_name" to "author.name"
)
// ...
val adapter = CriteriaJPASpecificationAdapter(fieldMap)
val result = adapter.apply(criteria, bookRepository)
```
