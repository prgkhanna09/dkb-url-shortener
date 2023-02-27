import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.1.0"
    id("org.flywaydb.flyway") version "9.8.1"
    jacoco
    id("com.adarshr.test-logger") version "3.2.0"
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
    implementation("org.springframework.boot:spring-boot-starter-data-redis") {
        exclude("io.lettuce", "lettuce-core")
    }
    implementation("redis.clients:jedis:4.3.1")
    implementation("io.springfox:springfox-swagger2:3.0.0")
    implementation("io.springfox:springfox-swagger-ui:3.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("io.cucumber:cucumber-java:6.10.4")
    testImplementation("io.cucumber:cucumber-spring:6.10.4")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:6.10.4")
    testRuntimeOnly("org.junit.platform:junit-platform-console")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
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

tasks {
    val consoleLauncherTest by registering(JavaExec::class) {
        dependsOn(testClasses)
        doFirst {
            println("Running parallel test")
        }
        classpath = sourceSets["test"].runtimeClasspath
        mainClass.set("org.junit.platform.console.ConsoleLauncher")
        args("--include-engine", "cucumber")
        args("--details", "tree")
        args("--scan-classpath")

        systemProperty("cucumber.execution.parallel.enabled", true)
        systemProperty("cucumber.execution.parallel.config.strategy", "dynamic")
        systemProperty(
            "cucumber.plugin",
            "pretty, summary, timeline:build/reports/timeline, html:build/reports/cucumber.html"
        )
        systemProperty("cucumber.publish.quiet", true)
    }

    test {
        dependsOn(consoleLauncherTest)
        useJUnitPlatform()
    }
}
