package com.pokemon.pokecenter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class PokeCenterApplication

fun main(args: Array<String>) {
	runApplication<PokeCenterApplication>(*args)
}
