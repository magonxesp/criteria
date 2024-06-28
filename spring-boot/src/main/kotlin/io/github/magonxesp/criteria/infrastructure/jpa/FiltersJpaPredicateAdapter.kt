package io.github.magonxesp.criteria.infrastructure.jpa

import io.github.magonxesp.criteria.domain.Filter
import io.github.magonxesp.criteria.infrastructure.map.FieldMap
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root

class FiltersJpaPredicateAdapter(
    root: Root<*>,
    builder: CriteriaBuilder,
    fieldMap: FieldMap,
    joinMap: JoinMap
) : JpaAdapter(root, builder, joinMap, fieldMap) {
	private val scalarPredicates = FilterScalarPredicateAdapter(root, builder, fieldMap, joinMap)
	private val listPredicates = FilterListJpaPredicateAdapter(root, builder, fieldMap, joinMap)

    private fun Filter.toPredicate(): Predicate =
		scalarPredicates.numberPredicate(this)
        ?: scalarPredicates.stringPredicate(this)
        ?: scalarPredicates.booleanPredicate(this)
        ?: scalarPredicates.instantPredicate(this)
		?: listPredicates.listPredicate(this)
        ?: error("The filter operator ${operator.operator} is not supported by the given value type")

    fun adapt(filters: List<Filter>): Array<Predicate> = filters.map { it.toPredicate() }.toTypedArray()
}

