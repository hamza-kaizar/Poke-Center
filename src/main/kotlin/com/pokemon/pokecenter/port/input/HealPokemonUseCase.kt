package com.pokemon.pokecenter.port.input

import com.pokemon.pokecenter.domain.entity.Pokemon

interface HealPokemonUseCase {
	fun startHealing(pokemonId: Long): Pokemon
}
