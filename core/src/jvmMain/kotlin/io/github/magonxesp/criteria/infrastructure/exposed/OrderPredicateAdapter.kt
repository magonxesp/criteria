package io.github.magonxesp.criteria.infrastructure.exposed

import com.mongodb.client.model.Sorts
import io.github.magonxesp.criteria.domain.Filter
import io.github.magonxesp.criteria.domain.FilterOperator
import io.github.magonxesp.criteria.domain.Order
import io.github.magonxesp.criteria.domain.OrderBy
import io.github.magonxesp.criteria.infrastructure.Adapter
import io.github.magonxesp.criteria.infrastructure.map.FieldMap
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greater
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.notInList
import org.jetbrains.exposed.sql.SqlExpressionBuilder.notLike
import org.jetbrains.exposed.sql.SqlExpressionBuilder.regexp

class OrderPredicateAdapter(
	private val columns: Map<String, Column<Any>>,
	fieldMap: FieldMap
) : Adapter(fieldMap) {
	private val OrderBy.column: Column<Any>
		get() = columns[mappedField] ?: error("Column for field $mappedField not found")

    private fun OrderBy.toPredicate(): Pair<Expression<*>, SortOrder> =
		if (order == Order.ASC) {
			column to SortOrder.ASC
		} else {
			column to SortOrder.DESC
		}

    fun adapt(orderBys: List<OrderBy>): Array<Pair<Expression<*>, SortOrder>> =
		orderBys.map { it.toPredicate() }.toTypedArray()
}
