import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
  `java-library`
  id("io.papermc.paperweight.userdev") version "1.7.4"
  id("xyz.jpenilla.run-paper") version "2.3.1"
  id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.2.0"
}

group = "me.sheepbell"
version = "1.0.0-SNAPSHOT"
description = "KKuTu in Minecraft"

java {
  toolchain.languageVersion = JavaLanguageVersion.of(21)
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

dependencies {
  paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
}

tasks {
  compileJava {
    options.encoding = Charsets.UTF_8.name()
    options.release = 21
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }
}

tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
  javaLauncher = javaToolchains.launcherFor {
    vendor = JvmVendorSpec.JETBRAINS
    languageVersion = JavaLanguageVersion.of(21)
  }
  jvmArgs("-XX:+AllowEnhancedClassRedefinition")
}

bukkitPluginYaml {
  main = "me.sheepbell.kkutu.KKuTu"
  load = BukkitPluginYaml.PluginLoadOrder.STARTUP
  author = "sheepbell"
  apiVersion = "1.21.1"
}
