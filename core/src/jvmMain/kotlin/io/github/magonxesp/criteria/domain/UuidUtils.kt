package io.github.magonxesp.criteria.domain

import java.util.UUID

fun String.toUUID(): UUID = UUID.fromString(this)

fun String.isUUID() = 
	try {
		this.toUUID()
		true
	} catch (_: IllegalArgumentException) {
		false
	}
