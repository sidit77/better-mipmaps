import net.fabricmc.loom.task.AbstractRemapJarTask

plugins {
    id("java")
    id("idea")
    id("fabric-loom")
}

val MINECRAFT_VERSION: String by rootProject.extra
val PARCHMENT_VERSION: String? by rootProject.extra
val FABRIC_LOADER_VERSION: String by rootProject.extra
val FABRIC_API_VERSION: String by rootProject.extra

val SODIUM_VERSION: String by rootProject.extra


dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = MINECRAFT_VERSION)
    mappings(loom.layered {
        officialMojangMappings()
        if (PARCHMENT_VERSION != null) {
            parchment("org.parchmentmc.data:parchment-${MINECRAFT_VERSION}:${PARCHMENT_VERSION}@zip")
        }
    })

    compileOnly(group = "net.fabricmc", name = "sponge-mixin", version = "0.13.2+mixin.0.8.5")

    compileOnly(group = "io.github.llamalad7", name = "mixinextras-common", version = "0.3.5")
    annotationProcessor(group = "io.github.llamalad7", name = "mixinextras-common", version = "0.3.5")

    modCompileOnly(group = "net.fabricmc", name= "fabric-loader", version = FABRIC_LOADER_VERSION)
    modCompileOnly(group = "maven.modrinth", name = "sodium", version = "$SODIUM_VERSION-fabric")
}

tasks.withType<AbstractRemapJarTask>().forEach {
    it.targetNamespace = "named"
}

loom {
    accessWidenerPath = file("src/main/resources/better_mipmaps.accesswidener")
    mixin {
        defaultRefmapName = "better_mipmaps.refmap.json"
    }
}
