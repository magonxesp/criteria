plugins {
    kotlin("multiplatform") version "2.0.20" apply false
    kotlin("jvm") version "2.0.20" apply false
    kotlin("plugin.serialization") version "2.0.20" apply false
	id("com.vanniktech.maven.publish") version "0.28.0" apply false
	id("com.android.library") version "8.3.1" apply false
	id("org.jetbrains.dokka") version "1.9.20"
	id("io.kotest.multiplatform") version "5.0.2" apply false
}
