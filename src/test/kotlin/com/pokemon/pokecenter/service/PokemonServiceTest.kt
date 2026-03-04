package com.pokemon.pokecenter.service

import com.pokemon.pokecenter.domain.entity.Pokemon
import com.pokemon.pokecenter.domain.value.Health
import com.pokemon.pokecenter.port.input.RegisterPokemonCommand
import com.pokemon.pokecenter.port.output.SavePokemonPort
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class PokemonServiceTest {
	private val savePort = mockk<SavePokemonPort>()
	private val service = PokemonService(savePort)

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
}
