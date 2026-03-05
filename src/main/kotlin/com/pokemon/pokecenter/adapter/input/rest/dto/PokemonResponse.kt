package com.pokemon.pokecenter.adapter.input.rest.dto

import com.pokemon.pokecenter.domain.entity.Pokemon
import java.time.LocalDateTime

data class PokemonResponse(
	val id: Long,
	val name: String,
	val currentHealth: Int,
	val maximumHealth: Int,
	val healthPercentage: Int,
	val trainerName: String,
	val status: String,
	val arrivedAt: LocalDateTime,
) {
	companion object {
		fun fromDomain(pokemon: Pokemon) =
			PokemonResponse(
				id = pokemon.id,
				name = pokemon.name,
				currentHealth = pokemon.health.current,
				maximumHealth = pokemon.health.maximum,
				healthPercentage = pokemon.health.recoveryPercentage,
				trainerName = pokemon.trainerName,
				status = pokemon.status.name,
				arrivedAt = pokemon.arrivedAt,
			)
	}
}
