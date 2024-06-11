import { exec, OutputMode } from "https://deno.land/x/exec/mod.ts"
import { replaceVersion } from "./helpers.js"

const context = JSON.parse(
    (await exec('git cliff --unreleased --bump --context', { output: OutputMode.Capture })).output
)
const version = context[0]?.version?.replace(/^([0-9.]+\.?[a-z]*\.?[0-9]*)/, 'v$1')

if (version == null) {
    console.info('There is not a new version available')
    Deno.exit(0)
}

console.info(`The new version is ${version}`)

await exec('git cliff --bump -o CHANGELOG.md')

const buildGradleFiles = [
    'core/build.gradle.kts',
    'spring-boot/build.gradle.kts',
]

buildGradleFiles.forEach((file) => replaceVersion(
    file,
    /version = \"v?[0-9.]+\.?[a-z]*\.?[0-9]*\"/g, 
    `version = \"${version.replace(/^v/, '')}\"`,
))

replaceVersion(
    'README.md',
    /(io\.github\.magonxesp:[a-z\-]+):v?[0-9.]+\.?[a-z]*\.?[0-9]*/g, 
    `\$1:${version.replace(/^v/, '')}`,
)

await Promise.all([
    'CHANGELOG.md',
    'README.md',
    'core/build.gradle.kts',
    'spring-boot/build.gradle.kts',
].map((file) => exec(`git add ${file}`)))

await exec(`git commit -m "🚀 bump version to ${version}"`)
await exec(`git tag ${version}`)

console.info(`🚀 bumped to version ${version}`)
console.info('now you can run "git push"')
