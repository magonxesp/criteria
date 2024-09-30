package io.github.magonxesp.criteria.infrastructure.serialization

import io.github.magonxesp.criteria.domain.Order
import io.github.magonxesp.criteria.domain.OrderBy
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

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

private val json = Json { ignoreUnknownKeys = true }

fun List<OrderBy>.serializeToJson() = json.encodeToString(map { it.toSerializableOrderBy() })
fun String.deserializeOrderByFromJson() = json.decodeFromString<List<SerializableOrderBy>>(this)
	.map { it.toOrderBy() }

@OptIn(ExperimentalEncodingApi::class)
fun List<OrderBy>.encodeToBase64() = Base64.encode(serializeToJson().toByteArray())

@OptIn(ExperimentalEncodingApi::class)
fun String.decodeOrderByFromBase64() =
	Base64.decode(this)
		.decodeToString()
		.deserializeOrderByFromJson()
