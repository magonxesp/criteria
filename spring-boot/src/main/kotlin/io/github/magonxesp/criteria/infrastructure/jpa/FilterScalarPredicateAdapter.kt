package io.github.magonxesp.criteria.infrastructure.jpa

import io.github.magonxesp.criteria.domain.Filter
import io.github.magonxesp.criteria.domain.FilterOperator
import io.github.magonxesp.criteria.infrastructure.FieldMap
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import java.time.Instant

class FilterScalarPredicateAdapter(
	root: Root<*>,
	builder: CriteriaBuilder,
	fieldMap: FieldMap = mapOf(),
	joinMap: JoinMap = mapOf()
) : JpaAdapter(root, builder, joinMap, fieldMap) {

	fun numberPredicate(filter: Filter): Predicate? {
		if (filter.value !is Number) {
			return null
		}

		return when (filter.operator) {
			FilterOperator.EQUALS -> builder.equal(filter.field(), filter.value)
			FilterOperator.NOT_EQUALS -> builder.notEqual(filter.field(), filter.value)
			FilterOperator.LESS_THAN -> when (filter.value) {
				is Int -> builder.lessThan(filter.field() as Path<Int>, filter.value as Int)
				is Long -> builder.lessThan(filter.field() as Path<Long>, filter.value as Long)
				is Double -> builder.lessThan(filter.field() as Path<Double>, filter.value as Double)
				else -> null
			}

			FilterOperator.GREATER_THAN -> when (filter.value) {
				is Int -> builder.greaterThan(filter.field() as Path<Int>, filter.value as Int)
				is Long -> builder.greaterThan(filter.field() as Path<Long>, filter.value as Long)
				is Double -> builder.greaterThan(filter.field() as Path<Double>, filter.value as Double)
				else -> null
			}

			FilterOperator.LESS_THAN_EQUALS -> when (filter.value) {
				is Int -> builder.lessThanOrEqualTo(filter.field() as Path<Int>, filter.value as Int)
				is Long -> builder.lessThanOrEqualTo(filter.field() as Path<Long>, filter.value as Long)
				is Double -> builder.lessThanOrEqualTo(filter.field() as Path<Double>, filter.value as Double)
				else -> null
			}

			FilterOperator.GREATER_THAN_EQUALS -> when (filter.value) {
				is Int -> builder.greaterThanOrEqualTo(filter.field() as Path<Int>, filter.value as Int)
				is Long -> builder.greaterThanOrEqualTo(filter.field() as Path<Long>, filter.value as Long)
				is Double -> builder.greaterThanOrEqualTo(filter.field() as Path<Double>, filter.value as Double)
				else -> null
			}

			else -> null
		}
	}

	fun stringPredicate(filter: Filter): Predicate? {
		if (filter.value !is String) {
			return null
		}

		return when (filter.operator) {
			FilterOperator.EQUALS -> builder.equal(filter.field(), filter.value)
			FilterOperator.NOT_EQUALS -> builder.notEqual(filter.field(), filter.value)
			FilterOperator.CONTAINS -> builder.like(filter.field() as Path<String>, "%${filter.value}%")
			FilterOperator.NOT_CONTAINS -> builder.notLike(filter.field() as Path<String>, "%${filter.value}%")
			else -> null
		}
	}

	fun instantPredicate(filter: Filter): Predicate? {
		if (filter.value !is String) {
			return null
		}

		val date = runCatching {
			Instant.parse(filter.value as String)
		}.getOrNull() ?: return null

		return when (filter.operator) {
			FilterOperator.EQUALS -> builder.equal(filter.field(), date)
			FilterOperator.NOT_EQUALS -> builder.notEqual(filter.field(), date)
			FilterOperator.GREATER_THAN -> builder.greaterThan(filter.field() as Path<Instant>, date)
			FilterOperator.LESS_THAN -> builder.lessThan(filter.field() as Path<Instant>, date)
			FilterOperator.GREATER_THAN_EQUALS -> builder.greaterThanOrEqualTo(filter.field() as Path<Instant>, date)
			FilterOperator.LESS_THAN_EQUALS -> builder.lessThanOrEqualTo(filter.field() as Path<Instant>, date)
			else -> null
		}
	}

	fun booleanPredicate(filter: Filter): Predicate? {
		if (filter.value !is Boolean) {
			return null
		}

		return when (filter.operator) {
			FilterOperator.EQUALS -> builder.equal(filter.field(), filter.value)
			FilterOperator.NOT_EQUALS -> builder.notEqual(filter.field(), filter.value)
			else -> null
		}
	}
}
