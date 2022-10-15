apply {
    from("$rootDir/android-library-build.gradle")
}

plugins {
    kotlin(KotlinPlugins.serialization) version Kotlin.version
}

dependencies {
    "implementation"(project(Modules.components))
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.restCountryDomain))
    "implementation"(project(Modules.restCountryInteractors))

    "implementation"(Coil.coil)
}