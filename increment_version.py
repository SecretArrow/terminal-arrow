import os
import re

def increment_version():
    gradle_path = 'app/build.gradle.kts'
    with open(gradle_path, 'r') as f:
        content = f.read()

    # Increment versionCode
    version_code_match = re.search(r'versionCode\s*=\s*(\d+)', content)
    if version_code_match:
        current_code = int(version_code_match.group(1))
        new_code = current_code + 1
        content = re.sub(r'versionCode\s*=\s*\d+', f'versionCode = {new_code}', content)
    else:
        new_code = 1

    # Increment versionName
    version_name_match = re.search(r'versionName\s*=\s*"(\d+)\.(\d+)"', content)
    if version_name_match:
        major = int(version_name_match.group(1))
        minor = int(version_name_match.group(2))
        new_minor = minor + 1
        new_name = f"{major}.{new_minor}"
        content = re.sub(r'versionName\s*=\s*"\d+\.\d+"', f'versionName = "{new_name}"', content)
    else:
        new_name = "1.1"

    with open(gradle_path, 'w') as f:
        f.write(content)

    print(f"v{new_name}")

if __name__ == "__main__":
    increment_version()
