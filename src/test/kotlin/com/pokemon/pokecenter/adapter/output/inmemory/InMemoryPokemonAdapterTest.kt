package com.pokemon.pokecenter.adapter.output.inmemory

import com.pokemon.pokecenter.domain.entity.Pokemon
import com.pokemon.pokecenter.domain.value.Health
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class InMemoryPokemonAdapterTest {
	@Test
	fun `save should store and return pokemon with id`() {
		val adapter = InMemoryPokemonAdapter()
		val pokemon =
			Pokemon(
				id = 0,
				name = "Bulbasaur",
				trainerName = "Misty",
				health = Health(50, 100),
			)

		val savedPokemon = adapter.save(pokemon)

		val expectedPokemon = pokemon.copy(id = 1)
		assertEquals(expectedPokemon, savedPokemon)
	}
}
