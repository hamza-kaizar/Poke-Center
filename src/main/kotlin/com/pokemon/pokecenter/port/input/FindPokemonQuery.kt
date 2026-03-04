package com.pokemon.pokecenter.port.input

import com.pokemon.pokecenter.domain.entity.Pokemon

interface FindPokemonQuery {
	fun findById(id: Long): Pokemon

	fun findAll(): List<Pokemon>
}
