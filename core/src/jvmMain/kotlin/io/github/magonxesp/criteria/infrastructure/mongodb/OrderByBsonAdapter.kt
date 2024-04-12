package io.github.magonxesp.criteria.infrastructure.mongodb

import com.mongodb.client.model.Sorts
import io.github.magonxesp.criteria.domain.Order
import io.github.magonxesp.criteria.domain.OrderBy
import io.github.magonxesp.criteria.infrastructure.Adapter
import io.github.magonxesp.criteria.infrastructure.FieldMap
import org.bson.conversions.Bson

class OrderByBsonAdapter(fieldMap: FieldMap = mapOf()) : Adapter(fieldMap) {
    private fun OrderBy.toBson(): Bson =
        if (order == Order.ASC) {
            Sorts.ascending(mappedField)
        } else {
            Sorts.descending(mappedField)
        }

    fun adapt(orderBy: List<OrderBy>): Bson = Sorts.orderBy(*orderBy.map { it.toBson() }.toTypedArray())
}