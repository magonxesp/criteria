package io.github.magonxesp.criteria.domain

class CriteriaBuilder(
	private val filters: MutableList<Filter> = mutableListOf(),
	private val orderBy: MutableList<OrderBy> = mutableListOf(),
	var page: Int = 1,
	var pageSize: Int? = null
) {
	fun filter(field: String, value: Any, operator: FilterOperator) {
		filters.add(Filter(field, value, operator))
	}

	fun orderBy(field: String, order: Order) {
		orderBy.add(OrderBy(field, order))
	}

	fun build() = Criteria(filters, orderBy, Pagination(page, pageSize))
}

fun criteria(builder: CriteriaBuilder.() -> Unit) = CriteriaBuilder().apply { builder() }.build()
