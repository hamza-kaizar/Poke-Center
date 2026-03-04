package com.pokemon.pokecenter.adapter.output.inmemory

import com.pokemon.pokecenter.domain.entity.Pokemon
import com.pokemon.pokecenter.port.output.SavePokemonPort
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Component
class InMemoryPokemonAdapter : SavePokemonPort {
	private val store = ConcurrentHashMap<Long, Pokemon>()
	private val idGen = AtomicLong(1)

	override fun save(pokemon: Pokemon): Pokemon {
		val toSave = if (pokemon.id == 0L) pokemon.copy(id = idGen.getAndIncrement()) else pokemon
		return toSave.also { store[it.id] = it }
	}
}
