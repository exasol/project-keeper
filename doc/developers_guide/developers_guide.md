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

## Requirement Tracing

```sh
mvn openfasttrace:trace -projects .
```
