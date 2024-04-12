package io.github.magonxesp.criteria.infrastructure.spring

import io.github.magonxesp.criteria.domain.Criteria
import io.github.magonxesp.criteria.domain.PaginatedCollection
import io.github.magonxesp.criteria.infrastructure.FieldMap
import io.github.magonxesp.criteria.infrastructure.jpa.FiltersJpaPredicateAdapter
import io.github.magonxesp.criteria.infrastructure.jpa.OrderByJpaOrderAdapter
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

class CriteriaJPASpecificationAdapter(private val fieldMap: FieldMap = mapOf()) {
    fun <T> adapt(criteria: Criteria) = Specification<T> { root, query, builder ->
        val filterMapper = FiltersJpaPredicateAdapter(root, builder, fieldMap)
        val orderMapper = OrderByJpaOrderAdapter(root, builder, fieldMap)

        query.orderBy(*orderMapper.adapt(criteria.orderBy))
        builder.and(*filterMapper.adapt(criteria.filters))
    }

    fun adaptPagination(criteria: Criteria): PageRequest =
        PageRequest.of(criteria.pagination.page - 1, criteria.pagination.size!!)

    private fun <T> applyNonPaged(criteria: Criteria, repository: JpaSpecificationExecutor<T>): List<T> =
        repository.findAll(adapt(criteria))

    private fun <T> applyPaged(criteria: Criteria, repository: JpaSpecificationExecutor<T>): Page<T> =
        repository.findAll(adapt(criteria), adaptPagination(criteria))

    fun <T> apply(criteria: Criteria, repository: JpaSpecificationExecutor<T>): PaginatedCollection<T> =
        if (criteria.pagination.size != null) {
            val page = applyPaged(criteria, repository)
            PaginatedCollection(
                items = page.content,
                totalItems = page.totalElements,
                totalPages = page.totalPages.toLong(),
                currentPage = criteria.pagination.page.toLong()
            )
        } else {
            val items = applyNonPaged(criteria, repository)
            PaginatedCollection(
                items = items,
                totalItems = items.size.toLong(),
                totalPages = 1,
                currentPage = 1
            )
        }
}
