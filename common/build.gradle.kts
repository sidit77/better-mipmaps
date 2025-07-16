plugins {
    id("multiloader-base")
    id("java-library")

    id("fabric-loom")
}

base {
    archivesName = BuildConfig.COMMON_ARCHIVES_NAME
}

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = BuildConfig.MINECRAFT_VERSION)
    mappings(loom.layered {
        officialMojangMappings()
        if (BuildConfig.PARCHMENT_VERSION != null) {
            parchment("org.parchmentmc.data:parchment-${BuildConfig.MINECRAFT_VERSION}:${BuildConfig.PARCHMENT_VERSION}@zip")
        }
    })

    compileOnly(group = "org.ow2.asm", name = "asm-tree", version = "9.8")
    compileOnly(group = "org.spongepowered", name = "mixin", version = "0.8.5")

    compileOnly(group = "io.github.llamalad7", name = "mixinextras-common", version = "0.3.5")
    annotationProcessor(group = "io.github.llamalad7", name = "mixinextras-common", version = "0.3.5")

    compileOnly(group = "net.fabricmc", name= "fabric-loader", version = BuildConfig.FABRIC_LOADER_VERSION)
    modCompileOnly(group = "maven.modrinth", name = "sodium", version = BuildConfig.SODIUM_FABRIC_VERSION)
}

loom {
    accessWidenerPath.set(file("src/main/resources/better_mipmaps.accesswidener"))
}

val commonJava by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
}

val commonResources by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
}

artifacts {
    add(commonJava.name, tasks.jar)
    add(commonResources.name, tasks.processResources)
}
