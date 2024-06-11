import { parseArgs } from "https://deno.land/std@0.224.0/cli/mod.ts"
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
