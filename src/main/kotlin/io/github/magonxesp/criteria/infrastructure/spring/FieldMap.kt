package io.github.magonxesp.criteria.infrastructure.spring

typealias FieldMap = Map<String, String>

fun FieldMap.mappedField(field: String) = get(field) ?: field
