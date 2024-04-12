package io.github.magonxesp.criteria.infrastructure

import io.github.magonxesp.criteria.domain.Filter

typealias FieldMap = Map<String, String>

fun FieldMap.mappedField(field: String) = get(field) ?: field
