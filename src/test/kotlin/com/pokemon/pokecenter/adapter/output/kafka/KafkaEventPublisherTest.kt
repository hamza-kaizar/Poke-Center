package com.pokemon.pokecenter.adapter.output.kafka

import com.pokemon.pokecenter.domain.event.PokemonArrivalEvent
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import java.util.concurrent.CompletableFuture
import kotlin.test.assertEquals

class KafkaEventPublisherTest {
	private val kafkaTemplate = mockk<KafkaTemplate<String, Any>>()
	private val publisher = KafkaEventPublisher(kafkaTemplate)

	private val testEvent =
		PokemonArrivalEvent(
			pokemonId = 1L,
			name = "Pikachu",
			trainerName = "Ash",
			initialHealth = 50,
			maximumHealth = 100,
		)

	@Test
	fun `should send message to pokemon arrivals topic`() {
		// Arrange
		val messageSlot = slot<Message<*>>()
		every { kafkaTemplate.send(capture(messageSlot)) } returns
			CompletableFuture.completedFuture(mockk())

		// Act
		publisher.publishPokemonArrival(testEvent)

		// Assert
		assertEquals(
			"pokemon.arrivals",
			messageSlot.captured.headers[KafkaHeaders.TOPIC],
		)
		verify(exactly = 1) { kafkaTemplate.send(any<Message<*>>()) }
	}

	@Test
	fun `should use pokemonId as partition key for event ordering`() {
		// Arrange
		val messageSlot = slot<Message<*>>()
		every { kafkaTemplate.send(capture(messageSlot)) } returns
			CompletableFuture.completedFuture(mockk())

		// Act
		publisher.publishPokemonArrival(testEvent)

		// Assert
		assertEquals(
			testEvent.pokemonId.toString(),
			messageSlot.captured.headers[KafkaHeaders.KEY],
		)
	}

	@Test
	fun `should include pokemon arrival event as message payload`() {
		// Arrange
		val messageSlot = slot<Message<*>>()
		every { kafkaTemplate.send(capture(messageSlot)) } returns
			CompletableFuture.completedFuture(mockk())

		// Act
		publisher.publishPokemonArrival(testEvent)

		// Assert
		assertEquals(testEvent, messageSlot.captured.payload)
	}

	@Test
	fun `should handle exceptions gracefully during message sending`() {
		// Arrange
		val exception = RuntimeException("Kafka send failed")
		every { kafkaTemplate.send(any<Message<*>>()) } returns
			CompletableFuture.failedFuture(exception)

		// Act & Assert
		assertDoesNotThrow { publisher.publishPokemonArrival(testEvent) }
	}
}
