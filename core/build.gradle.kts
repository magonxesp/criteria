import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
	id("com.vanniktech.maven.publish")
	id("org.jetbrains.dokka")
	id("io.kotest.multiplatform")
}

group = "io.github.magonxesp"
version = "1.1.1"

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

android {
	namespace = "io.github.magonxesp.criteria"
	compileSdk = 34
	defaultConfig {
		minSdk = 24
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	testOptions {
		unitTests.all {
			it.useJUnitPlatform()
		}
	}
}

kotlin {
	androidTarget()
    jvm()

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
                implementation("io.kotest:kotest-framework-engine:5.8.0")
				implementation("org.slf4j:slf4j-api:2.0.13")
				implementation("org.slf4j:slf4j-simple:2.0.13")
            }
        }
        jvmMain {
            dependencies {
				val mongodbVersion = "5.0.0"
				val exposedVersion = "0.55.0"

                implementation("org.mongodb:mongodb-driver-kotlin-coroutine:$mongodbVersion")
                implementation("org.mongodb:bson-kotlinx:$mongodbVersion")
				implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
				implementation("org.jetbrains.exposed:exposed-crypt:$exposedVersion")
				implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
				implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
				implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
				implementation("org.jetbrains.exposed:exposed-json:$exposedVersion")
				implementation("org.jetbrains.exposed:exposed-money:$exposedVersion")
				implementation("org.postgresql:postgresql:42.7.1")
				implementation("org.mariadb.jdbc:mariadb-java-client:3.3.3")
            }
        }
		jvmTest {
			dependencies {
				val testContainersVersion = "1.19.7"

				implementation("io.kotest:kotest-runner-junit5:5.8.0")
				implementation("io.github.serpro69:kotlin-faker:1.15.0")
				implementation("org.testcontainers:testcontainers:$testContainersVersion")
				implementation("org.testcontainers:mongodb:$testContainersVersion")
				implementation("org.testcontainers:postgresql:$testContainersVersion")
				implementation("org.testcontainers:mariadb:$testContainersVersion")
			}
		}

    }
}

tasks.withType<KotlinCompile> {
	compilerOptions {
		jvmTarget.set(JvmTarget.JVM_1_8)
	}
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}
