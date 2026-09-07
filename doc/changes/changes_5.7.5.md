# Project Keeper 5.7.5, released 2026-??-??

Code name:

## Summary

This release avoids invoking `go-licenses` from cached module directories when retrieving Go test dependency licenses. This avoids problems for dependencies that don't contain a `go.sum` file.

The release also upgrades the Go version used in GitHub workflow `project-keeper-verify.yml` to 1.27. This won't affect the Go version in the actual CI workflows of your projects.

## Features

## Bug Fixes

* #770: Avoid invoking `go-licenses` from cached module directories when retrieving Go test dependency licenses.

## Dependency Updates

### Project Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `jakarta.json.bind:jakarta.json.bind-api:3.0.2` to `3.0.3`
* Updated `org.eclipse.jgit:org.eclipse.jgit:7.7.0.202606012155-r` to `7.7.1.202607240634-r`
* Updated `org.eclipse:yasson:3.0.4` to `3.0.5`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:4.5` to `4.5.2`
* Updated `org.junit.jupiter:junit-jupiter:6.1.2` to `6.1.3`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.0` to `2.1.1`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:10.0.0` to `10.0.1`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.3.0`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.3` to `1.8.0`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.4` to `5.7.5`
* Updated `org.snakeyaml:snakeyaml-engine:3.0.1` to `3.1.1`
* Updated `org.xmlunit:xmlunit-core:2.12.0` to `2.13.0`
* Updated `org.yaml:snakeyaml:2.6` to `2.7`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:5.7.4` to `5.7.5`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.4` to `5.7.5`
* Updated `nl.jqno.equalsverifier:equalsverifier:4.5` to `4.5.2`
* Updated `org.junit.jupiter:junit-jupiter:6.1.2` to `6.1.3`
* Updated `org.xmlunit:xmlunit-matchers:2.12.0` to `2.13.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.0` to `2.1.1`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:10.0.0` to `10.0.1`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.5.0` to `3.5.1`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.3.0`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.3` to `1.8.0`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.7.4` to `5.7.5`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.4` to `5.7.5`
* Updated `org.junit.jupiter:junit-jupiter:6.1.2` to `6.1.3`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.0` to `2.1.1`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:10.0.0` to `10.0.1`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.5.0` to `3.5.1`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.3.0`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.3` to `1.8.0`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:5.7.4` to `5.7.5`

#### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter:6.1.2` to `6.1.3`
* Updated `org.xmlunit:xmlunit-matchers:2.12.0` to `2.13.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.0` to `2.1.1`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:10.0.0` to `10.0.1`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.5.0` to `3.5.1`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.3.0`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.3` to `1.8.0`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.4` to `5.7.5`
* Updated `org.eclipse.jgit:org.eclipse.jgit:7.7.0.202606012155-r` to `7.7.1.202607240634-r`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:5.7.4` to `5.7.5`
* Updated `org.junit.jupiter:junit-jupiter:6.1.2` to `6.1.3`
* Updated `org.xmlunit:xmlunit-matchers:2.12.0` to `2.13.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.0` to `2.1.1`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:10.0.0` to `10.0.1`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.3.0`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.3` to `1.8.0`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:5.7.4` to `5.7.5`
* Updated `org.yaml:snakeyaml:2.6` to `2.7`

#### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter:6.1.2` to `6.1.3`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.0` to `2.1.1`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:10.0.0` to `10.0.1`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.3.0`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.3` to `1.8.0`
