package io.github.magonxesp.criteria.infrastructure.map

abstract class FieldMap {
	abstract val map: Map<String, String>
	open operator fun get(index: String): String = map[index] ?: index
}
