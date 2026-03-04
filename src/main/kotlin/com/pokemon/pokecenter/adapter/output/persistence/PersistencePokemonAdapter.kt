package com.pokemon.pokecenter.adapter.output.persistence

import com.pokemon.pokecenter.adapter.output.persistence.entity.PokemonJpaEntity
import com.pokemon.pokecenter.adapter.output.persistence.mapper.toDomain
import com.pokemon.pokecenter.adapter.output.persistence.mapper.toJpaEntity
import com.pokemon.pokecenter.domain.entity.Pokemon
import com.pokemon.pokecenter.port.output.LoadPokemonPort
import com.pokemon.pokecenter.port.output.SavePokemonPort
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
interface SpringDataPokemonRepository : JpaRepository<PokemonJpaEntity, Long>

@Primary
@Component
class PersistencePokemonAdapter(
	private val repository: SpringDataPokemonRepository,
) : LoadPokemonPort,
	SavePokemonPort {
	override fun save(pokemon: Pokemon): Pokemon = repository.save(pokemon.toJpaEntity()).toDomain()

	override fun loadById(id: Long): Pokemon =
		repository
			.findById(id)
			.orElseThrow { NoSuchElementException("Pokemon not found: $id") }
			.toDomain()

	override fun loadAll(): List<Pokemon> = repository.findAll().map { it.toDomain() }
}
