package com.pokemon.pokecenter.domain.event

import com.pokemon.pokecenter.domain.entity.Pokemon
import com.pokemon.pokecenter.domain.value.Health
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import kotlin.test.Test

class PokemonHealStartEventTest {
	@Test
	fun `should create heal start event from pokemon`() {
		val name = "Pikachu"
		val trainerName = "Ash"
		val health = Health(current = 50, maximum = 100)
		val pokemon =
			Pokemon(
				name = name,
				trainerName = trainerName,
				health = health,
			)

		val event = PokemonHealStartEvent.from(pokemon)

		assertEquals(pokemon.name, event.pokemonName)
		assertEquals(pokemon.health.current, event.healthAtStart)
		assertNotNull(event.eventId)
		assertNotNull(event.occurredAt)
	}
}
