package io.github.magonxesp.criteria.domain

data class OrderBy(
    val field: String,
    val order: Order = Order.ASC
)
