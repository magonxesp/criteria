import org.gradle.api.publish.maven.MavenPom
import org.gradle.kotlin.dsl.assign

fun MavenPom.basicInformation() {
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
