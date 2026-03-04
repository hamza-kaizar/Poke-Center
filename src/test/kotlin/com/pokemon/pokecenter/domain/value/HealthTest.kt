package com.pokemon.pokecenter.domain.value

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class HealthTest {
	private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("validationScenarios")
	fun `should validate health constraints`(
		name: String,
		current: Int,
		maximum: Int,
		expectedCount: Int,
		expectedMessages: List<String>,
	) {
		val health = Health(current = current, maximum = maximum)
		val violations = validator.validate(health)

		assertEquals(expectedCount, violations.size)

		val messages = violations.map { it.message }
		expectedMessages.forEach { expectedMessage ->
			if (expectedMessage.isNotBlank()) {
				assertTrue(messages.contains(expectedMessage.trim()), "Expected violation message: '$expectedMessage'")
			}
		}
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("healthyCheckScenarios")
	fun `should verify healthy check`(
		name: String,
		current: Int,
		maximum: Int,
		expectedIsHealthy: Boolean,
	) {
		val health = Health(current = current, maximum = maximum)
		assertEquals(expectedIsHealthy, health.isHealthy)
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("criticalCheckScenarios")
	fun `should verify critical check`(
		name: String,
		current: Int,
		maximum: Int,
		expectedIsCritical: Boolean,
	) {
		val health = Health(current = current, maximum = maximum)
		assertEquals(expectedIsCritical, health.isCritical)
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("recoveryPercentageScenarios")
	fun `should verify recovery percentage calculation`(
		name: String,
		current: Int,
		maximum: Int,
		expectedPercentage: Int,
	) {
		val health = Health(current = current, maximum = maximum)
		assertEquals(expectedPercentage, health.recoveryPercentage)
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

	private data class ValidationScenario(
		val name: String,
		val current: Int,
		val maximum: Int,
		val expectedCount: Int,
		val expectedMessages: List<String> = emptyList(),
	)

	private data class HealthConditionCheckScenario(
		val name: String,
		val current: Int,
		val maximum: Int,
		val expected: Boolean,
	)

	private data class RecoveryPercentageScenario(
		val name: String,
		val current: Int,
		val maximum: Int,
		val expectedPercentage: Int,
	)

	companion object {
		@JvmStatic
		fun validationScenarios(): Stream<Arguments> =
			listOf(
				ValidationScenario(
					name = "Valid health should have no violations",
					current = 50,
					maximum = 100,
					expectedCount = 0,
				),
				ValidationScenario(
					name = "Current health cannot be negative",
					current = -1,
					maximum = 100,
					expectedCount = 1,
					expectedMessages = listOf("Current health must be positive or zero"),
				),
				ValidationScenario(
					name = "Maximum health must be positive",
					current = 0,
					maximum = 0,
					expectedCount = 1,
					expectedMessages = listOf("Maximum health must be positive"),
				),
				ValidationScenario(
					name = "Maximum health cannot be negative",
					current = 0,
					maximum = -1,
					expectedCount = 2,
					expectedMessages =
						listOf(
							"Maximum health must be positive",
							"Current health cannot exceed maximum health",
						),
				),
				ValidationScenario(
					name = "Current health cannot exceed maximum",
					current = 110,
					maximum = 100,
					expectedCount = 1,
					expectedMessages = listOf("Current health cannot exceed maximum health"),
				),
				ValidationScenario(
					name = "Multiple field failures: negative current and zero maximum",
					current = -5,
					maximum = 0,
					expectedCount = 2,
					expectedMessages =
						listOf(
							"Current health must be positive or zero",
							"Maximum health must be positive",
						),
				),
			).map {
				Arguments.of(it.name, it.current, it.maximum, it.expectedCount, it.expectedMessages)
			}.stream()

		@JvmStatic
		fun healthyCheckScenarios(): Stream<Arguments> =
			listOf(
				HealthConditionCheckScenario(
					name = "Unhealthy when current health is less than maximum",
					current = 90,
					maximum = 100,
					expected = false,
				),
				HealthConditionCheckScenario(
					name = "Healthy when current health equals maximum",
					current = 100,
					maximum = 100,
					expected = true,
				),
			).map {
				Arguments.of(it.name, it.current, it.maximum, it.expected)
			}.stream()

		@JvmStatic
		fun criticalCheckScenarios(): Stream<Arguments> =
			listOf(
				HealthConditionCheckScenario(
					name = "Critical when current health is below 25",
					current = 24,
					maximum = 100,
					expected = true,
				),
				HealthConditionCheckScenario(
					name = "Not critical when current health is exactly 25",
					current = 25,
					maximum = 100,
					expected = false,
				),
				HealthConditionCheckScenario(
					name = "Not critical when current health is above 25",
					current = 26,
					maximum = 100,
					expected = false,
				),
			).map {
				Arguments.of(it.name, it.current, it.maximum, it.expected)
			}.stream()

		@JvmStatic
		fun recoveryPercentageScenarios(): Stream<Arguments> =
			listOf(
				RecoveryPercentageScenario(
					name = "0% recovery when current health is 0",
					current = 0,
					maximum = 100,
					expectedPercentage = 0,
				),
				RecoveryPercentageScenario(
					name = "100% recovery when current health equals maximum",
					current = 100,
					maximum = 100,
					expectedPercentage = 100,
				),
				RecoveryPercentageScenario(
					name = "33% recovery when not fully recovered (integer division)",
					current = 1,
					maximum = 3,
					expectedPercentage = 33,
				),
			).map {
				Arguments.of(it.name, it.current, it.maximum, it.expectedPercentage)
			}.stream()
	}
}
