# Project-Keepers Developers Guide

## Install Dependencies

You need the following dependencies for running the tests:

* Java Development Kit 11
* Maven 3.6.3 or later
* Go 1.16 or later for testing Go support, see [installation guide](https://go.dev/doc/install)
* [go-licenses](https://github.com/google/go-licenses/) for extracting Go module license information
    * Version 1.0.0 has issues finding all dependencies, that's why we need the current development version (`latest`):
        ```sh
        go install github.com/google/go-licenses@latest
        ```

## Requirement Tracing

```sh
mvn openfasttrace:trace -projects .
```
