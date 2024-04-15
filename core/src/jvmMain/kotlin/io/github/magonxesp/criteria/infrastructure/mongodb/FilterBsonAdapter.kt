package io.github.magonxesp.criteria.infrastructure.mongodb

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.ne
import com.mongodb.client.model.Filters.lt
import com.mongodb.client.model.Filters.gt
import com.mongodb.client.model.Filters.gte
import com.mongodb.client.model.Filters.lte
import com.mongodb.client.model.Filters.regex
import com.mongodb.client.model.Filters.not
import com.mongodb.client.model.Filters.`in`
import io.github.magonxesp.criteria.domain.Filter
import io.github.magonxesp.criteria.domain.FilterOperator
import io.github.magonxesp.criteria.infrastructure.Adapter
import io.github.magonxesp.criteria.infrastructure.FieldMap
import org.bson.conversions.Bson
import kotlinx.datetime.Instant

class FilterBsonAdapter(fieldMap: FieldMap = mapOf()) : Adapter(fieldMap) {
    private fun Filter.numberBson(): Bson? {
        if (value !is Number) {
            return null
        }

        return when (operator) {
            FilterOperator.EQUALS -> eq(mappedField, value)
            FilterOperator.NOT_EQUALS -> ne(mappedField, value)
            FilterOperator.LESS_THAN -> lt(mappedField, value)
            FilterOperator.GREATER_THAN -> gt(mappedField, value)
            FilterOperator.LESS_THAN_EQUALS -> lte(mappedField, value)
            FilterOperator.GREATER_THAN_EQUALS -> gte(mappedField, value)
            else -> null
        }
    }

    private fun Filter.stringBson(): Bson? {
        if (value !is String) {
            return null
        }

        return when (operator) {
            FilterOperator.EQUALS -> eq(mappedField, value)
            FilterOperator.NOT_EQUALS -> ne(mappedField, value)
            FilterOperator.CONTAINS -> regex(mappedField, ".*$value.*")
            FilterOperator.NOT_CONTAINS -> not(regex(mappedField, ".*$value.*"))
            FilterOperator.REGEX -> regex(mappedField, value)
            else -> null
        }
    }

    private fun Filter.instantBson(): Bson? =
        try {
            if (value !is String) {
                error("The value is not a string")
            }

            val timestamp = Instant.parse(value).toEpochMilliseconds()

            when (operator) {
                FilterOperator.EQUALS -> eq(mappedField, timestamp)
                FilterOperator.NOT_EQUALS -> ne(mappedField, timestamp)
                FilterOperator.GREATER_THAN -> gt(mappedField, timestamp)
                FilterOperator.LESS_THAN -> lt(mappedField, timestamp)
                FilterOperator.GREATER_THAN_EQUALS -> gte(mappedField, timestamp)
                FilterOperator.LESS_THAN_EQUALS -> lte(mappedField, timestamp)
                else -> null
            }
        } catch (_: Exception) {
            null
        }

    private fun Filter.booleanBson(): Bson? {
        if (value !is Boolean) {
            return null
        }

        return when (operator) {
            FilterOperator.EQUALS -> eq(mappedField, value)
            FilterOperator.NOT_EQUALS -> ne(mappedField, value)
            else -> null
        }
    }

	private fun Filter.listBson(): Bson? {
		if (value !is List<*>) {
			return null
		}

		return when (operator) {
			FilterOperator.CONTAINS -> `in`(mappedField, value)
			FilterOperator.NOT_CONTAINS -> not(`in`(mappedField, value))
			else -> null
		}
	}

    private fun Filter.toBson(): Bson =
		numberBson()
        ?: stringBson()
        ?: booleanBson()
        ?: instantBson()
		?: listBson()
        ?: error("The filter operator ${operator.operator} is not supported by the given value type")

    fun adapt(filters: List<Filter>): Array<Bson> = filters.map { it.toBson() }.toTypedArray()
}
