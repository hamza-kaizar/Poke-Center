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

	@Test
	fun `loadById should return stored pokemon`() {
		val adapter = InMemoryPokemonAdapter()
		val pokemon =
			Pokemon(
				id = 0,
				name = "Onyx",
				trainerName = "Brock",
				health = Health(60, 100),
			)
		val savedPokemon = adapter.save(pokemon)

		val loadedPokemon = adapter.loadById(savedPokemon.id)

		assertEquals(savedPokemon, loadedPokemon)
	}

	@Test
	fun `loadById should throw exception if pokemon not found`() {
		val adapter = InMemoryPokemonAdapter()

		try {
			adapter.loadById(999)
		} catch (e: NoSuchElementException) {
			assertEquals("Pokemon not found: 999", e.message)
		}
	}

	@Test
	fun `loadAll should return all stored pokemons`() {
		val adapter = InMemoryPokemonAdapter()
		val pokemon1 =
			Pokemon(
				id = 0,
				name = "Charmander",
				trainerName = "Ash",
				health = Health(70, 100),
			)
		val pokemon2 =
			Pokemon(
				id = 0,
				name = "Squirtle",
				trainerName = "Ash",
				health = Health(80, 100),
			)
		val savedPokemon1 = adapter.save(pokemon1)
		val savedPokemon2 = adapter.save(pokemon2)

		val allPokemon = adapter.loadAll()

		assertEquals(listOf(savedPokemon1, savedPokemon2), allPokemon)
	}
}
