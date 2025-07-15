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
    val minecraft_version = properties["minecraft_version"]
    val fabric_loader_version = properties["fabric_loader_version"]
    val parchment_version = properties["parchment_version"]
    val sodium_version = properties["sodium_version"]
    val fabric_api_version = properties["fabric_api_version"]

    minecraft("com.mojang:minecraft:${minecraft_version}")
    mappings(loom.layered {
        officialMojangMappings()

        parchment("org.parchmentmc.data:parchment-${minecraft_version}:${parchment_version}@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${fabric_loader_version}")

    modCompileOnly("maven.modrinth:sodium:${sodium_version}-fabric")

    modRuntimeOnly("maven.modrinth:sodium:${sodium_version}-fabric")
    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:${fabric_api_version}")
}

tasks {
    remapJar {
        destinationDirectory.set(file(rootProject.layout.buildDirectory).resolve("mods"))
    }
}