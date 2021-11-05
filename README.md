# Project Keeper Maven Plugin

[![Build Status](https://github.com/exasol/project-keeper-maven-plugin/actions/workflows/ci-build.yml/badge.svg)](https://github.com/exasol/project-keeper-maven-plugin/actions/workflows/ci-build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.exasol/project-keeper-maven-plugin)](https://search.maven.org/artifact/com.exasol/project-keeper-maven-plugin)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-maven-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-maven-plugin)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-maven-plugin&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-maven-plugin)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-maven-plugin&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-maven-plugin)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-maven-plugin&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-maven-plugin)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-maven-plugin&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-maven-plugin)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-maven-plugin&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-maven-plugin)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-maven-plugin&metric=coverage)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-maven-plugin)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-maven-plugin&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-maven-plugin)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-maven-plugin&metric=ncloc)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-maven-plugin)

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
        <configuration>
            <modules>
                <!-- add modules here: -->
                <!-- <module>For available modules see below.</module>-->
            </modules>
        </configuration>
    </plugin>
</plugins>
```

### Modules

This plugin provides different template modules for different kinds of projects.

#### `default` (always included)

* [required files (must exist)](src/main/resources/templates/default/require_exist)
* [required files (must have same content)](src/main/resources/templates/default/require_exact)
* required maven plugins
    * [maven-versions-plugin](https://www.mojohaus.org/versions-maven-plugin/)
    * [ossindex-maven-plugin](https://sonatype.github.io/ossindex-maven/maven-plugin/)
    * [maven-enforcer-plugin](https://maven.apache.org/enforcer/maven-enforcer-plugin/)
    * [maven-surefire-plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
    * [error-code-crawler-maven-plugin](https://github.com/exasol/error-code-crawler-maven-plugin)
    * [reproducible-build-maven-plugin](https://zlika.github.io/reproducible-build-maven-plugin/)

#### `jar_artifact`

This module creates a "fat" JAR, containing not only the project binary, but also the runtime dependencies.

* [required files (must have same content)](src/main/resources/templates/jar_artifact/require_exact)
* required maven plugins
    * [maven-assembly-plugin](http://maven.apache.org/plugins/maven-assembly-plugin/)
    * [artifact-reference-checker-maven-plugin](https://github.com/exasol/artifact-reference-checker-maven-plugin)

#### `integration_tests`

* required maven plugins
    * [maven-failsafe-plugin](https://maven.apache.org/surefire/maven-failsafe-plugin/)
    * jacoco coverage configuration for integration tests

#### `maven_central`

This module checks the required configuration for releasing on maven central.

* required maven plugins
    * [maven-gpg-plugin](https://maven.apache.org/plugins/maven-gpg-plugin/)
    * [maven-deploy-plugin](https://maven.apache.org/plugins/maven-deploy-plugin/)
    * [maven-source-plugin](https://maven.apache.org/plugins/maven-source-plugin/)
    * [maven-javadoc-plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/)
    * [nexus-staging-maven-plugin](https://github.com/sonatype/nexus-maven-plugins/tree/master/staging/maven-plugin)

#### `udf_coverage`

This module configures the pom for extracting the code coverage from UDF executions.

In addition, you need to upload the Jacoco agent to BucketFS and run it using JVM options. You can use the [Udf-Debugging-Java](https://github.com/exasol/udf-debugging-java/) to do so.

It makes no sense to use this module without the `integration_tests` module

* required maven plugins
    * jacoco UDF coverage configuration for integration tests
    * [mvn-dependency-plugin](https://maven.apache.org/plugins/maven-dependency-plugin/) configured to export jacoco-agent to `target/`
* required dependencies
    * `org.jacoco:org.jacoco.agent`

#### `lombok`

This module configures the pom.xml for the use of [Project Lombok](https://projectlombok.org/).

* Required maven plugins:
    * [`lombok-maven-plugin`](https://projectlombok.org/setup/maven)
* Enforced configuration:
    * Modified `sourcePaths` for the `error-code-crawler-maven-plugin`.
* Required dependencies:
    * `org.projectlombok:lombok`

### Excluding Files

Using the `excludedFiles` you can tell project-keeper to ignore some files:

```xml

<configuration>
    <excludedFiles>
        <excludedFile>test/fileToExclude.md</excludedFile>
        <excludedFile>doc/*</excludedFile>
        <excludedFile>**/excludeNoMatterWhere.md</excludedFile>
    </excludedFiles>
</configuration>
```

Inside of the `<excludedFile>` tag you can use GLOB wildcards.

## Excluding Plugins

Using the `excludedPlugins`configuration you can tell Project Keeper to ignore some missing or differently configured maven plugins:

```xml

<configuration>
    <excludedPlugins>
        <excludedPlugin>com.exasol:error-code-crawler-maven-plugin</excludedPlugin>
    </excludedPlugins>
</configuration>
```

The syntax is `<group_id>:<artifact_id>`.

### Replacing Broken Links

Some maven projects define invalid / outdated links to their project homepage. PK writes these links to the `dependencies.md` file. This will make the link checker break the build since a project file contains broken links.

The best way to solve this is to open an issue / pull request at the projects that contain the wrong url. Since this is, however not always possible you can, as a mitigation, also define a replacement for links:

```xml

<configuration>
    <linkReplacements>
        <linkReplacement>http://broken.com|http://example.com/</linkReplacement>
    </linkReplacements>
</configuration>
```

The syntax for the `linkReplacement` is `broken-url|replacement`.

Project-keeper will then apply the use the replacement in the `dependencies.md` file instead of the original url.

## Usage

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

## Development

### Adding a Required File

Copy the file to `src/main/resources/templates/<module>/<require_exist | require_exact>`
For `module` use the name of the module you want to add the required files to. If you want this plugin to only check that the file exists, put it into `require_exist`. If you also want that it check that the file has the same content like the template, add it to `require_exact`. Inside of these folders you can also create sub folders. The sub folder structure of the templates defines the folder structure of the repository.

**Example:**

You created the file `src/main/resources/templates/default/require_exist/test/my_file.md`

This makes the project-keeper check that in all repositories exists the file `test/my_file.md`.

### Adding a Pom File Validation.

Validations for the POM file are defined using code. For maven plugins there is the abstract basis class `AbstractPluginPomTemplate` that facilitates the template implementation.

## Troubleshooting

**Problem:** Validation on CI fails but locally succeeds. In the CI PK want's to list all dependencies as `Added`.

PK retrieves the dependency update information from the git history. In CI sometimes only the latest commit is fetched. Then PK thinks that the latest release would be the very first one.

For GitHub Actions you can solve this by adding `fetch-depth: 0` to the checkout action:

```yaml
- name: Checkout the repository
    uses: actions/checkout@v2
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
