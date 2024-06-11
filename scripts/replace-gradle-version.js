import { parseArgs } from "@std/cli/parse-args"
import { replaceVersion } from "./helpers"

const args = parseArgs(Deno.args)
const version = args.version

[
    'core/build.gradle.kts',
    'spring-boot/build.gradle.kts',
].forEach((file) => replaceVersion(
    file,
    /version = \"v?[0-9.]+\.?[a-z]*\.?[0-9]*\"/g, 
    `version = \"${version.replace(/^v/, '')}\"`,
))