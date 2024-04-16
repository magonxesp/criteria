#!/bin/bash

VERSION=$1
git cliff --bump -o CHANGELOG.md
git add CHANGELOG.md
sed -i .bak "s/version = \"v?[0-9.]+\.?[a-z]*\.?[0-9]*\"/version = \"$VERSION\"/g" core/build.gradle.kts
sed -i .bak "s/version = \"v?[0-9.]+\.?[a-z]*\.?[0-9]*\"/version = \"$VERSION\"/g" spring-boot/build.gradle.kts
git add core/build.gradle.kts spring-boot/build.gradle.kts
sed -i .bak -r "s/io\.github\.magonxesp:([a-z\-]+):v?[0-9.]+\.?[a-z]*\.?[0-9]*/io\.github\.magonxesp:\1:$VERSION/g" README.md
git add README.md
git commit -m "🚀 bump version to $VERSION"
git tag "$VERSION"
