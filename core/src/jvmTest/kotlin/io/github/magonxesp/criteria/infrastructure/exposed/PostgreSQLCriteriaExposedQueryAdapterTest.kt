package io.github.magonxesp.criteria.infrastructure.exposed

import org.jetbrains.exposed.sql.Database

class PostgreSQLCriteriaExposedQueryAdapterTest : CriteriaExposedQueryAdapterTest() {
	override fun setupDatabaseConnection() = Database.connect(
		postgresql.getJdbcUrl(),
		driver = "org.postgresql.Driver",
		user = postgresql.username,
		password = postgresql.password
	)
}
