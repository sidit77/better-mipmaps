plugins {
    id("multiloader-platform")
    id("net.neoforged.moddev")
}

base {
    archivesName = BuildConfig.NEOFORGE_ARCHIVES_NAME
}

neoForge {
    version = BuildConfig.NEOFORGE_VERSION

    if (BuildConfig.PARCHMENT_VERSION != null) {
        parchment {
            minecraftVersion = BuildConfig.MINECRAFT_VERSION
            mappingsVersion = BuildConfig.PARCHMENT_VERSION
        }
    }

    runs {
        create("Client") {
            client()
            ideName = "NeoForge Client"
        }
    }

    mods {
        create("better-mipmaps") {
            sourceSet(sourceSets["main"])
        }
    }
}

tasks {
    jar {
        destinationDirectory.set(file(rootProject.layout.buildDirectory).resolve("mods"))
    }
}