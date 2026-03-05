# Pokémon Center

A Spring Boot Kotlin application simulating a Pokémon Center where trainers can bring their Pokémon to be registered, healed, and cared for.

## Project Overview

This repository is a learning project to learn some software development concepts and get hands-on experience on new tools and frameworks.

## Learning Objectives

### 1. **Kotlin** 🎯
- Modern language features: data classes, extension functions, coroutines
- Type-safe, null-safe development with concise syntax
- Interoperability with Java Spring ecosystem
- Functional programming patterns and idioms

### 2. **Kafka** 📨
- Event-driven architecture with asynchronous messaging
- Decoupling services through publish-subscribe patterns
- Building reactive systems that respond to domain events
- Scaling applications through event streams
*(Upcoming phase: integrating Kafka for healing progress notifications)*

### 3. **Hexagonal Architecture** 🐝
- Clean separation of concerns: domain, application, adapter layers
- Port-based dependency inversion (ports & adapters pattern)
- Testing at each layer independently (unit, integration, end-to-end)
- Framework-agnostic business logic that's easy to change and extend

## Architecture

```
src/main/kotlin/com/pokemon/pokecenter/
├── domain/                          # Pure business logic
│   ├── entity/                      # Pokémon and domain models
│   ├── value/                       # Health, Status value objects
│   └── validation/                  # Domain-level validation rules
│
├── application/                     # Use case orchestration
│   └── port/
│       ├── input/                   # Use cases (RegisterPokemon, HealPokemon, FindPokemon)
│       └── output/                  # Port interfaces for repositories
│
└── adapter/                         # Framework-specific implementation
    ├── input/rest/                  # HTTP REST controller layer
    │   ├── PokemonController.kt
    │   └── dto/                     # Request/response DTOs
    └── output/persistence/          # JPA persistence adapter
        ├── PokemonJpaEntity.kt
        └── PokemonPersistenceAdapter.kt
```

## Getting Started

### Prerequisites
- JDK 25+
- Gradle 8.x

### Build & Run

```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Start the application
./gradlew bootRun
```

The API will be available at `http://localhost:8080/api/pokemon`

## API Endpoints

### Register a Pokémon
```bash
POST /api/pokemon
Content-Type: application/json

{
  "name": "Pikachu",
  "trainerName": "Ash",
  "currentHealth": 80,
  "maximumHealth": 100
}
```

### Get Pokémon by ID
```bash
GET /api/pokemon/{id}
```

### List All Pokémon
```bash
GET /api/pokemon
```

### Start Healing
```bash
POST /api/pokemon/{id}/heal/start
```

### Apply Healing
```bash
POST /api/pokemon/{id}/heal/apply
Content-Type: application/json

{
  "amount": 15
}
```

### Complete Healing
```bash
POST /api/pokemon/{id}/heal/complete
```
