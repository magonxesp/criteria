package io.github.magonxesp.criteria.domain


class Criteria(
	/**
	 * The filters list, for now they are chained with the "and" condition.
	 */
    val filters: List<Filter>,
    val orderBy: List<OrderBy>,
    val pagination: Pagination
)
