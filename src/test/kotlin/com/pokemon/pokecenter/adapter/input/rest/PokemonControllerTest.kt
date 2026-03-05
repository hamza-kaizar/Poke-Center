package com.pokemon.pokecenter.adapter.input.rest

import com.pokemon.pokecenter.domain.constant.Status
import com.pokemon.pokecenter.domain.entity.Pokemon
import com.pokemon.pokecenter.domain.value.Health
import com.pokemon.pokecenter.port.input.RegisterPokemonCommand
import com.pokemon.pokecenter.port.input.RegisterPokemonUseCase
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime

@WebMvcTest(PokemonController::class)
class PokemonControllerTest(
	@Autowired private val mockMvc: MockMvc,
) {
	@MockitoBean
	private lateinit var registerPokemon: RegisterPokemonUseCase

	private val testPokemon =
		Pokemon(
			id = 1,
			name = "Pikachu",
			health = Health(80, 100),
			trainerName = "Ash",
			arrivedAt = LocalDateTime.now(),
			status = Status.HEALING,
		)

	@Test
	fun `POST register should create pokemon and return CREATED status`() {
		val cmd = RegisterPokemonCommand("Pikachu", "Ash", 80, 100)
		doReturn(testPokemon).`when`(registerPokemon).register(cmd)

		mockMvc
			.post("/api/pokemon") {
				contentType = MediaType.APPLICATION_JSON
				content =
					"""
					{
						"name": "Pikachu",
						"trainerName": "Ash",
						"currentHealth": 80,
						"maximumHealth": 100
					}
					""".trimIndent()
			}.andExpect {
				status { isCreated() }
				jsonPath("$.id") { value(1) }
				jsonPath("$.name") { value("Pikachu") }
				jsonPath("$.trainerName") { value("Ash") }
				jsonPath("$.currentHealth") { value(80) }
				jsonPath("$.maximumHealth") { value(100) }
			}

		verify(registerPokemon, times(1)).register(cmd)
	}
}
