import org.apache.tools.ant.filters.LineContains

plugins {
    id("idea")
    id("net.neoforged.moddev")
    id("java-library")
}

val MINECRAFT_VERSION: String by rootProject.extra
val PARCHMENT_VERSION: String? by rootProject.extra
val NEOFORGE_VERSION: String by rootProject.extra
val ARCHIVE_NAME: String by rootProject.extra
val SODIUM_VERSION: String by rootProject.extra

base {
    archivesName = "$ARCHIVE_NAME-neoforge"
}

dependencies {
    implementation(group = "maven.modrinth", name = "sodium", version = "$SODIUM_VERSION-neoforge")

    compileOnly(project(":common"))
}


neoForge {
    version = NEOFORGE_VERSION

    if (PARCHMENT_VERSION != null) {
        parchment {
            mappingsVersion = PARCHMENT_VERSION
            minecraftVersion = MINECRAFT_VERSION
        }
    }

    runs {
        create("client") {
            client()
            ideName = "NeoForge Client"
        }
    }

    mods {
        create("better_mipmaps") {
            sourceSet(sourceSets.main.get())
        }
    }
}


// NeoGradle compiles the game, but we don't want to add our common code to the game's code
val notNeoTask: (Task) -> Boolean = { it: Task -> !it.name.startsWith("neo") && !it.name.startsWith("compileService") }

tasks {

    withType<JavaCompile>().matching(notNeoTask).configureEach {
        source(project(":common").sourceSets.main.get().allSource)
    }

    withType<Javadoc>().matching(notNeoTask).configureEach {
        source(project(":common").sourceSets.main.get().allJava)
    }

    withType<ProcessResources>().matching(notNeoTask).configureEach {
        from(project(":common").sourceSets.main.get().resources)

        inputs.property("version", project.version)
        inputs.property("minecraft_version", MINECRAFT_VERSION)
        inputs.property("neoforge_version", NEOFORGE_VERSION)

        filesMatching("META-INF/neoforge.mods.toml") {
            expand(
                mapOf(
                    "version" to project.version,
                    "minecraft_version" to MINECRAFT_VERSION,
                    "neoforge_version" to NEOFORGE_VERSION
                )
            )
        }

        filesMatching("*.mixins.json") {
            filter<LineContains>("negate" to true, "contains" to setOf("refmap"))
        }
    }

    jar {
        from(rootDir.resolve("LICENSE.md"))
    }
}