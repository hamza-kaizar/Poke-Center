package com.pokemon.pokecenter.adapter.input.rest

import com.pokemon.pokecenter.adapter.input.rest.dto.PokemonResponse
import com.pokemon.pokecenter.adapter.input.rest.dto.RegisterPokemonRequest
import com.pokemon.pokecenter.port.input.FindPokemonQuery
import com.pokemon.pokecenter.port.input.HealPokemonUseCase
import com.pokemon.pokecenter.port.input.RegisterPokemonCommand
import com.pokemon.pokecenter.port.input.RegisterPokemonUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/pokemon")
class PokemonController(
	private val registerPokemon: RegisterPokemonUseCase,
	private val findPokemon: FindPokemonQuery,
	private val healPokemon: HealPokemonUseCase,
) {
	@PostMapping
	fun register(
		@RequestBody request: RegisterPokemonRequest,
	): ResponseEntity<PokemonResponse> {
		val command =
			RegisterPokemonCommand(
				name = request.name,
				trainerName = request.trainerName,
				currentHealth = request.currentHealth,
				maximumHealth = request.maximumHealth,
			)
		val pokemon = registerPokemon.register(command)
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(PokemonResponse.fromDomain(pokemon))
	}

	@GetMapping("/{id}")
	fun get(
		@PathVariable id: Long,
	): ResponseEntity<PokemonResponse> =
		try {
			val pokemon = findPokemon.findById(id)
			ResponseEntity.ok(PokemonResponse.fromDomain(pokemon))
		} catch (e: NoSuchElementException) {
			ResponseEntity.notFound().build()
		}

	@GetMapping
	fun getAll(): ResponseEntity<List<PokemonResponse>> {
		val allPokemon = findPokemon.findAll()
		return ResponseEntity.ok(allPokemon.map { PokemonResponse.fromDomain(it) })
	}

	@PostMapping("/{id}/heal/start")
	fun startHealing(
		@PathVariable id: Long,
	): ResponseEntity<PokemonResponse> =
		try {
			val healing = healPokemon.startHealing(id)
			ResponseEntity.ok(PokemonResponse.fromDomain(healing))
		} catch (e: Exception) {
			ResponseEntity.badRequest().build()
		}
}
