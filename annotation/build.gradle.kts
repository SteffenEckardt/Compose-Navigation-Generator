plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

ksp {
    arg("autoserviceKsp.verify", "true")
    arg("autoserviceKsp.verbose", "true")
    arg("verbose", "true")
}