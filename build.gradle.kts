plugins {
	kotlin("jvm") version "2.3.10"
	kotlin("plugin.spring") version "2.3.10"
	id("org.springframework.boot") version "4.1.0-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "2.3.10"
	id("org.jlleitschuh.gradle.ktlint") version "14.0.1"
	jacoco
}

group = "com.pokemon"
version = "0.0.1-SNAPSHOT"
description = "Medical facility to care for Pokémon"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-h2console")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.core:jackson-databind")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	implementation("org.springframework.kafka:spring-kafka:4.0.3")
	implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka:5.0.1")
	implementation("org.springframework.retry:spring-retry:2.0.12")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("io.mockk:mockk:1.13.11")
	testImplementation("org.testcontainers:testcontainers:2.0.3")
	testImplementation("org.testcontainers:kafka:1.21.4")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
		jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25)
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

ktlint {
	version.set("1.8.0")
	outputToConsole.set(true)
	coloredOutput.set(true)
	ignoreFailures.set(false)
}

tasks.register("format") {
	dependsOn("ktlintFormat")
	description = "Format code with KtLint"
}

tasks.register("lint") {
	dependsOn("ktlintCheck")
	description = "Check code style with KtLint"
}

jacoco {
	toolVersion = "0.8.14"
}

tasks.test {
	finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	classDirectories.setFrom(
		files(
			classDirectories.files.map {
				fileTree(it) { exclude("**/config/**", "**/dto/**", "**/*Kt.class") }
			},
		),
	)
}

tasks.jacocoTestCoverageVerification {
	classDirectories.setFrom(
		files(
			classDirectories.files.map {
				fileTree(it) { exclude("**/config/**", "**/dto/**", "**/*Kt.class") }
			},
		),
	)
	violationRules {
		rule {
			element = "CLASS"
			limit {
				minimum = "0.75".toBigDecimal()
			}
		}
	}
}

tasks.register("checkCoverage") {
	dependsOn("jacocoTestCoverageVerification")
	description = "Verify code coverage meets minimum threshold"
}

tasks.register("quality") {
	dependsOn("lint", "checkCoverage")
	description = "Run all quality checks (linting + coverage)"
}
