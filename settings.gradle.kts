rootProject.name = "spring-simple-archetype"

include(":api")
include(":batch")
include(":consumer")
include(":database")

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "se.ohou.mortar.plugin") {
                useModule("se.ohou.mortar:mortar-plugin:${requested.version}")
            }
        }
    }
    repositories {
        gradlePluginPortal()
        maven(url = "https://nexus.co-workerhou.se/repository/maven-public")
    }
}
