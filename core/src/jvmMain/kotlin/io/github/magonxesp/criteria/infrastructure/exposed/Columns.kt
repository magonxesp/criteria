package io.github.magonxesp.criteria.infrastructure.exposed

import org.jetbrains.exposed.sql.Column

infix fun String.toColumn(column: Column<*>): Pair<String, Column<Any>> = Pair(this, column as Column<Any>)
