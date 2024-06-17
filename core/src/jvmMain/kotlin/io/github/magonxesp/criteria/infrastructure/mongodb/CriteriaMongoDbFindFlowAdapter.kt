package io.github.magonxesp.criteria.infrastructure.mongodb

import com.mongodb.client.model.Filters.and
import com.mongodb.kotlin.client.coroutine.FindFlow
import io.github.magonxesp.criteria.domain.Criteria
import io.github.magonxesp.criteria.infrastructure.FieldMap

class CriteriaMongoDbFindFlowAdapter(private val fieldMap: FieldMap = mapOf()) {
    fun <T : Any> apply(criteria: Criteria, findFlow: FindFlow<T>) {
        val filterMapper = FilterBsonAdapter(fieldMap)
        val orderMapper = OrderByBsonAdapter(fieldMap)

        criteria.filters.takeIf { it.isNotEmpty() }?.also {
            findFlow.filter(and(*filterMapper.adapt(it)))
        }

        criteria.orderBy.takeIf { it.isNotEmpty() }?.also {
            findFlow.sort(orderMapper.adapt(it))
        }

        if (criteria.pagination.size != null) {
            findFlow.skip((criteria.pagination.page - 1) * criteria.pagination.size)
            findFlow.limit(criteria.pagination.size)
        }
    }
}
