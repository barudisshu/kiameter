buildscript {
  repositories {
    gradlePluginPortal()
    mavenLocal()
    google()
    mavenCentral()
  }
}
configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

repositories {
  mavenCentral()
  mavenLocal()
}

plugins {
  kotlin("jvm") version "1.8.21"
  application
  java
  idea
}

group = "com.cplier"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val logbackVersion = "1.4.6"

dependencies {
  implementation(kotlin("stdlib"))

  // Logging
  implementation("ch.qos.logback", "logback-classic", logbackVersion)

  // Test
  testImplementation(kotlin("test"))

  // Kotlin Mocking
  testImplementation("io.mockk", "mockk", "1.13.5")

}

tasks.compileKotlin {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "17"
  }
}
java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
  sourceCompatibility = JavaVersion.VERSION_17
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(17)
}

application {
  mainClass.set("MainKt")
}

idea {
  module.isDownloadJavadoc = true
  module.isDownloadSources = true
}
