package io.github.magonxesp.criteria.infrastructure.serialization

import io.github.magonxesp.criteria.domain.Order
import io.github.magonxesp.criteria.domain.OrderBy
import kotlinx.serialization.Serializable

@Serializable
class SerializableOrderBy(
    val field: String,
    val order: String,
)

fun SerializableOrderBy.toOrderBy() = OrderBy(
    field = field,
    order = Order.valueOf(order.uppercase()),
)

fun OrderBy.toSerializableOrderBy() = SerializableOrderBy(
    field = field,
    order = order.name
)