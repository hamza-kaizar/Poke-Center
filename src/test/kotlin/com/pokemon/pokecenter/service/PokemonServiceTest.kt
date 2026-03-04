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
		val name = "Pikachu"
		val trainerName = "Ash"
		val health = Health(current = 80, maximum = 100)
		val command =
			RegisterPokemonCommand(
				name = name,
				trainerName = trainerName,
				currentHealth = health.current,
				maximumHealth = health.maximum,
			)
		val saved =
			Pokemon(
				id = 1,
				name = name,
				trainerName = trainerName,
				health = health,
			)

		every { savePort.save(any()) } returns saved

		val result = service.register(command)

		assertEquals(name, result.name)
		assertEquals(saved.id, result.id)
		verify { savePort.save(any()) }
	}

	@Test
	fun `startHealing should load, start healing and save pokemon`() {
		val name = "Bulbasaur"
		val trainerName = "Misty"
		val health = Health(current = 80, maximum = 100)
		val pokemon =
			Pokemon(
				id = 1,
				name = name,
				trainerName = trainerName,
				health = health,
			)
		val healingPokemon = pokemon.copy(status = Status.HEALING)

		every { loadPort.loadById(1) } returns pokemon
		every { savePort.save(any()) } returns healingPokemon

		val result = service.startHealing(1)

		assertEquals(pokemon.name, result.name)
		assertEquals(pokemon.id, result.id)
		assertEquals(Status.HEALING, result.status)
		verify { loadPort.loadById(1) }
		verify { savePort.save(any()) }
	}
}
