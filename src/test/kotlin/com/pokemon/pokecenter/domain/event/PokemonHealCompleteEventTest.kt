package com.pokemon.pokecenter.domain.event

import com.pokemon.pokecenter.domain.entity.Pokemon
import com.pokemon.pokecenter.domain.value.Health
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import kotlin.test.Test

class PokemonHealCompleteEventTest {
	@Test
	fun `should create heal complete event from pokemon`() {
		val name = "Pikachu"
		val trainerName = "Ash"
		val health = Health(current = 50, maximum = 100)
		val pokemon =
			Pokemon(
				name = name,
				trainerName = trainerName,
				health = health,
			)

		val healedPokemon = pokemon.copy(health = health.copy(current = 100))
		val event = PokemonHealCompleteEvent.from(healedPokemon)

		assertEquals(pokemon.name, event.pokemonName)
		assertNotEquals(pokemon.health.current, event.healthAtComplete)
		assertNotNull(event.eventId)
		assertNotNull(event.occurredAt)
	}
}
