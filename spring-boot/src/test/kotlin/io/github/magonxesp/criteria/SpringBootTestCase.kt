package io.github.magonxesp.criteria

import io.kotest.core.spec.style.AnnotationSpec
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.utility.DockerImageName

@SpringBootApplication
@EnableJpaRepositories
class SpringBootTestApplication

@SpringBootTest(classes = [SpringBootTestApplication::class])
@ContextConfiguration(initializers = [SpringBootTestCase.Initializer::class])
abstract class SpringBootTestCase {
	companion object {
		val mariaDb = MariaDBContainer(DockerImageName.parse("mariadb:11.3.2"))
	}

	class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
		override fun initialize(applicationContext: ConfigurableApplicationContext) {
			mariaDb.start()

			TestPropertyValues.of(
				"spring.datasource.url=${mariaDb.jdbcUrl}",
				"spring.datasource.driver-class-name=org.mariadb.jdbc.Driver",
				"spring.jpa.database-platform=org.hibernate.dialect.MariaDBDialect",
				"spring.jpa.properties.hibernate.jdbc.time_zone=UTC",
				"spring.jpa.properties.hibernate.hbm2ddl.auto=update",
				"spring.datasource.username=${mariaDb.username}",
				"spring.datasource.password=${mariaDb.password}"
			).applyTo(applicationContext.environment)
		}
	}
}
