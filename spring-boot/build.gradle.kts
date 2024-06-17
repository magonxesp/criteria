import com.vanniktech.maven.publish.SonatypeHost
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    kotlin("plugin.spring") version "1.9.23"
	kotlin("plugin.jpa") version "1.9.23"
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	id("com.vanniktech.maven.publish")
	id("org.jetbrains.dokka")
}

group = "io.github.magonxesp"
version = "0.2.0"

mavenPublishing {
	coordinates(group as String, "criteria-spring-boot", version as String)
	publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
	signAllPublications()

	pom {
		name = "Criteria for Spring Boot"
		description = "The Criteria library adapted with Spring Boot data specification adapter"
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

java {
	targetCompatibility = JavaVersion.VERSION_17
	sourceCompatibility = JavaVersion.VERSION_17
}

kotlin {
	java {
		jvmToolchain(17)
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation(project(":core"))

	testImplementation(kotlin("test"))
	testImplementation("io.mockk:mockk:1.13.8")
	testImplementation("io.github.serpro69:kotlin-faker:1.15.0")
	testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.testcontainers:testcontainers:1.19.7")
	testImplementation("org.testcontainers:mariadb:1.19.7")
	testImplementation("org.testcontainers:junit-jupiter:1.19.7")
	testRuntimeOnly("org.mariadb.jdbc:mariadb-java-client:3.3.3")
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}

tasks.withType<BootJar>().configureEach {
	enabled = false
}
