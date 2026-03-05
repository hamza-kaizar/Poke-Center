package com.pokemon.pokecenter.port.output

import com.pokemon.pokecenter.domain.event.PokemonArrivalEvent

interface PublishEventPort {
	fun publishPokemonArrival(event: PokemonArrivalEvent)
}
