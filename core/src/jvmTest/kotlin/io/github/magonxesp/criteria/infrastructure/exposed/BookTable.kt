package io.github.magonxesp.criteria.infrastructure.exposed

import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert

object BookTable : Table() {
	val id = varchar("id", length = 65)
	val numericalId = integer("numerical_id")
	val title = varchar("title", length = 255)
	val author = varchar("author", length = 255)
	val stock = integer("stock")

	override val primaryKey = PrimaryKey(id)
}

data class BookEntity(
	val id: String,
	val numericalId: Int,
	val title: String,
	val author: String,
	val stock: Int,
) {
	fun insert() {
		BookTable.insert {
			it[id] = this@BookEntity.id
			it[numericalId] = this@BookEntity.numericalId
			it[title] = this@BookEntity.title
			it[author] = this@BookEntity.author
			it[stock] = this@BookEntity.stock
		}
	}
}

fun ResultRow.toEntity() = BookEntity(
	id = get(BookTable.id),
	numericalId = get(BookTable.numericalId),
	title = get(BookTable.title),
	author = get(BookTable.author),
	stock = get(BookTable.stock),
)

fun Query.toEntityList() = toList().map { it.toEntity() }
