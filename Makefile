.PHONY: first-version bump-version bump-version-alpha publish publish-local

first-version:
	@/bin/bash -c scripts/create-first-version.sh

bump-version:
	@/bin/bash -c scripts/bump-version-stable.sh

bump-version-alpha:
	@/bin/bash -c scripts/bump-version-alpha.sh

publish:
	@./gradlew publishAndReleaseToMavenCentral

publish-local:
	@./gradlew publishToMavenLocal
