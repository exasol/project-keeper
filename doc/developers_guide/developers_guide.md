# Project-Keepers Developers Guide

## Install Dependencies

You need the following dependencies for running the tests:

* Java Development Kit 11
* Maven 3.6.3 or later
* Go 1.16 or later for testing Go support, see [installation guide](https://go.dev/doc/install)

### go-licenses

[go-licenses](https://github.com/google/go-licenses/) is required for extracting Go module license information.

```sh
go install github.com/google/go-licenses@v1.2.1
```

This will install the binary to `$(go env GOPATH)/bin/go-licenses` (by default `$HOME/go/bin/go-licenses`). Project Keeper tries to find the `go-licenses` binary in `$(go env GOPATH)/bin`. If this fails, you will need to add `$(go env GOPATH)/bin` to your `PATH`.

## Building 

When building a new release of PK then maven might display the following error

```
[ERROR] Failed to execute goal on project project-keeper-java-project-crawler: 
Could not resolve dependencies for project com.exasol:project-keeper-java-project-crawler:maven-plugin:2.5.0: 
com.exasol:project-keeper-shared-model-classes:jar:2.5.0 was not found in 
https://repo.maven.apache.org/maven2 during a previous attempt. 
```

This is due to dependencies: project-keeper depends on java-project-crawler, which in turn depends on shared-model-classes
and all these projects are part of the code base of project-keeper git repository.

In order to fix this, please call `mvn install` for each dependency from leaf to root:
```
mvn install -pl shared-model-classes -DskipTests
mvn install -pl java-project-crawler -DskipTests
```

After that the dependencies of PK are available in your local maven repository in the version of the current release and 
hence references to these versions in the pom of the current release are valid.

## Requirement Tracing

```sh
mvn openfasttrace:trace -projects .
```
