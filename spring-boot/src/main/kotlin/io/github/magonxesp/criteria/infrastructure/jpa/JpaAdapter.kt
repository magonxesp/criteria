package io.github.magonxesp.criteria.infrastructure.jpa

import io.github.magonxesp.criteria.domain.Filter
import io.github.magonxesp.criteria.domain.OrderBy
import io.github.magonxesp.criteria.infrastructure.Adapter
import io.github.magonxesp.criteria.infrastructure.FieldMap
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.From
import jakarta.persistence.criteria.Root

abstract class JpaAdapter(
	protected val root: Root<*>,
	protected val builder: CriteriaBuilder,
	private val joinMap: JoinMap = mapOf(),
	fieldMap: FieldMap = mapOf(),
) : Adapter(fieldMap) {
	private fun String.stripJoinIdentifier() = replace(Regex("[a-zA-Z_]+\\."), "")
	private fun String.stripJoinField() = replace(Regex("\\.[a-zA-Z_]+"), "")

	protected val Filter.mappedRoot: From<*, *>
		get() = joinMap[mappedField.stripJoinField()] ?: root

	protected val OrderBy.mappedRoot: From<*, *>
		get() = joinMap[mappedField.stripJoinField()] ?: root

	protected fun Filter.field() = when (value) {
		is String -> mappedRoot.get<String>(mappedField.stripJoinIdentifier())
		is Int -> mappedRoot.get<Int>(mappedField.stripJoinIdentifier())
		is Long -> mappedRoot.get<Long>(mappedField.stripJoinIdentifier())
		is Double -> mappedRoot.get<Double>(mappedField.stripJoinIdentifier())
		is Boolean -> mappedRoot.get<Boolean>(mappedField.stripJoinIdentifier())
		is List<*> -> mappedRoot.get<Any>(mappedField.stripJoinIdentifier())
		else -> error("Unable to resolve the field type")
	}
}
