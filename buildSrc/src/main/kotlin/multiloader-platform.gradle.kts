plugins {
    id("multiloader-base")
    id("maven-publish")
}

val configurationCommonModJava: Configuration = configurations.create("commonJava") {
    isCanBeResolved = true
}
val configurationCommonModResources: Configuration = configurations.create("commonResources") {
    isCanBeResolved = true
}

dependencies {
    configurationCommonModJava(project(":common", configuration = "commonJava"))
    configurationCommonModResources(project(":common", configuration = "commonResources"))
}

tasks {
    processResources {
        inputs.property("version", version)
        inputs.property("minecraft_version", BuildConfig.MINECRAFT_VERSION)
        inputs.property("fabric_loader_version", BuildConfig.FABRIC_LOADER_VERSION)

        filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml")) {
            expand(mapOf(
                "version" to inputs.properties["version"],
                "minecraft_version" to inputs.properties["minecraft_version"],
                "fabric_loader_version" to inputs.properties["fabric_loader_version"]
            ))
        }
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.FAIL
        from(rootDir.resolve("LICENSE.md"))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = rootProject.name + "-" + project.name
            version = version

            from(components["java"])
        }
    }
}