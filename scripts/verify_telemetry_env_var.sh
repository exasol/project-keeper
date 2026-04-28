#!/bin/bash

# This script verifies that an environment variable has the expected value.
# It is used to verify the configuration of the exec plugin. Call it via Maven:
# mvn -f project-keeper/pom.xml exec:exec@verify-exec-plugin-configuration

set -o errexit
set -o nounset
set -o pipefail

readonly variable_name="EXASOL_TELEMETRY_DISABLE"
readonly expected_value="true"

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
