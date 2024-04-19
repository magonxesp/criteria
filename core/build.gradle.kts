import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
	id("com.vanniktech.maven.publish")
}

group = "io.github.magonxesp"
version = "0.0.3"

mavenPublishing {
	coordinates(group as String, "criteria-core", version as String)
	publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
	signAllPublications()

	pom {
		name = "Criteria core"
		description = "The core of the Criteria library"
		url = "https://github.com/magonxesp/criteria"
		licenses {
			license {
				name = "The MIT License (MIT)"
				url = "https://mit-license.org/"
			}
		}
		developers {
			developer {
				id = "magonxesp"
				name = "MagonxESP"
				email = "magonxesp@gmail.com"
				url = "https://github.com/magonxesp"
			}
		}
		scm {
			connection = "scm:git:git://github.com/magonxesp/criteria.git"
			developerConnection = "scm:git:ssh://github.com/magonxesp/criteria.git"
			url = "https://github.com/magonxesp/criteria"
		}
	}
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        java {
            targetCompatibility = JavaVersion.VERSION_1_8
            sourceCompatibility = JavaVersion.VERSION_1_8
        }
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

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}
