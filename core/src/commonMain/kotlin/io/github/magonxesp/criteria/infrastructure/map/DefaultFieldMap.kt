package io.github.magonxesp.criteria.infrastructure.map

class DefaultFieldMap(override val map: Map<String, String> = mapOf()) : FieldMap()

fun fieldMapOf(vararg mapping: Pair<String, String>) = DefaultFieldMap(map = mapping.toMap())
