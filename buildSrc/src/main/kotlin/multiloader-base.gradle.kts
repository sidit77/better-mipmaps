plugins {
    id("java-library")
    id("idea")
}

group = "com.github.sidit77"
version = BuildConfig.BUILD_VERSION

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
    withSourcesJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(21)
}

tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}

repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = uri("https://api.modrinth.com/maven")
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }

    exclusiveContent {
        forRepository {
            maven {
                name = "Parchment"
                url = uri("https://maven.parchmentmc.org")
            }
        }
        filter {
            includeGroup("org.parchmentmc.data")
        }
    }

    exclusiveContent {
        forRepository {
            maven {
                name = "Sponge"
                url = uri("https://repo.spongepowered.org/repository/maven-public")
            }
        }
        filter { includeGroupAndSubgroups("org.spongepowered") }
    }
}