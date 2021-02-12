# Project keeper maven plugin 0.5.0, released 2021-02-12

Code name: Completed the maven central validation; Added error-code-crawler

## Features

* #52: Changed behavior to not force user to create a changes file if version contains a snapshot tag
* #61: Updated the template for Maven Central release workflow.
* #18: Added validations for maven-source-plugin and maven-javadoc-plugin
* #56: Added validation for error-code-crawler-maven-plugin

## Refactoring

* #30: Refactored to use error message builder
* #46: Refactored integration tests

## Bugfixes

* #55: Fixed CVE-2020-8908 by excluding guava dependency
* #70: Fixed changes template

## Dependency Updates:

* Added `com.exasol:error-code-crawler-maven-plugin:0.1.1`
* Updated `com.exasol:error-reporting-java:0.1.1` to `0.2.2`
* Updated `io.github.classgraph:classgraph:4.8.90` to `4.8.98`
* Updated `org.glassfish.jaxb:jaxb-runtime:2.3.3` to `3.0.0`
* Updated `org.xmlunit:xmlunit-core:2.7.0` to `2.8.2`
* Updated `org.xmlunit:xmlunit-matchers:2.7.0` to `2.8.2`
* Updated `org.mockito:mockito-core:3.6.0` to `3.7.0`
* Removed `org.testcontainers:testcontainers`
* Removed `org.testcontainers:junit-jupiter`
* Added `org.apache.maven.shared:maven-verifier:1.7.2`
