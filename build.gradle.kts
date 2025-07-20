plugins {
    id("java")
    id("fabric-loom") version "1.10.1" apply false
    id("net.neoforged.moddev") version "2.0.78" apply false
}

//https://modmuss50.me/fabric.html
//https://parchmentmc.org/docs/getting-started
//https://modrinth.com/mod/sodium/versions
//https://neoforged.net/
val MINECRAFT_VERSION by extra { "1.21.6" }
val NEOFORGE_VERSION by extra { "21.6.4-beta" }
val FABRIC_LOADER_VERSION by extra { "0.16.14" }
val FABRIC_API_VERSION by extra { "0.127.0+1.21.6" }

// This value can be set to null to disable Parchment.
val PARCHMENT_VERSION by extra { null }

val ARCHIVE_NAME by extra { "better-mipmaps" }
val MOD_VERSION by extra { "0.2.1" }

val SODIUM_VERSION by extra { "mc1.21.6-0.6.13" }

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
    group = "com.github.sidit77"
    version = createVersionString()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

subprojects {
    apply(plugin = "maven-publish")

    repositories {
        maven("https://maven.parchmentmc.org/")
        maven("https://api.modrinth.com/maven")
    }

    java {
        toolchain.languageVersion = JavaLanguageVersion.of(21)
        withSourcesJar()
    }

    version = createVersionString()
    group = "com.github.sidit77"

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    tasks.withType<GenerateModuleMetadata>().configureEach {
        enabled = false
    }
}

fun createVersionString(): String {
    val builder = StringBuilder()

    builder.append(MOD_VERSION)
    builder.append("+mc").append(MINECRAFT_VERSION)

    return builder.toString()
}