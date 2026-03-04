package com.pokemon.pokecenter.port.input

import com.pokemon.pokecenter.domain.entity.Pokemon

interface HealPokemonUseCase {
	fun startHealing(pokemonId: Long): Pokemon

	fun applyHealing(
		pokemonId: Long,
		amount: Int,
	): Pokemon

	fun completeHealing(pokemonId: Long): Pokemon
}
