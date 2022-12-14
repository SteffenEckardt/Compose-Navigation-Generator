val navVersion: String by project
val kspVersion: String by project
val composeVersion: String by project

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
    `maven-publish`
}

ksp {
    arg("autoserviceKsp.verify", "true")
    arg("autoserviceKsp.verbose", "true")
    arg("verbose", "true")
}

dependencies {
    implementation(project(":annotation"))
    implementation(kotlin("stdlib"))

    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation("com.squareup:kotlinpoet-ksp:1.12.0")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")

    implementation("com.google.auto.service:auto-service-annotations:1.0.1")
    ksp("dev.zacsweers.autoservice:auto-service-ksp:1.0.0")

    // (Required) Writing and executing Unit Tests on the JUnit Platform
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")

    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.4.9")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.4.9")
    implementation(kotlin("reflect"))
}

group = "de.se.cng"
version = "0.0.2-alpha" // TODO: Update programmatically

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/SteffenEckardt/Compose-Navigation-Generator")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USER_NAME")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("PACKAGE_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}