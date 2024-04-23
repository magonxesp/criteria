package io.github.magonxesp.criteria.domain

class CriteriaBuilder(
	private val filters: MutableList<Filter> = mutableListOf(),
	private val orderBy: MutableList<OrderBy> = mutableListOf(),
	var page: Int = 1,
	var pageSize: Int? = null
) {
	constructor(criteria: Criteria) : this(
		filters = criteria.filters.toMutableList(),
		orderBy = criteria.orderBy.toMutableList(),
		page = criteria.pagination.page,
		pageSize = criteria.pagination.size
	)

	/**
	 * Add a filter, for now all filters are chained with the "and" condition.
	 */
	fun filter(field: String, value: Any, operator: FilterOperator) {
		filters.add(Filter(field, value, operator))
	}

	/**
	 * Add an order by sentence
	 */
	fun orderBy(field: String, order: Order) {
		orderBy.add(OrderBy(field, order))
	}

	fun build() = Criteria(filters, orderBy, Pagination(page, pageSize))
}

/**
 * Create a new Criteria instance with [CriteriaBuilder]
 */
fun criteria(builder: CriteriaBuilder.() -> Unit) = CriteriaBuilder().apply { builder() }.build()

/**
 * Create a copy of an existing [Criteria] and return the copy with the modified one
 */
fun Criteria.modify(builder: CriteriaBuilder.() -> Unit) = CriteriaBuilder(this).apply { builder() }.build()
