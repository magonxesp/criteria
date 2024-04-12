package io.github.magonxesp.criteria.infrastructure.serialization

import io.github.magonxesp.criteria.domain.Criteria
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Serializable
class SerializableCriteria(
    val filters: List<SerializableFilter>,
    val orderBy: List<SerializableOrderBy>,
    val pagination: SerializablePagination,
)

fun SerializableCriteria.toCriteria() = Criteria(
    filters = filters.map { it.toFilter() },
    orderBy = orderBy.map { it.toOrderBy() },
    pagination = pagination.toPagination()
)

fun Criteria.toSerializableCriteria() = SerializableCriteria(
    filters = filters.map { it.toSerializableFilter() },
    orderBy = orderBy.map { it.toSerializableOrderBy() },
    pagination = pagination.toSerializable()
)

private val jsonEncoder = Json { ignoreUnknownKeys = true }

fun Criteria.serializeToJson() = jsonEncoder.encodeToString(toSerializableCriteria())
fun String.deserializeCriteriaFromJson() = jsonEncoder.decodeFromString<SerializableCriteria>(this).toCriteria()

@OptIn(ExperimentalEncodingApi::class)
fun Criteria.encodeToBase64() = Base64.encode(serializeToJson().toByteArray())

@OptIn(ExperimentalEncodingApi::class)
fun String.decodeCriteriaFromBase64() =
    Base64.decode(this)
        .decodeToString()
        .deserializeCriteriaFromJson()
