package com.pokemon.pokecenter.domain.entity

import com.pokemon.pokecenter.domain.value.Health
import com.pokemon.pokecenter.domain.value.Status
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class Pokemon(
	val id: Long = 0,
	@NotBlank(message = "Pokemon name must not be blank")
	val name: String,
	@NotBlank(message = "Trainer name must not be blank")
	val trainerName: String,
	@Valid
	val health: Health,
	val status: Status = Status.ARRIVED,
	var arrivedAt: LocalDateTime = LocalDateTime.now(),
)
