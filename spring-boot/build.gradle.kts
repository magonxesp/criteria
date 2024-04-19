import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
	id("com.vanniktech.maven.publish")
}

group = "io.github.magonxesp"
version = "0.0.4"

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

repositories {
    mavenCentral()
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
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.4")
	implementation(project(":core"))

	testImplementation(kotlin("test"))
	testImplementation("io.mockk:mockk:1.13.8")
	testImplementation("io.github.serpro69:kotlin-faker:1.15.0")
	testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
}

