package com.pokemon.pokecenter.domain.value

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class HealthTest {
	private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

	@Test
	fun `should have no violations for valid health`() {
		val health = Health(current = 50, maximum = 100)
		val violations = validator.validate(health)
		assertTrue(violations.isEmpty())
	}

	@Test
	fun `should violate constraints when current health is negative`() {
		val health = Health(current = -1, maximum = 100)
		val violations = validator.validate(health)

		val messages = violations.map { it.message }
		assertTrue(messages.contains("Current health must be positive or zero"))
	}

	@Test
	fun `should violate constraints when maximum health is negative`() {
		val health = Health(current = 0, maximum = -1)
		val violations = validator.validate(health)

		val messages = violations.map { it.message }
		assertTrue(messages.contains("Maximum health must be positive"))
	}

	@Test
	fun `should violate constraints when maximum health is 0`() {
		val health = Health(current = 0, maximum = 0)
		val violations = validator.validate(health)

		val messages = violations.map { it.message }
		assertTrue(messages.contains("Maximum health must be positive"))
	}

	@Test
	fun `should violate ratio constraint when current exceeds maximum`() {
		val health = Health(current = 101, maximum = 100)
		val violations = validator.validate(health)
		assertTrue(violations.any { it.message == "Current health cannot exceed maximum health" })
	}

	@Test
	fun `should violate constraints when multiple fields are invalid`() {
		val health = Health(current = -1, maximum = 0)
		val violations = validator.validate(health)

		val messages = violations.map { it.message }
		assertTrue(messages.contains("Current health must be positive or zero"))
		assertTrue(messages.contains("Maximum health must be positive"))
	}

	@Test
	fun `should cover data class generated methods`() {
		val h1 = Health(50, 100)
		val h2 = Health(50, 100)
		val h3 = h1.copy(current = 60)

		assertAll(
			{ assertEquals(h1, h2) },
			{ assertNotEquals(h1, h3) },
			{ assertEquals(h1.hashCode(), h2.hashCode()) },
			{ assertTrue(h1.toString().contains("current=50")) },
			{ assertEquals(60, h3.current) },
		)
	}
}
