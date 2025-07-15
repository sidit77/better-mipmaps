plugins {
    id("multiloader-base")
    id("maven-publish")
}

tasks {
    processResources {
        inputs.property("version", version)
        inputs.property("minecraft_version", properties["minecraft_version"])
        inputs.property("fabric_loader_version", properties["fabric_loader_version"])

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