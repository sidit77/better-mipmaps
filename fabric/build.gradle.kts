plugins {
    id("multiloader-platform")
    id("fabric-loom")
}

base {
    archivesName = BuildConfig.FABRIC_ARCHIVES_NAME
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
    minecraft(group = "com.mojang", name = "minecraft", version = BuildConfig.MINECRAFT_VERSION)
    mappings(loom.layered {
        officialMojangMappings()
        if (BuildConfig.PARCHMENT_VERSION != null) {
            parchment("org.parchmentmc.data:parchment-${BuildConfig.MINECRAFT_VERSION}:${BuildConfig.PARCHMENT_VERSION}@zip")
        }
    })
    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = BuildConfig.FABRIC_LOADER_VERSION)

    modCompileOnly(group = "maven.modrinth", name = "sodium", version = BuildConfig.SODIUM_FABRIC_VERSION)

    modRuntimeOnly(group = "maven.modrinth", name = "sodium", version = BuildConfig.SODIUM_FABRIC_VERSION)
    modRuntimeOnly(group = "net.fabricmc.fabric-api", name = "fabric-api", version = BuildConfig.FABRIC_API_VERSION)
}

tasks {
    remapJar {
        destinationDirectory.set(file(rootProject.layout.buildDirectory).resolve("mods"))
    }
}