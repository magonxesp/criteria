package io.github.magonxesp.criteria.infrastructure.map

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class DefaultFieldMapTest : AnnotationSpec() {
	@Test
	fun `it should map a field`() {
		val map = fieldMapOf(
			"author" to "author.name"
		)

		val mapped = map["author"]

		mapped shouldBe "author.name"
	}

	@Test
	fun `it should map a not existing field`() {
		val map = fieldMapOf(
			"author" to "author.name"
		)

		val mapped = map["title"]

		mapped shouldBe "title"
	}
}
