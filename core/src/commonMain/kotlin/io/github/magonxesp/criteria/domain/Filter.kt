package io.github.magonxesp.criteria.domain

data class Filter(
	/**
	 * The field name corresponds to the field name of the table database in case of relational databases or the field name
	 * on a document in case of no sql databases.
	 */
    val field: String,
	/**
	 * The value should be a primitive type or a list of a primitive type.
	 */
    val value: Any,
    val operator: FilterOperator = FilterOperator.EQUALS
)
