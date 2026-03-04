package com.pokemon.pokecenter.domain.entity

import com.pokemon.pokecenter.domain.constant.Status
import com.pokemon.pokecenter.domain.value.Health
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class PokemonTest {
	private val validator = Validation.buildDefaultValidatorFactory().validator

	@Test
	fun `should create new pokemon and add arrived at time as current time`() {
		// Arrange
		val pokemonName = "Pikachu"
		val trainerName = "Ash"
		val health = Health(current = 50, maximum = 100)
		val beforeCreation = LocalDateTime.now()

		// Act
		val pokemon =
			Pokemon(
				name = pokemonName,
				trainerName = trainerName,
				health = health,
			)
		val afterCreation = LocalDateTime.now()

		// Assert
		assertNotNull(pokemon)
		assertEquals(pokemonName, pokemon.name)
		assertEquals(trainerName, pokemon.trainerName)
		assertEquals(Status.ARRIVED, pokemon.status)
		assertNotNull(pokemon.arrivedAt)

		// Verify arrivedAt is set to current time (within 1 second tolerance)
		val arrivedAt = pokemon.arrivedAt
		assertTrue(
			arrivedAt.isAfter(beforeCreation.minusSeconds(1)) &&
				arrivedAt.isBefore(afterCreation.plusSeconds(1)),
			"arrivedAt should be approximately equal to current time",
		)
	}

	@Test
	fun `should not create pokemon without name`() {
		// Arrange
		val pokemonName = ""
		val trainerName = "Ash"
		val health = Health(current = 50, maximum = 100)

		// Act
		val pokemon =
			Pokemon(
				name = pokemonName,
				trainerName = trainerName,
				health = health,
			)
		val violations: Set<ConstraintViolation<Pokemon>> = validator.validate(pokemon)

		// Assert
		assertFalse(violations.isEmpty(), "Should have validation violations for blank name")
		val nameViolation = violations.find { it.propertyPath.toString() == "name" }
		assertNotNull(nameViolation, "Should have a violation for the name field")
		assertEquals(
			"Pokemon name must not be blank",
			nameViolation?.message,
			"Should have the correct error message for blank name",
		)
	}

	@Test
	fun `should not create pokemon without trainer name`() {
		// Arrange
		val pokemonName = "Pikachu"
		val trainerName = ""
		val health = Health(current = 50, maximum = 100)

		// Act
		val pokemon =
			Pokemon(
				name = pokemonName,
				trainerName = trainerName,
				health = health,
			)
		val violations: Set<ConstraintViolation<Pokemon>> = validator.validate(pokemon)

		// Assert
		assertFalse(violations.isEmpty(), "Should have validation violations for blank trainerName")
		val trainerNameViolation = violations.find { it.propertyPath.toString() == "trainerName" }
		assertNotNull(trainerNameViolation, "Should have a violation for the trainerName field")
		assertEquals(
			"Trainer name must not be blank",
			trainerNameViolation?.message,
			"Should have the correct error message for blank trainerName",
		)
	}

	@Test
	fun `should not create pokemon without name or trainer name`() {
		// Arrange
		val pokemonName = ""
		val trainerName = ""
		val health = Health(current = 50, maximum = 100)

		// Act
		val pokemon =
			Pokemon(
				name = pokemonName,
				trainerName = trainerName,
				health = health,
			)
		val violations: Set<ConstraintViolation<Pokemon>> = validator.validate(pokemon)

		// Assert
		assertTrue(violations.size >= 2, "Should have at least 2 validation violations")
		val nameViolation = violations.find { it.propertyPath.toString() == "name" }
		val trainerNameViolation = violations.find { it.propertyPath.toString() == "trainerName" }
		assertNotNull(nameViolation, "Should have a violation for the name field")
		assertNotNull(trainerNameViolation, "Should have a violation for the trainerName field")
		assertEquals(
			"Pokemon name must not be blank",
			nameViolation?.message,
			"Should have the correct error message for blank name",
		)
		assertEquals(
			"Trainer name must not be blank",
			trainerNameViolation?.message,
			"Should have the correct error message for blank trainerName",
		)
	}

	@Test
	fun `should not create pokemon with invalid health`() {
		// Arrange
		val pokemonName = "Pikachu"
		val trainerName = "Ash"
		val health = Health(current = -10, maximum = 100)

		// Act
		val pokemon =
			Pokemon(
				name = pokemonName,
				trainerName = trainerName,
				health = health,
			)
		val violations: Set<ConstraintViolation<Pokemon>> = validator.validate(pokemon)

		// Assert
		assertFalse(violations.isEmpty(), "Should have validation violations for invalid health")
		val healthViolation = violations.find { it.propertyPath.toString() == "health.current" }
		assertNotNull(healthViolation, "Should have a violation for the health.current field")
		assertEquals(
			"Current health must be positive or zero",
			healthViolation?.message,
			"Should have the correct error message for invalid current health",
		)
	}

	@Test
	fun `should start healing when pokemon is arrived`() {
		// Arrange
		val pokemon =
			Pokemon(
				name = "Pikachu",
				trainerName = "Ash",
				health = Health(current = 50, maximum = 100),
				status = Status.ARRIVED,
			)

		// Act
		val healedPokemon = pokemon.startHealing()

		// Assert
		assertEquals(Status.HEALING, healedPokemon.status, "Pokemon should be in HEALING status")
	}

	@Test
	fun `should not start healing when pokemon is not arrived`() {
		// Arrange
		val pokemon =
			Pokemon(
				name = "Pikachu",
				trainerName = "Ash",
				health = Health(current = 50, maximum = 100),
				status = Status.HEALED,
			)

		// Act & Assert
		try {
			pokemon.startHealing()
		} catch (e: IllegalArgumentException) {
			assertEquals("Can only heal arrived pokemon", e.message)
		}
	}

	@Test
	fun `should apply healing when pokemon is in healing status`() {
		// Arrange
		val pokemon =
			Pokemon(
				name = "Pikachu",
				trainerName = "Ash",
				health = Health(current = 50, maximum = 100),
				status = Status.HEALING,
			)

		// Act
		val healedPokemon = pokemon.applyHealing(30)

		// Assert
		assertEquals(80, healedPokemon.health.current, "Current health should be increased by 30")
	}

	@Test
	fun `should not apply healing when pokemon is not in healing status`() {
		// Arrange
		val pokemon =
			Pokemon(
				name = "Pikachu",
				trainerName = "Ash",
				health = Health(current = 50, maximum = 100),
				status = Status.ARRIVED,
			)

		// Act & Assert
		try {
			pokemon.applyHealing(30)
		} catch (e: IllegalArgumentException) {
			assertEquals("Can only apply to pokemon in healing status", e.message)
		}
	}

	@Test
	fun `should complete healing when pokemon is in healing status`() {
		// Arrange
		val pokemon =
			Pokemon(
				name = "Pikachu",
				trainerName = "Ash",
				health = Health(current = 80, maximum = 100),
				status = Status.HEALING,
			)

		// Act
		val healedPokemon = pokemon.completeHealing()

		// Assert
		assertEquals(100, healedPokemon.health.current, "Current health should be set to maximum")
		assertEquals(Status.HEALED, healedPokemon.status, "Pokemon should be in HEALED status")
	}

	@Test
	fun `should not complete healing when pokemon is not in healing status`() {
		// Arrange
		val pokemon =
			Pokemon(
				name = "Pikachu",
				trainerName = "Ash",
				health = Health(current = 80, maximum = 100),
				status = Status.ARRIVED,
			)

		// Act & Assert
		try {
			pokemon.completeHealing()
		} catch (e: IllegalArgumentException) {
			assertEquals("Must be in healing status to complete healing", e.message)
		}
	}
}
