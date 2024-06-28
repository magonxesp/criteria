package io.github.magonxesp.criteria.infrastructure

import io.github.magonxesp.criteria.domain.Filter
import io.github.magonxesp.criteria.domain.OrderBy
import io.github.magonxesp.criteria.infrastructure.map.FieldMap

abstract class Adapter(private val fieldMap: FieldMap) {
    protected val Filter.mappedField
        get() = fieldMap[field]

    protected val OrderBy.mappedField
        get() = fieldMap[field]
}
