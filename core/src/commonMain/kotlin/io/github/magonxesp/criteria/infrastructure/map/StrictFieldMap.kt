package io.github.magonxesp.criteria.infrastructure.map

import io.github.magonxesp.criteria.domain.RequiredFieldException

class StrictFieldMap(override val map: Map<String, String>) : FieldMap() {
	override fun get(index: String): String = map[index]
		?: throw RequiredFieldException("The field $index is not present in field map")
}

fun strictFieldMapOf(vararg mapping: Pair<String, String>) = StrictFieldMap(map = mapping.toMap())
