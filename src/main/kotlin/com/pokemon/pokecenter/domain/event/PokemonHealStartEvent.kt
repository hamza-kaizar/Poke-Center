package com.pokemon.pokecenter.domain.event

import com.pokemon.pokecenter.domain.entity.Pokemon
import java.time.LocalDateTime
import java.util.UUID

data class PokemonHealStartEvent(
	val pokemonId: Long,
	val pokemonName: String,
	val careStaff: String = "Nurse Joy",
	val healthAtStart: Int,
	override val eventId: String = UUID.randomUUID().toString(),
	override val occurredAt: LocalDateTime = LocalDateTime.now(),
) : DomainEvent() {
	companion object {
		fun from(pokemon: Pokemon): PokemonHealStartEvent =
			PokemonHealStartEvent(
				pokemonId = pokemon.id,
				pokemonName = pokemon.name,
				healthAtStart = pokemon.health.current,
			)
	}
}
