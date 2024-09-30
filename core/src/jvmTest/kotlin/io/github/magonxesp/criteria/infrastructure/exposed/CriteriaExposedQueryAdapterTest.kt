package io.github.magonxesp.criteria.infrastructure.exposed

import io.github.magonxesp.criteria.IntegrationTestCase
import io.github.magonxesp.criteria.domain.FilterOperator
import io.github.magonxesp.criteria.domain.Order
import io.github.magonxesp.criteria.domain.criteria
import io.github.magonxesp.criteria.infrastructure.mongodb.BookDocument
import io.github.magonxesp.criteria.random
import io.kotest.common.runBlocking
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.random.Random

abstract class CriteriaExposedQueryAdapterTest : IntegrationTestCase() {
	lateinit var db: Database

	val books = (0..50).map {
		BookEntity(
			id = UUID.randomUUID().toString(),
			numericalId = it,
			title = random().book.title(),
			author = random().book.author(),
			stock = Random.nextInt(1, 20)
		)
	}

	val book = books.first()

	override fun onContainersStart(): Unit = runBlocking {
		db = setupDatabaseConnection()
		transaction(db) {
			SchemaUtils.create(BookTable)
			books.forEach { it.insert() }
		}
	}

	abstract fun setupDatabaseConnection(): Database

	private val columns = mapOf(
		BookTable::id.name to BookTable.id as Column<Any>,
		BookTable::title.name to BookTable.title as Column<Any>,
		BookTable::author.name to BookTable.author as Column<Any>,
		BookTable::stock.name to BookTable.stock as Column<Any>,
		BookTable::numericalId.name to BookTable.numericalId as Column<Any>,
	)

	@Test
	fun `it should find by exact title`() = transaction(db) {
		val criteria = criteria {
			filter(BookDocument::title.name, book.title, FilterOperator.EQUALS)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldContain book
	}

	@Test
	fun `it should not find by exact title`() = transaction(db) {
		val criteria = criteria {
			filter(BookDocument::title.name, book.title, FilterOperator.NOT_EQUALS)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldNotContain book
	}

	@Test
	fun `it should find by stock is more than`() = transaction(db) {
		val criteria = criteria {
			filter(BookDocument::stock.name, book.stock - 1, FilterOperator.GREATER_THAN)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldContain book
	}

	@Test
	fun `it should not find by stock is more than`() = transaction(db) {
		val criteria = criteria {
			filter(BookDocument::stock.name, book.stock, FilterOperator.GREATER_THAN)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldNotContain book
	}

	@Test
	fun `it should find by stock is more than or equals`() = transaction(db) {
		val criteria = criteria {
			filter(BookDocument::stock.name, book.stock, FilterOperator.GREATER_THAN_EQUALS)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldContain book
	}

	@Test
	fun `it should not find by stock is more than or equals`() = transaction(db) {
		val criteria = criteria {
			filter(BookDocument::stock.name, book.stock + 1, FilterOperator.GREATER_THAN_EQUALS)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldNotContain book
	}


	@Test
	fun `it should find by stock is less than`() = transaction(db) {
		val criteria = criteria {
			filter(BookDocument::stock.name, book.stock + 1, FilterOperator.LESS_THAN)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldContain book
	}

	@Test
	fun `it should not find by stock is less than`() = transaction(db) {
		val criteria = criteria {
			filter(BookDocument::stock.name, book.stock, FilterOperator.LESS_THAN)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldNotContain book
	}

	@Test
	fun `it should find by stock is less than or equals`() = transaction(db) {
		val criteria = criteria {
			filter(BookDocument::stock.name, book.stock, FilterOperator.LESS_THAN_EQUALS)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldContain book
	}

	@Test
	fun `it should not find by stock is less than or equals`() = transaction(db) {
		val criteria = criteria {
			filter(BookDocument::stock.name, book.stock - 1, FilterOperator.LESS_THAN_EQUALS)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldNotContain book
	}

	@Test
	fun `it should find by title contains`() = transaction(db) {
		val fragment = book.title.split(" ").first()

		val criteria = criteria {
			filter(BookDocument::title.name, fragment, FilterOperator.CONTAINS)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldContain book
	}

	@Test
	fun `it should not find by title contains`() = transaction(db) {
		val fragment = "noexistingtitle"

		val criteria = criteria {
			filter(BookDocument::title.name, fragment, FilterOperator.CONTAINS)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldNotContain book
	}

	@Test
	fun `it should find by title not contains`() = transaction(db) {
		val fragment = book.title.split(" ").first()

		val criteria = criteria {
			filter(BookDocument::title.name, fragment, FilterOperator.NOT_CONTAINS)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldNotContain book
	}

	@Test
	fun `it should not find by title not contains`() = transaction(db) {
		val fragment = "noexistingtitle"

		val criteria = criteria {
			filter(BookDocument::title.name, fragment, FilterOperator.NOT_CONTAINS)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldContain book
	}

	@Test
	fun `it should find by title regex`() = transaction(db) {
		val regex = ".*${book.title.split(" ").first()}.*"

		val criteria = criteria {
			filter(BookDocument::title.name, regex, FilterOperator.REGEX)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldContain book
	}

	@Test
	fun `it should not find by title regex`() = transaction(db) {
		val regex = ".*noexitingbook.*"

		val criteria = criteria {
			filter(BookDocument::title.name, regex, FilterOperator.REGEX)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldNotContain book
	}

	@Test
	fun `it should find by title contains in list`() = transaction(db) {
		val titles = listOf(
			books.first().title,
			books.last().title
		)

		val criteria = criteria {
			filter(BookDocument::title.name, titles, FilterOperator.CONTAINS)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldContain books.first()
		result shouldContain books.last()
	}

	@Test
	fun `it should not find by title contains in list`() = transaction(db) {
		val titles = listOf(
			"noexistingbook1",
			"noexistingbook2"
		)

		val criteria = criteria {
			filter(BookDocument::title.name, titles, FilterOperator.CONTAINS)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldNotContain books.first()
		result shouldNotContain books.last()
	}

	@Test
	fun `it should find by title not contains in list`() = transaction(db) {
		val titles = listOf(
			books.first().title,
			books.last().title
		)

		val criteria = criteria {
			filter(BookDocument::title.name, titles, FilterOperator.NOT_CONTAINS)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldNotContain books.first()
		result shouldNotContain books.last()
	}

	@Test
	fun `it should not find by title not contains in list`() = transaction(db) {
		val titles = listOf(
			"noexistingbook1",
			"noexistingbook2"
		)

		val criteria = criteria {
			filter(BookDocument::title.name, titles, FilterOperator.NOT_CONTAINS)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldContain books.first()
		result shouldContain books.last()
	}

	@Test
	fun `it should return paged results`() = transaction(db) {
		val criteria = criteria {
			page = 1
			pageSize = 2
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()
		val expectedPage = books.slice(0..1)

		result.size shouldBe 2
		result shouldBe expectedPage
	}

	@Test
	fun `it should return paged results and return next page items`() = transaction(db) {
		for (page in 2..5) {
			val criteria = criteria {
				this.page = page
				this.pageSize = 2
			}

			val query = BookTable.selectAll()
			CriteriaExposedQueryAdapter(columns).apply(criteria, query)

			val result = query.toEntityList()
			val offset = (page - 1) * 2
			val expectedPage = books.slice(offset..offset + 1)

			result.size shouldBe 2
			result shouldBe expectedPage
		}
	}

	@Test
	fun `it should sort asc`() = transaction(db) {
		val criteria = criteria {
			orderBy(BookDocument::numericalId.name, Order.ASC)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldBe books
	}

	@Test
	fun `it should sort desc`() = transaction(db) {
		val criteria = criteria {
			orderBy(BookDocument::numericalId.name, Order.DESC)
		}

		val query = BookTable.selectAll()
		CriteriaExposedQueryAdapter(columns).apply(criteria, query)

		val result = query.toEntityList()

		result shouldBe books.reversed()
	}
}
