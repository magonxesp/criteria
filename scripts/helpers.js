/**
 * Replace a file content by a regexp
 * 
 * @param {string} file 
 * @param {RegExp} searchRegex 
 * @param {(string[]) => string} newStringCallback 
 */
export function replaceVersion (file, searchRegex, newString) {
    const content = Deno.readTextFileSync(file)
    const newContent = content.replaceAll(searchRegex, newString)
    Deno.writeTextFileSync(file, newContent)
}