plugins {
    id("multiloader-platform")
    id("fabric-loom") version ("1.10.1")
}

base {
    archivesName = "better-mipmaps-fabric"
}

loom {
    accessWidenerPath.set(file("src/main/resources/better-mipmaps.accesswidener"))

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

dependencies {
    minecraft("com.mojang:minecraft:${BuildConfig.MINECRAFT_VERSION}")
    mappings(loom.layered {
        officialMojangMappings()
        if (BuildConfig.PARCHMENT_VERSION != null) {
            parchment("org.parchmentmc.data:parchment-${BuildConfig.MINECRAFT_VERSION}:${BuildConfig.PARCHMENT_VERSION}@zip")
        }
    })
    modImplementation("net.fabricmc:fabric-loader:${BuildConfig.FABRIC_LOADER_VERSION}")

    modCompileOnly("maven.modrinth:sodium:${BuildConfig.SODIUM_VERSION}-fabric")

    modRuntimeOnly("maven.modrinth:sodium:${BuildConfig.SODIUM_VERSION}-fabric")
    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:${BuildConfig.FABRIC_API_VERSION}")
}

tasks {
    remapJar {
        destinationDirectory.set(file(rootProject.layout.buildDirectory).resolve("mods"))
    }
}