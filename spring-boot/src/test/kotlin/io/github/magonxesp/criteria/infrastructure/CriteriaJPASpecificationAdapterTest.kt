package io.github.magonxesp.criteria.infrastructure

import io.github.magonxesp.criteria.*
import io.github.magonxesp.criteria.domain.FilterOperator
import io.github.magonxesp.criteria.domain.criteria
import io.github.magonxesp.criteria.infrastructure.map.fieldMapOf
import io.github.magonxesp.criteria.infrastructure.spring.CriteriaJPASpecificationAdapter
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import jakarta.persistence.criteria.JoinType
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.Ignore
import kotlin.test.Test

class CriteriaJPASpecificationAdapterTest : SpringBootTestCase() {
	@Autowired
	lateinit var testEntityFactory: TestEntityFactory

	@Autowired
	lateinit var bookRepository: BookRepository

	@Test
	fun `it should find books by book author making a inner join`() {
		val author = testEntityFactory.createBookAuthor()
		val books = (0..100).map { testEntityFactory.createBook(author = author) }

		val criteria = criteria {
			filter("authorName", author.name, FilterOperator.EQUALS)
		}

		val fieldMap = fieldMapOf(
			"authorName" to "author.name",
		)

		val adapter = CriteriaJPASpecificationAdapter(fieldMap)
		val found = adapter.apply(criteria, bookRepository) {
			mapOf(
				"author" to join<Book, BookAuthor>("author", JoinType.INNER)
			)
		}

		val booksIds = books.map { it.id }
		val foundIds = found.items.map { it.id }

		foundIds shouldContainAll booksIds
	}

	@Test
	@Ignore("Spring Boot criteria API don't support this approach")
	fun `it should find book by genre making a inner join without join annotations`() {
		val book = testEntityFactory.createBook()
		val genres = (0..100).map { testEntityFactory.createBookGenre(book = book) }
		val genre = genres.random()

		val criteria = criteria {
			filter("bookGenre", genre.genre, FilterOperator.EQUALS)
		}

		val fieldMap = fieldMapOf(
			"bookGenre" to "genres.genre",
		)

		val adapter = CriteriaJPASpecificationAdapter(fieldMap)
		val found = adapter.apply(criteria, bookRepository) {
			mapOf(
				"genres" to join<Book, BookGenres>("bookId", JoinType.INNER)
			)
		}

		val foundIds = found.items.map { it.id }

		foundIds shouldContain book.id
	}
}
