#!/bin/bash

scripts_dir=$(dirname ${BASH_SOURCE[0]})
tags=$(git tag | wc -l)

if [[ $tags -ne 0 ]]; then
  echo "The project already has versions"
  exit 1
fi

$scripts_dir/bump-version.sh v0.0.0
