# Project Keeper Maven Plugin

[![Build Status](https://github.com/exasol/project-keeper/actions/workflows/ci-build.yml/badge.svg)](https://github.com/exasol/project-keeper/actions/workflows/ci-build.yml)
Project keeper core: [![Maven Central – Project keeper core](https://img.shields.io/maven-central/v/com.exasol/project-keeper-core)](https://search.maven.org/artifact/com.exasol/project-keeper-core), Project Keeper Command Line Interface: [![Maven Central – Project Keeper Command Line Interface](https://img.shields.io/maven-central/v/com.exasol/project-keeper-cli)](https://search.maven.org/artifact/com.exasol/project-keeper-cli), Project keeper maven plugin: [![Maven Central – Project keeper maven plugin](https://img.shields.io/maven-central/v/com.exasol/project-keeper-maven-plugin)](https://search.maven.org/artifact/com.exasol/project-keeper-maven-plugin)

This maven plugin checks and unifies the project's structure according to the Exasol integration team's repository standards.

[Features](doc/system_requirements.md)

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

For project-keeper a 'source' is a project inside a repository. For example a maven-project. In the future project-keeper will be able to crawl multiple source-projects from one repository. However, for now, you must specify exactly one maven source.

Supported project types:

* `maven`: Projects with maven build. The path must point to the `pom.xml` file.

If you have multiple sources in a project, project-keeper will list all of them as badges in the project's `README.md`. If you want to hide one source, you can set `advertise: false` for this source.

### Modules

This plugin provides different template modules for different kinds of projects.

| Module              | Description                                                                                                                                                                                                                 |
|---------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `jar_artifact`      | This module creates a "fat" JAR, containing not only the project binary, but also the runtime dependencies.                                                                                                                 |
| `integration_tests` | Module for sources with integration tests.                                                                                                                                                                                  |
| `maven_central`     | This module checks the required configuration for releasing on maven central.                                                                                                                                               |
| `udf_coverage`      | This module configures the pom for extracting the code coverage from UDF executions.                                                                                                                                        |
| `lombok`            | This module configures the pom.xml for the use of [Project Lombok](https://projectlombok.org/).                                                                                                                             |
| `native_image`      | This module configures the project for building Java native-images (executables) for windows, linux and mac. For details see our [native-image guide](doc/developers_guide/preparing_a_project_for_native_image_builds.md). |

### Excluding Findings

Using the `excludes` tag you can tell project-keeper to ignore some error-messages:

```yml
sources:
  - type: maven
    path: pom.xml
    modules:
      - maven_central
    excludes:
      - "E-PK-CORE-17: Missing required file: '.github/workflows/broken_links_checker.yml'."
excludes:
  - "E-PK-CORE-15: Missing maven plugin org.codehaus.mojo:versions-maven-plugin."
  - regex: "E-PK-CORE-16: .*"
```

Note that you can also use regular expressions (see example above). If a finding is excluded it will not show up on validation and will not be fixed.

You can define excludes globally (like `E-PK-CORE-15` and `E-PK-CORE-16` in the example) or scoped for one source directory (like `E-PK-CORE-17`).

### Replacing Broken Links

Some dependencies define invalid / outdated links to their project homepage. PK writes these links to the `dependencies.md` file. This will make the link checker break the build since a project file contains broken links.

The best way to solve this is to open an issue / pull request at the projects that contain the wrong url. Since this is, however not always possible you can, as a mitigation, also define a replacement for links:

```yml
sources:
  - type: maven
    path: pom.xml
linkReplacements:
  - "http://wrong-url.com|https://www.my-dependency.de"
```

The syntax for a replacement is `broken-url|replacement`.

Project-keeper will then use the replacement in the `dependencies.md` file instead of the original url.

## Pom File

For maven projects, project-keeper generates a `pk_generated_parent.pom` file. This file contains all the required plugins, dependencies and configurations. PK configures your `pom.xml` to use this file as a parent pom. By that, your `pom.xml` inherits all the configuration.

The `pk_generated_parent.pom` file is required during the build and must be checked into version control. Run `mvn project-keeper:fix` to update the file instead of editing it manually.

### Using a Parent Pom

If you want to use a parent pom for your project, that's not possible directly since your `pom.xml` must use the `pk_generated_parent.pom` as parent.

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

## Usage

### Maven Plugin

Use the `project-keeper-maven-plugin` for analyzing Maven projects.

The verification is bound to the maven `package` lifecycle phase. So it is automatically executed if you run `mvn package` or `mvn verify`.

You can also run the checks manually using:

```shell script
mvn project-keeper:verify
```

In addition this plugin can also fix the project structure. For that use:

```shell script
mvn project-keeper:fix
```

You can skip the execution of project-keeper by adding `-Dproject-keeper.skip=true` to your maven command.

### Standalone Command Line Interface

Use the `project-keeper-cli` for analyzing non-Maven projects like Golang.

Run the following commands to verify a project:

```shell
cd path/to/project
java -jar path/to/project-keeper-cli-2.7.0.jar verify
```

Run the following commands to fix the project structure:

```shell
cd path/to/project
java -jar path/to/project-keeper-cli-2.7.0.jar fix
```

## Development

### Adding a Required File

Copy the file to `src/main/resources/templates/<module>/<require_exist | require_exact>`
For `module` use the name of the module you want to add the required files to. If you want this plugin to only check that the file exists, put it into `require_exist`. If you also want that it check that the file has the same content like the template, add it to `require_exact`. Inside of these folders you can also create sub folders. The sub folder structure of the templates defines the folder structure of the repository.

**Example:**

You created the file `src/main/resources/templates/default/require_exist/test/my_file.md`

This makes the project-keeper check that in all repositories exists the file `test/my_file.md`.

### Adding a Pom File Validation.

Validations for the POM file are defined using code. For maven plugins there is the abstract basis class `AbstractPluginPomTemplate` that facilitates the template implementation.

### Project Version

PK needs to know about the overall version of the project. For example for validating it in the changes file. For single source projects, PK simply takes the version from the project. For other projects you can:

* Define the version explicitly in the config:
  ```yaml
  version: "1.2.3"
  ```
* Define a source. PK will then take the version of that source:
  ```yaml
  version:
    fromSource: "subModule1/pom.xml"
  ```

## Troubleshooting

**Problem:** Validation on CI fails but locally succeeds. In the CI PK wants to list all dependencies as `Added`.

PK retrieves the dependency update information from the git history. In CI sometimes only the latest commit is fetched. Then PK thinks that the latest release would be the very first one.

For GitHub Actions you can solve this by adding `fetch-depth: 0` to the checkout action:

```yaml
- name: Checkout the repository
  uses: actions/checkout@v3
  with:
    fetch-depth: 0
```

---------

**Problem:** PK locally shows more dependency changes than in CI.

Typically, this happens if you did not fetch all tags. Simply run `git pull`.

## Additional Resources

* [Dependencies](dependencies.md)
* [Changelog](doc/changes/changelog.md)
* [Features & Requirements](doc/system_requirements.md)
* [Design](doc/design.md)
* [Developers Guide](doc/developers_guide/developers_guide.md)
