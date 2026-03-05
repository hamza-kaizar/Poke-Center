package com.pokemon.pokecenter.adapter.output.kafka

import com.pokemon.pokecenter.domain.event.PokemonArrivalEvent
import com.pokemon.pokecenter.port.output.PublishEventPort
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.kafka.support.SendResult
import org.springframework.messaging.support.MessageBuilder
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class KafkaEventPublisher(
	private val kafkaTemplate: KafkaTemplate<String, Any>,
) : PublishEventPort {
	private val logger = LoggerFactory.getLogger(this::class.java)

	@Async
	override fun publishPokemonArrival(event: PokemonArrivalEvent) {
		logger.info("Publishing pokemon arrival: ${event.name}")
		val message =
			MessageBuilder
				.withPayload(event)
				.setHeader(KafkaHeaders.TOPIC, "pokemon.arrivals")
				.setHeader(KafkaHeaders.KEY, event.pokemonId.toString())
				.build()

		kafkaTemplate
			.send(message)
			.whenComplete { result: SendResult<String, Any>?, ex: Throwable? ->
				if (ex != null) {
					logger.error("Failed to publish pokemon arrival: ${event.eventId}", ex)
				} else if (result != null) {
					logger.info("Published pokemon arrival: ${event.eventId} in topic ${result.recordMetadata.topic()}")
				}
			}
	}
}
