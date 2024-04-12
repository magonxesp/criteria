package io.github.magonxesp.criteria.domain


class Criteria(
    val filters: List<Filter>,
    val orderBy: List<OrderBy>,
    val pagination: Pagination
)