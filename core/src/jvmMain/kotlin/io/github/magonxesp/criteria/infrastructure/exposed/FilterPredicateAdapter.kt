package io.github.magonxesp.criteria.infrastructure.exposed

import io.github.magonxesp.criteria.domain.Filter
import io.github.magonxesp.criteria.domain.FilterOperator
import io.github.magonxesp.criteria.domain.isUUID
import io.github.magonxesp.criteria.domain.toUUID
import io.github.magonxesp.criteria.infrastructure.Adapter
import io.github.magonxesp.criteria.infrastructure.map.FieldMap
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.Op
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
import java.util.UUID

class FilterPredicateAdapter(
	private val columns: Map<String, Column<Any>>,
	fieldMap: FieldMap
) : Adapter(fieldMap) {
	private val Filter.column: Column<Any>
		get() = columns[mappedField] ?: error("Column for field $mappedField not found")

    private fun Filter.numberPredicate(): Op<Boolean>? {
        if (value !is Number) {
            return null
        }

        return when (operator) {
            FilterOperator.EQUALS -> column eq value
            FilterOperator.NOT_EQUALS -> column neq value
            FilterOperator.LESS_THAN -> when (value) {
				is Float -> column less value
				is Long -> column less value
				is Double -> column less value
				is Int -> column less value
				else -> null
			}
            FilterOperator.GREATER_THAN -> when (value) {
				is Float -> column greater value
				is Long -> column greater value
				is Double -> column greater value
				is Int -> column greater value
				else -> null
			}
            FilterOperator.LESS_THAN_EQUALS -> when (value) {
				is Float -> column lessEq value
				is Long -> column lessEq value
				is Double -> column lessEq value
				is Int -> column lessEq value
				else -> null
			}
            FilterOperator.GREATER_THAN_EQUALS -> when (value) {
				is Float -> column greaterEq value
				is Long -> column greaterEq value
				is Double -> column greaterEq value
				is Int -> column greaterEq value
				else -> null
			}
            else -> null
        }
    }

	private fun Filter.uuidPredicate(): Op<Boolean>? {
        if (value !is String && value !is UUID) {
            return null
        }

		if (value is String && !value.isUUID()) {
			return null
		}

		val uuid: UUID = if (value is String) value.toUUID() else value as UUID

        return when (operator) {
            FilterOperator.EQUALS -> column eq uuid
            FilterOperator.NOT_EQUALS -> column neq uuid
            else -> null
        }
    }

    private fun Filter.stringPredicate(): Op<Boolean>? {
        if (value !is String) {
            return null
        }

        return when (operator) {
            FilterOperator.EQUALS -> column eq value
            FilterOperator.NOT_EQUALS -> column neq value
            FilterOperator.CONTAINS -> (column as Expression<String>) like "%$value%"
            FilterOperator.NOT_CONTAINS -> (column as Expression<String>) notLike "%$value%"
            FilterOperator.REGEX -> (column as Expression<String>) regexp value
            else -> null
        }
    }

    private fun Filter.instantPredicate(): Op<Boolean>? =
        try {
			val timestamp: Instant = when(value) {
				is String -> Instant.parse(value)
				is Long -> Instant.fromEpochMilliseconds(value)
				is Instant -> value
				else -> error("The value is not a ISO 8601 date format, milliseconds or Instant")
			}

            when (operator) {
                FilterOperator.EQUALS -> column eq timestamp
                FilterOperator.NOT_EQUALS -> column neq timestamp
                FilterOperator.GREATER_THAN -> column greater timestamp
                FilterOperator.LESS_THAN -> column less timestamp
                FilterOperator.GREATER_THAN_EQUALS -> column greaterEq timestamp
                FilterOperator.LESS_THAN_EQUALS -> column lessEq timestamp
                else -> null
            }
        } catch (_: Exception) {
            null
        }

    private fun Filter.booleanPredicate(): Op<Boolean>? {
        if (value !is Boolean) {
            return null
        }

        return when (operator) {
            FilterOperator.EQUALS -> column eq value
            FilterOperator.NOT_EQUALS -> column neq value
            else -> null
        }
    }

	private fun Filter.listPredicate(): Op<Boolean>? {
		if (value !is List<*>) {
			return null
		}

		return when (operator) {
			FilterOperator.CONTAINS -> column inList value as List<Any>
			FilterOperator.NOT_CONTAINS -> column notInList value as List<Any>
			else -> null
		}
	}

    private fun Filter.toPredicate(): Op<Boolean> =
		numberPredicate()
		?: uuidPredicate()
		?: instantPredicate()
        ?: stringPredicate()
        ?: booleanPredicate()
		?: listPredicate()
        ?: error("The filter operator ${operator.operator} is not supported by the given value type")

	fun adapt(filter: Filter) = filter.toPredicate()
    fun adapt(filters: List<Filter>): Array<Op<Boolean>> = filters.map { it.toPredicate() }.toTypedArray()
}
