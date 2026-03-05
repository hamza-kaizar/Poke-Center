package com.pokemon.pokecenter.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.boot.kafka.autoconfigure.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties

@Configuration
@EnableKafka
class KafkaConfiguration(
	private val kafkaProperties: KafkaProperties,
) {
	@Bean
	fun consumerFactory(): ConsumerFactory<String, Any> {
		val configs = kafkaProperties.buildConsumerProperties().toMutableMap()
		configs[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
		configs[ConsumerConfig.GROUP_ID_CONFIG] = "pokecenter-app"
		return DefaultKafkaConsumerFactory(configs)
	}

	@Bean
	fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Any> {
		val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
		factory.setConsumerFactory(consumerFactory())
		factory.containerProperties.ackMode = ContainerProperties.AckMode.BATCH
		return factory
	}
}
