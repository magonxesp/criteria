package io.github.magonxesp.criteria.infrastructure.serialization

import io.github.magonxesp.criteria.domain.Pagination
import kotlinx.serialization.Serializable

@Serializable
class SerializablePagination(
    val page: Int,
    val size: Int?
)

fun SerializablePagination.toPagination() = Pagination(page, size)
fun Pagination.toSerializable() = SerializablePagination(page, size)