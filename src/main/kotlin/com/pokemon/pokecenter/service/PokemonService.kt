package com.pokemon.pokecenter.service

import com.pokemon.pokecenter.domain.entity.Pokemon
import com.pokemon.pokecenter.domain.event.PokemonArrivalEvent
import com.pokemon.pokecenter.domain.event.PokemonHealApplyEvent
import com.pokemon.pokecenter.domain.event.PokemonHealStartEvent
import com.pokemon.pokecenter.domain.value.Health
import com.pokemon.pokecenter.port.input.FindPokemonQuery
import com.pokemon.pokecenter.port.input.HealPokemonUseCase
import com.pokemon.pokecenter.port.input.RegisterPokemonCommand
import com.pokemon.pokecenter.port.input.RegisterPokemonUseCase
import com.pokemon.pokecenter.port.output.LoadPokemonPort
import com.pokemon.pokecenter.port.output.PublishEventPort
import com.pokemon.pokecenter.port.output.SavePokemonPort
import org.springframework.stereotype.Service

@Service
class PokemonService(
	private val loadPokemon: LoadPokemonPort,
	private val savePokemon: SavePokemonPort,
	private val publishEventPort: PublishEventPort,
) : RegisterPokemonUseCase,
	HealPokemonUseCase,
	FindPokemonQuery {
	override fun register(command: RegisterPokemonCommand): Pokemon {
		val health = Health(current = command.currentHealth, maximum = command.maximumHealth)
		val pokemon =
			Pokemon(
				name = command.name,
				trainerName = command.trainerName,
				health = health,
			)

		val saved = savePokemon.save(pokemon)
		publishEventPort.publishPokemonArrival(PokemonArrivalEvent.from(saved))

		return saved
	}

	override fun startHealing(pokemonId: Long): Pokemon {
		val pokemon = loadPokemon.loadById(pokemonId)
		val healing = pokemon.startHealing()

		val saved = savePokemon.save(healing)
		publishEventPort.publishPokemonHealStart(PokemonHealStartEvent.from(saved))

		return saved
	}

	override fun applyHealing(
		pokemonId: Long,
		amount: Int,
	): Pokemon {
		val pokemon = loadPokemon.loadById(pokemonId)

		val healed = pokemon.applyHealing(amount)
		val saved = savePokemon.save(healed)
		publishEventPort.publishPokemonHealApply(PokemonHealApplyEvent.from(saved, amount))

		return saved
	}

	override fun completeHealing(pokemonId: Long): Pokemon {
		val pokemon = loadPokemon.loadById(pokemonId)
		val healed = pokemon.completeHealing()
		return savePokemon.save(healed)
	}

	override fun findById(id: Long): Pokemon = loadPokemon.loadById(id)

	override fun findAll(): List<Pokemon> = loadPokemon.loadAll()
}
