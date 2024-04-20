pluginManagement {
	repositories {
		google()
		mavenCentral()
		mavenLocal()
		gradlePluginPortal()
	}
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		google()
		mavenCentral()
		mavenLocal()
	}
}

rootProject.name = "criteria"

include(":core")
include(":spring-boot")