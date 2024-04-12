package io.github.magonxesp.criteria.infrastructure.jpa

import io.github.magonxesp.criteria.domain.Filter
import io.github.magonxesp.criteria.domain.FilterOperator
import io.github.magonxesp.criteria.infrastructure.Adapter
import io.github.magonxesp.criteria.infrastructure.FieldMap
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import java.time.Instant

class FiltersJpaPredicateAdapter(
    private val root: Root<*>,
    private val builder: CriteriaBuilder,
    fieldMap: FieldMap = mapOf()
) : Adapter(fieldMap) {
    private fun Filter.field() = when (value) {
        is String -> root.get<String>(mappedField)
        is Int -> root.get<Int>(mappedField)
        is Long -> root.get<Long>(mappedField)
        is Double -> root.get<Double>(mappedField)
        is Boolean -> root.get<Boolean>(mappedField)
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
                is Int -> builder.lessThan(field() as Path<Int>, value as Int)
                is Long -> builder.lessThan(field() as Path<Long>, value as Long)
                is Double -> builder.lessThan(field() as Path<Double>, value as Double)
                else -> null
            }

            FilterOperator.GREATER_THAN -> when (value) {
                is Int -> builder.greaterThan(field() as Path<Int>, value as Int)
                is Long -> builder.greaterThan(field() as Path<Long>, value as Long)
                is Double -> builder.greaterThan(field() as Path<Double>, value as Double)
                else -> null
            }

            FilterOperator.LESS_THAN_EQUALS -> when (value) {
                is Int -> builder.lessThanOrEqualTo(field() as Path<Int>, value as Int)
                is Long -> builder.lessThanOrEqualTo(field() as Path<Long>, value as Long)
                is Double -> builder.lessThanOrEqualTo(field() as Path<Double>, value as Double)
                else -> null
            }

            FilterOperator.GREATER_THAN_EQUALS -> when (value) {
                is Int -> builder.greaterThanOrEqualTo(field() as Path<Int>, value as Int)
                is Long -> builder.greaterThanOrEqualTo(field() as Path<Long>, value as Long)
                is Double -> builder.greaterThanOrEqualTo(field() as Path<Double>, value as Double)
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

    fun adapt(filters: List<Filter>): Array<Predicate> = filters.map { it.toPredicate() }.toTypedArray()
}

