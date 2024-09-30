package io.github.magonxesp.criteria

import io.kotest.core.spec.style.AnnotationSpec
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

abstract class IntegrationTestCase : AnnotationSpec() {
	companion object {
		val mongodb = MongoDBContainer(DockerImageName.parse("mongo:7.0.7"))
		val postgresql = PostgreSQLContainer(DockerImageName.parse("postgres:17.0-alpine"))
		val mariadb = MariaDBContainer(DockerImageName.parse("mariadb:11.5.2"))
	}

	@BeforeAll
	fun setupContainers() {
		mongodb.start()
		postgresql.start()
		mariadb.start()
		onContainersStart()
	}

	open fun onContainersStart() { }
}
