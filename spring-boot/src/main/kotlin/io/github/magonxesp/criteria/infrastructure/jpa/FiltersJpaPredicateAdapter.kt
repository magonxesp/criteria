package io.github.magonxesp.criteria.infrastructure.jpa

import io.github.magonxesp.criteria.domain.Filter
import io.github.magonxesp.criteria.infrastructure.FieldMap
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root

class FiltersJpaPredicateAdapter(
    root: Root<*>,
   	builder: CriteriaBuilder,
    fieldMap: FieldMap = mapOf()
) : JpaPredicateAdapter(root, builder, fieldMap) {
	private val scalarPredicates = FilterScalarPredicateAdapter(root, builder, fieldMap)
	private val listPredicates = FilterListJpaPredicateAdapter(root, builder, fieldMap)

    private fun Filter.toPredicate(): Predicate =
		scalarPredicates.numberPredicate(this)
        ?: scalarPredicates.stringPredicate(this)
        ?: scalarPredicates.booleanPredicate(this)
        ?: scalarPredicates.instantPredicate(this)
		?: listPredicates.listPredicate(this)
        ?: error("The filter operator ${operator.operator} is not supported by the given value type")

    fun adapt(filters: List<Filter>): Array<Predicate> = filters.map { it.toPredicate() }.toTypedArray()
}

