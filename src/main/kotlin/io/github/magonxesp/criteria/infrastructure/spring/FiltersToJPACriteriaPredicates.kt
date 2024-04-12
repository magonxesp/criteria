package io.github.magonxesp.criteria.infrastructure.spring

import io.github.magonxesp.criteria.domain.Filter
import io.github.magonxesp.criteria.domain.FilterOperator
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import java.time.Instant

class FiltersToJPACriteriaPredicates(
    private val root: Root<*>,
    private val builder: CriteriaBuilder,
    private val fieldMap: FieldMap = mapOf()
) {
    private fun Filter.field() = when (value) {
        is String -> root.get<String>(fieldMap.mappedField(field))
        is Int -> root.get<Int>(fieldMap.mappedField(field))
        is Long -> root.get<Long>(fieldMap.mappedField(field))
        is Double -> root.get<Double>(fieldMap.mappedField(field))
        is Boolean -> root.get<Boolean>(fieldMap.mappedField(field))
        else -> error("Unable to resolve the field type")
    }

    private fun Filter.numberPredicate(): Predicate? {
        if (value !is Number) {
            return null
        }

        return when (operator) {
            FilterOperator.EQUALS -> builder.equal(field(), value)
            FilterOperator.NOT_EQUALS -> builder.notEqual(field(), value)
            FilterOperator.LESS_THAN -> when (value) {
                is Int -> builder.lessThan(field() as Path<Int>, value)
                is Long -> builder.lessThan(field() as Path<Long>, value)
                is Double -> builder.lessThan(field() as Path<Double>, value)
                else -> null
            }

            FilterOperator.GREATER_THAN -> when (value) {
                is Int -> builder.greaterThan(field() as Path<Int>, value)
                is Long -> builder.greaterThan(field() as Path<Long>, value)
                is Double -> builder.greaterThan(field() as Path<Double>, value)
                else -> null
            }

            FilterOperator.LESS_THAN_EQUALS -> when (value) {
                is Int -> builder.lessThanOrEqualTo(field() as Path<Int>, value)
                is Long -> builder.lessThanOrEqualTo(field() as Path<Long>, value)
                is Double -> builder.lessThanOrEqualTo(field() as Path<Double>, value)
                else -> null
            }

            FilterOperator.GREATER_THAN_EQUALS -> when (value) {
                is Int -> builder.greaterThanOrEqualTo(field() as Path<Int>, value)
                is Long -> builder.greaterThanOrEqualTo(field() as Path<Long>, value)
                is Double -> builder.greaterThanOrEqualTo(field() as Path<Double>, value)
                else -> null
            }

            else -> null
        }
    }

    private fun Filter.stringPredicate(): Predicate? {
        if (value !is String) {
            return null
        }

        return when (operator) {
            FilterOperator.EQUALS -> builder.equal(field(), value)
            FilterOperator.NOT_EQUALS -> builder.notEqual(field(), value)
            FilterOperator.CONTAINS -> builder.like(field() as Path<String>, "%$value%")
            FilterOperator.NOT_CONTAINS -> builder.notLike(field() as Path<String>, "%$value%")
            else -> null
        }
    }

    private fun Filter.instantPredicate(): Predicate? {
        if (value !is String) {
            return null
        }

        val date = runCatching {
            Instant.parse(value as String)
        }.getOrNull() ?: return null

        return when (operator) {
            FilterOperator.EQUALS -> builder.equal(field(), date)
            FilterOperator.NOT_EQUALS -> builder.notEqual(field(), date)
            FilterOperator.GREATER_THAN -> builder.greaterThan(field() as Path<Instant>, date)
            FilterOperator.LESS_THAN -> builder.lessThan(field() as Path<Instant>, date)
            FilterOperator.GREATER_THAN_EQUALS -> builder.greaterThanOrEqualTo(field() as Path<Instant>, date)
            FilterOperator.LESS_THAN_EQUALS -> builder.lessThanOrEqualTo(field() as Path<Instant>, date)
            else -> null
        }
    }

    private fun Filter.booleanPredicate(): Predicate? {
        if (value !is Boolean) {
            return null
        }

        return when (operator) {
            FilterOperator.EQUALS -> builder.equal(field(), value)
            FilterOperator.NOT_EQUALS -> builder.notEqual(field(), value)
            else -> null
        }
    }

    private fun Filter.toPredicate(): Predicate = numberPredicate()
        ?: stringPredicate()
        ?: booleanPredicate()
        ?: instantPredicate()
        ?: error("The filter operator ${operator.operator} is not supported by the given value type")

    fun mapFiltersToPredicates(filters: List<Filter>): Array<Predicate> =
        filters.map { it.toPredicate() }.toTypedArray()
}

