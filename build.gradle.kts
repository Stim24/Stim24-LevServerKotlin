plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    application
}

group = "com.lev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val ktor_version = "3.0.1"

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-swagger:$ktor_version")  // ✅ Только swagger, без openapi
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}

application {
    mainClass.set("com.lev.ApplicationKt")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.lev.ApplicationKt"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}