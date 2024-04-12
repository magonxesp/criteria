plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "io.github.magonxesp.criteria"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        java {
            targetCompatibility = JavaVersion.VERSION_17
            sourceCompatibility = JavaVersion.VERSION_17
        }

        jvmToolchain(21)
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0-RC.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.mockk:mockk:1.13.8")
                implementation("io.github.serpro69:kotlin-faker:1.15.0")
                implementation("io.kotest:kotest-runner-junit5:5.8.0")
            }
        }
        jvmMain {
            dependencies {
                implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.0.0")
                implementation("org.mongodb:bson-kotlinx:5.0.0")
            }
        }
    }
}
