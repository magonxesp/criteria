import argparse
import subprocess
import json
import re
from functions import replace_version

parser = argparse.ArgumentParser(
    prog='Bump version script',
    description='Create new version number and add the git tag'
)

parser.add_argument('-f', '--first-version', dest='first_version', default=False, action='store_true')
parser.add_argument('-a', '--alpha', default=False, action='store_true')
parser.add_argument('-b', '--beta', default=False, action='store_true')

args = parser.parse_args()

version = None
result = subprocess.run(['git', 'cliff', '--unreleased', '--bump', '--context'], capture_output=True)
result.check_returncode()

if result.stdout is not None:
    context = json.loads(result.stdout.decode('utf-8'))

    if len(context) > 0 and 'version' in context[0].keys():
        version = re.sub(r'^([0-9.]+\.?[a-z]*\.?[0-9]*)', r'v\1', context[0]['version'])

if version is None:
    print('There is not a new version available')
    exit(0)

if args.first_version is True and version is None:
    version = 'v0.0.0'
elif args.alpha is True:
    version = re.sub(r'^(v?[0-9.]+)$', r'\1-alpha.1', version)
elif args.beta is True:
    version = re.sub(r'^(v?[0-9.]+)$', r'\1-beta.1', version)
else:
    version = re.sub(r'^(v[0-9.]+)(-[a-z]+\.?[0-9]*)$', r'\1', version)

print(f'The new version is {version}')

subprocess.run(['git', 'tag', version]).check_returncode()
subprocess.run(['git', 'cliff', '--bump', '-o', 'CHANGELOG.md']).check_returncode()

version_code = re.sub(r'^v', '', version)

buildGradleFiles = [
    'core/build.gradle.kts',
    'spring-boot/build.gradle.kts',
]

for buildGradleFile in buildGradleFiles:
    replace_version(
        file_path=buildGradleFile,
        search=r'^version = "v?[0-9.]+\-?[a-z]*\.?[0-9]*"',
        replacement=f'version = "{version_code}"'
    )

replace_version(
    file_path='README.md',
    search=r'io\.github\.magonxesp:([a-z\-]+):v?[0-9.]+\-?[a-z]*\.?[0-9]*',
    replacement=r'io.github.magonxesp:\1:' + version_code
)

files = [
    'CHANGELOG.md',
    'core/build.gradle.kts',
    'spring-boot/build.gradle.kts',
    'README.md'
]

for file in files:
    subprocess.run(['git', 'add', file]).check_returncode()

subprocess.run(['git', 'commit', '-m', f'🚀 bump version to {version}']).check_returncode()
print(f'🚀 bumped to version {version}')
print('now you can run "git push"')
