package io.github.magonxesp.criteria.infrastructure.mongodb

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.github.magonxesp.criteria.IntegrationTestCase
import io.github.magonxesp.criteria.domain.FilterOperator
import io.github.magonxesp.criteria.domain.criteria
import io.github.magonxesp.criteria.infrastructure.mongodb.documents.BookDocument
import io.github.magonxesp.criteria.random
import io.kotest.common.runBlocking
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import kotlinx.coroutines.flow.toList
import java.util.UUID
import kotlin.random.Random

class CriteriaMongoDbFindFlowAdapterTest : IntegrationTestCase() {
	val client: MongoClient
		get() = MongoClient.create(mongodb.connectionString)

	val database: MongoDatabase
		get() = client.getDatabase("book_store")

	val collection: MongoCollection<BookDocument>
		get() = database.getCollection("books")

	val books = (0..50).map {
		BookDocument(
			id = UUID.randomUUID().toString(),
			numericalId = it,
			title = random().book.title(),
			author = random().book.author(),
			stock = Random.nextInt(1, 20)
		)
	}

	val book = books.first()

	override fun onContainersStart(): Unit = runBlocking {
		collection.insertMany(books)
	}

	@Test
	suspend fun `it should find by exact title`() {
		val criteria = criteria {
			filter(BookDocument::title.name, book.title, FilterOperator.EQUALS)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldContain book
	}

	@Test
	suspend fun `it should not find by exact title`() {
		val criteria = criteria {
			filter(BookDocument::title.name, book.title, FilterOperator.NOT_EQUALS)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldNotContain book
	}

	@Test
	suspend fun `it should find by stock is more than`() {
		val criteria = criteria {
			filter(BookDocument::stock.name, book.stock - 1, FilterOperator.GREATER_THAN)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldContain book
	}

	@Test
	suspend fun `it should not find by stock is more than`() {
		val criteria = criteria {
			filter(BookDocument::stock.name, book.stock, FilterOperator.GREATER_THAN)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldNotContain book
	}

	@Test
	suspend fun `it should find by stock is more than or equals`() {
		val criteria = criteria {
			filter(BookDocument::stock.name, book.stock, FilterOperator.GREATER_THAN_EQUALS)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldContain book
	}

	@Test
	suspend fun `it should not find by stock is more than or equals`() {
		val criteria = criteria {
			filter(BookDocument::stock.name, book.stock + 1, FilterOperator.GREATER_THAN_EQUALS)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldNotContain book
	}


	@Test
	suspend fun `it should find by stock is less than`() {
		val criteria = criteria {
			filter(BookDocument::stock.name, book.stock + 1, FilterOperator.LESS_THAN)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldContain book
	}

	@Test
	suspend fun `it should not find by stock is less than`() {
		val criteria = criteria {
			filter(BookDocument::stock.name, book.stock, FilterOperator.LESS_THAN)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldNotContain book
	}

	@Test
	suspend fun `it should find by stock is less than or equals`() {
		val criteria = criteria {
			filter(BookDocument::stock.name, book.stock, FilterOperator.LESS_THAN_EQUALS)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldContain book
	}

	@Test
	suspend fun `it should not find by stock is less than or equals`() {
		val criteria = criteria {
			filter(BookDocument::stock.name, book.stock - 1, FilterOperator.LESS_THAN_EQUALS)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldNotContain book
	}

	@Test
	suspend fun `it should find by title contains`() {
		val fragment = book.title.split(" ").first()
		
		val criteria = criteria {
			filter(BookDocument::title.name, fragment, FilterOperator.CONTAINS)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldContain book
	}

	@Test
	suspend fun `it should not find by title contains`() {
		val fragment = "noexistingtitle"

		val criteria = criteria {
			filter(BookDocument::title.name, fragment, FilterOperator.CONTAINS)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldNotContain book
	}

	@Test
	suspend fun `it should find by title not contains`() {
		val fragment = book.title.split(" ").first()

		val criteria = criteria {
			filter(BookDocument::title.name, fragment, FilterOperator.NOT_CONTAINS)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldNotContain book
	}

	@Test
	suspend fun `it should not find by title not contains`() {
		val fragment = "noexistingtitle"

		val criteria = criteria {
			filter(BookDocument::title.name, fragment, FilterOperator.NOT_CONTAINS)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldContain book
	}

	@Test
	suspend fun `it should find by title regex`() {
		val regex = ".*${book.title.split(" ").first()}.*"

		val criteria = criteria {
			filter(BookDocument::title.name, regex, FilterOperator.REGEX)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldContain book
	}

	@Test
	suspend fun `it should not find by title regex`() {
		val regex = ".*noexitingbook.*"

		val criteria = criteria {
			filter(BookDocument::title.name, regex, FilterOperator.REGEX)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldNotContain book
	}

	@Test
	suspend fun `it should find by title contains in list`() {
		val titles = listOf(
			books.first().title,
			books.last().title
		)

		val criteria = criteria {
			filter(BookDocument::title.name, titles, FilterOperator.CONTAINS)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldContain books.first()
		result shouldContain books.last()
	}

	@Test
	suspend fun `it should not find by title contains in list`() {
		val titles = listOf(
			"noexistingbook1",
			"noexistingbook2"
		)

		val criteria = criteria {
			filter(BookDocument::title.name, titles, FilterOperator.CONTAINS)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldNotContain books.first()
		result shouldNotContain books.last()
	}

	@Test
	suspend fun `it should find by title not contains in list`() {
		val titles = listOf(
			books.first().title,
			books.last().title
		)

		val criteria = criteria {
			filter(BookDocument::title.name, titles, FilterOperator.NOT_CONTAINS)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldNotContain books.first()
		result shouldNotContain books.last()
	}

	@Test
	suspend fun `it should not find by title not contains in list`() {
		val titles = listOf(
			"noexistingbook1",
			"noexistingbook2"
		)

		val criteria = criteria {
			filter(BookDocument::title.name, titles, FilterOperator.NOT_CONTAINS)
		}

		val findFlow = collection.find()
		CriteriaMongoDbFindFlowAdapter().apply(criteria, findFlow)

		val result = findFlow.toList()

		result shouldContain books.first()
		result shouldContain books.last()
	}
}
