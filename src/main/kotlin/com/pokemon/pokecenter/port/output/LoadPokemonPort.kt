package com.pokemon.pokecenter.port.output

import com.pokemon.pokecenter.domain.entity.Pokemon

interface LoadPokemonPort {
	fun loadById(id: Long): Pokemon

	fun loadAll(): List<Pokemon>
}
