.PHONY: bump-first-version bump-version publish publish-local

first-version:
	@PYTHONPATH="$$(pwd)/scripts" python scripts/bump-version.py --first-version

bump-version:
	@PYTHONPATH="$$(pwd)/scripts" python scripts/bump-version.py

publish:
	@./gradlew publishAndReleaseToMavenCentral

publish-local:
	@./gradlew publishToMavenLocal
