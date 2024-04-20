.PHONY: bump-version publish publish-local

bump-version:
	@deno run --allow-run --allow-write --allow-read scripts/bump-version.js

publish:
	@./gradlew publishAndReleaseToMavenCentral

publish-local:
	@./gradlew publishToMavenLocal
