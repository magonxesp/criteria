package io.github.magonxesp.criteria.infrastructure.serialization

import io.github.magonxesp.criteria.domain.Filter
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class DeserializeFiltersTest : AnnotationSpec() {
	@Test
	fun `it should deserialize filters list`() {
		val filters = listOf(
			Filter("title", "Atomic Kotlin"),
			Filter("title", "JavaScript the good parts"),
		)

		val base64Encoded = filters.encodeToBase64()
		val decoded = base64Encoded.decodeFiltersFromBase64()

		decoded shouldBe filters
	}
}
