apply {
    from("$rootDir/android-library-build.gradle")
}


dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.components))
    "implementation"(project(Modules.restCountryDomain))
    "implementation"(project(Modules.restCountryInteractors))

    "implementation"(Coil.coil)
}