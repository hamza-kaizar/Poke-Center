package com.pokemon.pokecenter.adapter.output.persistence

import com.pokemon.pokecenter.adapter.output.persistence.mapper.toJpaEntity
import com.pokemon.pokecenter.domain.entity.Pokemon
import com.pokemon.pokecenter.domain.value.Health
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.anyLong
import java.util.Optional
import kotlin.test.Test

class PersistencePokemonAdapterTest {
	private val repository = mockk<SpringDataPokemonRepository>()
	val adapter = PersistencePokemonAdapter(repository)

	@Test
	fun `save should call repository and return saved pokemon`() {
		val pokemon =
			Pokemon(
				id = 0,
				name = "Bulbasaur",
				trainerName = "Misty",
				health = Health(50, 100),
			)

		every { repository.save(any()) } returns pokemon.copy(id = 1).toJpaEntity()

		val savedPokemon = adapter.save(pokemon)

		val expectedPokemon = pokemon.copy(id = 1)
		assertEquals(expectedPokemon, savedPokemon)
	}

	@Test
	fun `loadById should call repository and return pokemon`() {
		val pokemon =
			Pokemon(
				id = 0,
				name = "Onyx",
				trainerName = "Brock",
				health = Health(60, 100),
			)

		every { repository.findById(anyLong()) } returns Optional.of(pokemon.toJpaEntity())

		val loadedPokemon = adapter.loadById(pokemon.id)

		assertEquals(pokemon, loadedPokemon)
	}

	@Test
	fun `loadById should throw exception when pokemon not found`() {
		val id = 999L
		every { repository.findById(id) } returns Optional.empty()

		val exception =
			assertThrows<NoSuchElementException> {
				adapter.loadById(id)
			}

		assertEquals("Pokemon not found: 999", exception.message)
		verify(exactly = 1) { repository.findById(id) }
	}

	@Test
	fun `loadAll should call repository and return list of pokemon`() {
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

		every { repository.findAll() } returns listOf(pokemon1.toJpaEntity(), pokemon2.toJpaEntity())

		val allPokemon = adapter.loadAll()

		assertEquals(listOf(pokemon1, pokemon2), allPokemon)
	}
}
