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

    compileOnly(group = "org.spongepowered", name = "mixin", version = "0.8.5")
    compileOnly(group = "net.fabricmc", name= "fabric-loader", version = BuildConfig.FABRIC_LOADER_VERSION)
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
