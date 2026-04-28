#!/bin/bash

# This script verifies that an environment variable has the expected value.
# It is used to verify the configuration of the exec plugin. Call it via Maven:
# mvn -f project-keeper/pom.xml exec:exec@verify-exec-plugin-configuration

set -o errexit
set -o nounset
set -o pipefail

if [[ "$#" -ne 2 ]]; then
  echo "Usage: $0 <ENV_VAR_NAME> <EXPECTED_VALUE>" >&2
  exit 2
fi

readonly variable_name="$1"
readonly expected_value="$2"

if [[ ! "$variable_name" =~ ^[A-Za-z_][A-Za-z0-9_]*$ ]]; then
  echo "ERROR: Invalid environment variable name '$variable_name'" >&2
  exit 2
fi

if ! actual_value="$(printenv "$variable_name")"; then
  echo "ERROR: Environment variable '$variable_name' is not set" >&2
  exit 1
fi
readonly actual_value

if [[ "$actual_value" != "$expected_value" ]]; then
  echo "ERROR: Environment variable '$variable_name' has value '$actual_value', expected '$expected_value'" >&2
  exit 1
fi

echo "Environment variable '$variable_name' has the expected value '$actual_value'."
