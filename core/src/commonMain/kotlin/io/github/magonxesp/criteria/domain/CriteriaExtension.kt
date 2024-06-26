package io.github.magonxesp.criteria.domain

/**
 * Replace any filter by Its field name
 */
fun Criteria.replaceFilter(filter: Filter): Criteria {
	val filters = filters.filter { it.field != filter.field }.toTypedArray()
	return copy(filters = listOf(*filters, filter))
}
