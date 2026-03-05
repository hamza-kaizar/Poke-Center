package com.pokemon.pokecenter.adapter.input.rest

import com.pokemon.pokecenter.adapter.input.rest.dto.PokemonResponse
import com.pokemon.pokecenter.adapter.input.rest.dto.RegisterPokemonRequest
import com.pokemon.pokecenter.port.input.RegisterPokemonCommand
import com.pokemon.pokecenter.port.input.RegisterPokemonUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/pokemon")
class PokemonController(
	private val registerPokemon: RegisterPokemonUseCase,
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
}
