.PHONY: first-version bump-version bump-version-alpha

first-version:
	@/bin/bash -c scripts/create-first-version.sh

bump-version:
	@/bin/bash -c scripts/bump-version-stable.sh

bump-version-alpha:
	@/bin/bash -c scripts/bump-version-alpha.sh
