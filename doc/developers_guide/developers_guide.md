# Project-Keepers Developers Guide

## Install Dependencies

You need the following dependencies for running the tests:

* Java Development Kit 11
* Maven 3.6.3 or later
* Go 1.16 or later for testing Go support, see [installation guide](https://go.dev/doc/install)

### go-licenses

[go-licenses](https://github.com/google/go-licenses/) is required for extracting Go module license information.
Since version 2.7.0 PK will automatically install `go-licenses` if required.

## Building

When building a new release of PK then Maven might display the following error:

```
[ERROR] Failed to execute goal on project project-keeper-java-project-crawler:
Could not resolve dependencies for project com.exasol:project-keeper-java-project-crawler:maven-plugin:2.5.0:
com.exasol:project-keeper-shared-model-classes:jar:2.7.0 was not found in
https://repo.maven.apache.org/maven2 during a previous attempt.
```

In order to fix this, just install project-keeper:

```sh
mvn install --projects . -DskipTests
```

After that the dependencies of PK are available in your local maven repository in the version of the current release and hence references to these versions in the pom of the current release are valid.

## Running Manual Tests To Examine a Specific Project

In case one of the users of PK reports a bug for a specific project you can reproduce and debug PK's behavior in your IDE by using an appropriate run configuration.

Usually you need to
1. Checkout the project to be examined
2. Potentially modify the project's files
3. [Make PK's project crawler available to maven using the version opened in your IDE](#make-project-crawler-available)
4. Create an appropriate launch configuration for PK
   * using the folder containing the examined project as working directory
   * potentially setting some system properties with `-Dproperty=value`], see [Specify PK Version](#specify-pk-version)

### Make Project Crawler Available

In some cases PK runs maven as a shell command. Maven will then search your for PK module "project crawler" in the version identical to PK opened in your IDE. The following command publishes project crawler to your local maven repository `~/.m2`:

```shell
mvn clean install -DskipTests
```

### Specify PK Version

PK gets its own version from source package. Unfortunatly this isn't available in IDE. Instead you can specify the version by setting the follwing system property:
```
-Dcom.exasol.projectkeeper.ownVersion=2.7.0
```

## Requirement Tracing

```sh
mvn openfasttrace:trace --projects .
```
