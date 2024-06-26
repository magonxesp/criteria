package io.github.magonxesp.criteria.domain

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class CriteriaExtensionTest : AnnotationSpec() {
	@Test
	fun `it should replace a filter`() {
		val criteria = Criteria(
			filters = listOf(Filter("title", "Atomic Kotlin"))
		)

		val filter = Filter("title", "The Rust programming language")

		val newCriteria = criteria.replaceFilter(filter)

		newCriteria.filters shouldBe listOf(filter)
	}
}
