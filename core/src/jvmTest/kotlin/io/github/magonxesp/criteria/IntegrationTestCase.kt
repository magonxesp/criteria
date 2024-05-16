package io.github.magonxesp.criteria

import io.kotest.core.spec.style.AnnotationSpec
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

abstract class IntegrationTestCase : AnnotationSpec() {
	companion object {
		val mongodb = MongoDBContainer(DockerImageName.parse("mongo:7.0.7"))
	}

	@BeforeAll
	fun setupContainers() {
		mongodb.start()
		onContainersStart()
	}

	open fun onContainersStart() { }
}
