package io.github.magonxesp.criteria

import io.github.serpro69.kfaker.faker
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.ZoneId
import kotlin.random.Random

@Entity
class Book(
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var id: Long? = null,
	@Column
	var title: String,
	@Column
	var isPublished: Boolean,
	@ManyToOne
	@JoinColumn(name = "author_id")
	var author: BookAuthor,
)

@Entity
class BookGenres(
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var id: Long? = null,
	@Column
	var genre: String,
	@Column
	var bookId: Long
)

@Entity
class BookAuthor(
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var id: Long? = null,
	@Column
	var name: String,
	@Column
	val birthDate: Instant,
	@OneToMany(mappedBy = "author")
	val books: MutableList<Book> = mutableListOf(),
)

@Repository
interface BookRepository : CrudRepository<Book, Long>, JpaSpecificationExecutor<Book>

@Repository
interface BookAuthorRepository : CrudRepository<BookAuthor, Long>, JpaSpecificationExecutor<BookAuthor>

@Repository
interface BookGenreRepository : CrudRepository<BookGenres, Long>, JpaSpecificationExecutor<BookGenres>

private val random = faker { }

@Service
class TestEntityFactory(
	private val bookRepository: BookRepository,
	private val bookAuthorRepository: BookAuthorRepository,
	private val bookGenreRepository: BookGenreRepository
) {
	@Transactional
	fun createBookAuthor(
		name: String? = null,
		birthDate: Instant? = null
	) = BookAuthor(
		name = name ?: random.book.author(),
		birthDate = birthDate ?: random.person.birthDate(Random.nextLong(1, 99)).atStartOfDay(ZoneId.systemDefault()).toInstant(),
	).let { bookAuthorRepository.save(it) }

	@Transactional
	fun createBook(
		title: String? = null,
		isPublished: Boolean? = null,
		author: BookAuthor? = null
	) = Book(
		title = title ?: random.book.title(),
		isPublished = isPublished ?: random.random.nextBoolean(),
		author = author ?: createBookAuthor()
	).let { bookRepository.save(it) }

	@Transactional
	fun createBookGenre(
		name: String? = null,
		book: Book
	) = BookGenres(
		genre = name ?: random.book.genre(),
		bookId = book.id!!
	).let { bookGenreRepository.save(it) }
}

