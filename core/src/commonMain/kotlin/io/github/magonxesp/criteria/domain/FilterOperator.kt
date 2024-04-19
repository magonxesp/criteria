package io.github.magonxesp.criteria.domain

enum class FilterOperator(val operator: String) {
	/**
	 * The "equals" operator.
	 */
    EQUALS("="),
	/**
	 * The "not equals" operator.
	 */
    NOT_EQUALS("!="),
	/**
	 * The "<" operator.
	 */
    LESS_THAN("<"),
	/**
	 * The "<=" operator.
	 */
    LESS_THAN_EQUALS("<="),
	/**
	 * The ">" operator.
	 */
    GREATER_THAN(">"),
	/**
	 * The ">=" operator.
	 */
    GREATER_THAN_EQUALS(">="),
	/**
	 * The "contains" operator or "in" operator.
	 */
    CONTAINS("~="),
	/**
	 * The "not contains" operator or "not in" operator.
	 */
    NOT_CONTAINS("!~="),
	/**
	 * The "regex" operator.
	 */
    REGEX("*=");

    companion object {
		/**
		 * Get an instance of [FilterOperator] by the operator symbol string
		 */
        fun fromOperator(operator: String): FilterOperator =
            entries.singleOrNull { it.operator == operator } ?: error("The operator $operator does not exist.")
    }
}
