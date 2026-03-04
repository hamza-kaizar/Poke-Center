package com.pokemon.pokecenter.domain.value

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.PositiveOrZero

data class Health(
	@PositiveOrZero(message = "Current health must be positive or zero")
	val current: Int,
	@Min(value = 1, message = "Maximum health must be positive")
	val maximum: Int,
)
