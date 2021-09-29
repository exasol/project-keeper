# Design

## Validators

`dsn~validators~1`

As an interface for the different validations, we created the `Validator` interface. It only contains the `validate` method:

```java
public List<ValidationFinding> validate();
```

The `ValidationFinding` consists of:

* A message
* An optional fix for the violation finding

A fix is a function closure that changes the project so that the fixed finding does not happen again.

We decided on this interface since it allows us to:

* Report errors that can't be fixed automatically
* Report an error and directly provide a fix for it

A problem with this approach is that some validation fixes change a resource such as files. If a validator creates multiple validation findings for the same resource, each fix needs to read and write the file.

Consider, for example, the plugin validator for the pom file. If the pom file validator detects multiple missing plugins, each of these fixes would need to write the file since the fixes are executed by the surrounding code and not in a specific order. To solve this, we used the trick to create a compound finding. So basically one finding with one error message that combines one of all its sub findings. Its fix is a method that runs the fixes of all sub-findings and then writes the pom file.

Covers:

* `feat~fix-findings~1`

Needs: impl

### Required Files Validator

`dsn~required-files-validator~1`

We decided to add the template for the required files by a file structure in the resources of this project. There are two folders: `require_exist` and `require_exact`.

For files in the `require_exist` folder, PK only checks if they exist in the project. If they don't exist it creates the file with the content from the file in `require_exist` as a template.

For the files in the `require_exact` folder, PK also validates that the content is exactly the same.

Rationale:

This way of defining the project structure makes it very convenient to add new content. No knowledge about the internal implementation of this plugin is required. That allows users to quickly add or modify the template.

An alternative would be to define the files by Java classes. In order to contain the contents of the required files, these classes would contain many string concatenations which would be very unreadable.

Covers:

* `req~verify-existence-of-files~1`

Needs: impl, utest, itest

### Deleted Files Validator

`dsn~deleted-files-validator~1`

For configuring the deleted files validator we decided on a Java API. For each deleted file PK developers must specify a reason that will be printed as an error message. By that PK can for example tell that the file was moved.

Covers:

* `req~verify-non-existence-of-files~1`

Needs: impl, utest, itest

### Maven Plugin Validator

`dsn~mvn-plugin-validator~1`

We decided to implement the verification of the pom plugins using a combination of xml files and Java code. The XML files (in resources/mvn_templates) contain the XML of a typical plugin definition. The Java class defines which XML properties must be like define in the template.

This approach allows us to quickly add new templates by copying an exemplary plugin definition from a pom.xml file, and defining which parts are mandatory using Java.

Covers:

* `req~verify-mvn-plugins~1`

Needs: impl, utest, itest

### Mvn Dependency Validator

`dsn~mvn-dependency-validator~1`

We implemented the validation for dependency declaration using Java code. That allows us to add specific validations for certain dependencies. For example, for the Jacoco dependency, we need to validate the scope.

However, implementing it purely via configuration would probably also be a good solution. In the future maybe even a better one, depending on how many dependencies should get validated.

Covers:

* `req~verify-maven-dependencies~1`

Needs: impl, utest, itest

### Dependency Changes Section in changes_x.x.x.md File Validator

`dsn~dependency-section-in-changes_x.x.x.md-file-validator~1`

The basic approach for this validator is to generate the markdown for the `Dependency Changes` section and then compare the content of the project's file with that generated content. In case they differ the validation fails. The automatic fix of the validation error is to override the section with the generated one.

For this feature we need to generate the list of dependency changes since the last release. We decided to generate this list by comparing the dependencies of the last release with the dependencies of the current release.

For both versions we [read the project's dependencies](#reading-project-dependencies) and create a Java model that we then compare to extract the differences.

For getting the last release we use Git. We detect the previous release by walking through the commits on the current branch in reverse order. If we find a tag that looks like a version number we extract the `pom.xml` file from this commit and analyze its dependencies.

We separated the crawling of the dependencies from the rendering of the table so that we can add different crawlers for other languages or build systems in the future.

Needs: impl, utest, itest

Covers:

* `req~verify-dependency-section-in-changes_x.x.x.md-file~1`

### Changelog.md File Validator

`dsn~verify-changelog-file~1`

For validating the `changelog.md` file we first generate the expected content and then compare the actual file with the expected content.

Covers:

* `req~verify-changelog-file~1`

Needs: impl, utest, itest

### Dependencies.md File Validator

`dsn~depnedency.md-file-validator~1`

For validating the `dependencies.md` file we first generate the expected content and then compare the actual file with the expected content.

For generating the content we first [read the project's dependencies](#reading-project-dependencies) and then use the maven API to get the project information. To do so, we again use the maven `ProjectBuilder`. This maven class can fetch a dependency from the maven repository (local or remote) and provide a Java representation of the pom file.

We separated the crawling of the dependency information from the rendering of the files so that we can add support for different languages or build systems in the future.

Covers:

* `req~verify-dependencies-file~1`

Needs: impl, itest

### Readme.md File Validator

`dsn~readme-validator~1`

We decided that the readme validator should not be too strict. That means, that it should not enforce a specific structure but only check that the required parts are somewhere in the file. That also means, that the PK can't automatically fix the findings. By that PK makes sure the the `README.md` contains the required parts, but still leaves users a lot of freedom.

Needs: impl, utest, itest

Covers:

* `req~verify-readme~1`

### License File Validator

`dsn~license-file-validator~1`

We decided that the license validator only inserts the current year on creation. Another option would have been to add a year range and also validate that it's always up to date. However, since it's not very likely that these projects require a copyright longer than 50-70 years, we decided adding the creation year is enough. In case it still required one can of course add a date range by hand.

Needs: impl, utest, itest

Covers:

* `req~verify-license-file~1`

## Gitignore Validator

`dsn~gitignore-validator~1`

Needs: impl, utest, itest

Covers:

* `req~verify-gitignore-file~1`

### Reading Project Dependencies

`dsn~reading-project-dependencies~1`

For reading the project's dependencies we decided to use the maven `ProjectBuilder`. Maven injects this object into the main class of PK maven plugin. In contrast to simply instantiating it, this approach introduces coupling and makes unit tests impossible. For that reason in the future, we might want to move the dependency discovery into a dedicated maven plugin and call it via `mvn exec` command.

Unit testing is not possible since `ProjectBuilder` needs to be injected by maven.

Needs: impl, itest

## Maven Integration

### Maven Verify Goal

`dsn~mvn-verify-goal~1`

This plugin defines a maven goal named `verify` that checks if the project matches the defined structure. The default maven lifecycle phase for this goal is the `package` phase.

Covers:

* `feat~mvn-integration~1`

Needs: impl, itest

### Maven fix Gaol

`dsn~mvn-fix-goal~1`

This plugin defines a maven goal named `fix` that creates or updates the project files so that they match the required project structure.

Covers:

* `feat~mvn-integration~1`

Needs: impl, itest

## Configuration

### Modules

`dsn~modules~1`

We decided to group sets of validations into modules. These modules represent typical use cases. For example one module is `maven_central` which contains the validations for publishing to Maven Central Artifact Repository.

Covers:

* `feat~configuration~1`

Needs: impl, utest

### Excluding Files

`dsn~excluding-files~1`

We added the possibility to exclude files from the validation.

Rationale:

* Some projects need a non-default configuration
* It helps trying something out

Covers:

* `feat~configuration~1`

Needs: impl, utest, itest

### Excluding Plugins

`dsn~exclduding-mvn-plugins~1`

We added the possibility to exclude maven plugins from the validation.

Covers:

* `feat~configuration~1`

Needs: impl, itest
