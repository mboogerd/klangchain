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
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}