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

	@Test
	fun `applyHealing should load, apply healing and save pokemon`() {
		val name = "Charmander"
		val trainerName = "Ash"
		val health = Health(70, 100)
		val pokemon =
			Pokemon(
				id = 1,
				name = name,
				trainerName = trainerName,
				health = health,
				status = Status.HEALING,
			)
		val healedPokemon = pokemon.copy(health = Health(90, 100))

		every { loadPort.loadById(1) } returns pokemon
		every { savePort.save(any()) } returns healedPokemon

		val result = service.applyHealing(1, 20)

		assertEquals(pokemon.name, result.name)
		assertEquals(pokemon.id, result.id)
		assertEquals(healedPokemon.health.current, result.health.current)
		verify { loadPort.loadById(1) }
		verify { savePort.save(any()) }
	}

	@Test
	fun `completeHealing should load, complete healing and save pokemon`() {
		val name = "Squirtle"
		val trainerName = "Ash"
		val health = Health(70, 100)
		val pokemon =
			Pokemon(
				id = 1,
				name = name,
				trainerName = trainerName,
				health = health,
				status = Status.HEALING,
			)
		val healedPokemon = pokemon.copy(health = Health(100, 100))

		every { loadPort.loadById(1) } returns pokemon
		every { savePort.save(any()) } returns healedPokemon

		val result = service.completeHealing(1)

		assertEquals(pokemon.name, result.name)
		assertEquals(pokemon.id, result.id)
		assertEquals(healedPokemon.health.current, result.health.current)
		verify { loadPort.loadById(1) }
		verify { savePort.save(any()) }
	}

	@Test
	fun `findById should load and return pokemon`() {
		val name = "Eevee"
		val trainerName = "Gary"
		val health = Health(60, 100)
		val pokemon =
			Pokemon(
				id = 1,
				name = name,
				trainerName = trainerName,
				health = health,
			)

		every { loadPort.loadById(1) } returns pokemon

		val result = service.findById(1)

		assertEquals(pokemon.name, result.name)
		assertEquals(pokemon.id, result.id)
		assertEquals(pokemon.health.current, result.health.current)
		verify { loadPort.loadById(1) }
	}

	@Test
	fun `findAll should load and return all pokemons`() {
		val pokemon1 =
			Pokemon(
				id = 1,
				name = "Pidgey",
				trainerName = "Ash",
				health = Health(50, 100),
			)
		val pokemon2 =
			Pokemon(
				id = 2,
				name = "Rattata",
				trainerName = "Ash",
				health = Health(40, 100),
			)
		val allPokemon = listOf(pokemon1, pokemon2)

		every { loadPort.loadAll() } returns allPokemon

		val result = service.findAll()

		assertEquals(2, result.size)
		assertEquals(pokemon1.name, result[0].name)
		assertEquals(pokemon2.name, result[1].name)
		verify { loadPort.loadAll() }
	}
}
