package com.pokemon.pokecenter.domain.entity

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
			Pokemon.create(
				name = pokemonName,
				trainerName = trainerName,
				health = health,
			)
		val afterCreation = LocalDateTime.now()

		// Assert
		assertNotNull(pokemon)
		assertEquals(pokemonName, pokemon.name)
		assertEquals(trainerName, pokemon.trainerName)
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
			Pokemon.create(
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
			Pokemon.create(
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
			Pokemon.create(
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
			Pokemon.create(
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
}
