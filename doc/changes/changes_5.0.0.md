# Project Keeper 5.0.0, released 2025-03-??

Code name: Maven 3.8.7, custom artifact clarifications and security policy

## Summary

**Breaking change:** From release 5.0.0 on this project requires a minimum Maven version of 3.8.7. 

This release we improved the documentation on the configuration of custom artifacts that project keeper can build into GitHub workflows.

We also added a security policy to let our contributors know how to best report security problems.

This release improves verification of Maven projects with multiple modules. `project-keeper-maven-plugin` is now only required in the root `pom.xml`, not in sub-modules. This simplifies configuration of multi-module projects.

The release also also updates the Eclipse formatter settings to never join wrapped lines and to indent `case` inside of `switch`. This helps avoid forcing line wraps with trailing `//` comments.

The release also runs the CI build workflow also when a PR is converted from "draft" to "ready for review". This ensures that the complete build runs even if some workflow steps are skipped for draft PRs.

The release also adds stack traces to the default log format configured via `logging.properties`. This will print stack traces for logged exceptions.

The release also moves execution of `display-plugin-updates` and `display-dependency-updates` from Maven phase `package` to `verify`. This improves developer experience by speeding up the local build process.

The release also improves the error message for unknown job ID in workflow customization. Users now directly get a list of available job IDs they can use instead.

The release also adds documentation of `job` parameter for workflow customizations to the user guide.

The release also allows customizing the permissions of generated GitHub workflow jobs. This is useful when the build needs additional permissions like `packages: read` for reading Docker images from the GitHub Container registry `ghcr.io`.

The release also generates the `SECURITY.md` file with instructions for reporting vulnerabilities in the project.

The release also stops overriding Maven property `test.excludeTags` from parent POM. This allows specifying the property in a parent POM for all child modules. Before you had to specify the property in each child module because it was overwritten in each `pk_generated_parent.pom`.

## Features

* #609: Adjusted Eclipse formatter to never join wrapped lines and to indent `case` inside of `switch`
* #620: Run CI build also when a PR is converted from "draft" to "ready for review"
* #612: Added stack traces to default log format
* #619: Moved `versions-maven-plugin` display updates execution to verify phase
* #614: Improved error message for unknown job ID in workflow customization
* #611: Allow customizing GitHub workflow job permissions
* #617: Generate `SECURITY.md` file
* #613: Don't override Maven property `test.excludeTags` from parent POM

## Bugfixes

* #601: Improved verification of Maven projects with multiple modules

## Documentation

* Added security policy (PR #618)
* Added clarifications for custom artifacts (PR #621)
* #610: Added documentation of `job` field for workflow customizations

## Refactoring

* #533: Fixed sonar warnings after migration to Java 17

## Dependency Updates

### Project Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `org.eclipse.jgit:org.eclipse.jgit:7.0.0.202409031743-r` to `7.1.0.202411261347-r`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.17.3` to `3.19.1`
* Removed `org.junit.jupiter:junit-jupiter-engine:5.11.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.11.3` to `5.12.0`
* Updated `org.mockito:mockito-core:5.14.2` to `5.16.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.16` to `2.0.17`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-clean-plugin:3.4.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.13.0` to `3.14.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.3` to `3.1.4`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.3` to `3.1.4`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.11.1` to `3.11.2`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.6.0` to `1.7.0`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.5.0` to `5.0.0`
* Updated `org.snakeyaml:snakeyaml-engine:2.8` to `2.9`
* Updated `org.yaml:snakeyaml:2.3` to `2.4`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:4.5.0` to `5.0.0`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.5.0` to `5.0.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.17.3` to `3.19.1`
* Removed `org.junit.jupiter:junit-jupiter-engine:5.11.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.11.3` to `5.12.0`
* Updated `org.mockito:mockito-junit-jupiter:5.14.2` to `5.16.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.16` to `2.0.17`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-clean-plugin:3.4.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.13.0` to `3.14.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.3` to `3.1.4`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.3` to `3.1.4`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.11.1` to `3.11.2`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.6.0` to `1.7.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.5.0` to `5.0.0`

#### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-api:2.0.16` to `2.0.17`
* Updated `org.slf4j:slf4j-jdk14:2.0.16` to `2.0.17`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.5.0` to `5.0.0`
* Removed `org.junit.jupiter:junit-jupiter-engine:5.11.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.11.3` to `5.12.0`

#### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.4.2` to `0.4.3`
* Updated `org.apache.maven.plugins:maven-clean-plugin:3.4.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.13.0` to `3.14.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.3` to `3.1.4`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.3` to `3.1.4`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.11.1` to `3.11.2`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.6.0` to `1.7.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.5.0` to `5.0.0`

#### Test Dependency Updates

* Removed `org.junit.jupiter:junit-jupiter-engine:5.11.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.11.3` to `5.12.0`
* Updated `org.mockito:mockito-core:5.14.2` to `5.16.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.16` to `2.0.17`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-clean-plugin:3.4.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.13.0` to `3.14.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.3` to `3.1.4`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.3` to `3.1.4`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.11.1` to `3.11.2`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.6.0` to `1.7.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.5.0` to `5.0.0`
* Updated `org.eclipse.jgit:org.eclipse.jgit:7.0.0.202409031743-r` to `7.1.0.202411261347-r`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.5.0` to `5.0.0`
* Removed `org.junit.jupiter:junit-jupiter-engine:5.11.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.11.3` to `5.12.0`
* Updated `org.mockito:mockito-core:5.14.2` to `5.16.0`
* Updated `org.mockito:mockito-junit-jupiter:5.14.2` to `5.16.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.16` to `2.0.17`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-clean-plugin:3.4.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.13.0` to `3.14.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.3` to `3.1.4`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.3` to `3.1.4`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.11.1` to `3.11.2`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.6.0` to `1.7.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.5.0` to `5.0.0`
* Updated `org.yaml:snakeyaml:2.3` to `2.4`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-clean-plugin:3.4.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.13.0` to `3.14.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.3` to `3.1.4`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.6.0` to `1.7.0`
