package io.github.magonxesp.criteria.domain

enum class FilterOperator(val operator: String) {
    EQUALS("="),
    NOT_EQUALS("!="),
    LESS_THAN("<"),
    LESS_THAN_EQUALS("<="),
    GREATER_THAN(">"),
    GREATER_THAN_EQUALS(">="),
    CONTAINS("~="),
    NOT_CONTAINS("!~="),
    REGEX("*=");

    companion object {
        fun fromOperator(operator: String): FilterOperator =
            entries.singleOrNull { it.operator == operator } ?: error("The operator $operator does not exist.")
    }
}