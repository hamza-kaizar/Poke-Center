package com.pokemon.pokecenter.domain.value

import com.pokemon.pokecenter.domain.validation.ValidHealthRatio
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.PositiveOrZero

@ValidHealthRatio
data class Health(
	@PositiveOrZero(message = "Current health must be positive or zero")
	val current: Int,
	@Min(value = 1, message = "Maximum health must be positive")
	val maximum: Int,
) {
	val isHealthy: Boolean
		get() = current == maximum
}
