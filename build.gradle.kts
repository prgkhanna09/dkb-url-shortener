import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.1.0"
    id("org.flywaydb.flyway") version "9.8.1"
    jacoco
}

group = "com.dkb"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

val jdbc = configurations.create("jdbc")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.postgresql:r2dbc-postgresql")
    jdbc("org.postgresql:postgresql:42.5.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

flyway {
    configurations = arrayOf("jdbc")
    url = "jdbc:postgresql://localhost:5432/url-shortener"
    user = "postgres"
    password = "postgres"
    baselineOnMigrate = true
}

// should be part of ci
tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

// should be part of ci
tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            element = "PACKAGE"

            limit {
                value = "COVEREDRATIO"
                minimum = "0.90".toBigDecimal()
            }

            excludes = listOf("io.github.raeperd.realworldspringbootkotlin")
        }
    }
}