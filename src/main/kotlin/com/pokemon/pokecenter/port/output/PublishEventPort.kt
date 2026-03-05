package com.pokemon.pokecenter.port.output

import com.pokemon.pokecenter.domain.event.PokemonArrivalEvent
import com.pokemon.pokecenter.domain.event.PokemonHealApplyEvent
import com.pokemon.pokecenter.domain.event.PokemonHealStartEvent

interface PublishEventPort {
	fun publishPokemonArrival(event: PokemonArrivalEvent)

	fun publishPokemonHealStart(event: PokemonHealStartEvent)

	fun publishPokemonHealApply(event: PokemonHealApplyEvent)
}
