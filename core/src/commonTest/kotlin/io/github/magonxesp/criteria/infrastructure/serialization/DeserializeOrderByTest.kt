package io.github.magonxesp.criteria.infrastructure.serialization

import io.github.magonxesp.criteria.domain.Order
import io.github.magonxesp.criteria.domain.OrderBy
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class DeserializeOrderByTest : AnnotationSpec() {
	@Test
	fun `it should deserialize orderBy list`() {
		val orderBy = listOf(
			OrderBy("title", Order.ASC),
			OrderBy("releaseDate", Order.DESC),
		)

		val base64Encoded = orderBy.encodeToBase64()
		val decoded = base64Encoded.decodeOrderByFromBase64()

		decoded shouldBe orderBy
	}
}
