package io.github.magonxesp.criteria.infrastructure.spring

import io.github.magonxesp.criteria.domain.Criteria
import org.springframework.data.jpa.domain.Specification

class CriteriaToJPASpecification(private val fieldMap: FieldMap = mapOf()) {
    fun <T> mapToSpecification(criteria: Criteria) = Specification<T> { root, query, builder ->
        val filterMapper = FiltersToJPACriteriaPredicates(root, builder, fieldMap)
        val orderMapper = OrderByToJPACriteriaPredicates(root, builder, fieldMap)

        query.orderBy(*orderMapper.mapOrderByToOrder(criteria.orderBy))
        builder.and(*filterMapper.mapFiltersToPredicates(criteria.filters))
    }
}
