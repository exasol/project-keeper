# Project Keeper 4.3.4, released 2024-10-15

Code name: Fix vulnerabilities

## Summary

This release fixes vulnerabilities
* CVE-2024-47554 in transitive test dependency `commons-io:commons-io` via `com.exasol:maven-plugin-integration-testing:1.1.2` and `com.jcabi:jcabi-github:1.8.0`
* CVE-2023-7272 in transitive runtime dependency `org.glassfish:javax.json:1.1.4` via `com.jcabi:jcabi-github:1.8.0`

### Security

* #586: Fixed vulnerability CVE-2024-47554 in test dependency `commons-io:commons-io:2.11.0`
* #587: Fixed vulnerability CVE-2024-47554 in test dependency `commons-io:commons-io:2.13.0`
* #588: Fixed vulnerability CVE-2023-7272 in runtime dependency `org.glassfish:javax.json:1.1.4`

## Dependency Updates

### Project Keeper Shared Model Classes

#### Compile Dependency Updates

* Updated `org.eclipse:yasson:3.0.3` to `3.0.4`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.16.1` to `3.17.1`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.itsallcode:junit5-system-extensions:1.2.0` to `1.2.2`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.2` to `5.11.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.2` to `5.11.2`
* Updated `org.mockito:mockito-core:5.12.0` to `5.14.1`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.2`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.2` to `3.1.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.5.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.4` to `3.2.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.7.0` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.1.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121` to `3.11.0.3922`
* Updated `org.sonatype.plugins:nexus-staging-maven-plugin:1.7.0` to `1.6.13`

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
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.2` to `5.11.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.2` to `5.11.2`
* Updated `org.mockito:mockito-junit-jupiter:5.12.0` to `5.14.1`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.2`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.2` to `3.1.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.5.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.4` to `3.2.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.7.0` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.1.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121` to `3.11.0.3922`
* Updated `org.sonatype.plugins:nexus-staging-maven-plugin:1.7.0` to `1.6.13`

### Project Keeper Command Line Interface

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.3.3` to `4.3.4`
* Updated `org.apache.maven:maven-model:3.9.7` to `3.9.9`

#### Test Dependency Updates

* Updated `com.exasol:project-keeper-shared-test-setup:4.3.3` to `4.3.4`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.2` to `5.11.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.2` to `5.11.2`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.2`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.2` to `3.1.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.5.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.4` to `3.2.2`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.4.1` to `3.3.0`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.7.0` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.1.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121` to `3.11.0.3922`
* Updated `org.sonatype.plugins:nexus-staging-maven-plugin:1.7.0` to `1.6.13`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-core:4.3.3` to `4.3.4`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.2` to `1.1.3`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.jacoco:org.jacoco.agent:0.8.12` to `0.8.11`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.2` to `5.11.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.2` to `5.11.2`
* Updated `org.mockito:mockito-core:5.12.0` to `5.14.1`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.2`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.2` to `3.1.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.5.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.4` to `3.2.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.7.0` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.1.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121` to `3.11.0.3922`
* Updated `org.sonatype.plugins:nexus-staging-maven-plugin:1.7.0` to `1.6.13`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.3.3` to `4.3.4`

#### Test Dependency Updates

* Updated `com.exasol:maven-plugin-integration-testing:1.1.2` to `1.1.3`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.jacoco:org.jacoco.agent:0.8.12` to `0.8.11`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.2` to `5.11.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.2` to `5.11.2`
* Updated `org.mockito:mockito-core:5.12.0` to `5.14.1`
* Updated `org.mockito:mockito-junit-jupiter:5.12.0` to `5.14.1`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.2`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.2` to `3.1.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.5.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.4` to `3.2.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.7.0` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.1.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121` to `3.11.0.3922`
* Updated `org.sonatype.plugins:nexus-staging-maven-plugin:1.7.0` to `1.6.13`

### Project Keeper Shared Test Setup

#### Compile Dependency Updates

* Updated `com.exasol:project-keeper-shared-model-classes:4.3.3` to `4.3.4`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.yaml:snakeyaml:2.2` to `2.3`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.2`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.5.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.1.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121` to `3.11.0.3922`
