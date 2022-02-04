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
* #238: Refactored PomFileValidator to maven projects with parent

## Dependency Updates

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:1.3.3` to `1.3.4`
* Updated `io.github.zlika:reproducible-build-maven-plugin:0.13` to `0.14`
