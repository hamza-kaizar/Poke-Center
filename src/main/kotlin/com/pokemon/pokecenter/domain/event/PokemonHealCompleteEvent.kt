package com.pokemon.pokecenter.domain.event

import com.pokemon.pokecenter.domain.entity.Pokemon
import java.time.LocalDateTime
import java.util.UUID

data class PokemonHealCompleteEvent(
	val pokemonId: Long,
	val pokemonName: String,
	val careStaff: String = "Nurse Joy",
	val healthAtComplete: Int,
	override val eventId: String = UUID.randomUUID().toString(),
	override val occurredAt: LocalDateTime = LocalDateTime.now(),
) : DomainEvent() {
	companion object {
		fun from(pokemon: Pokemon): PokemonHealCompleteEvent =
			PokemonHealCompleteEvent(
				pokemonId = pokemon.id,
				pokemonName = pokemon.name,
				healthAtComplete = pokemon.health.current,
			)
	}
}
