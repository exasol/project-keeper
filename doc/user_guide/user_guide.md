# User Guide

## Installation

Install this plugin by adding the following lines to your project's `pom.xml` file:

```xml
<plugins>
    <plugin>
        <groupId>com.exasol</groupId>
        <artifactId>project-keeper-maven-plugin</artifactId>
        <version>CURRENT VERSION</version>
        <executions>
            <execution>
                <goals>
                    <goal>verify</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
</plugins>
```

## Configuration

Create a `.project-keeper.yml` configuration file in the project's root directory:

```yml
sources:
  - type: maven
    path: pom.xml
    modules:
      - maven_central
```

### Sources

For PK a 'source' is a project inside a repository, e.g. a Maven module. PK is able to crawl multiple source projects from one repository.

Supported project types:

- `maven`: Projects with Maven build. The path must point to the `pom.xml` file
- `golang`: Go projects. Path must point to the `go.mod` file
- `npm`: NPM based JavaScript or TypeScript projects. Path must point to the `package.json` file

If you have multiple sources in a project, PK will list all of them as badges in the project's `README.md`. If you want to hide one source, you can set `advertise: false` for this source.

### Project Keeper Verify for non-Maven Projects

Maven projects use PK's Maven plugin to run PK verify during the `verify` Maven lifecycle. To run PK verify also for other projects, PK generates GitHub workflow `.github/workflows/project-keeper-verify.yml` and shell script `.github/workflows/project-keeper.sh`. Both files are only generated if there is **no** Maven module in the project root, i.e. there is no Maven source with `path: pom.xml` in `.project-keeper.yml`.

You can run PK fix for non-Maven projects with the following command:

```sh
./.github/workflows/project-keeper.sh fix
```

### Modules

This plugin provides different template modules for different kinds of projects.

| Module              | Description                                                                                                                                                                                            |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `jar_artifact`      | This module creates a "fat" JAR, containing not only the project binary, but also the runtime dependencies.                                                                                            |
| `integration_tests` | Module for sources with integration tests.                                                                                                                                                             |
| `maven_central`     | This module checks the required configuration for releasing on maven central.                                                                                                                          |
| `udf_coverage`      | This module configures the pom for extracting the code coverage from UDF executions.                                                                                                                   |
| `lombok`            | This module configures the pom.xml for the use of [Project Lombok](https://projectlombok.org/).                                                                                                        |
| `native_image`      | This module configures the project for building Java native-images (executables) for windows, linux and mac. For details see our [native-image guide](preparing_a_project_for_native_image_builds.md). |

### Excluding Findings

Using the `excludes` tag you can tell PK to ignore some error-messages:

```yml
sources:
  - type: maven
    path: pom.xml
    modules:
      - maven_central
excludes:
  - "E-PK-CORE-15: Missing maven plugin org.codehaus.mojo:versions-maven-plugin."
  - regex: "E-PK-CORE-16: .*"
  - regex: "W-PK-CORE-151: Pom file .* contains no reference to project-keeper-maven-plugin."
```

Note that you can also use regular expressions (see example above). If a finding is excluded it will not show up on validation and will not be fixed.

You can define excludes globally (like `E-PK-CORE-15` and `E-PK-CORE-16` in the example) or scoped for one source directory (like `E-PK-CORE-17`).

### Replacing Broken Links

Some dependencies define invalid / outdated links to their project homepage. PK writes these links to the `dependencies.md` file. This will make the link checker break the build since a project file contains broken links.

The best way to solve this is to open an issue / pull request at the projects that contain the wrong URL. Since this is, however not always possible you can, as a mitigation, also define a replacement for links:

```yml
sources:
  - type: maven
    path: pom.xml
linkReplacements:
  - "http://wrong-url.com|https://www.my-dependency.de"
```

The syntax for a replacement is `broken-url|replacement`.

PK will then use the replacement in the `dependencies.md` file instead of the original URL.

### CI Build Configuration

PK allows customizing the generated CI-Build workflow scripts using the `build` section in file `.project-keeper.yml` using the following options:

#### GitHub Runner Operating System

Some projects require to run the integration tests in the CI build on an operating system other than the default `ubuntu-latest`. In this case you can use the following:

```yml
build:
  runnerOs: ubuntu-20.04
```

PK will use this setting for GitHub workflows `ci-build.yml` and `release_droid_prepare_original_checksum.yml` which run integration tests. The other workflows don't run integration tests and will stick to the default `ubuntu-latest`.

#### Free Disk Space

Some projects need more disk space during build, e.g. for Docker images. You can free up disk space before the build like this:

```yml
build:
  freeDiskSpace: true
```

This will slow down the build by about one minute.

#### Matrix Build with Exasol DB Versions

To CI build as a matrix build with multiple Exasol DB versions you can add the following:

```yml
build:
  exasolDbVersions:
    - "7.1.24"
    - "8.23.1"
```

Sonar will only run for the first version in the list.

## POM File

For Maven projects, PK generates a `pk_generated_parent.pom` file. This file contains all the required plugins, dependencies and configurations. PK configures your `pom.xml` to use this file as a parent pom. By that, your `pom.xml` inherits all the configuration.

The `pk_generated_parent.pom` file is required during the build and must be checked into version control. Run `mvn project-keeper:fix` to update the file instead of editing it manually.

### Using a Parent POM

If you want to use a parent POM for your project, that's not possible directly since your `pom.xml` must use the `pk_generated_parent.pom` as parent.

Instead, configure the parent in the PK config:

```yaml
sources:
  - type: maven
    path: pom.xml
    parentPom:
      groupId: "com.example"
      artifactId: "my-parent"
      version: "1.2.3"
      relativePath: "../my-parent.pom" # optional
```

PK will then use this parent-pom as parent for the `pk_generated_parent.pom`.

## Golang Projects

To use Project Keeper for Golang projects, create a `.project-keeper.yml` like this:

```yaml
sources:
  - type: golang
    path: go.mod
version: 1.2.3
```

You must specify the project version explicitly, `version > fromSource` is not supported for Golang projects.

## NPM Projects

Project Keeper supports projects using the Node Package Manager (NPM). For NPM projects create a `.project-keeper.yml` like this:

```yaml
sources:
  - type: npm
    path: package.json
```

Project Keeper will read the version of your project from file `package.json`.

## Usage

### Maven Plugin

Use the `project-keeper-maven-plugin` for analyzing Maven projects.

The verification is bound to the maven `package` lifecycle phase. So it is automatically executed if you run `mvn package` or `mvn verify`.

You can also run the checks manually using:

```sh
mvn project-keeper:verify
```

In addition this plugin can also fix the project structure. For that use:

```sh
mvn project-keeper:fix
```

For multi-module projects these commands may fail with the following error:

```
[ERROR] No plugin found for prefix 'project-keeper' in the current project and in the plugin groups [org.apache.maven.plugins, org.codehaus.mojo] available from the repositories [local (/home/user/.m2/repository), central (https://repo.maven.apache.org/maven2)] -> [Help 1]
```

In this case add command line option `--projects .`:

```sh
mvn project-keeper:verify --projects .
mvn project-keeper:fix --projects .
```

You can skip the execution of project-keeper by adding `-Dproject-keeper.skip=true` to your maven command.

### Standalone Command Line Interface

Use the `project-keeper-cli` for analyzing non-Maven projects like Golang.

Run the following commands to verify a project:

```sh
cd path/to/project
java -jar path/to/project-keeper-cli-2.7.1.jar verify
```

Run the following commands to fix the project structure:

```sh
cd path/to/project
java -jar path/to/project-keeper-cli-2.7.1.jar fix
```

### Project Version

PK needs to know about the overall version of the project. For example for validating it in the changes file. For single source projects, PK simply takes the version from the project. For other projects you can:

- Define the version explicitly in the config:
  ```yaml
  version: "1.2.3"
  ```
  This is required for Go projects.
- Define a source. PK will then take the version of that source:
  ```yaml
  version:
    fromSource: "subModule1/pom.xml"
  ```

## Troubleshooting

**Problem:** Validation on CI fails but succeeds locally. In the CI PK wants to list all dependencies as `Added`.

PK retrieves the dependency update information from the git history. In CI sometimes only the latest commit is fetched. Then PK thinks that the latest release would be the very first one.

For GitHub Actions you can solve this by adding `fetch-depth: 0` to the checkout action:

```yaml
- name: Checkout the repository
  uses: actions/checkout@v4
  with:
    fetch-depth: 0
```

---

**Problem:** PK locally shows more dependency changes than in CI.

Typically, this happens if you did not fetch all tags. Simply run `git pull`.
