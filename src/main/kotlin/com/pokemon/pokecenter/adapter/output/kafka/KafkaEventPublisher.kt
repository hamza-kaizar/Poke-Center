package com.pokemon.pokecenter.adapter.output.kafka

import com.pokemon.pokecenter.domain.event.PokemonArrivalEvent
import com.pokemon.pokecenter.port.output.PublishEventPort
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class KafkaEventPublisher(
	private val kafkaTemplate: KafkaTemplate<String, Any>,
) : PublishEventPort {
	private val logger = LoggerFactory.getLogger(this::class.java)

	@Retryable(
		value = [Exception::class],
		maxAttempts = 3,
		backoff = Backoff(delay = 500),
	)
	override fun publishPokemonArrival(event: PokemonArrivalEvent) {
		logger.info("Publishing pokemon arrival: ${event.name}")
		val message =
			MessageBuilder
				.withPayload(event)
				.setHeader(KafkaHeaders.TOPIC, "pokemon.arrivals")
				.setHeader(KafkaHeaders.KEY, event.pokemonId.toString())
				.build()

		kafkaTemplate.send(message).get()
		logger.info("Published pokemon arrival: ${event.eventId}")
	}

	@Recover
	fun recoverPublishPokemonArrival(
		ex: Exception,
		event: PokemonArrivalEvent,
	) {
		logger.error("Failed to publish pokemon arrival after 3 attempts: ${event.eventId}", ex)
	}
}
