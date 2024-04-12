package io.github.magonxesp.criteria.domain

data class PaginatedCollection<T>(
    val items: List<T>,
    val totalPages: Long,
    val totalItems: Long,
    val currentPage: Long
)