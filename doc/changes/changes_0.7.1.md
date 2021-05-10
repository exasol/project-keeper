# Project keeper maven plugin 0.7.1, released 2021-05-10

Code name: Support running on Windows

## Refactoring:

* #97: Removed hard-coded version number from tests
* #110: Replaced explicit types by var
* #111: Extracted coverage from integration tests
* #80: Made validation API functional
* #114: Extracted library for maven plugin testing
* #79: Added builder for ValidationFinding

## Bugfixes

* #119: Fixed windows compatibility issues
* #123: Fixed jar compatibility (jar built on linux did not work as expected on Windows)
* #125: Fixed sonar findings

## Dependency Updates

### Compile Dependency Updates

* Updated `org.eclipse.jgit:org.eclipse.jgit:5.10.0.202012080955-r` to `5.11.0.202103091610-r`

### Runtime Dependency Updates

* Added `org.jacoco:org.jacoco.agent:0.8.6`

### Test Dependency Updates

* Added `com.exasol:maven-plugin-integration-testing:0.1.0`
* Added `com.exasol:maven-project-version-getter:0.1.0`
* Removed `org.apache.maven.shared:maven-verifier:1.7.2`
* Removed `org.jacoco:org.jacoco.agent:0.8.5`
* Updated `org.jacoco:org.jacoco.core:0.8.5` to `0.8.6`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:0.7.0` to `0.7.1`
* Added `org.apache.maven.plugins:maven-dependency-plugin:2.8`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.5` to `0.8.6`
