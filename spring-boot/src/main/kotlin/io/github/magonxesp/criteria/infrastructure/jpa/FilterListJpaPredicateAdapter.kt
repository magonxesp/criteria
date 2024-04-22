package io.github.magonxesp.criteria.infrastructure.jpa

import io.github.magonxesp.criteria.domain.Filter
import io.github.magonxesp.criteria.domain.FilterOperator
import io.github.magonxesp.criteria.infrastructure.FieldMap
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root

class FilterListJpaPredicateAdapter(
    root: Root<*>,
   	builder: CriteriaBuilder,
    fieldMap: FieldMap = mapOf(),
	joinMap: JoinMap = mapOf()
) : JpaAdapter(root, builder, joinMap, fieldMap) {
	private fun Any.toList(): List<Any> {
		if (this is Collection<*>) {
			return this.map { it as Any }
		}

		if (this is Array<*>) {
			return this.map { it as Any }
		}

		return listOf(this)
	}

	fun listPredicate(filter: Filter): Predicate? {
		if (filter.value !is List<*>) {
			return null
		}

		return when (filter.operator) {
			FilterOperator.CONTAINS -> filter.field().`in`(filter.value.toList())
			FilterOperator.NOT_CONTAINS -> filter.field().`in`(filter.value.toList()).not()
			else -> null
		}
	}
}

