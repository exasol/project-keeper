# Project Keeper Maven Plugin

This maven plugin checks and unifies the project's structure according to the Exasol integration team's repository standards.

## Installation

Install this plugin by adding the following lines to your project's `pom.xml` file:

```xml
<plugins>
    <plugin>
        <groupId>com.exasol</groupId>
        <artifactId>project-keeper-maven-plugin</artifactId>
        <version>0.2.0</version>
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
#### `jar_artifact`
* [required files (must have same content)](src/main/resources/templates/jarArtifact/require_exact)
* required maven plugins
    * [maven-assembly-plugin](http://maven.apache.org/plugins/maven-assembly-plugin/)
    * [artifact-reference-checker-maven-plugin](https://github.com/exasol/artifact-reference-checker-maven-plugin)
#### `integration_tests`
* required maven plugins
    * [maven-failsafe-plugin](https://maven.apache.org/surefire/maven-failsafe-plugin/)
    * jacoco coverage configuration for integration tests
#### `maven_central`
*Not yet implemented.*

This module will check required configuration for releasing on maven central in the future.
You can, however, already add this module. By that you can't forget to add it later.

## Usage

The verification is bound to the maven `package` lifecycle phase.
So it is automatically executed if you run `mvn package` or `mvn verify`.

You can also run the checks manually using:

```shell script
mvn project-keeper:verify
```

In addition this plugin can also fix the project structure. For that use:

```shell script
mvn project-keeper:fix
```

## Development

### Adding a Required File

Copy the file to `src/main/resources/templates/<module>/<require_exist | require_exact>`
For `module` use the name of the module you want to add the required files to.
If you want this plugin to only check that the file exists, put it into `require_exist`.
If you also want that it check that the file has the same content like the template, add it to `require_exact`.
Inside of these folders you can also create sub folders.
The sub folder structure of the templates defines the folder structure of the repository.

**Example:**
 
You created the file `src/main/resources/templates/default/require_exist/test/my_file.md`

This makes the project-keeper check that in all repositories exists the file `test/my_file.md`.

### Adding a Pom File Validation.

Validations for the POM file are defined using code.
For maven plugins there is the abstract basis class `AbstractPluginPomTemplate` that facilitates the template implementation.

## Additional Resources

* [Dependencies](NOTICE)
* [Changelog](doc/changes/changelog.md)
