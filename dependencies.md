<!-- @formatter:off -->
# Dependencies

## Project-keeper Shared Model Classes

### Compile Dependencies

| Dependency                       | License                                                                                                      |
| -------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [Project Lombok][0]              | [The MIT License][1]                                                                                         |
| [Jakarta JSON Processing API][2] | [Eclipse Public License 2.0][3]; [GNU General Public License, version 2 with the GNU Classpath Exception][4] |
| [JSON-B API][5]                  | [Eclipse Public License 2.0][3]; [GNU General Public License, version 2 with the GNU Classpath Exception][4] |
| [org.eclipse.yasson][6]          | [Eclipse Public License v. 2.0][7]; [Eclipse Distribution License v. 1.0][8]                                 |
| [error-reporting-java][9]        | [MIT License][10]                                                                                            |
| [JGit - Core][11]                | Eclipse Distribution License (New BSD License)                                                               |

### Test Dependencies

| Dependency                                | License                           |
| ----------------------------------------- | --------------------------------- |
| [JUnit Jupiter Engine][12]                | [Eclipse Public License v2.0][13] |
| [JUnit Jupiter Params][12]                | [Eclipse Public License v2.0][13] |
| [Hamcrest][14]                            | [BSD License 3][15]               |
| [JUnit5 System Extensions][16]            | [Eclipse Public License v2.0][7]  |
| [EqualsVerifier | release normal jar][17] | [Apache License, Version 2.0][18] |
| [mockito-core][19]                        | [The MIT License][20]             |
| [SLF4J JDK14 Binding][21]                 | [MIT License][22]                 |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                  |
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][18] |
| [Apache Maven Enforcer Plugin][26]                      | [Apache-2.0][18]                  |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][18]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][28] | [ASL2][29]                        |
| [Maven Surefire Plugin][30]                             | [Apache License, Version 2.0][18] |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][18] |
| [Apache Maven Deploy Plugin][32]                        | [Apache-2.0][18]                  |
| [Apache Maven GPG Plugin][33]                           | [Apache License, Version 2.0][18] |
| [Apache Maven Source Plugin][34]                        | [Apache License, Version 2.0][18] |
| [Apache Maven Javadoc Plugin][35]                       | [Apache License, Version 2.0][18] |
| [Nexus Staging Maven Plugin][36]                        | [Eclipse Public License][37]      |
| [Lombok Maven Plugin][38]                               | [The MIT License][39]             |
| [JaCoCo :: Maven Plugin][40]                            | [Eclipse Public License 2.0][41]  |
| [error-code-crawler-maven-plugin][42]                   | [MIT License][43]                 |
| [Reproducible Build Maven Plugin][44]                   | [Apache 2.0][29]                  |
| [Apache Maven Clean Plugin][45]                         | [Apache License, Version 2.0][18] |
| [Apache Maven Resources Plugin][46]                     | [Apache License, Version 2.0][18] |
| [Apache Maven JAR Plugin][47]                           | [Apache License, Version 2.0][18] |
| [Apache Maven Install Plugin][48]                       | [Apache License, Version 2.0][18] |
| [Apache Maven Site Plugin][49]                          | [Apache License, Version 2.0][18] |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project-Keeper shared model classes][50] | [The MIT License][51]                          |
| [jaxb-api][52]                            | [CDDL 1.1][53]; [GPL2 w/ CPE][53]              |
| [org.xmlunit:xmlunit-core][54]            | [The Apache Software License, Version 2.0][29] |
| [error-reporting-java][9]                 | [MIT License][10]                              |
| [Markdown Generator][55]                  | [The Apache Software License, Version 2.0][29] |
| [semver4j][56]                            | [The MIT License][22]                          |
| [Project Lombok][0]                       | [The MIT License][1]                           |
| [SnakeYAML][57]                           | [Apache License, Version 2.0][29]              |
| [Maven Model][58]                         | [Apache-2.0][18]                               |

### Test Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project Keeper shared test setup][50]    | [The MIT License][51]                          |
| [Maven Project Version Getter][59]        | [MIT License][60]                              |
| [JUnit Jupiter Engine][12]                | [Eclipse Public License v2.0][13]              |
| [JUnit Jupiter Params][12]                | [Eclipse Public License v2.0][13]              |
| [Hamcrest][14]                            | [BSD License 3][15]                            |
| [org.xmlunit:xmlunit-matchers][54]        | [The Apache Software License, Version 2.0][29] |
| [mockito-junit-jupiter][19]               | [The MIT License][20]                          |
| [Maven Plugin Integration Testing][61]    | [MIT License][62]                              |
| [EqualsVerifier | release normal jar][17] | [Apache License, Version 2.0][18]              |
| [SLF4J JDK14 Binding][21]                 | [MIT License][22]                              |

### Runtime Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project keeper Java project crawler][50] | [The MIT License][51] |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                  |
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][18] |
| [Apache Maven Enforcer Plugin][26]                      | [Apache-2.0][18]                  |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][18]    |
| [Apache Maven JAR Plugin][47]                           | [Apache License, Version 2.0][18] |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][28] | [ASL2][29]                        |
| [Maven Surefire Plugin][30]                             | [Apache License, Version 2.0][18] |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][18] |
| [Apache Maven Deploy Plugin][32]                        | [Apache-2.0][18]                  |
| [Apache Maven GPG Plugin][33]                           | [Apache License, Version 2.0][18] |
| [Apache Maven Source Plugin][34]                        | [Apache License, Version 2.0][18] |
| [Apache Maven Javadoc Plugin][35]                       | [Apache License, Version 2.0][18] |
| [Nexus Staging Maven Plugin][36]                        | [Eclipse Public License][37]      |
| [Lombok Maven Plugin][38]                               | [The MIT License][39]             |
| [Maven Failsafe Plugin][63]                             | [Apache License, Version 2.0][18] |
| [JaCoCo :: Maven Plugin][40]                            | [Eclipse Public License 2.0][41]  |
| [error-code-crawler-maven-plugin][42]                   | [MIT License][43]                 |
| [Reproducible Build Maven Plugin][44]                   | [Apache 2.0][29]                  |
| [Apache Maven Clean Plugin][45]                         | [Apache License, Version 2.0][18] |
| [Apache Maven Resources Plugin][46]                     | [Apache License, Version 2.0][18] |
| [Apache Maven Install Plugin][48]                       | [Apache License, Version 2.0][18] |
| [Apache Maven Site Plugin][49]                          | [Apache License, Version 2.0][18] |

## Project Keeper Command Line Interface

### Compile Dependencies

| Dependency                | License               |
| ------------------------- | --------------------- |
| [Project keeper core][50] | [The MIT License][51] |
| [error-reporting-java][9] | [MIT License][10]     |
| [Maven Model][58]         | [Apache-2.0][18]      |

### Test Dependencies

| Dependency                             | License                           |
| -------------------------------------- | --------------------------------- |
| [Project Keeper shared test setup][50] | [The MIT License][51]             |
| [JUnit Jupiter Engine][12]             | [Eclipse Public License v2.0][13] |
| [JUnit Jupiter Params][12]             | [Eclipse Public License v2.0][13] |
| [Hamcrest][14]                         | [BSD License 3][15]               |
| [Maven Project Version Getter][59]     | [MIT License][60]                 |

### Runtime Dependencies

| Dependency                | License           |
| ------------------------- | ----------------- |
| [SLF4J JDK14 Binding][21] | [MIT License][22] |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                  |
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][18] |
| [Apache Maven Enforcer Plugin][26]                      | [Apache-2.0][18]                  |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][18]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][28] | [ASL2][29]                        |
| [Maven Surefire Plugin][30]                             | [Apache License, Version 2.0][18] |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][18] |
| [Apache Maven Assembly Plugin][64]                      | [Apache License, Version 2.0][18] |
| [Apache Maven JAR Plugin][47]                           | [Apache License, Version 2.0][18] |
| [Artifact reference checker and unifier][65]            | [MIT License][66]                 |
| [Apache Maven Deploy Plugin][32]                        | [Apache-2.0][18]                  |
| [Apache Maven GPG Plugin][33]                           | [Apache License, Version 2.0][18] |
| [Apache Maven Source Plugin][34]                        | [Apache License, Version 2.0][18] |
| [Apache Maven Javadoc Plugin][35]                       | [Apache License, Version 2.0][18] |
| [Nexus Staging Maven Plugin][36]                        | [Eclipse Public License][37]      |
| [Maven Failsafe Plugin][63]                             | [Apache License, Version 2.0][18] |
| [JaCoCo :: Maven Plugin][40]                            | [Eclipse Public License 2.0][41]  |
| [error-code-crawler-maven-plugin][42]                   | [MIT License][43]                 |
| [Reproducible Build Maven Plugin][44]                   | [Apache 2.0][29]                  |
| [Apache Maven Clean Plugin][45]                         | [Apache License, Version 2.0][18] |
| [Apache Maven Resources Plugin][46]                     | [Apache License, Version 2.0][18] |
| [Apache Maven Install Plugin][48]                       | [Apache License, Version 2.0][18] |
| [Apache Maven Site Plugin][49]                          | [Apache License, Version 2.0][18] |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project keeper core][50]                 | [The MIT License][51]                          |
| [Maven Plugin Tools Java Annotations][67] | [Apache-2.0][18]                               |
| [Maven Plugin API][68]                    | [Apache-2.0][18]                               |
| [Maven Project Builder][69]               | [The Apache Software License, Version 2.0][29] |
| [Maven Core][70]                          | [Apache-2.0][18]                               |
| [error-reporting-java][9]                 | [MIT License][10]                              |
| [Project Lombok][0]                       | [The MIT License][1]                           |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][59]     | [MIT License][60]                              |
| [JUnit Jupiter Engine][12]             | [Eclipse Public License v2.0][13]              |
| [JUnit Jupiter Params][12]             | [Eclipse Public License v2.0][13]              |
| [Hamcrest][14]                         | [BSD License 3][15]                            |
| [org.xmlunit:xmlunit-matchers][54]     | [The Apache Software License, Version 2.0][29] |
| [mockito-core][19]                     | [The MIT License][20]                          |
| [Maven Plugin Integration Testing][61] | [MIT License][62]                              |
| [SLF4J JDK14 Binding][21]              | [MIT License][22]                              |
| [JaCoCo :: Agent][71]                  | [Eclipse Public License 2.0][41]               |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                  |
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][18] |
| [Apache Maven Enforcer Plugin][26]                      | [Apache-2.0][18]                  |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][18]    |
| [Maven Plugin Plugin][72]                               | [Apache-2.0][18]                  |
| [Apache Maven JAR Plugin][47]                           | [Apache License, Version 2.0][18] |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][28] | [ASL2][29]                        |
| [Maven Surefire Plugin][30]                             | [Apache License, Version 2.0][18] |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][18] |
| [Apache Maven Deploy Plugin][32]                        | [Apache-2.0][18]                  |
| [Apache Maven GPG Plugin][33]                           | [Apache License, Version 2.0][18] |
| [Apache Maven Source Plugin][34]                        | [Apache License, Version 2.0][18] |
| [Apache Maven Javadoc Plugin][35]                       | [Apache License, Version 2.0][18] |
| [Nexus Staging Maven Plugin][36]                        | [Eclipse Public License][37]      |
| [Apache Maven Dependency Plugin][73]                    | [Apache License, Version 2.0][18] |
| [Lombok Maven Plugin][38]                               | [The MIT License][39]             |
| [Maven Failsafe Plugin][63]                             | [Apache License, Version 2.0][18] |
| [JaCoCo :: Maven Plugin][40]                            | [Eclipse Public License 2.0][41]  |
| [error-code-crawler-maven-plugin][42]                   | [MIT License][43]                 |
| [Reproducible Build Maven Plugin][44]                   | [Apache 2.0][29]                  |
| [Apache Maven Clean Plugin][45]                         | [Apache License, Version 2.0][18] |
| [Apache Maven Resources Plugin][46]                     | [Apache License, Version 2.0][18] |
| [Apache Maven Install Plugin][48]                       | [Apache License, Version 2.0][18] |
| [Apache Maven Site Plugin][49]                          | [Apache License, Version 2.0][18] |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project-Keeper shared model classes][50] | [The MIT License][51]                          |
| [Maven Plugin Tools Java Annotations][67] | [Apache-2.0][18]                               |
| [Maven Plugin API][68]                    | [Apache-2.0][18]                               |
| [Maven Project Builder][69]               | [The Apache Software License, Version 2.0][29] |
| [error-reporting-java][9]                 | [MIT License][10]                              |
| [JGit - Core][11]                         | Eclipse Distribution License (New BSD License) |
| [semver4j][56]                            | [The MIT License][22]                          |
| [Maven Core][70]                          | [Apache-2.0][18]                               |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][59]     | [MIT License][60]                              |
| [JUnit Jupiter Engine][12]             | [Eclipse Public License v2.0][13]              |
| [JUnit Jupiter Params][12]             | [Eclipse Public License v2.0][13]              |
| [Hamcrest][14]                         | [BSD License 3][15]                            |
| [org.xmlunit:xmlunit-matchers][54]     | [The Apache Software License, Version 2.0][29] |
| [SLF4J JDK14 Binding][21]              | [MIT License][22]                              |
| [mockito-core][19]                     | [The MIT License][20]                          |
| [Maven Plugin Integration Testing][61] | [MIT License][62]                              |
| [JaCoCo :: Agent][71]                  | [Eclipse Public License 2.0][41]               |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                  |
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][18] |
| [Apache Maven Enforcer Plugin][26]                      | [Apache-2.0][18]                  |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][18]    |
| [Maven Plugin Plugin][72]                               | [Apache-2.0][18]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][28] | [ASL2][29]                        |
| [Maven Surefire Plugin][30]                             | [Apache License, Version 2.0][18] |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][18] |
| [Apache Maven Deploy Plugin][32]                        | [Apache-2.0][18]                  |
| [Apache Maven GPG Plugin][33]                           | [Apache License, Version 2.0][18] |
| [Apache Maven Source Plugin][34]                        | [Apache License, Version 2.0][18] |
| [Apache Maven Javadoc Plugin][35]                       | [Apache License, Version 2.0][18] |
| [Nexus Staging Maven Plugin][36]                        | [Eclipse Public License][37]      |
| [Apache Maven Dependency Plugin][73]                    | [Apache License, Version 2.0][18] |
| [Maven Failsafe Plugin][63]                             | [Apache License, Version 2.0][18] |
| [JaCoCo :: Maven Plugin][40]                            | [Eclipse Public License 2.0][41]  |
| [error-code-crawler-maven-plugin][42]                   | [MIT License][43]                 |
| [Reproducible Build Maven Plugin][44]                   | [Apache 2.0][29]                  |
| [Apache Maven Clean Plugin][45]                         | [Apache License, Version 2.0][18] |
| [Apache Maven Resources Plugin][46]                     | [Apache License, Version 2.0][18] |
| [Apache Maven JAR Plugin][47]                           | [Apache License, Version 2.0][18] |
| [Apache Maven Install Plugin][48]                       | [Apache License, Version 2.0][18] |
| [Apache Maven Site Plugin][49]                          | [Apache License, Version 2.0][18] |

## Project Keeper Shared Test Setup

### Compile Dependencies

| Dependency                                | License                           |
| ----------------------------------------- | --------------------------------- |
| [Project-Keeper shared model classes][50] | [The MIT License][51]             |
| [SnakeYAML][57]                           | [Apache License, Version 2.0][29] |
| [Hamcrest][14]                            | [BSD License 3][15]               |
| [Maven Model][58]                         | [Apache-2.0][18]                  |
| [Project Lombok][0]                       | [The MIT License][1]              |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                  |
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][18] |
| [Apache Maven Enforcer Plugin][26]                      | [Apache-2.0][18]                  |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][18]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][28] | [ASL2][29]                        |
| [Maven Surefire Plugin][30]                             | [Apache License, Version 2.0][18] |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][18] |
| [Lombok Maven Plugin][38]                               | [The MIT License][39]             |
| [JaCoCo :: Maven Plugin][40]                            | [Eclipse Public License 2.0][41]  |
| [error-code-crawler-maven-plugin][42]                   | [MIT License][43]                 |
| [Reproducible Build Maven Plugin][44]                   | [Apache 2.0][29]                  |
| [Apache Maven Clean Plugin][45]                         | [Apache License, Version 2.0][18] |
| [Apache Maven Resources Plugin][46]                     | [Apache License, Version 2.0][18] |
| [Apache Maven JAR Plugin][47]                           | [Apache License, Version 2.0][18] |
| [Apache Maven Install Plugin][48]                       | [Apache License, Version 2.0][18] |
| [Apache Maven Deploy Plugin][32]                        | [Apache License, Version 2.0][18] |
| [Apache Maven Site Plugin][49]                          | [Apache License, Version 2.0][18] |

[0]: https://projectlombok.org
[1]: https://projectlombok.org/LICENSE
[2]: https://github.com/eclipse-ee4j/jsonp
[3]: https://projects.eclipse.org/license/epl-2.0
[4]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[5]: https://projects.eclipse.org/projects/ee4j.jsonp
[6]: https://projects.eclipse.org/projects/ee4j.yasson
[7]: http://www.eclipse.org/legal/epl-v20.html
[8]: http://www.eclipse.org/org/documents/edl-v10.php
[9]: https://github.com/exasol/error-reporting-java/
[10]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[11]: https://www.eclipse.org/jgit/
[12]: https://junit.org/junit5/
[13]: https://www.eclipse.org/legal/epl-v20.html
[14]: http://hamcrest.org/JavaHamcrest/
[15]: http://opensource.org/licenses/BSD-3-Clause
[16]: https://github.com/itsallcode/junit5-system-extensions
[17]: https://www.jqno.nl/equalsverifier
[18]: https://www.apache.org/licenses/LICENSE-2.0.txt
[19]: https://github.com/mockito/mockito
[20]: https://github.com/mockito/mockito/blob/main/LICENSE
[21]: http://www.slf4j.org
[22]: http://www.opensource.org/licenses/mit-license.php
[23]: http://sonarsource.github.io/sonar-scanner-maven/
[24]: http://www.gnu.org/licenses/lgpl.txt
[25]: https://maven.apache.org/plugins/maven-compiler-plugin/
[26]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[27]: https://www.mojohaus.org/flatten-maven-plugin/
[28]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[29]: http://www.apache.org/licenses/LICENSE-2.0.txt
[30]: https://maven.apache.org/surefire/maven-surefire-plugin/
[31]: https://www.mojohaus.org/versions/versions-maven-plugin/
[32]: https://maven.apache.org/plugins/maven-deploy-plugin/
[33]: https://maven.apache.org/plugins/maven-gpg-plugin/
[34]: https://maven.apache.org/plugins/maven-source-plugin/
[35]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[36]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[37]: http://www.eclipse.org/legal/epl-v10.html
[38]: https://anthonywhitford.com/lombok.maven/lombok-maven-plugin/
[39]: https://opensource.org/licenses/MIT
[40]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[41]: https://www.eclipse.org/legal/epl-2.0/
[42]: https://github.com/exasol/error-code-crawler-maven-plugin/
[43]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[44]: http://zlika.github.io/reproducible-build-maven-plugin
[45]: https://maven.apache.org/plugins/maven-clean-plugin/
[46]: https://maven.apache.org/plugins/maven-resources-plugin/
[47]: https://maven.apache.org/plugins/maven-jar-plugin/
[48]: https://maven.apache.org/plugins/maven-install-plugin/
[49]: https://maven.apache.org/plugins/maven-site-plugin/
[50]: https://github.com/exasol/project-keeper/
[51]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[52]: https://github.com/eclipse-ee4j/jaxb-api
[53]: https://oss.oracle.com/licenses/CDDL+GPL-1.1
[54]: https://www.xmlunit.org/
[55]: https://github.com/Steppschuh/Java-Markdown-Generator
[56]: https://github.com/vdurmont/semver4j
[57]: https://bitbucket.org/snakeyaml/snakeyaml
[58]: https://maven.apache.org/ref/3.9.0/maven-model/
[59]: https://github.com/exasol/maven-project-version-getter/
[60]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[61]: https://github.com/exasol/maven-plugin-integration-testing/
[62]: https://github.com/exasol/maven-plugin-integration-testing/blob/main/LICENSE
[63]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[64]: https://maven.apache.org/plugins/maven-assembly-plugin/
[65]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[66]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[67]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[68]: https://maven.apache.org/ref/3.9.0/maven-plugin-api/
[69]: http://maven.apache.org/
[70]: https://maven.apache.org/ref/3.9.0/maven-core/
[71]: https://www.eclemma.org/jacoco/index.html
[72]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[73]: https://maven.apache.org/plugins/maven-dependency-plugin/
