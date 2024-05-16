package io.github.magonxesp.criteria.infrastructure.serialization

import io.github.magonxesp.criteria.domain.FilterOperator
import io.github.magonxesp.criteria.domain.Order
import io.github.magonxesp.criteria.domain.criteria
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class DeserializeCriteriaTest : AnnotationSpec() {
	val base64 = "ewogICJmaWx0ZXJzIjogWwogICAgewogICAgICAiZmllbGQiOiAiZ2VucmVzIiwKICAgICAgInZhbHVlIjogWyJTbGljZU9mTGlmZSJdLAogICAgICAib3BlcmF0b3IiOiAifj0iCiAgICB9CiAgXSwKICAib3JkZXJCeSI6IFsKICAgIHsKICAgICAgImZpZWxkIjogInBvcHVsYXJpdHkiLAogICAgICAib3JkZXIiOiAiREVTQyIKICAgIH0KICBdLAogICJwYWdpbmF0aW9uIjogewogICAgInBhZ2UiOiAxLAogICAgInNpemUiOiA1MAogIH0KfQ=="
	val expectedFilterValue = arrayOf("SliceOfLife")
	val expected = criteria {
		filter("genres", expectedFilterValue, FilterOperator.CONTAINS)
		orderBy("popularity", Order.DESC)
		page = 1
		pageSize = 50
	}

	@Test
	fun `it should deserialize criteria with a list as value`() {
		val criteria = base64.decodeCriteriaFromBase64()
		val filterValue = criteria.filters.single { it.field == "genres" }.value

		filterValue shouldBe expectedFilterValue
	}
}
