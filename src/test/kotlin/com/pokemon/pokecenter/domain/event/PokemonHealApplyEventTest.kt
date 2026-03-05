package com.pokemon.pokecenter.domain.event

import com.pokemon.pokecenter.domain.entity.Pokemon
import com.pokemon.pokecenter.domain.value.Health
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import kotlin.test.Test

class PokemonHealApplyEventTest {
	@Test
	fun `should create heal apply event from pokemon`() {
		val name = "Pikachu"
		val trainerName = "Ash"
		val health = Health(current = 50, maximum = 100)
		val healAmount = 30
		val pokemon =
			Pokemon(
				name = name,
				trainerName = trainerName,
				health = health,
			)

		val healedPokemon = pokemon.copy(health = health.copy(current = health.current + healAmount))
		val event = PokemonHealApplyEvent.from(healedPokemon, healAmount)

		assertEquals(pokemon.name, event.pokemonName)
		assertEquals(healAmount, event.medsApplied)
		assertNotEquals(pokemon.health.current, event.updatedHealth)
		assertNotNull(event.eventId)
		assertNotNull(event.occurredAt)
	}
}
