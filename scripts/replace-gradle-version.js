import { parseArgs } from "@std/cli/parse-args"
import { replaceVersion } from "./helpers"

const args = parseArgs(Deno.args)
const version = args.version

const buildGradleFiles = [
    'core/build.gradle.kts',
    'spring-boot/build.gradle.kts',
]

buildGradleFiles.forEach((file) => replaceVersion(
    file,
    /version = \"v?[0-9.]+\.?[a-z]*\.?[0-9]*\"/g,
    `version = \"${version.replace(/^v/, '')}\"`,
))
