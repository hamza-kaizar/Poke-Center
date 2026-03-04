package com.pokemon.pokecenter.domain.validation

import com.pokemon.pokecenter.domain.value.Health
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [HealthRatioValidator::class])
annotation class ValidHealthRatio(
	val message: String = "Current health cannot exceed maximum health",
	val groups: Array<KClass<*>> = [],
	val payload: Array<KClass<out Payload>> = [],
)

class HealthRatioValidator : ConstraintValidator<ValidHealthRatio, Health> {
	override fun isValid(
		health: Health,
		context: ConstraintValidatorContext,
	): Boolean = health.current <= health.maximum
}
