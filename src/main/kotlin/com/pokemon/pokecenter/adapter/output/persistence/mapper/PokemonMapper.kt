package com.pokemon.pokecenter.adapter.output.persistence.mapper

import com.pokemon.pokecenter.adapter.output.persistence.entity.PokemonJpaEntity
import com.pokemon.pokecenter.domain.constant.Status
import com.pokemon.pokecenter.domain.entity.Pokemon
import com.pokemon.pokecenter.domain.value.Health

fun PokemonJpaEntity.toDomain(): Pokemon =
	Pokemon(
		id = id,
		name = name,
		health = Health(current = currentHealth, maximum = maximumHealth),
		trainerName = trainerName,
		arrivedAt = arrivedAt,
		status = Status.valueOf(status.name),
	)

fun Pokemon.toJpaEntity(): PokemonJpaEntity =
	PokemonJpaEntity(
		id = id,
		name = name,
		currentHealth = health.current,
		maximumHealth = health.maximum,
		trainerName = trainerName,
		arrivedAt = arrivedAt,
		status = PokemonJpaEntity.Status.valueOf(status.name),
	)
