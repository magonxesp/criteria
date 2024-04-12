plugins {
    kotlin("multiplatform") version "1.9.23" apply false
    kotlin("jvm") version "1.9.23" apply false
    kotlin("plugin.serialization") version "1.9.23" apply false
	id("net.thebugmc.gradle.sonatype-central-portal-publisher") version "1.2.3" apply false
}
