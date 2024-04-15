package io.github.magonxesp.criteria.infrastructure.jpa

import io.github.magonxesp.criteria.domain.Filter
import io.github.magonxesp.criteria.infrastructure.Adapter
import io.github.magonxesp.criteria.infrastructure.FieldMap
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Root

abstract class JpaPredicateAdapter(
	protected val root: Root<*>,
	protected val builder: CriteriaBuilder,
	fieldMap: FieldMap = mapOf()
) : Adapter(fieldMap) {
	protected fun Filter.field() = when (value) {
		is String -> root.get<String>(mappedField)
		is Int -> root.get<Int>(mappedField)
		is Long -> root.get<Long>(mappedField)
		is Double -> root.get<Double>(mappedField)
		is Boolean -> root.get<Boolean>(mappedField)
		is List<*> -> root.get<Any>(mappedField)
		else -> error("Unable to resolve the field type")
	}
}
