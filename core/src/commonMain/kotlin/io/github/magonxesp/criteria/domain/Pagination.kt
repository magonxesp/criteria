package io.github.magonxesp.criteria.domain

class Pagination(
    val page: Int = 1,
    val size: Int? = 25
) {
    init {
        require(page > 0) { "page must be > 0" }

        if (size != null) {
            require(size > 0) { "size must be > 0" }
        }
    }
}