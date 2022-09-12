pluginManagement {
    plugins {
        id("com.google.devtools.ksp") version "1.7.10-1.0.6"
    }
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ComposeNavigationGenerator"

include(":demoApp")
include(":annotation")
include(":processor")
