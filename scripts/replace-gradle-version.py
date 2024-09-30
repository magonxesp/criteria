import argparse
import re
from functions import replace_version

parser = argparse.ArgumentParser(prog='Gradle version replace',)
parser.add_argument('version')
args = parser.parse_args()

version_code = re.sub(r'^v', '', args.version)

files = [
    'core/build.gradle.kts',
    'spring-boot/build.gradle.kts',
]

for file in files:
    replace_version(
        file_path=file,
        search=r'^version = "v?[0-9.]+\-?[a-z]*\.?[0-9]*"',
        replacement=f'version = "{version_code}"'
    )
