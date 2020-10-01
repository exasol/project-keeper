# Project Keeper Maven Plugin

This maven plugin checks and unifies the project's structure according to the Exasol integration team's repository standards.

## Installation

Install this plugin by adding the following lines to your project's `pom.xml` file:

```xml
<plugins>
    <plugin>
        <groupId>com.exasol</groupId>
        <artifactId>project-keeper-maven-plugin</artifactId>
        <version>0.1.0</version>
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

* `default` (always included)

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

## Additional Resources

* [Dependencies](NOTICE)
* [Changelog](doc/changes/changelog.md)
