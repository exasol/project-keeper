# Project keeper maven plugin 2.0.0, released 2021-XX-XX

Code name:

## Summary:

This release has **breaking changes**:

* The configuration moved from the maven plugin to a dedicated `.project-keeper.yml` file. See [README](../../README.md).

## Refactoring

* #212: Decoupled maven plugin as executor from maven project analysis
* #135: Add unified exclude mechanism
* #224: Moved configuration to config file
* #227: Declared file templates in Java code
* #229: Refactored project files validation to support maven multi-module projects
* #231: Refactored ReadmeValidator to support maven multi-module projects
* #233: Refactored DependenciesValidator to support maven multi-module projects
* #235: Refactored ChangesFileValidator to support maven multi-module projects
* #238: Refactored PomFileValidator
* #238: Refactored PomFileValidator to support parent pom
* #247: Detect project version from source

## Bugfixes

* #218: Fixed executing Maven under Windows.

## Dependency Updates

### Project-keeper-shared-model-classes

#### Compile Dependency Updates

* Added `com.exasol:error-reporting-java:0.4.1`
* Added `jakarta.json.bind:jakarta.json.bind-api:2.0.0`
* Added `jakarta.json:jakarta.json-api:2.0.1`
* Added `org.eclipse.jgit:org.eclipse.jgit:6.0.0.202111291000-r`
* Added `org.eclipse:yasson:2.0.4`

#### Runtime Dependency Updates

* Added `org.glassfish:jakarta.json:2.0.1`

#### Test Dependency Updates

* Added `org.hamcrest:hamcrest:2.2`
* Added `org.itsallcode:junit5-system-extensions:1.2.0`
* Added `org.junit.jupiter:junit-jupiter-engine:5.8.2`
* Added `org.junit.jupiter:junit-jupiter-params:5.8.2`

#### Plugin Dependency Updates

* Added `com.exasol:error-code-crawler-maven-plugin:0.7.1`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.15`
* Added `org.apache.maven.plugins:maven-clean-plugin:2.5`
* Added `org.apache.maven.plugins:maven-compiler-plugin:3.9.0`
* Added `org.apache.maven.plugins:maven-deploy-plugin:3.0.0-M1`
* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0`
* Added `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M3`
* Added `org.apache.maven.plugins:maven-gpg-plugin:3.0.1`
* Added `org.apache.maven.plugins:maven-install-plugin:2.4`
* Added `org.apache.maven.plugins:maven-jar-plugin:2.4`
* Added `org.apache.maven.plugins:maven-javadoc-plugin:3.3.1`
* Added `org.apache.maven.plugins:maven-resources-plugin:2.6`
* Added `org.apache.maven.plugins:maven-site-plugin:3.3`
* Added `org.apache.maven.plugins:maven-source-plugin:3.2.1`
* Added `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M5`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.2.7`
* Added `org.codehaus.mojo:versions-maven-plugin:2.8.1`
* Added `org.jacoco:jacoco-maven-plugin:0.8.7`
* Added `org.projectlombok:lombok-maven-plugin:1.18.20.0`
* Added `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.1.0`
* Added `org.sonatype.plugins:nexus-staging-maven-plugin:1.6.8`

### Project Keeper Core

#### Compile Dependency Updates

* Added `com.exasol:error-reporting-java:0.4.1`
* Added `com.exasol:project-keeper-shared-model-classes:2.0.0`
* Added `com.vdurmont:semver4j:3.1.0`
* Added `javax.xml.bind:jaxb-api:2.3.1`
* Added `net.steppschuh.markdowngenerator:markdowngenerator:1.3.1.1`
* Added `org.glassfish.jaxb:jaxb-runtime:3.0.2`
* Added `org.xmlunit:xmlunit-core:2.9.0`
* Added `org.yaml:snakeyaml:1.30`

#### Runtime Dependency Updates

* Added `com.exasol:project-keeper-java-project-crawler:2.0.0`

#### Test Dependency Updates

* Added `com.exasol:maven-plugin-integration-testing:1.1.0`
* Added `com.exasol:maven-project-version-getter:1.1.0`
* Added `org.hamcrest:hamcrest:2.2`
* Added `org.junit.jupiter:junit-jupiter-engine:5.8.2`
* Added `org.junit.jupiter:junit-jupiter-params:5.8.2`
* Added `org.mockito:mockito-core:4.3.1`
* Added `org.xmlunit:xmlunit-matchers:2.9.0`

#### Plugin Dependency Updates

* Added `com.exasol:error-code-crawler-maven-plugin:0.7.1`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.15`
* Added `org.apache.maven.plugins:maven-clean-plugin:2.5`
* Added `org.apache.maven.plugins:maven-compiler-plugin:3.9.0`
* Added `org.apache.maven.plugins:maven-deploy-plugin:3.0.0-M1`
* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0`
* Added `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M5`
* Added `org.apache.maven.plugins:maven-gpg-plugin:3.0.1`
* Added `org.apache.maven.plugins:maven-install-plugin:2.4`
* Added `org.apache.maven.plugins:maven-jar-plugin:3.2.2`
* Added `org.apache.maven.plugins:maven-javadoc-plugin:3.3.1`
* Added `org.apache.maven.plugins:maven-resources-plugin:2.6`
* Added `org.apache.maven.plugins:maven-site-plugin:3.3`
* Added `org.apache.maven.plugins:maven-source-plugin:3.2.1`
* Added `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M5`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.2.7`
* Added `org.codehaus.mojo:versions-maven-plugin:2.8.1`
* Added `org.jacoco:jacoco-maven-plugin:0.8.7`
* Added `org.projectlombok:lombok-maven-plugin:1.18.20.0`
* Added `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.1.0`
* Added `org.sonatype.plugins:nexus-staging-maven-plugin:1.6.8`

### Project Keeper Maven Plugin

#### Compile Dependency Updates

* Added `com.exasol:error-reporting-java:0.4.1`
* Added `com.exasol:project-keeper-core:2.0.0`

#### Test Dependency Updates

* Added `com.exasol:maven-plugin-integration-testing:1.1.0`
* Added `com.exasol:maven-project-version-getter:1.1.0`
* Added `org.hamcrest:hamcrest:2.2`
* Added `org.jacoco:org.jacoco.agent:0.8.5`
* Added `org.jacoco:org.jacoco.core:0.8.7`
* Added `org.junit.jupiter:junit-jupiter-engine:5.8.2`
* Added `org.junit.jupiter:junit-jupiter-params:5.8.2`
* Added `org.mockito:mockito-core:4.3.1`
* Added `org.xmlunit:xmlunit-matchers:2.9.0`

#### Plugin Dependency Updates

* Added `com.exasol:error-code-crawler-maven-plugin:0.7.1`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.15`
* Added `org.apache.maven.plugins:maven-clean-plugin:2.5`
* Added `org.apache.maven.plugins:maven-compiler-plugin:3.9.0`
* Added `org.apache.maven.plugins:maven-dependency-plugin:3.2.0`
* Added `org.apache.maven.plugins:maven-deploy-plugin:3.0.0-M1`
* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0`
* Added `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M5`
* Added `org.apache.maven.plugins:maven-gpg-plugin:3.0.1`
* Added `org.apache.maven.plugins:maven-install-plugin:2.4`
* Added `org.apache.maven.plugins:maven-jar-plugin:3.2.2`
* Added `org.apache.maven.plugins:maven-javadoc-plugin:3.3.1`
* Added `org.apache.maven.plugins:maven-plugin-plugin:3.6.4`
* Added `org.apache.maven.plugins:maven-resources-plugin:2.6`
* Added `org.apache.maven.plugins:maven-site-plugin:3.3`
* Added `org.apache.maven.plugins:maven-source-plugin:3.2.1`
* Added `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M5`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.2.7`
* Added `org.codehaus.mojo:versions-maven-plugin:2.8.1`
* Added `org.jacoco:jacoco-maven-plugin:0.8.7`
* Added `org.projectlombok:lombok-maven-plugin:1.18.20.0`
* Added `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.1.0`
* Added `org.sonatype.plugins:nexus-staging-maven-plugin:1.6.8`

### Project Keeper Java Project Crawler

#### Compile Dependency Updates

* Added `com.exasol:error-reporting-java:0.4.1`
* Added `com.exasol:project-keeper-shared-model-classes:2.0.0`
* Added `com.vdurmont:semver4j:3.1.0`
* Added `org.eclipse.jgit:org.eclipse.jgit:6.0.0.202111291000-r`

#### Test Dependency Updates

* Added `com.exasol:maven-plugin-integration-testing:1.1.0`
* Added `com.exasol:maven-project-version-getter:1.1.0`
* Added `org.hamcrest:hamcrest:2.2`
* Added `org.jacoco:org.jacoco.agent:0.8.7`
* Added `org.jacoco:org.jacoco.core:0.8.7`
* Added `org.junit.jupiter:junit-jupiter-engine:5.8.2`
* Added `org.junit.jupiter:junit-jupiter-params:5.8.2`
* Added `org.mockito:mockito-core:4.3.1`
* Added `org.slf4j:slf4j-jdk14:1.7.35`
* Added `org.xmlunit:xmlunit-matchers:2.9.0`

#### Plugin Dependency Updates

* Added `com.exasol:error-code-crawler-maven-plugin:0.7.1`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.15`
* Added `org.apache.maven.plugins:maven-clean-plugin:2.5`
* Added `org.apache.maven.plugins:maven-compiler-plugin:3.9.0`
* Added `org.apache.maven.plugins:maven-dependency-plugin:3.2.0`
* Added `org.apache.maven.plugins:maven-deploy-plugin:3.0.0-M1`
* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0`
* Added `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M5`
* Added `org.apache.maven.plugins:maven-gpg-plugin:3.0.1`
* Added `org.apache.maven.plugins:maven-install-plugin:2.4`
* Added `org.apache.maven.plugins:maven-jar-plugin:2.4`
* Added `org.apache.maven.plugins:maven-javadoc-plugin:3.3.1`
* Added `org.apache.maven.plugins:maven-plugin-plugin:3.6.4`
* Added `org.apache.maven.plugins:maven-resources-plugin:2.6`
* Added `org.apache.maven.plugins:maven-site-plugin:3.3`
* Added `org.apache.maven.plugins:maven-source-plugin:3.2.1`
* Added `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M5`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.2.7`
* Added `org.codehaus.mojo:versions-maven-plugin:2.8.1`
* Added `org.jacoco:jacoco-maven-plugin:0.8.7`
* Added `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.1.0`
* Added `org.sonatype.plugins:nexus-staging-maven-plugin:1.6.8`
