# Project-Keepers Developer Guide

## Install Dependencies

You need the following dependencies for running the tests:

* Java Development Kit 11
* Maven 3.6.3 or later
* Go 1.20 or later for testing Go support, see [installation guide](https://go.dev/doc/install)

### go-licenses

[go-licenses](https://github.com/google/go-licenses/) is required for extracting Go module license information.
Since version 2.7.0 PK will automatically install `go-licenses` if required.

## Requirements and Design

Design documents are located at
* [system_requirements.md](../system_requirements.md)
* [design.md](../design.md)

After modifying the `.plantuml` files in `doc/images/` please generate the `.svg` diagrams by running the following command and commit them to Git:

```sh
./scripts/build_diagrams.sh
```

## Building

When building a new release of PK then Maven might display the following error:

```
[ERROR] Failed to execute goal on project project-keeper-java-project-crawler:
Could not resolve dependencies for project com.exasol:project-keeper-java-project-crawler:maven-plugin:2.5.0:
com.exasol:project-keeper-shared-model-classes:jar:2.7.1 was not found in
https://repo.maven.apache.org/maven2 during a previous attempt.
```

In order to fix this, just install project-keeper:

```sh
mvn install --projects . -DskipTests -Dossindex.skip=true
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

```sh
mvn clean install -DskipTests
```

### Specify PK Version

PK gets its own version from source package. Unfortunatly this isn't available in IDE. Instead you can specify the version by setting the following system property:
```
-Dcom.exasol.projectkeeper.ownVersion=2.7.0
```

## Updating Maven Plugins Versions in Template Resources

For Maven projects PK creates a file `pk_generated_parent.pom` containing sections for all Maven plugins required for the current project depending on the PK modules selected in file `.project-keeper.yml`. PK copies each plugin section from a corresponding resource file in folder `project-keeper/src/main/resources/maven_templates/`.

Each Maven plugin is identified by its Maven coordinates group, artifact, and version. Here is an example from resource file `artifact-reference-checker-maven-plugin.xml`:
```xml
<plugin>
    <groupId>com.exasol</groupId>
    <artifactId>artifact-reference-checker-maven-plugin</artifactId>
    <version>0.4.0</version>
</plugin>
```

If you want to check if newer versions of some of the plugins are available and potentially update the plugin versions in the templates you can run the java class `TemplateUpdater` as java application.

`TemplateUpdater` will inspect the template files and replace the plugin version by the latest version available from maven-central.

## Requirement Tracing

```sh
mvn openfasttrace:trace --projects .
```

## Adding a Required File

Copy the required file to directory `src/main/resources/templates/<module>/<require_exist | require_exact>`.

Replace `<module>` with the name of the module the required file should be added to. 

If you want this plugin to only check that the file exists, put it into `require_exist`. If you also want that it check that the file has the same content like the template, add it to `require_exact`. 

Inside of these folders you can also create sub folders. The sub folder structure of the templates defines the folder structure of the repository.

**Example:**

You created the file `src/main/resources/templates/default/require_exist/test/my_file.md`

This makes PK in all repositories to verify that the file `test/my_file.md` exists.

## Adding a Pom File Validation

Validations for the POM file are defined using code. For maven plugins there is the abstract base class `AbstractPluginPomTemplate` that facilitates the template implementation.
