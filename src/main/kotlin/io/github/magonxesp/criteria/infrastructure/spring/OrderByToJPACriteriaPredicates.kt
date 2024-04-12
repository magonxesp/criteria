package io.github.magonxesp.criteria.infrastructure.spring

import io.github.magonxesp.criteria.domain.OrderBy
import jakarta.persistence.criteria.*
import io.github.magonxesp.criteria.domain.Order as DomainOrder

class OrderByToJPACriteriaPredicates(
    private val root: Root<*>,
    private val builder: CriteriaBuilder,
    private val fieldMap: FieldMap = mapOf()
) {
    private fun OrderBy.toOrder(): Order =
        if (order == DomainOrder.ASC) {
            builder.asc(root.get<Any>(fieldMap.mappedField(field)))
        } else {
            builder.desc(root.get<Any>(fieldMap.mappedField(field)))
        }

    fun mapOrderByToOrder(orderBy: List<OrderBy>): Array<Order> =
        orderBy.map { it.toOrder() }.toTypedArray()
}

