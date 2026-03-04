package com.pokemon.pokecenter.service

import com.pokemon.pokecenter.domain.constant.Status
import com.pokemon.pokecenter.domain.entity.Pokemon
import com.pokemon.pokecenter.domain.value.Health
import com.pokemon.pokecenter.port.input.RegisterPokemonCommand
import com.pokemon.pokecenter.port.output.LoadPokemonPort
import com.pokemon.pokecenter.port.output.SavePokemonPort
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class PokemonServiceTest {
	private val loadPort = mockk<LoadPokemonPort>()
	private val savePort = mockk<SavePokemonPort>()
	private val service = PokemonService(loadPort, savePort)

	@Test
	fun `register should create and save pokemon`() {
		val command =
			RegisterPokemonCommand(
				name = "Pikachu",
				trainerName = "Ash",
				currentHealth = 80,
				maximumHealth = 100,
			)
		val saved =
			Pokemon(
				id = 1,
				name = "Pikachu",
				trainerName = "Ash",
				health = Health(80, 100),
			)

		every { savePort.save(any()) } returns saved

		val result = service.register(command)

		assertEquals("Pikachu", result.name)
		assertEquals(1, result.id)
		verify { savePort.save(any()) }
	}

	@Test
	fun `startHealing should load, start healing and save pokemon`() {
		val pokemon =
			Pokemon(
				id = 1,
				name = "Bulbasaur",
				trainerName = "Misty",
				health = Health(50, 100),
			)
		val healingPokemon = pokemon.copy(health = Health(50, 100))

		every { loadPort.loadById(1) } returns pokemon
		every { savePort.save(any()) } returns healingPokemon

		val result = service.startHealing(1)

		assertEquals("Bulbasaur", result.name)
		assertEquals(1, result.id)
		verify { loadPort.loadById(1) }
		verify { savePort.save(any()) }
	}
}
