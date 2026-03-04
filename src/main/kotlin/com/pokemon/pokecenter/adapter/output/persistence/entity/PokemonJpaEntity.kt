package com.pokemon.pokecenter.adapter.output.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "pokemon")
data class PokemonJpaEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0,
	val name: String,
	val trainerName: String,
	val currentHealth: Int,
	val maximumHealth: Int,
	val arrivedAt: LocalDateTime = LocalDateTime.now(),
	@Enumerated(EnumType.STRING)
	val status: Status = Status.ARRIVED,
) {
	enum class Status {
		ARRIVED,
		HEALING,
		HEALED,
		DEPARTED,
	}
}
