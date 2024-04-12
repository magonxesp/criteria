package io.github.magonxesp.criteria.domain

class Filter(
    val field: String,
    val value: Any,
    val operator: FilterOperator = FilterOperator.EQUALS
)