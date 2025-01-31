plugins {
    kotlin("jvm") version "2.0.0"
}

group = "civic-pioneer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("dev.langchain4j:langchain4j-open-ai:1.0.0-alpha1")
    implementation("dev.langchain4j:langchain4j:1.0.0-alpha1")
    implementation("dev.langchain4j:langchain4j-ollama:1.0.0-alpha1")

    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.testcontainers:ollama:1.20.4")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}