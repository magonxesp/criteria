package io.github.magonxesp.criteria.infrastructure.exposed

import org.jetbrains.exposed.sql.Database

class MariaDBCriteriaExposedQueryAdapterTest : CriteriaExposedQueryAdapterTest() {
	override fun setupDatabaseConnection() = Database.connect(
		mariadb.jdbcUrl,
		driver = "org.mariadb.jdbc.Driver",
		user = mariadb.username,
		password = mariadb.password
	)
}
