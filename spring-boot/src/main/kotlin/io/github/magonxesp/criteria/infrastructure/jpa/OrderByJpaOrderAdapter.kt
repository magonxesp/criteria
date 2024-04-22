package io.github.magonxesp.criteria.infrastructure.jpa

import io.github.magonxesp.criteria.domain.OrderBy
import io.github.magonxesp.criteria.infrastructure.Adapter
import io.github.magonxesp.criteria.infrastructure.FieldMap
import jakarta.persistence.criteria.*
import io.github.magonxesp.criteria.domain.Order as DomainOrder

class OrderByJpaOrderAdapter(
    root: Root<*>,
    builder: CriteriaBuilder,
    fieldMap: FieldMap = mapOf(),
	joinMap: JoinMap = mapOf()
) : JpaAdapter(root, builder, joinMap, fieldMap) {
    private fun OrderBy.toOrder(): Order =
        if (order == DomainOrder.ASC) {
            builder.asc(mappedRoot.get<Any>(mappedField))
        } else {
            builder.desc(mappedRoot.get<Any>(mappedField))
        }

    fun adapt(orderBy: List<OrderBy>): Array<Order> = orderBy.map { it.toOrder() }.toTypedArray()
}

