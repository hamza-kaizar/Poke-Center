package com.pokemon.pokecenter.domain.entity

import com.pokemon.pokecenter.domain.value.Health
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated
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
	@Embedded @Valid
	val health: Health,
	var arrivedAt: LocalDateTime = LocalDateTime.now(),
) {
	companion object {
		fun create(
			name: String,
			trainerName: String,
			health: Health,
		): Pokemon =
			Pokemon(
				name = name,
				trainerName = trainerName,
				health = health,
				arrivedAt = LocalDateTime.now(),
			)
	}
}
