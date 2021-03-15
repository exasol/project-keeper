#Project keeper maven plugin 0.3.0, released 2020-11-13

Code name: Added more validations

## Summary

Added new validations:

* doc/changes/changelog_X.X.X.md
* travis.yml
* LICENSE
* for maven-central: .github/workflows/maven_central_release.yml
* maven-gpg-plugin
* maven-deploy-plugin
* nexus-staging-maven-plugin

Renamed file:

* .github/workflows/maven.yml --> *.github/workflows/dependencies_check.yml

## Features / Enhancements

* #21: Added validation for doc/changes/changelog_X.X.X.md
* #24: Updates in the templates

## Bugfixes:

* #26: Don't require -SNAPSHOT changes files
  This bug was introduced in #21 and never released

## Dependency Updates:

* Updated `org.mockito:mockito-core` from 3.5.15 to 3.6.0
* Updated `javax.xml.bind:jaxb-api` from 2.3.0 to 2.3.1
* Updated `org.glassfish.jaxb:jaxb-runtime` from 2.3.0 to 2.3.3
* Updated `org.apache.maven:plugin-tools` from 3.5 to 3.6.0
* Added `org.apache.maven:maven-core` 3.6.3
* Added `com.exasol:error-reporting-java` 0.1.1
* Added `org.apache.maven.plugins:maven-deploy-plugin`
* Added `org.sonatype.plugins:nexus-staging-maven-plugin` 1.6.8 
