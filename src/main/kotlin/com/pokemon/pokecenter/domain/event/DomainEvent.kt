package com.pokemon.pokecenter.domain.event

import java.time.LocalDateTime

sealed class DomainEvent {
	abstract val eventId: String
	abstract val occurredAt: LocalDateTime
}
