package com.pokemon.pokecenter.port.input

import com.pokemon.pokecenter.domain.entity.Pokemon

interface RegisterPokemonUseCase {
	fun register(command: RegisterPokemonCommand): Pokemon
}

data class RegisterPokemonCommand(
	val name: String,
	val trainerName: String,
	val currentHealth: Int,
	val maximumHealth: Int,
)
