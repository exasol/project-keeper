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
* [requirements/system_requirements.md](../requirements/system_requirements.md)
* [requirements/design.md](../requirements/design.md)

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

## Updating Dependency Versions

Updating dependency versions in **project-keeper** requires the following steps:

1. **Update the version in the `revision` property** of the **parent `pom.xml`**.
2. Build and install the current state of the project locally:

    ```sh
    mvn clean install -Dduplicate-finder.skip=true -DskipTests \
        -Dproject-keeper.skip=true -Dossindex.skip=true \
        -Dmaven.javadoc.skip=true -Djacoco.skip=true \
        -Derror-code-crawler.skip=true -Dopenfasttrace.skip=true -T 1C
    ```

3. Run the `project-keeper:fix` goal using the locally installed version:

    ```sh
    mvn com.exasol:project-keeper-maven-plugin:fix --projects .
    ```

This updates Project Keeper maven modules with the new Project Keeper version and generate the changelog with dependency updates.

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

## Debugging Maven Central Deployment

The PK module `maven_central` specified in `.project-keeper.yml` will deploy the marked Maven module to Maven Central. For debugging you can run this deployment locally.

### Verify Bundle Content

Maven plugin `central-publishing-maven-plugin` creates a bundle and uploads it to Maven Central. To verify the content of this bundle without uploading, run the following command. You don't need to Maven Central credentials to do this.

```sh
mvn clean deploy -Dgpg.skip=false -DskipTests \
  -Dcentral-publishing.deploymentName="Testing Deployment" \
  -Dcentral-publishing.skipPublishing=true
```

This will create a bundle containing all published modules **in one of the Maven modules**. You can find the bundle using `find . -name central-bundle.zip`. In Project Keeper this might return `./shared-model-classes/target/central-publishing/central-bundle.zip`. Check the content with `unzip -l`:

```
$ unzip -l ./shared-model-classes/target/central-publishing/central-bundle.zip
Archive:  ./shared-model-classes/target/central-publishing/central-bundle.zip
  Length      Date    Time    Name
---------  ---------- -----   ----
       64  06-06-2025 13:18   com/exasol/project-keeper-java-project-crawler/5.2.0/project-keeper-java-project-crawler-5.2.0.jar.sha256
       32  06-06-2025 13:18   com/exasol/project-keeper-cli/5.2.0/project-keeper-cli-5.2.0.pom.md5
       64  06-06-2025 13:18   com/exasol/project-keeper-shared-model-classes/5.2.0/project-keeper-shared-model-classes-5.2.0.pom.sha256
       64  06-06-2025 13:18   com/exasol/project-keeper-java-project-crawler/5.2.0/project-keeper-java-project-crawler-5.2.0-sources.jar.sha256
      833  06-06-2025 13:18   com/exasol/project-keeper-java-project-crawler/5.2.0/project-keeper-java-project-crawler-5.2.0-javadoc.jar.asc
       64  06-06-2025 13:18   com/exasol/project-keeper-core/5.2.0/project-keeper-core-5.2.0.jar.sha256
     3126  06-06-2025 13:18   com/exasol/project-keeper-java-project-crawler/5.2.0/project-keeper-java-project-crawler-5.2.0.pom
      833  06-06-2025 13:18   com/exasol/project-keeper-shared-model-classes/5.2.0/project-keeper-shared-model-classes-5.2.0.jar.asc
       32  06-06-2025 13:18   com/exasol/project-keeper-maven-plugin/5.2.0/project-keeper-maven-plugin-5.2.0.jar.md5
...
```

Ensure that **only modules meant for publishing** are contained in this bundle and that it does not contain test modules.

### Verify Bundle Upload

To test the bundle upload to Maven Central **without publishing**, configure Maven Central credentials in `~/.m2/settings.xml`:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>maven-central-portal</id>
            <username>user</username>
            <password>password</password>
        </server>
    </servers>
</settings>
```

Then run the following command:

```sh
mvn clean deploy -Dgpg.skip=false -DskipTests \
  -Dcentral-publishing.deploymentName="Testing Deployment"
```

**Important:** Do not specify property `central-publishing.autoPublish` to use the default value `false`. This ensures that the bundle will **not** be published automatically.

Then login to Maven Central Portal and go to the [deployments page](https://central.sonatype.com/publishing/deployments). Verify that your component was deployed successfully and has the expected content.

Don't forget to click the "Drop" button for the deployment to avoid accidentally publishing it.
