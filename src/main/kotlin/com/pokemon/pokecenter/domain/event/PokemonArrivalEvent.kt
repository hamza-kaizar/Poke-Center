package com.pokemon.pokecenter.domain.event

import com.pokemon.pokecenter.domain.entity.Pokemon
import java.time.LocalDateTime
import java.util.UUID

data class PokemonArrivalEvent(
	val pokemonId: Long,
	val name: String,
	val trainerName: String,
	val initialHealth: Int,
	val maximumHealth: Int,
	override val eventId: String = UUID.randomUUID().toString(),
	override val occurredAt: LocalDateTime = LocalDateTime.now(),
) : DomainEvent() {
	companion object {
		fun from(pokemon: Pokemon): PokemonArrivalEvent =
			PokemonArrivalEvent(
				pokemonId = pokemon.id,
				name = pokemon.name,
				trainerName = pokemon.trainerName,
				initialHealth = pokemon.health.current,
				maximumHealth = pokemon.health.maximum,
			)
	}
}
