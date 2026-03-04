package com.pokemon.pokecenter.service

import com.pokemon.pokecenter.domain.entity.Pokemon
import com.pokemon.pokecenter.domain.value.Health
import com.pokemon.pokecenter.port.input.RegisterPokemonCommand
import com.pokemon.pokecenter.port.input.RegisterPokemonUseCase
import com.pokemon.pokecenter.port.output.SavePokemonPort
import org.springframework.stereotype.Service

@Service
class PokemonService(
	private val savePokemon: SavePokemonPort,
) : RegisterPokemonUseCase {
	override fun register(command: RegisterPokemonCommand): Pokemon {
		val health = Health(current = command.currentHealth, maximum = command.maximumHealth)
		val pokemon =
			Pokemon(
				name = command.name,
				trainerName = command.trainerName,
				health = health,
			)
		return savePokemon.save(pokemon)
	}
}
