# Project Keeper Maven Plugin

[![Build Status](https://github.com/exasol/project-keeper/actions/workflows/ci-build.yml/badge.svg)](https://github.com/exasol/project-keeper/actions/workflows/ci-build.yml)
Project Keeper Core: [![Maven Central &ndash; Project Keeper Core](https://img.shields.io/maven-central/v/com.exasol/project-keeper-core)](https://search.maven.org/artifact/com.exasol/project-keeper-core), Project Keeper Command Line Interface: [![Maven Central &ndash; Project Keeper Command Line Interface](https://img.shields.io/maven-central/v/com.exasol/project-keeper-cli)](https://search.maven.org/artifact/com.exasol/project-keeper-cli), Project Keeper Maven plugin: [![Maven Central &ndash; Project Keeper Maven plugin](https://img.shields.io/maven-central/v/com.exasol/project-keeper-maven-plugin)](https://search.maven.org/artifact/com.exasol/project-keeper-maven-plugin)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-root&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-root)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-root&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-root)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-root&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-root)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-root&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-root)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-root&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-root)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-root&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-root)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-root&metric=coverage)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-root)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-root&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-root)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Aproject-keeper-root&metric=ncloc)](https://sonarcloud.io/dashboard?id=com.exasol%3Aproject-keeper-root)

This maven plugin checks and unifies the project's structure according to the Exasol integration team's repository standards.

## Usage in a Nutshell for Maven

1. Create config file `~/.m2/toolchains.xml` for the Maven toolchains plugin, see [user guide](doc/user_guide/user_guide.md#configure-mavens-toolchainsxml) for details.

2. Create config file `.project-keeper.yml`:
   ```yml
   sources:
     - type: maven
       path: pom.xml
       modules:
         - maven_central
   ```

3. Add PK plugin to your `pom.xml`:
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

4. Run PK fix:
   ```sh
   mvn project-keeper:fix --projects .
   ```

5. Run PK verify:
   ```sh
   mvn project-keeper:verify --projects .
   ```

See the [User Guide](doc/user_guide/user_guide.md) for details and for non-Maven projects.

## Additional Resources

- [User Guide](doc/user_guide/user_guide.md)
- [Developer Guide](doc/developer_guide/developer_guide.md)
- [Dependencies](dependencies.md)
- [Changelog](doc/changes/changelog.md)
- [Features & Requirements](doc/requirements/system_requirements.md)
- [Design](doc/requirements/design.md)
