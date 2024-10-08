package io.github.magonxesp.criteria.infrastructure.exposed

import io.github.magonxesp.criteria.domain.Criteria
import io.github.magonxesp.criteria.infrastructure.map.DefaultFieldMap
import io.github.magonxesp.criteria.infrastructure.map.FieldMap
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.andWhere

class CriteriaExposedQueryAdapter(
	private val columns: Map<String, Column<Any>>,
	private val fieldMap: FieldMap = DefaultFieldMap(),
) {
	fun apply(criteria: Criteria, query: Query) {
		val filterAdapter = FilterPredicateAdapter(columns, fieldMap)
		val orderAdapter = OrderPredicateAdapter(columns, fieldMap)

		criteria.filters.takeIf { it.isNotEmpty() }?.also { filters ->
			filters.forEach { filter ->
				query.andWhere {
					filterAdapter.adapt(filter)
				}
			}
		}

		criteria.orderBy.takeIf { it.isNotEmpty() }?.also {
			query.orderBy(*orderAdapter.adapt(it))
		}

		if (criteria.pagination.size != null) {
			query.limit(criteria.pagination.size)
			query.offset(((criteria.pagination.page - 1) * criteria.pagination.size).toLong())
		}
	}
}
