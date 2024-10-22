# Project Keeper 4.3.4, released 2024-??-??

Code name: Fix vulnerabilities

## Summary

This release fixes vulnerability CVE-2024-47554 in transitive test dependency `commons-io:commons-io` via `com.exasol:maven-plugin-integration-testing:1.1.2` and `com.jcabi:jcabi-github:1.8.0`

The release ignores vulnerability CVE-2023-7272 in transitive runtime dependency `org.glassfish:javax.json:1.1.4` via `com.jcabi:jcabi-github:jar:1.9.1` as this is accepted for accessing exasol json documents on GitHub.

The release also pins Maven plugin versions to avoid plugin versions depending on the Maven version.

The release also installs the Java versions required by Maven sources during the CI builds. Just set property `java.version` in your `pom.xml` and the required JDK will be installed in all GitHub workflows. See the [user guide](../user_guide/user_guide.md#jdk-toolchain-version) for details.

### Features

* #594: Install required Java versions in GitHub workflows

### Security

* #586: Fixed vulnerability CVE-2024-47554 in test dependency `commons-io:commons-io:2.11.0`
* #587: Fixed vulnerability CVE-2024-47554 in test dependency `commons-io:commons-io:2.13.0`
* #588: Ignore vulnerability CVE-2023-7272 in runtime dependency `org.glassfish:javax.json:1.1.4`

### Bugfixes

* #585: Pinned Maven plugin versions in generated parent pom
* #530: Omit `java.version` property when pom has a parent

### Documentation

* #582: Documented automatic release process in user guide

## Dependency Updates

### Project Keeper Root Project

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.2` to `3.1.3`
* Updated `org.itsallcode:openfasttrace-maven-plugin:1.8.0` to `2.2.0`
* Added `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.2.0`

### Project Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `org.eclipse.jgit:org.eclipse.jgit:6.7.0.202309050840-r` to `7.0.0.202409031743-r`
* Updated `org.eclipse:yasson:3.0.3` to `3.0.4`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.16.1` to `3.17.1`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.itsallcode:junit5-system-extensions:1.2.0` to `1.2.2`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.2` to `5.11.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.2` to `5.11.3`
* Updated `org.mockito:mockito-core:5.12.0` to `5.14.2`
* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.16`

#### Plugin Dependency Updates

* Updated `io.github.zlika:reproducible-build-maven-plugin:0.16` to `0.17`
* Updated `org.apache.maven.plugins:maven-clean-plugin:2.5` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.4` to `3.2.7`
* Updated `org.apache.maven.plugins:maven-install-plugin:2.4` to `3.1.3`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.7.0` to `3.10.1`
* Updated `org.apache.maven.plugins:maven-resources-plugin:2.6` to `3.3.1`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.3` to `3.9.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.5` to `3.5.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.2` to `2.17.1`

### Project Keeper Core

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.3.3` to `4.3.4`
* Updated `com.jcabi:jcabi-github:1.8.0` to `1.9.1`
* Updated `org.snakeyaml:snakeyaml-engine:2.7` to `2.8`
* Updated `org.yaml:snakeyaml:2.2` to `2.3`

#### Runtime Dependency Updates

* Updated `com.exasol:project-keeper-java-project-crawler:4.3.3` to `4.3.4`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.2` to `1.1.3`
* Updated `com.exasol:project-keeper-shared-test-setup:4.3.3` to `4.3.4`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.16.1` to `3.17.1`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.junit-pioneer:junit-pioneer:2.2.0` to `2.3.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.2` to `5.11.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.2` to `5.11.3`
* Updated `org.mockito:mockito-junit-jupiter:5.12.0` to `5.14.2`
* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.16`

#### Plugin Dependency Updates

* Updated `io.github.zlika:reproducible-build-maven-plugin:0.16` to `0.17`
* Updated `org.apache.maven.plugins:maven-clean-plugin:2.5` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.5` to `3.5.1`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.4` to `3.2.7`
* Updated `org.apache.maven.plugins:maven-install-plugin:2.4` to `3.1.3`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.4.1` to `3.4.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.7.0` to `3.10.1`
* Updated `org.apache.maven.plugins:maven-resources-plugin:2.6` to `3.3.1`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.3` to `3.9.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.5` to `3.5.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.2` to `2.17.1`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.3.3` to `4.3.4`
* Updated `org.apache.maven:maven-model:3.9.7` to `3.9.9`

#### Runtime Dependency Updates

* Added `org.slf4j:slf4j-api:2.0.16`
* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.16`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.3.3` to `4.3.4`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.2` to `5.11.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.2` to `5.11.3`

#### Plugin Dependency Updates

* Updated `io.github.zlika:reproducible-build-maven-plugin:0.16` to `0.17`
* Updated `org.apache.maven.plugins:maven-clean-plugin:2.5` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.5` to `3.5.1`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.4` to `3.2.7`
* Updated `org.apache.maven.plugins:maven-install-plugin:2.4` to `3.1.3`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.4.1` to `3.4.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.7.0` to `3.10.1`
* Updated `org.apache.maven.plugins:maven-resources-plugin:2.6` to `3.3.1`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.3` to `3.9.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.5` to `3.5.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.2` to `2.17.1`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.3.3` to `4.3.4`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.2` to `1.1.3`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.2` to `5.11.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.2` to `5.11.3`
* Updated `org.mockito:mockito-core:5.12.0` to `5.14.2`
* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.16`

#### Plugin Dependency Updates

* Updated `io.github.zlika:reproducible-build-maven-plugin:0.16` to `0.17`
* Updated `org.apache.maven.plugins:maven-clean-plugin:2.5` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.6.1` to `3.8.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.5` to `3.5.1`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.4` to `3.2.7`
* Updated `org.apache.maven.plugins:maven-install-plugin:2.4` to `3.1.3`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.7.0` to `3.10.1`
* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.13.1` to `3.15.0`
* Updated `org.apache.maven.plugins:maven-resources-plugin:2.6` to `3.3.1`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.3` to `3.9.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.5` to `3.5.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.2` to `2.17.1`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.3.3` to `4.3.4`
* Updated `org.eclipse.jgit:org.eclipse.jgit:6.7.0.202309050840-r` to `7.0.0.202409031743-r`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.2` to `1.1.3`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.2` to `5.11.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.2` to `5.11.3`
* Updated `org.mockito:mockito-core:5.12.0` to `5.14.2`
* Updated `org.mockito:mockito-junit-jupiter:5.12.0` to `5.14.2`
* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.16`

#### Plugin Dependency Updates

* Updated `io.github.zlika:reproducible-build-maven-plugin:0.16` to `0.17`
* Updated `org.apache.maven.plugins:maven-clean-plugin:2.5` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.6.1` to `3.8.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.5` to `3.5.1`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.4` to `3.2.7`
* Updated `org.apache.maven.plugins:maven-install-plugin:2.4` to `3.1.3`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.7.0` to `3.10.1`
* Updated `org.apache.maven.plugins:maven-plugin-plugin:3.13.1` to `3.15.0`
* Updated `org.apache.maven.plugins:maven-resources-plugin:2.6` to `3.3.1`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.3` to `3.9.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.5` to `3.5.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.2` to `2.17.1`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.3.3` to `4.3.4`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.yaml:snakeyaml:2.2` to `2.3`

#### Plugin Dependency Updates

* Updated `io.github.zlika:reproducible-build-maven-plugin:0.16` to `0.17`
* Updated `org.apache.maven.plugins:maven-clean-plugin:2.5` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:2.4` to `3.1.3`
* Updated `org.apache.maven.plugins:maven-resources-plugin:2.6` to `3.3.1`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.3` to `3.9.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.5` to `3.5.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.2` to `2.17.1`
