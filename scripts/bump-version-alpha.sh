#!/bin/bash

scripts_dir=$(dirname ${BASH_SOURCE[0]})

VERSION=$( \
	git cliff --unreleased --bump --context \
	| jq -r '.[0].version' \
	| sed -r -e 's/^([0-9.]+\-?[a-z]*\.?[0-9]*)/v\1/g' \
	| sed -r -e 's/^(v[0-9.]+)$/\1\-alpha\.1/g' \
)

if [[ "$VERSION" != "null" ]]; then
  $scripts_dir/bump-version.sh $VERSION
else
  echo "There is not a new version available"
fi
