# User Guide

This guide describes how to use Project Keeper (PK) in a project.

## Prerequisites for Using Project Keeper

### Install Required JDK Versions

Projects that use PK require JDK versions 17 (for running Maven) and 11 (for compiling and testing). This means that developers must install both JDK versions on their machine.

Make sure that environment variable `JAVA_HOME` points to JDK 17.

### Configure Maven's `toolchains.xml`

Create file `~/.m2/toolchains.xml` with the following content and adapt the `jdkHome` elements to your local installation.

```xml
<toolchains xmlns="http://maven.apache.org/TOOLCHAINS/1.1.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/TOOLCHAINS/1.1.0 http://maven.apache.org/xsd/toolchains-1.1.0.xsd">
    <toolchain>
        <type>jdk</type>
        <provides>
            <version>11</version>
        </provides>
        <configuration>
            <jdkHome>/path/to/jdk_11/</jdkHome>
        </configuration>
    </toolchain>
    <toolchain>
        <type>jdk</type>
        <provides>
            <version>17</version>
        </provides>
        <configuration>
            <jdkHome>/path/to/jdk_17/</jdkHome>
        </configuration>
    </toolchain>
</toolchains>
```

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

PK will use this setting for GitHub workflow `ci-build.yml` which runs integration tests. The other workflows don't run integration tests and will stick to the default `ubuntu-latest`.

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

## Maven Projects

For Maven projects, PK generates a `pk_generated_parent.pom` file. This file contains all the required plugins, dependencies and configurations. PK configures your `pom.xml` to use this file as a parent pom. By that, your `pom.xml` inherits all the configuration.

The `pk_generated_parent.pom` file is required during the build and must be checked into version control. Run `mvn project-keeper:fix` to update the file instead of editing it manually.

### Overriding Defaults

If a configuration in `pk_generated_parent.pom` does not apply to your project you can override it in `pom.xml`.

#### JDK Toolchain Version

By default PK configures the project to use Java 11. If you need to use a different version, you can override it by adding a property to your `pom.xml`:

```xml
<properties>
    <java.version>17</java.version>
</properties>
```

#### Exclude Dependencies From Automatic Version Update

PK's [update-dependencies](#update-dependencies) mode updates the project's `pom.xml` files and updates the versions of all dependencies. In certain cases this could break the build. To exclude a dependency from the automatic update, add the following to your `pom.xml`:

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>versions-maven-plugin</artifactId>
    <configuration>
        <excludes>
            <!-- Dependencies use SLF4J 1.7 so we can't upgrade to 2 -->
            <exclude>org.slf4j:slf4j-jdk14:jar:*:*</exclude>
            <!-- Upgrading to 6.8.0.202311291450-r causes java.lang.NoClassDefFoundError: org/eclipse/jgit/internal/JGitText in ShutdownHook-->
            <exclude>org.eclipse.jgit:org.eclipse.jgit:jar:*:6.8.0.202311291450-r</exclude>
        </excludes>
    </configuration>
</plugin>
```

See the [documentation for `<excludes>`](https://www.mojohaus.org/versions/versions-maven-plugin/use-latest-releases-mojo.html#excludes) of the versions-maven-plugin for details.

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

#### Verification

The verification is bound to the maven `package` lifecycle phase. So it is automatically executed if you run `mvn package` or `mvn verify`.

You can also run the checks manually using:

```sh
mvn project-keeper:verify
```

#### Release Verification

This goal verifies that the project is ready to release.

```sh
mvn project-keeper:verify-release
```

This goal requires reading issues from the GitHub API. To allow running this locally you need to install the [GitHub CLI](https://cli.github.com/) and configure your GitHub credentials by executing `gh auth login`.

#### Fix

In addition this plugin can also fix the project structure. For that use:

```sh
mvn project-keeper:fix
```

#### Update Dependencies

Run the following commands to update dependencies:

```sh
mvn project-keeper:update-dependencies
```

The command will increment the project's patch version if the current version is already released.

#### Multi-Module Maven Projects

For multi-module projects these commands may fail with the following error:

```
[ERROR] No plugin found for prefix 'project-keeper' in the current project and in the plugin groups [org.apache.maven.plugins, org.codehaus.mojo] available from the repositories [local (/home/user/.m2/repository), central (https://repo.maven.apache.org/maven2)] -> [Help 1]
```

In this case add command line option `--projects .`:

```sh
mvn project-keeper:verify --projects .
mvn project-keeper:verify-release --projects .
mvn project-keeper:fix --projects .
mvn project-keeper:update-dependencies --projects .
```

You can skip the execution of project-keeper by adding `-Dproject-keeper.skip=true` to your maven command.

### Standalone Command Line Interface

Use the `project-keeper-cli` for analyzing non-Maven projects like Golang. PK `fix` generates a shell script that simplifies running PK standalone on the command line:

```sh
cd path/to/project
./.github/workflows/project-keeper.sh $GOAL
```

The standalone variant supports the same goals as the Maven plugin: `fix`, `verify` and `update-dependencies`.

## Generated GitHub Workflows

PK generates the following GitHub scheduled Workflows:

### [`broken_links_checker.yml`](../../project-keeper/src/main/resources/templates/.github/workflows/broken_links_checker.yml)

Scheduled weekly, checks Markdown files for broken links.

### [`dependencies_check.yml`](../../project-keeper/src/main/resources/templates/.github/workflows/dependencies_check.yml)

Scheduled daily, checks the Maven project for vulnerable dependencies, creates new issues using [security_issues](https://exasol.github.io/python-toolbox/github_actions/security_issues.html) and starts the [`dependencies_update.yml`](#dependencies_updateyml) workflow that updates dependencies.

### [`dependencies_update.yml`](../../project-keeper/src/main/resources/templates/.github/workflows/dependencies_update.yml)

This workflow is triggered manually or by workflow [`dependencies_check.yml`](#dependencies_checkyml). It updates dependencies, creates a changelog with fixed vulnerabilities and creates a new Pull Requests.

In order to send notifications, this workflow requires GitHub secret `INTEGRATION_TEAM_SLACK_NOTIFICATION_WEBHOOK`.

### [`ci-build.yml`](../../project-keeper/src/main/resources/templates/.github/workflows/ci-build.yml)

This workflow runs for each change of a Pull Request and whenever a Pull Request is merged to `main`. It runs all tests and verification steps.

### [`release.yml`](../../project-keeper/src/main/resources/templates/.github/workflows/release.yml)

This workflow is triggered manually or by workflow [`ci-build.yml`](). It verifies that all preconditions for a release are met and performs the release.

In order to send notifications, this workflow requires GitHub secret `INTEGRATION_TEAM_SLACK_NOTIFICATION_WEBHOOK`.

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

---

**Problem:** Maven build fails with the following error message:

```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-enforcer-plugin:3.4.1:enforce (enforce-maven) on project project-keeper-shared-model-classes: 
[ERROR] Rule 1: org.apache.maven.enforcer.rules.version.RequireJavaVersion failed with message:
[ERROR] Detected JDK version 11.0.18 (JAVA_HOME=/Users/chp/Applications/java/jdk-11.0.18+10/Contents/Home) is not in the allowed range [17,).
```

This means that environment variable `JAVA_HOME` points to a JDK 11. Please ensure that JDK 17 is installed and `JAVA_HOME` points to it.

See section [Install Required JDK Versions](#install-required-jdk-versions) for details.

---

**Problem:**: Maven build fails with the following error message:

```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-toolchains-plugin:3.1.0:toolchain (default) on project project-keeper-shared-model-classes: Cannot find matching toolchain definitions for the following toolchain types:
[ERROR] jdk [ version='11' ]
[ERROR] Please make sure you define the required toolchains in your ~/.m2/toolchains.xml file.
```

Ensure that `~/.m2/toolchains.xml` exists and contains a JDK version 11. See section [Configure Maven's `toolchains.xml`](#configure-mavens-toolchainsxml) for details.

**Problem:**: Running PK using Maven fails but the error message is not helpful.

Run Maven with the `--errors` option to get a stack trace.
