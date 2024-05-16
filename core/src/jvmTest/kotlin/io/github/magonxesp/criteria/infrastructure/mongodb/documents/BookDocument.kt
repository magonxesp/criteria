package io.github.magonxesp.criteria.infrastructure.mongodb.documents

import kotlinx.serialization.Serializable

@Serializable
data class BookDocument(
	val id: String,
	val numericalId: Int,
	val title: String,
	val author: String,
	val stock: Int
)
