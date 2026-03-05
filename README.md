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

Implements **Hexagonal Architecture (Ports & Adapters)** with clear separation of concerns:

```
src/main/kotlin/com/pokemon/pokecenter/
│
├── domain/                          # Pure business logic (framework-independent)
│   ├── entity/                      # Pokemon.kt (core domain entity)
│   ├── event/                       # Domain events
│   │   ├── DomainEvent.kt           # Base sealed class
│   │   ├── PokemonArrivalEvent.kt
│   │   ├── PokemonHealStartEvent.kt
│   │   ├── PokemonHealApplyEvent.kt
│   │   └── PokemonHealCompleteEvent.kt
│   ├── value/                       # Health.kt (value objects)
│   ├── constant/                    # Status.kt (enum)
│   └── validation/                  # ValidHealthRatio.kt (custom validators)
│
├── port/                            # Interfaces (contracts between layers)
│   ├── input/                       # Use case ports (inbound)
│   │   ├── RegisterPokemonUseCase.kt
│   │   ├── HealPokemonUseCase.kt
│   │   └── FindPokemonQuery.kt
│   └── output/                      # Dependency ports (outbound)
│       ├── SavePokemonPort.kt
│       ├── LoadPokemonPort.kt
│       └── PublishEventPort.kt
│
├── service/                         # Application layer (orchestration)
│   └── PokemonService.kt            # Core service implementing all input ports
│
├── adapter/                         # Framework-specific implementations
│   ├── input/
│   │   └── rest/                    # HTTP REST adapter
│   │       ├── PokemonController.kt # REST endpoints
│   │       └── dto/                 # Request/response DTOs
│   └── output/
│       ├── persistence/             # JPA database adapter
│       │   ├── PersistencePokemonAdapter.kt
│       │   ├── entity/
│       │   │   └── PokemonJpaEntity.kt
│       │   └── mapper/
│       │       └── PokemonMapper.kt
│       ├── kafka/                   # Event publishing adapter
│       │   └── KafkaEventPublisher.kt (implements PublishEventPort)
│       └── inmemory/                # Testing adapter
│           └── InMemoryPokemonAdapter.kt
│
└── config/                          # Spring configuration
    └── KafkaConfiguration.kt        # Kafka beans and listeners
```

### Layer Responsibilities

**Domain Layer**: Pure business logic
- Pokemon entity with business rules
- Domain events representing state changes
- Value objects (Health) for type safety
- Validation rules

**Port Layer**: Contracts between layers
- Input ports define use cases (RegisterPokemon, HealPokemon, etc.)
- Output ports define dependencies (persistence, event publishing)
- Language of the domain (no framework-specific types)

**Application/Service Layer**: Use case orchestration
- PokemonService implements input ports
- Coordinates domain logic with output adapters
- Ensures business rules are enforced

**Adapter Layer**: Framework implementations
- REST adapter converts HTTP requests to domain commands
- Persistence adapter maps domain entities to JPA
- Kafka adapter publishes domain events asynchronously
- In-memory adapter for testing without dependencies

## Getting Started

### Prerequisites
- JDK 25+
- Gradle 8.x
- Docker (for running Kafka locally)

### Build & Run

```bash
# 1. Start Kafka and Zookeeper infrastructure
docker compose up -d

# 2. Build the project
./gradlew build

# 3. Run tests
./gradlew test

# 4. Start the application
./gradlew bootRun
```

The API will be available at `http://localhost:8080/api/pokemon`

## Local Infrastructure

Kafka and Zookeeper run via Docker Compose for local development:

| Service    | Image                            | Port  | Purpose                   |
|------------|----------------------------------|-------|---------------------------|
| Kafka      | confluentinc/cp-kafka:7.6.0      | 9092  | Message broker             |
| Zookeeper  | confluentinc/cp-zookeeper:7.6.0  | 2181  | Kafka cluster coordinator  |

### Infrastructure Commands

```bash
# Start all services in background
docker compose up -d

# View running containers and health status
docker compose ps

# Stream Kafka logs
docker compose logs -f kafka

# Stop and remove containers
docker compose down
```

**Note**: The application connects to Kafka at `localhost:9092`. Ensure Docker Compose is running before starting `bootRun`. The Kafka container includes a health check that verifies the broker is ready.

### Viewing Kafka Events

The application publishes domain events to three Kafka topics. Use these commands to view events in real-time:

#### Pokemon Arrivals (`pokemon.arrivals`)
When a new Pokémon is registered, a `PokemonArrivalEvent` is published:
```bash
docker exec -it $(docker ps -q -f "ancestor=confluentinc/cp-kafka:7.6.0") \
  kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic pokemon.arrivals \
  --from-beginning \
  --property print.key=true
```

#### Healing Progress (`pokemon.healing`)
When healing starts or is applied, `PokemonHealStartEvent` and `PokemonHealApplyEvent` are published:
```bash
docker exec -it $(docker ps -q -f "ancestor=confluentinc/cp-kafka:7.6.0") \
  kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic pokemon.healing \
  --from-beginning \
  --property print.key=true
```

#### Healing Complete (`pokemon.healed`)
When healing is completed, a `PokemonHealCompleteEvent` is published:
```bash
docker exec -it $(docker ps -q -f "ancestor=confluentinc/cp-kafka:7.6.0") \
  kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic pokemon.healed \
  --from-beginning \
  --property print.key=true
```

**Tip**: Open three terminals and run each consumer in parallel to watch all events simultaneously.

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
