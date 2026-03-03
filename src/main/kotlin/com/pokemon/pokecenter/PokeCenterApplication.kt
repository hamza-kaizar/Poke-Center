package com.pokemon.pokecenter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PokeCenterApplication

fun main(args: Array<String>) {
	runApplication<PokeCenterApplication>(*args)
}
