package com.pokemon.pokecenter.domain.event

import com.pokemon.pokecenter.domain.entity.Pokemon
import java.time.LocalDateTime
import java.util.UUID

data class PokemonHealApplyEvent(
	val pokemonId: Long,
	val pokemonName: String,
	val careStaff: String = "Nurse Joy",
	val medsApplied: Int,
	val updatedHealth: Int,
	override val eventId: String = UUID.randomUUID().toString(),
	override val occurredAt: LocalDateTime = LocalDateTime.now(),
) : DomainEvent() {
	companion object {
		fun from(
			pokemon: Pokemon,
			healingAmount: Int,
		): PokemonHealApplyEvent =
			PokemonHealApplyEvent(
				pokemonId = pokemon.id,
				pokemonName = pokemon.name,
				medsApplied = healingAmount,
				updatedHealth = pokemon.health.current,
			)
	}
}
