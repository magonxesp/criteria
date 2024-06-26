package io.github.magonxesp.criteria.domain


data class Criteria(
	/**
	 * The filters list, for now they are chained with the "and" condition.
	 */
    val filters: List<Filter> = listOf(),
	val orderBy: List<OrderBy> = listOf(),
	val pagination: Pagination = Pagination()
)
