package com.pokemon.pokecenter.domain.value

import com.pokemon.pokecenter.domain.validation.ValidHealthRatio
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.PositiveOrZero

@Embeddable
@ValidHealthRatio
data class Health(
	@PositiveOrZero(message = "Current health must be positive or zero")
	val current: Int,
	@Min(value = 1, message = "Maximum health must be positive")
	val maximum: Int,
) {
	val isHealthy: Boolean
		get() = current == maximum

	val isCritical: Boolean
		get() = current < 25

	val recoveryPercentage: Int
		get() = (current * 100) / maximum

	fun heal(amount: Int): Health {
		require(amount > 0) { "Healing amount must be positive" }
		val newHealth = (current + amount).coerceAtMost(maximum)
		return copy(current = newHealth)
	}

	fun damage(amount: Int): Health {
		require(amount > 0) { "Damage amount must be positive" }
		val newHealth = (current - amount).coerceAtLeast(0)
		return copy(current = newHealth)
	}
}
