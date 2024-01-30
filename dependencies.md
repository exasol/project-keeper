<!-- @formatter:off -->
# Dependencies

## Project Keeper Shared Model Classes

### Compile Dependencies

| Dependency                       | License                                                                                                      |
| -------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [Jakarta JSON Processing API][0] | [Eclipse Public License 2.0][1]; [GNU General Public License, version 2 with the GNU Classpath Exception][2] |
| [JSON-B API][3]                  | [Eclipse Public License 2.0][1]; [GNU General Public License, version 2 with the GNU Classpath Exception][2] |
| [Yasson][4]                      | [Eclipse Public License v. 2.0][5]; [Eclipse Distribution License v. 1.0][6]                                 |
| [error-reporting-java][7]        | [MIT License][8]                                                                                             |
| [JGit - Core][9]                 | [BSD-3-Clause][10]                                                                                           |

### Test Dependencies

| Dependency                                 | License                           |
| ------------------------------------------ | --------------------------------- |
| [JUnit Jupiter Engine][11]                 | [Eclipse Public License v2.0][12] |
| [JUnit Jupiter Params][11]                 | [Eclipse Public License v2.0][12] |
| [Hamcrest][13]                             | [BSD License 3][14]               |
| [JUnit5 System Extensions][15]             | [Eclipse Public License v2.0][5]  |
| [EqualsVerifier \| release normal jar][16] | [Apache License, Version 2.0][17] |
| [to-string-verifier][18]                   | [MIT License][19]                 |
| [mockito-core][20]                         | [MIT][21]                         |
| [SLF4J JDK14 Binding][22]                  | [MIT License][19]                 |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                  |
| [Apache Maven Toolchains Plugin][25]                    | [Apache License, Version 2.0][17] |
| [Apache Maven Compiler Plugin][26]                      | [Apache-2.0][17]                  |
| [Apache Maven Enforcer Plugin][27]                      | [Apache-2.0][17]                  |
| [Maven Flatten Plugin][28]                              | [Apache Software Licenese][17]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][29] | [ASL2][30]                        |
| [Maven Surefire Plugin][31]                             | [Apache-2.0][17]                  |
| [Versions Maven Plugin][32]                             | [Apache License, Version 2.0][17] |
| [duplicate-finder-maven-plugin Maven Mojo][33]          | [Apache License 2.0][34]          |
| [Apache Maven Deploy Plugin][35]                        | [Apache-2.0][17]                  |
| [Apache Maven GPG Plugin][36]                           | [Apache-2.0][17]                  |
| [Apache Maven Source Plugin][37]                        | [Apache License, Version 2.0][17] |
| [Apache Maven Javadoc Plugin][38]                       | [Apache-2.0][17]                  |
| [Nexus Staging Maven Plugin][39]                        | [Eclipse Public License][40]      |
| [JaCoCo :: Maven Plugin][41]                            | [Eclipse Public License 2.0][42]  |
| [error-code-crawler-maven-plugin][43]                   | [MIT License][44]                 |
| [Reproducible Build Maven Plugin][45]                   | [Apache 2.0][30]                  |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project Keeper shared model classes][46] | [The MIT License][47]                          |
| [org.xmlunit:xmlunit-core][48]            | [The Apache Software License, Version 2.0][30] |
| [error-reporting-java][7]                 | [MIT License][8]                               |
| [Markdown Generator][49]                  | [The Apache Software License, Version 2.0][30] |
| [semver4j][50]                            | [The MIT License][19]                          |
| [SnakeYAML][51]                           | [Apache License, Version 2.0][30]              |
| [Maven Model][52]                         | [Apache-2.0][17]                               |

### Test Dependencies

| Dependency                                 | License                                        |
| ------------------------------------------ | ---------------------------------------------- |
| [Project Keeper shared test setup][46]     | [The MIT License][47]                          |
| [Maven Project Version Getter][53]         | [MIT License][54]                              |
| [JUnit Jupiter Engine][11]                 | [Eclipse Public License v2.0][12]              |
| [JUnit Jupiter Params][11]                 | [Eclipse Public License v2.0][12]              |
| [Hamcrest][13]                             | [BSD License 3][14]                            |
| [org.xmlunit:xmlunit-matchers][48]         | [The Apache Software License, Version 2.0][30] |
| [mockito-junit-jupiter][20]                | [MIT][21]                                      |
| [Maven Plugin Integration Testing][55]     | [MIT License][56]                              |
| [EqualsVerifier \| release normal jar][16] | [Apache License, Version 2.0][17]              |
| [to-string-verifier][18]                   | [MIT License][19]                              |
| [SLF4J JDK14 Binding][22]                  | [MIT License][19]                              |

### Runtime Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Java project crawler][46] | [The MIT License][47] |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                  |
| [Apache Maven Toolchains Plugin][25]                    | [Apache License, Version 2.0][17] |
| [Apache Maven JAR Plugin][57]                           | [Apache License, Version 2.0][17] |
| [Apache Maven Compiler Plugin][26]                      | [Apache-2.0][17]                  |
| [Apache Maven Enforcer Plugin][27]                      | [Apache-2.0][17]                  |
| [Maven Flatten Plugin][28]                              | [Apache Software Licenese][17]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][29] | [ASL2][30]                        |
| [Maven Surefire Plugin][31]                             | [Apache-2.0][17]                  |
| [Versions Maven Plugin][32]                             | [Apache License, Version 2.0][17] |
| [duplicate-finder-maven-plugin Maven Mojo][33]          | [Apache License 2.0][34]          |
| [Apache Maven Deploy Plugin][35]                        | [Apache-2.0][17]                  |
| [Apache Maven GPG Plugin][36]                           | [Apache-2.0][17]                  |
| [Apache Maven Source Plugin][37]                        | [Apache License, Version 2.0][17] |
| [Apache Maven Javadoc Plugin][38]                       | [Apache-2.0][17]                  |
| [Nexus Staging Maven Plugin][39]                        | [Eclipse Public License][40]      |
| [Maven Failsafe Plugin][58]                             | [Apache-2.0][17]                  |
| [JaCoCo :: Maven Plugin][41]                            | [Eclipse Public License 2.0][42]  |
| [error-code-crawler-maven-plugin][43]                   | [MIT License][44]                 |
| [Reproducible Build Maven Plugin][45]                   | [Apache 2.0][30]                  |

## Project Keeper Command Line Interface

### Compile Dependencies

| Dependency                | License               |
| ------------------------- | --------------------- |
| [Project Keeper Core][46] | [The MIT License][47] |
| [error-reporting-java][7] | [MIT License][8]      |
| [Maven Model][52]         | [Apache-2.0][17]      |

### Test Dependencies

| Dependency                             | License                           |
| -------------------------------------- | --------------------------------- |
| [Project Keeper shared test setup][46] | [The MIT License][47]             |
| [JUnit Jupiter Engine][11]             | [Eclipse Public License v2.0][12] |
| [JUnit Jupiter Params][11]             | [Eclipse Public License v2.0][12] |
| [Hamcrest][13]                         | [BSD License 3][14]               |
| [Maven Project Version Getter][53]     | [MIT License][54]                 |

### Runtime Dependencies

| Dependency                | License           |
| ------------------------- | ----------------- |
| [SLF4J JDK14 Binding][22] | [MIT License][19] |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                  |
| [Apache Maven Toolchains Plugin][25]                    | [Apache License, Version 2.0][17] |
| [Apache Maven Compiler Plugin][26]                      | [Apache-2.0][17]                  |
| [Apache Maven Enforcer Plugin][27]                      | [Apache-2.0][17]                  |
| [Maven Flatten Plugin][28]                              | [Apache Software Licenese][17]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][29] | [ASL2][30]                        |
| [Maven Surefire Plugin][31]                             | [Apache-2.0][17]                  |
| [Versions Maven Plugin][32]                             | [Apache License, Version 2.0][17] |
| [duplicate-finder-maven-plugin Maven Mojo][33]          | [Apache License 2.0][34]          |
| [Apache Maven Assembly Plugin][59]                      | [Apache-2.0][17]                  |
| [Apache Maven JAR Plugin][57]                           | [Apache License, Version 2.0][17] |
| [Artifact reference checker and unifier][60]            | [MIT License][61]                 |
| [Apache Maven Deploy Plugin][35]                        | [Apache-2.0][17]                  |
| [Apache Maven GPG Plugin][36]                           | [Apache-2.0][17]                  |
| [Apache Maven Source Plugin][37]                        | [Apache License, Version 2.0][17] |
| [Apache Maven Javadoc Plugin][38]                       | [Apache-2.0][17]                  |
| [Nexus Staging Maven Plugin][39]                        | [Eclipse Public License][40]      |
| [Maven Failsafe Plugin][58]                             | [Apache-2.0][17]                  |
| [JaCoCo :: Maven Plugin][41]                            | [Eclipse Public License 2.0][42]  |
| [error-code-crawler-maven-plugin][43]                   | [MIT License][44]                 |
| [Reproducible Build Maven Plugin][45]                   | [Apache 2.0][30]                  |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Core][46]                 | [The MIT License][47] |
| [Maven Plugin Tools Java Annotations][62] | [Apache-2.0][17]      |
| [Maven Plugin API][63]                    | [Apache-2.0][17]      |
| [Maven Core][64]                          | [Apache-2.0][17]      |
| [error-reporting-java][7]                 | [MIT License][8]      |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][53]     | [MIT License][54]                              |
| [JUnit Jupiter Engine][11]             | [Eclipse Public License v2.0][12]              |
| [JUnit Jupiter Params][11]             | [Eclipse Public License v2.0][12]              |
| [Hamcrest][13]                         | [BSD License 3][14]                            |
| [org.xmlunit:xmlunit-matchers][48]     | [The Apache Software License, Version 2.0][30] |
| [mockito-core][20]                     | [MIT][21]                                      |
| [Maven Plugin Integration Testing][55] | [MIT License][56]                              |
| [SLF4J JDK14 Binding][22]              | [MIT License][19]                              |
| [JaCoCo :: Agent][65]                  | [Eclipse Public License 2.0][42]               |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                  |
| [Apache Maven Toolchains Plugin][25]                    | [Apache License, Version 2.0][17] |
| [Maven Plugin Plugin][66]                               | [Apache-2.0][17]                  |
| [Apache Maven Compiler Plugin][26]                      | [Apache-2.0][17]                  |
| [Apache Maven Enforcer Plugin][27]                      | [Apache-2.0][17]                  |
| [Maven Flatten Plugin][28]                              | [Apache Software Licenese][17]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][29] | [ASL2][30]                        |
| [Maven Surefire Plugin][31]                             | [Apache-2.0][17]                  |
| [Versions Maven Plugin][32]                             | [Apache License, Version 2.0][17] |
| [Apache Maven JAR Plugin][57]                           | [Apache License, Version 2.0][17] |
| [duplicate-finder-maven-plugin Maven Mojo][33]          | [Apache License 2.0][34]          |
| [Apache Maven Deploy Plugin][35]                        | [Apache-2.0][17]                  |
| [Apache Maven GPG Plugin][36]                           | [Apache-2.0][17]                  |
| [Apache Maven Source Plugin][37]                        | [Apache License, Version 2.0][17] |
| [Apache Maven Javadoc Plugin][38]                       | [Apache-2.0][17]                  |
| [Nexus Staging Maven Plugin][39]                        | [Eclipse Public License][40]      |
| [Apache Maven Dependency Plugin][67]                    | [Apache-2.0][17]                  |
| [Maven Failsafe Plugin][58]                             | [Apache-2.0][17]                  |
| [JaCoCo :: Maven Plugin][41]                            | [Eclipse Public License 2.0][42]  |
| [error-code-crawler-maven-plugin][43]                   | [MIT License][44]                 |
| [Reproducible Build Maven Plugin][45]                   | [Apache 2.0][30]                  |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper shared model classes][46] | [The MIT License][47] |
| [Maven Plugin Tools Java Annotations][62] | [Apache-2.0][17]      |
| [Maven Plugin API][63]                    | [Apache-2.0][17]      |
| [error-reporting-java][7]                 | [MIT License][8]      |
| [JGit - Core][9]                          | [BSD-3-Clause][10]    |
| [semver4j][50]                            | [The MIT License][19] |
| [Maven Core][64]                          | [Apache-2.0][17]      |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][53]     | [MIT License][54]                              |
| [JUnit Jupiter Engine][11]             | [Eclipse Public License v2.0][12]              |
| [JUnit Jupiter Params][11]             | [Eclipse Public License v2.0][12]              |
| [Hamcrest][13]                         | [BSD License 3][14]                            |
| [org.xmlunit:xmlunit-matchers][48]     | [The Apache Software License, Version 2.0][30] |
| [SLF4J JDK14 Binding][22]              | [MIT License][19]                              |
| [mockito-core][20]                     | [MIT][21]                                      |
| [mockito-junit-jupiter][20]            | [MIT][21]                                      |
| [Maven Plugin Integration Testing][55] | [MIT License][56]                              |
| [JaCoCo :: Agent][65]                  | [Eclipse Public License 2.0][42]               |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                  |
| [Apache Maven Toolchains Plugin][25]                    | [Apache License, Version 2.0][17] |
| [Apache Maven Compiler Plugin][26]                      | [Apache-2.0][17]                  |
| [Apache Maven Enforcer Plugin][27]                      | [Apache-2.0][17]                  |
| [Maven Flatten Plugin][28]                              | [Apache Software Licenese][17]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][29] | [ASL2][30]                        |
| [Maven Surefire Plugin][31]                             | [Apache-2.0][17]                  |
| [Versions Maven Plugin][32]                             | [Apache License, Version 2.0][17] |
| [Maven Plugin Plugin][66]                               | [Apache-2.0][17]                  |
| [duplicate-finder-maven-plugin Maven Mojo][33]          | [Apache License 2.0][34]          |
| [Apache Maven Deploy Plugin][35]                        | [Apache-2.0][17]                  |
| [Apache Maven GPG Plugin][36]                           | [Apache-2.0][17]                  |
| [Apache Maven Source Plugin][37]                        | [Apache License, Version 2.0][17] |
| [Apache Maven Javadoc Plugin][38]                       | [Apache-2.0][17]                  |
| [Nexus Staging Maven Plugin][39]                        | [Eclipse Public License][40]      |
| [Apache Maven Dependency Plugin][67]                    | [Apache-2.0][17]                  |
| [Maven Failsafe Plugin][58]                             | [Apache-2.0][17]                  |
| [JaCoCo :: Maven Plugin][41]                            | [Eclipse Public License 2.0][42]  |
| [error-code-crawler-maven-plugin][43]                   | [MIT License][44]                 |
| [Reproducible Build Maven Plugin][45]                   | [Apache 2.0][30]                  |

## Project Keeper Shared Test Setup

### Compile Dependencies

| Dependency                                | License                           |
| ----------------------------------------- | --------------------------------- |
| [Project Keeper shared model classes][46] | [The MIT License][47]             |
| [SnakeYAML][51]                           | [Apache License, Version 2.0][30] |
| [Hamcrest][13]                            | [BSD License 3][14]               |
| [Maven Model][52]                         | [Apache-2.0][17]                  |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                  |
| [Apache Maven Toolchains Plugin][25]                    | [Apache License, Version 2.0][17] |
| [Apache Maven Compiler Plugin][26]                      | [Apache-2.0][17]                  |
| [Apache Maven Enforcer Plugin][27]                      | [Apache-2.0][17]                  |
| [Maven Flatten Plugin][28]                              | [Apache Software Licenese][17]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][29] | [ASL2][30]                        |
| [Maven Surefire Plugin][31]                             | [Apache-2.0][17]                  |
| [Versions Maven Plugin][32]                             | [Apache License, Version 2.0][17] |
| [duplicate-finder-maven-plugin Maven Mojo][33]          | [Apache License 2.0][34]          |
| [JaCoCo :: Maven Plugin][41]                            | [Eclipse Public License 2.0][42]  |
| [error-code-crawler-maven-plugin][43]                   | [MIT License][44]                 |
| [Reproducible Build Maven Plugin][45]                   | [Apache 2.0][30]                  |

[0]: https://github.com/eclipse-ee4j/jsonp
[1]: https://projects.eclipse.org/license/epl-2.0
[2]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[3]: https://projects.eclipse.org/projects/ee4j.jsonp
[4]: https://projects.eclipse.org/projects/ee4j.yasson
[5]: http://www.eclipse.org/legal/epl-v20.html
[6]: http://www.eclipse.org/org/documents/edl-v10.php
[7]: https://github.com/exasol/error-reporting-java/
[8]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[9]: https://www.eclipse.org/jgit/
[10]: https://www.eclipse.org/org/documents/edl-v10.php
[11]: https://junit.org/junit5/
[12]: https://www.eclipse.org/legal/epl-v20.html
[13]: http://hamcrest.org/JavaHamcrest/
[14]: http://opensource.org/licenses/BSD-3-Clause
[15]: https://github.com/itsallcode/junit5-system-extensions
[16]: https://www.jqno.nl/equalsverifier
[17]: https://www.apache.org/licenses/LICENSE-2.0.txt
[18]: https://github.com/jparams/to-string-verifier
[19]: http://www.opensource.org/licenses/mit-license.php
[20]: https://github.com/mockito/mockito
[21]: https://opensource.org/licenses/MIT
[22]: http://www.slf4j.org
[23]: http://sonarsource.github.io/sonar-scanner-maven/
[24]: http://www.gnu.org/licenses/lgpl.txt
[25]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[26]: https://maven.apache.org/plugins/maven-compiler-plugin/
[27]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[28]: https://www.mojohaus.org/flatten-maven-plugin/
[29]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[30]: http://www.apache.org/licenses/LICENSE-2.0.txt
[31]: https://maven.apache.org/surefire/maven-surefire-plugin/
[32]: https://www.mojohaus.org/versions/versions-maven-plugin/
[33]: https://basepom.github.io/duplicate-finder-maven-plugin
[34]: http://www.apache.org/licenses/LICENSE-2.0.html
[35]: https://maven.apache.org/plugins/maven-deploy-plugin/
[36]: https://maven.apache.org/plugins/maven-gpg-plugin/
[37]: https://maven.apache.org/plugins/maven-source-plugin/
[38]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[39]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[40]: http://www.eclipse.org/legal/epl-v10.html
[41]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[42]: https://www.eclipse.org/legal/epl-2.0/
[43]: https://github.com/exasol/error-code-crawler-maven-plugin/
[44]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[45]: http://zlika.github.io/reproducible-build-maven-plugin
[46]: https://github.com/exasol/project-keeper/
[47]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[48]: https://www.xmlunit.org/
[49]: https://github.com/Steppschuh/Java-Markdown-Generator
[50]: https://github.com/vdurmont/semver4j
[51]: https://bitbucket.org/snakeyaml/snakeyaml
[52]: https://maven.apache.org/ref/3.9.6/maven-model/
[53]: https://github.com/exasol/maven-project-version-getter/
[54]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[55]: https://github.com/exasol/maven-plugin-integration-testing/
[56]: https://github.com/exasol/maven-plugin-integration-testing/blob/main/LICENSE
[57]: https://maven.apache.org/plugins/maven-jar-plugin/
[58]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[59]: https://maven.apache.org/plugins/maven-assembly-plugin/
[60]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[61]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[62]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[63]: https://maven.apache.org/ref/3.9.6/maven-plugin-api/
[64]: https://maven.apache.org/ref/3.9.6/maven-core/
[65]: https://www.eclemma.org/jacoco/index.html
[66]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[67]: https://maven.apache.org/plugins/maven-dependency-plugin/
