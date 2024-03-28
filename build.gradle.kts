plugins {
  `java-library`
  id("xyz.jpenilla.run-paper") version "2.2.3"
}

group = "me.sheepbell"
version = "1.0.0-SNAPSHOT"
description = "KKuTu in Minecraft"

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
  compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
  compileJava {
    options.encoding = Charsets.UTF_8.name()
    options.release.set(21)
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }
  processResources {
    filteringCharset = Charsets.UTF_8.name()
    val props = mapOf(
      "name" to project.name,
      "version" to project.version,
      "description" to project.description,
      "apiVersion" to "1.20"
    )
    inputs.properties(props)
    filesMatching("plugin.yml") {
      expand(props)
    }
  }
  runServer {
    minecraftVersion("1.20.4")
  }
}
