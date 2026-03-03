package com.pokemon.pokecenter.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

@Entity
data class Pokemon(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0,
	@NotBlank(message = "Pokemon name must not be blank")
	val name: String,
	@NotBlank(message = "Trainer name must not be blank")
	val trainerName: String,
	var arrivedAt: LocalDateTime = LocalDateTime.now(),
) {
	companion object {
		fun create(
			name: String,
			trainerName: String,
		): Pokemon =
			Pokemon(
				name = name,
				trainerName = trainerName,
				arrivedAt = LocalDateTime.now(),
			)
	}
}
