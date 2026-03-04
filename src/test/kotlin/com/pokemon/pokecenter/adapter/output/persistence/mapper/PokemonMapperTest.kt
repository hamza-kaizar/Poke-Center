package com.pokemon.pokecenter.adapter.output.persistence.mapper

import com.pokemon.pokecenter.adapter.output.persistence.entity.PokemonJpaEntity
import com.pokemon.pokecenter.domain.entity.Pokemon
import com.pokemon.pokecenter.domain.value.Health
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class PokemonMapperTest {
	@Test
	fun `toJpaEntity should map domain to entity correctly`() {
		val domain =
			Pokemon(
				id = 1,
				name = "Pikachu",
				trainerName = "Ash",
				health = Health(current = 80, maximum = 100),
			)
		val entity = domain.toJpaEntity()

		assertEquals(domain.id, entity.id)
		assertEquals(domain.name, entity.name)
		assertEquals(domain.trainerName, entity.trainerName)
		assertEquals(domain.health.current, entity.currentHealth)
		assertEquals(domain.health.maximum, entity.maximumHealth)
		assertEquals(domain.status.name, entity.status.name)
		assertEquals(domain.arrivedAt, entity.arrivedAt)
	}

	@Test
	fun `toDomainEntity should map entity to domain correctly`() {
		val entity =
			PokemonJpaEntity(
				id = 1,
				name = "Bulbasaur",
				trainerName = "Misty",
				currentHealth = 60,
				maximumHealth = 100,
				status = PokemonJpaEntity.Status.ARRIVED,
			)
		val domain = entity.toDomain()

		assertEquals(entity.id, domain.id)
		assertEquals(entity.name, domain.name)
		assertEquals(entity.trainerName, domain.trainerName)
		assertEquals(entity.currentHealth, domain.health.current)
		assertEquals(entity.maximumHealth, domain.health.maximum)
		assertEquals(entity.status.name, domain.status.name)
		assertEquals(entity.arrivedAt, domain.arrivedAt)
	}
}
