apply {
    from("$rootDir/library-build.gradle")
}

plugins {
    kotlin(KotlinPlugins.serialization) version Kotlin.version
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.restCountryDataSource))
    "implementation"(project(Modules.restCountryDomain))

    "implementation"(Kotlinx.coroutinesCore) // need for flows

    "testImplementation"(project(Modules.restCountryDataSourceTest))
    "testImplementation"(Junit.junit4)
    "testImplementation"(Ktor.ktorClientMock)
    "testImplementation"(Ktor.clientSerialization)
}