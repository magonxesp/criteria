package io.github.magonxesp.criteria.infrastructure.serialization

import io.github.magonxesp.criteria.domain.Filter
import io.github.magonxesp.criteria.domain.FilterOperator
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Serializable
class SerializableFilter(
    val field: String,
    val value: JsonElement,
    val operator: String,
)

private fun Any.toJsonElement(): JsonElement = when (this) {
    is String -> JsonPrimitive(this)
    is Int -> JsonPrimitive(this)
    is Long -> JsonPrimitive(this)
    is Double -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
	is Collection<*> -> JsonArray(this.map { it?.toJsonElement() ?: JsonNull })
    else -> JsonPrimitive(this.toString())
}

private fun JsonElement.toAny(): Any = when(this) {
	is JsonPrimitive -> when {
		isString -> content
		intOrNull != null -> int
		longOrNull != null -> long
		floatOrNull != null -> float
		doubleOrNull != null -> double
		booleanOrNull != null -> boolean
		else -> error("Unknown type for deserialize value property")
	}
    is JsonArray -> map { it.toAny() }
	else -> error("Unknown type for deserialize value property")
}

fun SerializableFilter.toFilter() = Filter(
    field = field,
    value = value.toAny(),
    operator = FilterOperator.fromOperator(operator)
)

fun Filter.toSerializableFilter() = SerializableFilter(
    field = field,
    value = value.toJsonElement(),
    operator = operator.operator
)

private val json = Json { ignoreUnknownKeys = true }

fun List<Filter>.serializeToJson() = json.encodeToString(map { it.toSerializableFilter() })
fun String.deserializeFiltersFromJson() = json.decodeFromString<List<SerializableFilter>>(this)
	.map { it.toFilter() }

@OptIn(ExperimentalEncodingApi::class)
fun List<Filter>.encodeToBase64() = Base64.encode(serializeToJson().toByteArray())

@OptIn(ExperimentalEncodingApi::class)
fun String.decodeFiltersFromBase64() =
	Base64.decode(this)
		.decodeToString()
		.deserializeFiltersFromJson()
