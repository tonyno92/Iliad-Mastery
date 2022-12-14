apply {
    from("$rootDir/library-build.gradle")
}


plugins {
    kotlin(KotlinPlugins.serialization) version Kotlin.version
    id(SqlDelight.plugin)
}

dependencies {
    "implementation"(project(Modules.restCountryDomain))

    "implementation"(Ktor.core)
    "implementation"(Ktor.clientSerialization)
    "implementation"(Ktor.android)

    "implementation"(SqlDelight.runtime)
}

sqldelight {
    database("IliadDemoDatabase") {
        packageName = "com.iliadmastery.iliad_demo_datasource.cache"
        sourceFolders = listOf("sqldelight")
    }
}