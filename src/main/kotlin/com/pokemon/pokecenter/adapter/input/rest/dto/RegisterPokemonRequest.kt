package com.pokemon.pokecenter.adapter.input.rest.dto

data class RegisterPokemonRequest(
	val name: String,
	val trainerName: String,
	val currentHealth: Int,
	val maximumHealth: Int,
)
