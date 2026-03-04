package com.pokemon.pokecenter.port.output

import com.pokemon.pokecenter.domain.entity.Pokemon

interface SavePokemonPort {
	fun save(pokemon: Pokemon): Pokemon
}
