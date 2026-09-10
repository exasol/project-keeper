<!-- @formatter:off -->
# Dependencies

## Project Keeper Root Project

### Plugin Dependencies

| Dependency                                             | License                              |
| ------------------------------------------------------ | ------------------------------------ |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                      |
| [OpenFastTrace Maven Plugin][2]                        | [GNU General Public License v3.0][3] |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                      |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                     |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                            |
| [SonarQube Scanner for Maven][9]                       | [GNU LGPL 3][10]                     |

## Project Keeper Shared Model Classes

### Compile Dependencies

| Dependency                        | License                                                                                                           |
| --------------------------------- | ----------------------------------------------------------------------------------------------------------------- |
| [Jakarta JSON Processing API][11] | [Eclipse Public License 2.0][12]; [GNU General Public License, version 2 with the GNU Classpath Exception][13]    |
| [Jakarta JSON Binding API][14]    | [Eclipse Public License 2.0][12]; [GNU General Public License, version 2 with the GNU Classpath Exception][13]    |
| [Yasson][15]                      | [Eclipse Public License v. 2.0][16]; [GNU General Public License, version 2 with the GNU Classpath Exception][17] |
| [error-reporting-java][18]        | [MIT License][19]                                                                                                 |
| [JGit - Core][20]                 | [BSD-3-Clause][21]                                                                                                |

### Test Dependencies

| Dependency                                 | License                           |
| ------------------------------------------ | --------------------------------- |
| [JUnit5 System Extensions][22]             | [Eclipse Public License v2.0][23] |
| [EqualsVerifier \| release normal jar][24] | [Apache License, Version 2.0][1]  |
| [to-string-verifier][25]                   | [MIT License][26]                 |
| [mockito-core][27]                         | [MIT][28]                         |
| [SLF4J JDK14 Provider][29]                 | [MIT][30]                         |
| [JUnit Jupiter (Aggregator)][31]           | [Eclipse Public License v2.0][32] |
| [Hamcrest][33]                             | [BSD-3-Clause][34]                |

### Plugin Dependencies

| Dependency                                             | License                                       |
| ------------------------------------------------------ | --------------------------------------------- |
| [SonarQube Scanner for Maven][9]                       | [GNU LGPL 3][10]                              |
| [Apache Maven Toolchains Plugin][35]                   | [Apache-2.0][1]                               |
| [Apache Maven Compiler Plugin][36]                     | [Apache-2.0][1]                               |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                               |
| [Maven Flatten Plugin][37]                             | [Apache Software License][1]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                                     |
| [Maven Surefire Plugin][38]                            | [Apache-2.0][1]                               |
| [Versions Maven Plugin][39]                            | [Apache License, Version 2.0][1]              |
| [duplicate-finder-maven-plugin Maven Mojo][40]         | [Apache License 2.0][41]                      |
| [Apache Maven Artifact Plugin][42]                     | [Apache-2.0][1]                               |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                               |
| [Apache Maven Source Plugin][43]                       | [Apache-2.0][1]                               |
| [Apache Maven Javadoc Plugin][44]                      | [Apache-2.0][1]                               |
| [spdx-maven-plugin Maven Plugin][45]                   | [The Apache Software License, Version 2.0][8] |
| [Build Helper Maven Plugin][46]                        | [The MIT License][47]                         |
| [Apache Maven GPG Plugin][48]                          | [Apache-2.0][1]                               |
| [Central Publishing Maven Plugin][49]                  | [The Apache License, Version 2.0][1]          |
| [Apache Maven Dependency Plugin][50]                   | [Apache-2.0][1]                               |
| [JaCoCo :: Maven Plugin][51]                           | [EPL-2.0][52]                                 |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                              |
| [Git Commit Id Maven Plugin][53]                       | [GNU Lesser General Public License 3.0][54]   |
| [Apache Maven Clean Plugin][55]                        | [Apache-2.0][1]                               |
| [Apache Maven Resources Plugin][56]                    | [Apache-2.0][1]                               |
| [Apache Maven Install Plugin][57]                      | [Apache-2.0][1]                               |
| [Apache Maven Site Plugin][58]                         | [Apache-2.0][1]                               |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                       |
| ----------------------------------------- | --------------------------------------------- |
| [Project Keeper shared model classes][59] | [The MIT License][60]                         |
| [org.xmlunit:xmlunit-core][61]            | [The Apache Software License, Version 2.0][8] |
| [error-reporting-java][18]                | [MIT License][19]                             |
| [Markdown Generator][62]                  | [The Apache Software License, Version 2.0][8] |
| [semver4j][63]                            | [The MIT License][26]                         |
| [SnakeYAML][64]                           | [Apache License, Version 2.0][8]              |
| [SnakeYAML Engine][65]                    | [Apache License, Version 2.0][1]              |
| [Maven Model][66]                         | [Apache-2.0][1]                               |
| [jcabi-github][67]                        | [3-Clause BSD License][68]                    |

### Test Dependencies

| Dependency                                 | License                                       |
| ------------------------------------------ | --------------------------------------------- |
| [Project Keeper shared test setup][59]     | [The MIT License][60]                         |
| [Maven Project Version Getter][69]         | [MIT License][70]                             |
| [org.xmlunit:xmlunit-matchers][61]         | [The Apache Software License, Version 2.0][8] |
| [mockito-junit-jupiter][27]                | [MIT][28]                                     |
| [Maven Plugin Integration Testing][71]     | [MIT License][72]                             |
| [EqualsVerifier \| release normal jar][24] | [Apache License, Version 2.0][1]              |
| [to-string-verifier][25]                   | [MIT License][26]                             |
| [junit-pioneer][73]                        | [Eclipse Public License v2.0][32]             |
| [SLF4J JDK14 Provider][29]                 | [MIT][30]                                     |
| [JUnit Jupiter (Aggregator)][31]           | [Eclipse Public License v2.0][32]             |
| [Hamcrest][33]                             | [BSD-3-Clause][34]                            |

### Runtime Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Java project crawler][59] | [The MIT License][60] |

### Plugin Dependencies

| Dependency                                             | License                                       |
| ------------------------------------------------------ | --------------------------------------------- |
| [SonarQube Scanner for Maven][9]                       | [GNU LGPL 3][10]                              |
| [Apache Maven Toolchains Plugin][35]                   | [Apache-2.0][1]                               |
| [Apache Maven JAR Plugin][74]                          | [Apache-2.0][1]                               |
| [Apache Maven Compiler Plugin][36]                     | [Apache-2.0][1]                               |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                               |
| [Maven Flatten Plugin][37]                             | [Apache Software License][1]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                                     |
| [Maven Surefire Plugin][38]                            | [Apache-2.0][1]                               |
| [Versions Maven Plugin][39]                            | [Apache License, Version 2.0][1]              |
| [Exec Maven Plugin][75]                                | [Apache License 2][1]                         |
| [duplicate-finder-maven-plugin Maven Mojo][40]         | [Apache License 2.0][41]                      |
| [Apache Maven Artifact Plugin][42]                     | [Apache-2.0][1]                               |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                               |
| [Apache Maven Source Plugin][43]                       | [Apache-2.0][1]                               |
| [Apache Maven Javadoc Plugin][44]                      | [Apache-2.0][1]                               |
| [spdx-maven-plugin Maven Plugin][45]                   | [The Apache Software License, Version 2.0][8] |
| [Build Helper Maven Plugin][46]                        | [The MIT License][47]                         |
| [Apache Maven GPG Plugin][48]                          | [Apache-2.0][1]                               |
| [Central Publishing Maven Plugin][49]                  | [The Apache License, Version 2.0][1]          |
| [Apache Maven Dependency Plugin][50]                   | [Apache-2.0][1]                               |
| [Maven Failsafe Plugin][76]                            | [Apache-2.0][1]                               |
| [JaCoCo :: Maven Plugin][51]                           | [EPL-2.0][52]                                 |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                              |
| [Git Commit Id Maven Plugin][53]                       | [GNU Lesser General Public License 3.0][54]   |
| [Apache Maven Clean Plugin][55]                        | [Apache-2.0][1]                               |
| [Apache Maven Resources Plugin][56]                    | [Apache-2.0][1]                               |
| [Apache Maven Install Plugin][57]                      | [Apache-2.0][1]                               |
| [Apache Maven Site Plugin][58]                         | [Apache-2.0][1]                               |

## Project Keeper Command Line Interface

### Compile Dependencies

| Dependency                 | License               |
| -------------------------- | --------------------- |
| [Project Keeper Core][59]  | [The MIT License][60] |
| [error-reporting-java][18] | [MIT License][19]     |
| [Maven Model][66]          | [Apache-2.0][1]       |

### Test Dependencies

| Dependency                             | License                           |
| -------------------------------------- | --------------------------------- |
| [Project Keeper shared test setup][59] | [The MIT License][60]             |
| [Maven Project Version Getter][69]     | [MIT License][70]                 |
| [JUnit Jupiter (Aggregator)][31]       | [Eclipse Public License v2.0][32] |
| [Hamcrest][33]                         | [BSD-3-Clause][34]                |

### Runtime Dependencies

| Dependency                 | License   |
| -------------------------- | --------- |
| [SLF4J API Module][29]     | [MIT][30] |
| [SLF4J JDK14 Provider][29] | [MIT][30] |

### Plugin Dependencies

| Dependency                                             | License                                       |
| ------------------------------------------------------ | --------------------------------------------- |
| [SonarQube Scanner for Maven][9]                       | [GNU LGPL 3][10]                              |
| [Apache Maven Toolchains Plugin][35]                   | [Apache-2.0][1]                               |
| [Apache Maven Compiler Plugin][36]                     | [Apache-2.0][1]                               |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                               |
| [Maven Flatten Plugin][37]                             | [Apache Software License][1]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                                     |
| [Maven Surefire Plugin][38]                            | [Apache-2.0][1]                               |
| [Versions Maven Plugin][39]                            | [Apache License, Version 2.0][1]              |
| [duplicate-finder-maven-plugin Maven Mojo][40]         | [Apache License 2.0][41]                      |
| [Apache Maven Artifact Plugin][42]                     | [Apache-2.0][1]                               |
| [Apache Maven Assembly Plugin][77]                     | [Apache-2.0][1]                               |
| [Apache Maven JAR Plugin][74]                          | [Apache-2.0][1]                               |
| [Artifact reference checker and unifier][78]           | [MIT License][79]                             |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                               |
| [Apache Maven Source Plugin][43]                       | [Apache-2.0][1]                               |
| [Apache Maven Javadoc Plugin][44]                      | [Apache-2.0][1]                               |
| [spdx-maven-plugin Maven Plugin][45]                   | [The Apache Software License, Version 2.0][8] |
| [Build Helper Maven Plugin][46]                        | [The MIT License][47]                         |
| [Apache Maven GPG Plugin][48]                          | [Apache-2.0][1]                               |
| [Central Publishing Maven Plugin][49]                  | [The Apache License, Version 2.0][1]          |
| [Maven Failsafe Plugin][76]                            | [Apache-2.0][1]                               |
| [JaCoCo :: Maven Plugin][51]                           | [EPL-2.0][52]                                 |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                              |
| [Git Commit Id Maven Plugin][53]                       | [GNU Lesser General Public License 3.0][54]   |
| [Apache Maven Clean Plugin][55]                        | [Apache-2.0][1]                               |
| [Apache Maven Resources Plugin][56]                    | [Apache-2.0][1]                               |
| [Apache Maven Install Plugin][57]                      | [Apache-2.0][1]                               |
| [Apache Maven Site Plugin][58]                         | [Apache-2.0][1]                               |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Core][59]                 | [The MIT License][60] |
| [Maven Plugin Tools Java Annotations][80] | [Apache-2.0][1]       |
| [Maven Plugin API][81]                    | [Apache-2.0][1]       |
| [Maven Core][82]                          | [Apache-2.0][1]       |
| [error-reporting-java][18]                | [MIT License][19]     |

### Test Dependencies

| Dependency                             | License                                       |
| -------------------------------------- | --------------------------------------------- |
| [Maven Project Version Getter][69]     | [MIT License][70]                             |
| [org.xmlunit:xmlunit-matchers][61]     | [The Apache Software License, Version 2.0][8] |
| [mockito-core][27]                     | [MIT][28]                                     |
| [Maven Plugin Integration Testing][71] | [MIT License][72]                             |
| [SLF4J JDK14 Provider][29]             | [MIT][30]                                     |
| [JaCoCo :: Agent][83]                  | [EPL-2.0][52]                                 |
| [JUnit Jupiter (Aggregator)][31]       | [Eclipse Public License v2.0][32]             |
| [Hamcrest][33]                         | [BSD-3-Clause][34]                            |

### Plugin Dependencies

| Dependency                                             | License                                       |
| ------------------------------------------------------ | --------------------------------------------- |
| [SonarQube Scanner for Maven][9]                       | [GNU LGPL 3][10]                              |
| [Apache Maven Toolchains Plugin][35]                   | [Apache-2.0][1]                               |
| [Maven Plugin Plugin][84]                              | [Apache-2.0][1]                               |
| [Apache Maven Compiler Plugin][36]                     | [Apache-2.0][1]                               |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                               |
| [Maven Flatten Plugin][37]                             | [Apache Software License][1]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                                     |
| [Maven Surefire Plugin][38]                            | [Apache-2.0][1]                               |
| [Versions Maven Plugin][39]                            | [Apache License, Version 2.0][1]              |
| [Apache Maven JAR Plugin][74]                          | [Apache-2.0][1]                               |
| [duplicate-finder-maven-plugin Maven Mojo][40]         | [Apache License 2.0][41]                      |
| [Apache Maven Artifact Plugin][42]                     | [Apache-2.0][1]                               |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                               |
| [Apache Maven Source Plugin][43]                       | [Apache-2.0][1]                               |
| [Apache Maven Javadoc Plugin][44]                      | [Apache-2.0][1]                               |
| [spdx-maven-plugin Maven Plugin][45]                   | [The Apache Software License, Version 2.0][8] |
| [Build Helper Maven Plugin][46]                        | [The MIT License][47]                         |
| [Apache Maven GPG Plugin][48]                          | [Apache-2.0][1]                               |
| [Central Publishing Maven Plugin][49]                  | [The Apache License, Version 2.0][1]          |
| [Apache Maven Dependency Plugin][50]                   | [Apache-2.0][1]                               |
| [Maven Failsafe Plugin][76]                            | [Apache-2.0][1]                               |
| [JaCoCo :: Maven Plugin][51]                           | [EPL-2.0][52]                                 |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                              |
| [Git Commit Id Maven Plugin][53]                       | [GNU Lesser General Public License 3.0][54]   |
| [Apache Maven Clean Plugin][55]                        | [Apache-2.0][1]                               |
| [Apache Maven Resources Plugin][56]                    | [Apache-2.0][1]                               |
| [Apache Maven Install Plugin][57]                      | [Apache-2.0][1]                               |
| [Apache Maven Site Plugin][58]                         | [Apache-2.0][1]                               |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper shared model classes][59] | [The MIT License][60] |
| [Maven Plugin Tools Java Annotations][80] | [Apache-2.0][1]       |
| [Maven Plugin API][81]                    | [Apache-2.0][1]       |
| [error-reporting-java][18]                | [MIT License][19]     |
| [JGit - Core][20]                         | [BSD-3-Clause][21]    |
| [semver4j][63]                            | [The MIT License][26] |
| [Maven Core][82]                          | [Apache-2.0][1]       |

### Test Dependencies

| Dependency                             | License                                       |
| -------------------------------------- | --------------------------------------------- |
| [Project Keeper shared test setup][59] | [The MIT License][60]                         |
| [Maven Project Version Getter][69]     | [MIT License][70]                             |
| [org.xmlunit:xmlunit-matchers][61]     | [The Apache Software License, Version 2.0][8] |
| [SLF4J JDK14 Provider][29]             | [MIT][30]                                     |
| [mockito-core][27]                     | [MIT][28]                                     |
| [mockito-junit-jupiter][27]            | [MIT][28]                                     |
| [Maven Plugin Integration Testing][71] | [MIT License][72]                             |
| [JaCoCo :: Agent][83]                  | [EPL-2.0][52]                                 |
| [JUnit Jupiter (Aggregator)][31]       | [Eclipse Public License v2.0][32]             |
| [Hamcrest][33]                         | [BSD-3-Clause][34]                            |

### Plugin Dependencies

| Dependency                                             | License                                       |
| ------------------------------------------------------ | --------------------------------------------- |
| [SonarQube Scanner for Maven][9]                       | [GNU LGPL 3][10]                              |
| [Apache Maven Toolchains Plugin][35]                   | [Apache-2.0][1]                               |
| [Apache Maven Compiler Plugin][36]                     | [Apache-2.0][1]                               |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                               |
| [Maven Flatten Plugin][37]                             | [Apache Software License][1]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                                     |
| [Maven Surefire Plugin][38]                            | [Apache-2.0][1]                               |
| [Versions Maven Plugin][39]                            | [Apache License, Version 2.0][1]              |
| [Maven Plugin Plugin][84]                              | [Apache-2.0][1]                               |
| [duplicate-finder-maven-plugin Maven Mojo][40]         | [Apache License 2.0][41]                      |
| [Apache Maven Artifact Plugin][42]                     | [Apache-2.0][1]                               |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                               |
| [Apache Maven Source Plugin][43]                       | [Apache-2.0][1]                               |
| [Apache Maven Javadoc Plugin][44]                      | [Apache-2.0][1]                               |
| [spdx-maven-plugin Maven Plugin][45]                   | [The Apache Software License, Version 2.0][8] |
| [Build Helper Maven Plugin][46]                        | [The MIT License][47]                         |
| [Apache Maven GPG Plugin][48]                          | [Apache-2.0][1]                               |
| [Central Publishing Maven Plugin][49]                  | [The Apache License, Version 2.0][1]          |
| [Apache Maven Dependency Plugin][50]                   | [Apache-2.0][1]                               |
| [Maven Failsafe Plugin][76]                            | [Apache-2.0][1]                               |
| [JaCoCo :: Maven Plugin][51]                           | [EPL-2.0][52]                                 |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                              |
| [Git Commit Id Maven Plugin][53]                       | [GNU Lesser General Public License 3.0][54]   |
| [Apache Maven Clean Plugin][55]                        | [Apache-2.0][1]                               |
| [Apache Maven Resources Plugin][56]                    | [Apache-2.0][1]                               |
| [Apache Maven Install Plugin][57]                      | [Apache-2.0][1]                               |
| [Apache Maven Site Plugin][58]                         | [Apache-2.0][1]                               |

## Project Keeper Shared Test Setup

### Compile Dependencies

| Dependency                                | License                          |
| ----------------------------------------- | -------------------------------- |
| [Project Keeper shared model classes][59] | [The MIT License][60]            |
| [SnakeYAML][64]                           | [Apache License, Version 2.0][8] |
| [Hamcrest][33]                            | [BSD-3-Clause][34]               |
| [Maven Model][66]                         | [Apache-2.0][1]                  |

### Test Dependencies

| Dependency                       | License                           |
| -------------------------------- | --------------------------------- |
| [JUnit Jupiter (Aggregator)][31] | [Eclipse Public License v2.0][32] |

### Plugin Dependencies

| Dependency                                             | License                                     |
| ------------------------------------------------------ | ------------------------------------------- |
| [SonarQube Scanner for Maven][9]                       | [GNU LGPL 3][10]                            |
| [Apache Maven Toolchains Plugin][35]                   | [Apache-2.0][1]                             |
| [Apache Maven Javadoc Plugin][44]                      | [Apache-2.0][1]                             |
| [Apache Maven Compiler Plugin][36]                     | [Apache-2.0][1]                             |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                             |
| [Maven Flatten Plugin][37]                             | [Apache Software License][1]                |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                                   |
| [Maven Surefire Plugin][38]                            | [Apache-2.0][1]                             |
| [Versions Maven Plugin][39]                            | [Apache License, Version 2.0][1]            |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                             |
| [duplicate-finder-maven-plugin Maven Mojo][40]         | [Apache License 2.0][41]                    |
| [Apache Maven Artifact Plugin][42]                     | [Apache-2.0][1]                             |
| [JaCoCo :: Maven Plugin][51]                           | [EPL-2.0][52]                               |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                            |
| [Git Commit Id Maven Plugin][53]                       | [GNU Lesser General Public License 3.0][54] |
| [Apache Maven Clean Plugin][55]                        | [Apache-2.0][1]                             |
| [Apache Maven Resources Plugin][56]                    | [Apache-2.0][1]                             |
| [Apache Maven Install Plugin][57]                      | [Apache-2.0][1]                             |
| [Apache Maven Site Plugin][58]                         | [Apache-2.0][1]                             |

[0]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[1]: https://www.apache.org/licenses/LICENSE-2.0.txt
[2]: https://github.com/itsallcode/openfasttrace-maven-plugin
[3]: https://www.gnu.org/licenses/gpl-3.0.html
[4]: https://maven.apache.org/plugins/maven-deploy-plugin/
[5]: https://github.com/exasol/error-code-crawler-maven-plugin/
[6]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[7]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[8]: http://www.apache.org/licenses/LICENSE-2.0.txt
[9]: https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/developing-a-plugin/plugin-basics/sonar-scanner-maven/sonar-maven-plugin/
[10]: http://www.gnu.org/licenses/lgpl.txt
[11]: https://github.com/eclipse-ee4j/jsonp
[12]: https://projects.eclipse.org/license/epl-2.0
[13]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[14]: https://projects.eclipse.org/projects/ee4j.jsonb/jakarta.json.bind-api
[15]: https://projects.eclipse.org/projects/ee4j/yasson
[16]: https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.txt
[17]: https://www.gnu.org/software/classpath/license.html
[18]: https://github.com/exasol/error-reporting-java/
[19]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[20]: https://www.eclipse.org/jgit/
[21]: https://www.eclipse.org/org/documents/edl-v10.php
[22]: https://github.com/itsallcode/junit5-system-extensions
[23]: http://www.eclipse.org/legal/epl-v20.html
[24]: https://www.jqno.nl/equalsverifier
[25]: https://github.com/jparams/to-string-verifier
[26]: http://www.opensource.org/licenses/mit-license.php
[27]: https://github.com/mockito/mockito
[28]: https://opensource.org/licenses/MIT
[29]: http://www.slf4j.org
[30]: https://opensource.org/license/mit
[31]: https://junit.org/
[32]: https://www.eclipse.org/legal/epl-v20.html
[33]: http://hamcrest.org/JavaHamcrest/
[34]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[35]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[36]: https://maven.apache.org/plugins/maven-compiler-plugin/
[37]: https://www.mojohaus.org/flatten-maven-plugin/
[38]: https://maven.apache.org/surefire/maven-surefire-plugin/
[39]: https://www.mojohaus.org/versions/versions-maven-plugin/
[40]: https://basepom.github.io/duplicate-finder-maven-plugin
[41]: http://www.apache.org/licenses/LICENSE-2.0.html
[42]: https://maven.apache.org/plugins/maven-artifact-plugin/
[43]: https://maven.apache.org/plugins/maven-source-plugin/
[44]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[45]: https://github.com/spdx/spdx-maven-plugin
[46]: https://www.mojohaus.org/build-helper-maven-plugin/
[47]: https://spdx.org/licenses/MIT.txt
[48]: https://maven.apache.org/plugins/maven-gpg-plugin/
[49]: https://central.sonatype.org
[50]: https://maven.apache.org/plugins/maven-dependency-plugin/
[51]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[52]: https://www.eclipse.org/legal/epl-2.0/
[53]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[54]: http://www.gnu.org/licenses/lgpl-3.0.txt
[55]: https://maven.apache.org/plugins/maven-clean-plugin/
[56]: https://maven.apache.org/plugins/maven-resources-plugin/
[57]: https://maven.apache.org/plugins/maven-install-plugin/
[58]: https://maven.apache.org/plugins/maven-site-plugin/
[59]: https://github.com/exasol/project-keeper/
[60]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[61]: https://www.xmlunit.org/
[62]: https://github.com/Steppschuh/Java-Markdown-Generator
[63]: https://github.com/vdurmont/semver4j
[64]: https://codeberg.org/snakeyaml/snakeyaml
[65]: https://codeberg.org/snakeyaml/snakeyaml-engine
[66]: https://maven.apache.org/ref/3.9.16/maven-model/
[67]: https://www.jcabi.com/jcabi-github
[68]: https://www.jcabi.com/LICENSE.txt
[69]: https://github.com/exasol/maven-project-version-getter/
[70]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[71]: https://github.com/exasol/maven-plugin-integration-testing/
[72]: https://github.com/exasol/maven-plugin-integration-testing/blob/main/LICENSE
[73]: https://junit-pioneer.org/
[74]: https://maven.apache.org/plugins/maven-jar-plugin/
[75]: https://www.mojohaus.org/exec-maven-plugin
[76]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[77]: https://maven.apache.org/plugins/maven-assembly-plugin/
[78]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[79]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[80]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[81]: https://maven.apache.org/ref/3.9.16/maven-plugin-api/
[82]: https://maven.apache.org/ref/3.9.16/maven-core/
[83]: https://www.eclemma.org/jacoco/index.html
[84]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
