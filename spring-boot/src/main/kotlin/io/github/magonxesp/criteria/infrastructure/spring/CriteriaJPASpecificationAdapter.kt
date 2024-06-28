package io.github.magonxesp.criteria.infrastructure.spring

import io.github.magonxesp.criteria.domain.Criteria
import io.github.magonxesp.criteria.domain.PaginatedCollection
import io.github.magonxesp.criteria.infrastructure.map.FieldMap
import io.github.magonxesp.criteria.infrastructure.jpa.FiltersJpaPredicateAdapter
import io.github.magonxesp.criteria.infrastructure.jpa.JoinMap
import io.github.magonxesp.criteria.infrastructure.jpa.OrderByJpaOrderAdapter
import io.github.magonxesp.criteria.infrastructure.map.DefaultFieldMap
import jakarta.persistence.criteria.Root
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

class CriteriaJPASpecificationAdapter(
    private val fieldMap: FieldMap = DefaultFieldMap(),
) {
    fun <T> adapt(
		criteria: Criteria,
		joins: Root<T>.() -> JoinMap = { mapOf() }
	) = Specification { root, query, builder ->
		val joinMap = root.joins()
        val filterMapper = FiltersJpaPredicateAdapter(root, builder, fieldMap, joinMap)
        val orderMapper = OrderByJpaOrderAdapter(root, builder, fieldMap, joinMap)

        query.orderBy(*orderMapper.adapt(criteria.orderBy))
        builder.and(*filterMapper.adapt(criteria.filters))
    }

    fun adaptPagination(criteria: Criteria): PageRequest =
        PageRequest.of(criteria.pagination.page - 1, criteria.pagination.size!!)

    private fun <T> applyNonPaged(
		criteria: Criteria,
		repository: JpaSpecificationExecutor<T>,
		joins: Root<T>.() -> JoinMap = { mapOf() }
	): List<T> = repository.findAll(adapt(criteria, joins))

    private fun <T> applyPaged(
		criteria: Criteria,
		repository: JpaSpecificationExecutor<T>,
		joins: Root<T>.() -> JoinMap = { mapOf() }
	): Page<T> = repository.findAll(adapt(criteria, joins), adaptPagination(criteria))

    fun <T> apply(
		criteria: Criteria,
		repository: JpaSpecificationExecutor<T>,
		joins: Root<T>.() -> JoinMap = { mapOf() }
	): PaginatedCollection<T> =
        if (criteria.pagination.size != null) {
            val page = applyPaged(criteria, repository, joins)
            PaginatedCollection(
                items = page.content,
                totalItems = page.totalElements,
                totalPages = page.totalPages.toLong(),
                currentPage = criteria.pagination.page.toLong()
            )
        } else {
            val items = applyNonPaged(criteria, repository, joins)
            PaginatedCollection(
                items = items,
                totalItems = items.size.toLong(),
                totalPages = 1,
                currentPage = 1
            )
        }
}
