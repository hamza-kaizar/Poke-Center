package com.pokemon.pokecenter.adapter.input.rest

import com.pokemon.pokecenter.domain.constant.Status
import com.pokemon.pokecenter.domain.entity.Pokemon
import com.pokemon.pokecenter.domain.value.Health
import com.pokemon.pokecenter.port.input.FindPokemonQuery
import com.pokemon.pokecenter.port.input.HealPokemonUseCase
import com.pokemon.pokecenter.port.input.RegisterPokemonCommand
import com.pokemon.pokecenter.port.input.RegisterPokemonUseCase
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime
import kotlin.collections.emptyList

@WebMvcTest(PokemonController::class)
class PokemonControllerTest(
	@Autowired private val mockMvc: MockMvc,
) {
	@MockitoBean
	private lateinit var registerPokemon: RegisterPokemonUseCase

	@MockitoBean
	private lateinit var findPokemon: FindPokemonQuery

	@MockitoBean
	private lateinit var healPokemon: HealPokemonUseCase

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

	@Test
	fun `GET by id should return pokemon when found`() {
		doReturn(testPokemon).`when`(findPokemon).findById(1)

		mockMvc.get("/api/pokemon/1").andExpect {
			status { isOk() }
			jsonPath("$.id") { value(1) }
			jsonPath("$.name") { value("Pikachu") }
			jsonPath("$.trainerName") { value("Ash") }
		}

		verify(findPokemon, times(1)).findById(1)
	}

	@Test
	fun `GET by id should return 404 when pokemon not found`() {
		`when`(findPokemon.findById(999L)).thenThrow(NoSuchElementException("Pokemon not found"))

		mockMvc.get("/api/pokemon/999").andExpect {
			status { isNotFound() }
		}
	}

	@Test
	fun `GET all should return list of pokemon`() {
		val pokemonList =
			listOf(
				testPokemon,
				testPokemon.copy(id = 2, name = "Charmander"),
			)

		doReturn(pokemonList).`when`(findPokemon).findAll()

		mockMvc.get("/api/pokemon").andExpect {
			status { isOk() }
			jsonPath("$[0].id") { value(1) }
			jsonPath("$[0].name") { value("Pikachu") }
			jsonPath("$[1].id") { value(2) }
			jsonPath("$[1].name") { value("Charmander") }
		}

		verify(findPokemon, times(1)).findAll()
	}

	@Test
	fun `GET all should return empty list when no pokemon are found`() {
		doReturn(emptyList<Pokemon>()).`when`(findPokemon).findAll()

		mockMvc.get("/api/pokemon").andExpect {
			status { isOk() }
			jsonPath("$.length()") { value(0) }
		}

		verify(findPokemon, times(1)).findAll()
	}

	@Test
	fun `POST heal start should transition pokemon to healing status`() {
		val healingPokemon = testPokemon.copy(status = Status.HEALING)

		doReturn(healingPokemon).`when`(healPokemon).startHealing(1)

		mockMvc.post("/api/pokemon/1/heal/start").andExpect {
			status { isOk() }
			jsonPath("$.status") { value("HEALING") }
		}

		verify(healPokemon, times(1)).startHealing(1)
	}

	@Test
	fun `POST start heal should return 400 when start healing fails`() {
		`when`(healPokemon.startHealing(999L)).thenThrow(IllegalStateException("Pokemon not found"))

		mockMvc
			.post("/api/pokemon/999/heal/start") {
				contentType = MediaType.APPLICATION_JSON
				content = """{"amount": 10}"""
			}.andExpect {
				status { isBadRequest() }
			}
	}

	@Test
	fun `POST apply heal should apply healing and return updated pokemon`() {
		val healedPokemon = testPokemon.copy(health = Health(95, 100))

		doReturn(healedPokemon).`when`(healPokemon).applyHealing(1, 15)

		mockMvc
			.post("/api/pokemon/1/heal/apply") {
				contentType = MediaType.APPLICATION_JSON
				content = """{"amount": 15}"""
			}.andExpect {
				status { isOk() }
				jsonPath("$.currentHealth") { value(95) }
				jsonPath("$.maximumHealth") { value(100) }
			}

		verify(healPokemon, times(1)).applyHealing(1, 15)
	}

	@Test
	fun `POST apply heal should return 400 when healing fails`() {
		`when`(healPokemon.applyHealing(999L, 10)).thenThrow(IllegalStateException("Pokemon not found"))

		mockMvc
			.post("/api/pokemon/999/heal/apply") {
				contentType = MediaType.APPLICATION_JSON
				content = """{"amount": 10}"""
			}.andExpect {
				status { isBadRequest() }
			}
	}

	@Test
	fun `POST complete healing should transition pokemon to healed status`() {
		val healedPokemon = testPokemon.copy(health = Health(100, 100), status = Status.HEALED)

		doReturn(healedPokemon).`when`(healPokemon).completeHealing(1)

		mockMvc.post("/api/pokemon/1/heal/complete").andExpect {
			status { isOk() }
			jsonPath("$.status") { value("HEALED") }
			jsonPath("$.currentHealth") { value(100) }
		}

		verify(healPokemon, times(1)).completeHealing(1)
	}

	@Test
	fun `POST complete heal should return 400 when complete healing fails`() {
		`when`(healPokemon.completeHealing(999L)).thenThrow(IllegalStateException("Pokemon not found"))

		mockMvc
			.post("/api/pokemon/999/heal/complete") {
				contentType = MediaType.APPLICATION_JSON
				content = """{"amount": 10}"""
			}.andExpect {
				status { isBadRequest() }
			}
	}
}
