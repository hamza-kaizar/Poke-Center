package com.pokemon.pokecenter.adapter.output.kafka

import com.pokemon.pokecenter.domain.event.DomainEvent
import com.pokemon.pokecenter.domain.event.PokemonArrivalEvent
import com.pokemon.pokecenter.domain.event.PokemonHealApplyEvent
import com.pokemon.pokecenter.domain.event.PokemonHealCompleteEvent
import com.pokemon.pokecenter.domain.event.PokemonHealStartEvent
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
	override fun publishPokemonArrival(event: PokemonArrivalEvent) = publishEvent(event, "pokemon.arrivals", event.pokemonId.toString())

	@Async
	override fun publishPokemonHealStart(event: PokemonHealStartEvent) = publishEvent(event, "pokemon.healing", event.pokemonId.toString())

	@Async
	override fun publishPokemonHealApply(event: PokemonHealApplyEvent) = publishEvent(event, "pokemon.healing", event.pokemonId.toString())

	@Async
	override fun publishPokemonHealComplete(event: PokemonHealCompleteEvent) =
		publishEvent(event, "pokemon.healed", event.pokemonId.toString())

	fun publishEvent(
		event: DomainEvent,
		topic: String,
		key: String,
	) {
		logger.info("Publishing: $event")

		val message =
			MessageBuilder
				.withPayload(event)
				.setHeader(KafkaHeaders.TOPIC, topic)
				.setHeader(KafkaHeaders.KEY, key)
				.build()

		kafkaTemplate
			.send(message)
			.whenComplete { result: SendResult<String, Any>?, ex: Throwable? ->
				if (ex != null) {
					logger.error("Failed to publish $topic: ${event.eventId}", ex)
				} else if (result != null) {
					logger.info("Published $topic: ${event.eventId}")
				}
			}
	}
}
