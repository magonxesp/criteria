plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
	`java-library`
	`maven-publish`
	signing
}

group = "io.github.magonxesp"
version = "1.0-SNAPSHOT"

publishing {
	publications {
		register<MavenPublication>("criteria-spring-boot") {
			artifactId = "criteria-spring-boot"
			from(components["java"])
			pom {
				name = "Criteria for Spring Boot"
				description = "The Criteria library adapted with Spring Boot data specification adapter"
				basicInformation()
			}
		}
	}
}

signing {
	sign(publishing.publications["criteria-spring-boot"])
}

tasks.javadoc {
	if (JavaVersion.current().isJava9Compatible) {
		(options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
	}
}


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
        jvmMain {
            dependencies {
                implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.4")
                implementation(project(":core"))
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.mockk:mockk:1.13.8")
                implementation("io.github.serpro69:kotlin-faker:1.15.0")
                implementation("io.kotest:kotest-runner-junit5:5.8.0")
            }
        }
    }
}
