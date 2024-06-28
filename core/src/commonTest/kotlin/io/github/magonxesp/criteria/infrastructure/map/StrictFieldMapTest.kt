package io.github.magonxesp.criteria.infrastructure.map

import io.github.magonxesp.criteria.domain.RequiredFieldException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class StrictFieldMapTest : AnnotationSpec() {
	@Test
	fun `it should map a field`() {
		val map = strictFieldMapOf(
			"author" to "author.name"
		)

		val mapped = map["author"]

		mapped shouldBe "author.name"
	}

	@Test
	fun `it should not map a not existing field`() {
		val map = strictFieldMapOf(
			"author" to "author.name"
		)

		shouldThrow<RequiredFieldException> {
			map["title"]
		}
	}
}
