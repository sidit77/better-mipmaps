plugins {
    id("java")
    id("idea")
    id("fabric-loom")
}

val MINECRAFT_VERSION: String by rootProject.extra
val PARCHMENT_VERSION: String? by rootProject.extra
val FABRIC_LOADER_VERSION: String by rootProject.extra
val FABRIC_API_VERSION: String by rootProject.extra
val MOD_VERSION: String by rootProject.extra
val ARCHIVE_NAME: String by rootProject.extra

val SODIUM_VERSION: String by rootProject.extra

base {
    archivesName = "$ARCHIVE_NAME-fabric"
}


dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = MINECRAFT_VERSION)
    mappings(loom.layered {
        officialMojangMappings()
        if (PARCHMENT_VERSION != null) {
            parchment("org.parchmentmc.data:parchment-${MINECRAFT_VERSION}:${PARCHMENT_VERSION}@zip")
        }
    })
    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = FABRIC_LOADER_VERSION)

    modImplementation(group = "maven.modrinth", name = "sodium", version = "$SODIUM_VERSION-fabric")
    modRuntimeOnly(group = "net.fabricmc.fabric-api", name = "fabric-api", version = FABRIC_API_VERSION)

    compileOnly(project(":common"))
}

loom {
    accessWidenerPath = file("../common/src/main/resources/better_mipmaps.accesswidener")
    mixin {
        defaultRefmapName = "better_mipmaps.refmap.json"
    }
    //accessWidenerPath.set(file("src/main/resources/better-mipmaps.accesswidener"))

    runs {
        named("client") {
            client()
            configName = "Fabric Client"
            appendProjectPathToConfigName = false
            ideConfigGenerated(true)
            runDir("run")
        }
    }
}


tasks {
    withType<JavaCompile> {
        source(project(":common").sourceSets.main.get().allSource)
    }

    javadoc { source(project(":common").sourceSets.main.get().allJava) }

    processResources {
        from(project(":common").sourceSets.main.get().resources)

        inputs.property("version", project.version)
        inputs.property("minecraft_version", MINECRAFT_VERSION)
        inputs.property("fabric_loader_version", FABRIC_LOADER_VERSION)

        filesMatching("fabric.mod.json") {
            expand(
                mapOf(
                    "version" to project.version,
                    "minecraft_version" to MINECRAFT_VERSION,
                    "fabric_loader_version" to FABRIC_LOADER_VERSION
                )
            )
        }
    }

    jar {
        from(rootDir.resolve("LICENSE.md"))
    }

}