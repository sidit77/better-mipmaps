plugins {
    id("multiloader-base")
    id("maven-publish")
}

val commonJava by configurations.creating {
    isCanBeResolved = true
}

val commonResources by configurations.creating {
    isCanBeResolved = true
}

dependencies {
    commonJava(project(":common", configuration = "commonJava"))
    commonResources(project(":common", configuration = "commonResources"))
}

sourceSets.apply {
    main {
        compileClasspath += commonJava
        runtimeClasspath += commonJava
    }
}

tasks {
    processResources {
        from(commonResources)

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
        from(commonJava)
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