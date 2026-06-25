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

## Project Keeper Shared Model Classes

### Compile Dependencies

| Dependency                       | License                                                                                                        |
| -------------------------------- | -------------------------------------------------------------------------------------------------------------- |
| [Jakarta JSON Processing API][9] | [Eclipse Public License 2.0][10]; [GNU General Public License, version 2 with the GNU Classpath Exception][11] |
| [Jakarta JSON Binding API][12]   | [Eclipse Public License 2.0][10]; [GNU General Public License, version 2 with the GNU Classpath Exception][11] |
| [Yasson][13]                     | [Eclipse Public License v. 2.0][14]; [Eclipse Distribution License v. 1.0][15]                                 |
| [error-reporting-java][16]       | [MIT License][17]                                                                                              |
| [JGit - Core][18]                | [BSD-3-Clause][19]                                                                                             |

### Test Dependencies

| Dependency                                 | License                           |
| ------------------------------------------ | --------------------------------- |
| [JUnit5 System Extensions][20]             | [Eclipse Public License v2.0][14] |
| [EqualsVerifier \| release normal jar][21] | [Apache License, Version 2.0][1]  |
| [to-string-verifier][22]                   | [MIT License][23]                 |
| [mockito-core][24]                         | [MIT][25]                         |
| [SLF4J JDK14 Provider][26]                 | [MIT][27]                         |
| [JUnit Jupiter (Aggregator)][28]           | [Eclipse Public License v2.0][29] |
| [Hamcrest][30]                             | [BSD-3-Clause][31]                |

### Plugin Dependencies

| Dependency                                             | License                                       |
| ------------------------------------------------------ | --------------------------------------------- |
| [SonarQube Scanner for Maven][32]                      | [GNU LGPL 3][33]                              |
| [Apache Maven Toolchains Plugin][34]                   | [Apache-2.0][1]                               |
| [Apache Maven Compiler Plugin][35]                     | [Apache-2.0][1]                               |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                               |
| [Maven Flatten Plugin][36]                             | [Apache Software License][1]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                                     |
| [Maven Surefire Plugin][37]                            | [Apache-2.0][1]                               |
| [Versions Maven Plugin][38]                            | [Apache License, Version 2.0][1]              |
| [duplicate-finder-maven-plugin Maven Mojo][39]         | [Apache License 2.0][40]                      |
| [Apache Maven Artifact Plugin][41]                     | [Apache-2.0][1]                               |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                               |
| [Apache Maven Source Plugin][42]                       | [Apache-2.0][1]                               |
| [Apache Maven Javadoc Plugin][43]                      | [Apache-2.0][1]                               |
| [spdx-maven-plugin Maven Plugin][44]                   | [The Apache Software License, Version 2.0][8] |
| [Build Helper Maven Plugin][45]                        | [The MIT License][46]                         |
| [Apache Maven GPG Plugin][47]                          | [Apache-2.0][1]                               |
| [Central Publishing Maven Plugin][48]                  | [The Apache License, Version 2.0][1]          |
| [Apache Maven Dependency Plugin][49]                   | [Apache-2.0][1]                               |
| [JaCoCo :: Maven Plugin][50]                           | [EPL-2.0][51]                                 |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                              |
| [Git Commit Id Maven Plugin][52]                       | [GNU Lesser General Public License 3.0][53]   |
| [Apache Maven Clean Plugin][54]                        | [Apache-2.0][1]                               |
| [Apache Maven Resources Plugin][55]                    | [Apache-2.0][1]                               |
| [Apache Maven Install Plugin][56]                      | [Apache-2.0][1]                               |
| [Apache Maven Site Plugin][57]                         | [Apache-2.0][1]                               |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                       |
| ----------------------------------------- | --------------------------------------------- |
| [Project Keeper shared model classes][58] | [The MIT License][59]                         |
| [org.xmlunit:xmlunit-core][60]            | [The Apache Software License, Version 2.0][8] |
| [error-reporting-java][16]                | [MIT License][17]                             |
| [Markdown Generator][61]                  | [The Apache Software License, Version 2.0][8] |
| [semver4j][62]                            | [The MIT License][23]                         |
| [SnakeYAML][63]                           | [Apache License, Version 2.0][8]              |
| [SnakeYAML Engine][64]                    | [Apache License, Version 2.0][8]              |
| [Maven Model][65]                         | [Apache-2.0][1]                               |
| [jcabi-github][66]                        | [3-Clause BSD License][67]                    |

### Test Dependencies

| Dependency                                 | License                                       |
| ------------------------------------------ | --------------------------------------------- |
| [Project Keeper shared test setup][58]     | [The MIT License][59]                         |
| [Maven Project Version Getter][68]         | [MIT License][69]                             |
| [org.xmlunit:xmlunit-matchers][60]         | [The Apache Software License, Version 2.0][8] |
| [mockito-junit-jupiter][24]                | [MIT][25]                                     |
| [Maven Plugin Integration Testing][70]     | [MIT License][71]                             |
| [EqualsVerifier \| release normal jar][21] | [Apache License, Version 2.0][1]              |
| [to-string-verifier][22]                   | [MIT License][23]                             |
| [junit-pioneer][72]                        | [Eclipse Public License v2.0][29]             |
| [SLF4J JDK14 Provider][26]                 | [MIT][27]                                     |
| [JUnit Jupiter (Aggregator)][28]           | [Eclipse Public License v2.0][29]             |
| [Hamcrest][30]                             | [BSD-3-Clause][31]                            |

### Runtime Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Java project crawler][58] | [The MIT License][59] |

### Plugin Dependencies

| Dependency                                             | License                                       |
| ------------------------------------------------------ | --------------------------------------------- |
| [SonarQube Scanner for Maven][32]                      | [GNU LGPL 3][33]                              |
| [Apache Maven Toolchains Plugin][34]                   | [Apache-2.0][1]                               |
| [Apache Maven JAR Plugin][73]                          | [Apache-2.0][1]                               |
| [Apache Maven Compiler Plugin][35]                     | [Apache-2.0][1]                               |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                               |
| [Maven Flatten Plugin][36]                             | [Apache Software License][1]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                                     |
| [Maven Surefire Plugin][37]                            | [Apache-2.0][1]                               |
| [Versions Maven Plugin][38]                            | [Apache License, Version 2.0][1]              |
| [Exec Maven Plugin][74]                                | [Apache License 2][1]                         |
| [duplicate-finder-maven-plugin Maven Mojo][39]         | [Apache License 2.0][40]                      |
| [Apache Maven Artifact Plugin][41]                     | [Apache-2.0][1]                               |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                               |
| [Apache Maven Source Plugin][42]                       | [Apache-2.0][1]                               |
| [Apache Maven Javadoc Plugin][43]                      | [Apache-2.0][1]                               |
| [spdx-maven-plugin Maven Plugin][44]                   | [The Apache Software License, Version 2.0][8] |
| [Build Helper Maven Plugin][45]                        | [The MIT License][46]                         |
| [Apache Maven GPG Plugin][47]                          | [Apache-2.0][1]                               |
| [Central Publishing Maven Plugin][48]                  | [The Apache License, Version 2.0][1]          |
| [Apache Maven Dependency Plugin][49]                   | [Apache-2.0][1]                               |
| [Maven Failsafe Plugin][75]                            | [Apache-2.0][1]                               |
| [JaCoCo :: Maven Plugin][50]                           | [EPL-2.0][51]                                 |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                              |
| [Git Commit Id Maven Plugin][52]                       | [GNU Lesser General Public License 3.0][53]   |
| [Apache Maven Clean Plugin][54]                        | [Apache-2.0][1]                               |
| [Apache Maven Resources Plugin][55]                    | [Apache-2.0][1]                               |
| [Apache Maven Install Plugin][56]                      | [Apache-2.0][1]                               |
| [Apache Maven Site Plugin][57]                         | [Apache-2.0][1]                               |

## Project Keeper Command Line Interface

### Compile Dependencies

| Dependency                 | License               |
| -------------------------- | --------------------- |
| [Project Keeper Core][58]  | [The MIT License][59] |
| [error-reporting-java][16] | [MIT License][17]     |
| [Maven Model][65]          | [Apache-2.0][1]       |

### Test Dependencies

| Dependency                             | License                           |
| -------------------------------------- | --------------------------------- |
| [Project Keeper shared test setup][58] | [The MIT License][59]             |
| [Maven Project Version Getter][68]     | [MIT License][69]                 |
| [JUnit Jupiter (Aggregator)][28]       | [Eclipse Public License v2.0][29] |
| [Hamcrest][30]                         | [BSD-3-Clause][31]                |

### Runtime Dependencies

| Dependency                 | License   |
| -------------------------- | --------- |
| [SLF4J API Module][26]     | [MIT][27] |
| [SLF4J JDK14 Provider][26] | [MIT][27] |

### Plugin Dependencies

| Dependency                                             | License                                       |
| ------------------------------------------------------ | --------------------------------------------- |
| [SonarQube Scanner for Maven][32]                      | [GNU LGPL 3][33]                              |
| [Apache Maven Toolchains Plugin][34]                   | [Apache-2.0][1]                               |
| [Apache Maven Compiler Plugin][35]                     | [Apache-2.0][1]                               |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                               |
| [Maven Flatten Plugin][36]                             | [Apache Software License][1]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                                     |
| [Maven Surefire Plugin][37]                            | [Apache-2.0][1]                               |
| [Versions Maven Plugin][38]                            | [Apache License, Version 2.0][1]              |
| [duplicate-finder-maven-plugin Maven Mojo][39]         | [Apache License 2.0][40]                      |
| [Apache Maven Artifact Plugin][41]                     | [Apache-2.0][1]                               |
| [Apache Maven Assembly Plugin][76]                     | [Apache-2.0][1]                               |
| [Apache Maven JAR Plugin][73]                          | [Apache-2.0][1]                               |
| [Artifact reference checker and unifier][77]           | [MIT License][78]                             |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                               |
| [Apache Maven Source Plugin][42]                       | [Apache-2.0][1]                               |
| [Apache Maven Javadoc Plugin][43]                      | [Apache-2.0][1]                               |
| [spdx-maven-plugin Maven Plugin][44]                   | [The Apache Software License, Version 2.0][8] |
| [Build Helper Maven Plugin][45]                        | [The MIT License][46]                         |
| [Apache Maven GPG Plugin][47]                          | [Apache-2.0][1]                               |
| [Central Publishing Maven Plugin][48]                  | [The Apache License, Version 2.0][1]          |
| [Maven Failsafe Plugin][75]                            | [Apache-2.0][1]                               |
| [JaCoCo :: Maven Plugin][50]                           | [EPL-2.0][51]                                 |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                              |
| [Git Commit Id Maven Plugin][52]                       | [GNU Lesser General Public License 3.0][53]   |
| [Apache Maven Clean Plugin][54]                        | [Apache-2.0][1]                               |
| [Apache Maven Resources Plugin][55]                    | [Apache-2.0][1]                               |
| [Apache Maven Install Plugin][56]                      | [Apache-2.0][1]                               |
| [Apache Maven Site Plugin][57]                         | [Apache-2.0][1]                               |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Core][58]                 | [The MIT License][59] |
| [Maven Plugin Tools Java Annotations][79] | [Apache-2.0][1]       |
| [Maven Plugin API][80]                    | [Apache-2.0][1]       |
| [Maven Core][81]                          | [Apache-2.0][1]       |
| [error-reporting-java][16]                | [MIT License][17]     |

### Test Dependencies

| Dependency                             | License                                       |
| -------------------------------------- | --------------------------------------------- |
| [Maven Project Version Getter][68]     | [MIT License][69]                             |
| [org.xmlunit:xmlunit-matchers][60]     | [The Apache Software License, Version 2.0][8] |
| [mockito-core][24]                     | [MIT][25]                                     |
| [Maven Plugin Integration Testing][70] | [MIT License][71]                             |
| [SLF4J JDK14 Provider][26]             | [MIT][27]                                     |
| [JaCoCo :: Agent][82]                  | [EPL-2.0][51]                                 |
| [JUnit Jupiter (Aggregator)][28]       | [Eclipse Public License v2.0][29]             |
| [Hamcrest][30]                         | [BSD-3-Clause][31]                            |

### Plugin Dependencies

| Dependency                                             | License                                       |
| ------------------------------------------------------ | --------------------------------------------- |
| [SonarQube Scanner for Maven][32]                      | [GNU LGPL 3][33]                              |
| [Apache Maven Toolchains Plugin][34]                   | [Apache-2.0][1]                               |
| [Maven Plugin Plugin][83]                              | [Apache-2.0][1]                               |
| [Apache Maven Compiler Plugin][35]                     | [Apache-2.0][1]                               |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                               |
| [Maven Flatten Plugin][36]                             | [Apache Software License][1]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                                     |
| [Maven Surefire Plugin][37]                            | [Apache-2.0][1]                               |
| [Versions Maven Plugin][38]                            | [Apache License, Version 2.0][1]              |
| [Apache Maven JAR Plugin][73]                          | [Apache-2.0][1]                               |
| [duplicate-finder-maven-plugin Maven Mojo][39]         | [Apache License 2.0][40]                      |
| [Apache Maven Artifact Plugin][41]                     | [Apache-2.0][1]                               |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                               |
| [Apache Maven Source Plugin][42]                       | [Apache-2.0][1]                               |
| [Apache Maven Javadoc Plugin][43]                      | [Apache-2.0][1]                               |
| [spdx-maven-plugin Maven Plugin][44]                   | [The Apache Software License, Version 2.0][8] |
| [Build Helper Maven Plugin][45]                        | [The MIT License][46]                         |
| [Apache Maven GPG Plugin][47]                          | [Apache-2.0][1]                               |
| [Central Publishing Maven Plugin][48]                  | [The Apache License, Version 2.0][1]          |
| [Apache Maven Dependency Plugin][49]                   | [Apache-2.0][1]                               |
| [Maven Failsafe Plugin][75]                            | [Apache-2.0][1]                               |
| [JaCoCo :: Maven Plugin][50]                           | [EPL-2.0][51]                                 |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                              |
| [Git Commit Id Maven Plugin][52]                       | [GNU Lesser General Public License 3.0][53]   |
| [Apache Maven Clean Plugin][54]                        | [Apache-2.0][1]                               |
| [Apache Maven Resources Plugin][55]                    | [Apache-2.0][1]                               |
| [Apache Maven Install Plugin][56]                      | [Apache-2.0][1]                               |
| [Apache Maven Site Plugin][57]                         | [Apache-2.0][1]                               |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper shared model classes][58] | [The MIT License][59] |
| [Maven Plugin Tools Java Annotations][79] | [Apache-2.0][1]       |
| [Maven Plugin API][80]                    | [Apache-2.0][1]       |
| [error-reporting-java][16]                | [MIT License][17]     |
| [JGit - Core][18]                         | [BSD-3-Clause][19]    |
| [semver4j][62]                            | [The MIT License][23] |
| [Maven Core][81]                          | [Apache-2.0][1]       |

### Test Dependencies

| Dependency                             | License                                       |
| -------------------------------------- | --------------------------------------------- |
| [Project Keeper shared test setup][58] | [The MIT License][59]                         |
| [Maven Project Version Getter][68]     | [MIT License][69]                             |
| [org.xmlunit:xmlunit-matchers][60]     | [The Apache Software License, Version 2.0][8] |
| [SLF4J JDK14 Provider][26]             | [MIT][27]                                     |
| [mockito-core][24]                     | [MIT][25]                                     |
| [mockito-junit-jupiter][24]            | [MIT][25]                                     |
| [Maven Plugin Integration Testing][70] | [MIT License][71]                             |
| [JaCoCo :: Agent][82]                  | [EPL-2.0][51]                                 |
| [JUnit Jupiter (Aggregator)][28]       | [Eclipse Public License v2.0][29]             |
| [Hamcrest][30]                         | [BSD-3-Clause][31]                            |

### Plugin Dependencies

| Dependency                                             | License                                       |
| ------------------------------------------------------ | --------------------------------------------- |
| [SonarQube Scanner for Maven][32]                      | [GNU LGPL 3][33]                              |
| [Apache Maven Toolchains Plugin][34]                   | [Apache-2.0][1]                               |
| [Apache Maven Compiler Plugin][35]                     | [Apache-2.0][1]                               |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                               |
| [Maven Flatten Plugin][36]                             | [Apache Software License][1]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                                     |
| [Maven Surefire Plugin][37]                            | [Apache-2.0][1]                               |
| [Versions Maven Plugin][38]                            | [Apache License, Version 2.0][1]              |
| [Maven Plugin Plugin][83]                              | [Apache-2.0][1]                               |
| [duplicate-finder-maven-plugin Maven Mojo][39]         | [Apache License 2.0][40]                      |
| [Apache Maven Artifact Plugin][41]                     | [Apache-2.0][1]                               |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                               |
| [Apache Maven Source Plugin][42]                       | [Apache-2.0][1]                               |
| [Apache Maven Javadoc Plugin][43]                      | [Apache-2.0][1]                               |
| [spdx-maven-plugin Maven Plugin][44]                   | [The Apache Software License, Version 2.0][8] |
| [Build Helper Maven Plugin][45]                        | [The MIT License][46]                         |
| [Apache Maven GPG Plugin][47]                          | [Apache-2.0][1]                               |
| [Central Publishing Maven Plugin][48]                  | [The Apache License, Version 2.0][1]          |
| [Apache Maven Dependency Plugin][49]                   | [Apache-2.0][1]                               |
| [Maven Failsafe Plugin][75]                            | [Apache-2.0][1]                               |
| [JaCoCo :: Maven Plugin][50]                           | [EPL-2.0][51]                                 |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                              |
| [Git Commit Id Maven Plugin][52]                       | [GNU Lesser General Public License 3.0][53]   |
| [Apache Maven Clean Plugin][54]                        | [Apache-2.0][1]                               |
| [Apache Maven Resources Plugin][55]                    | [Apache-2.0][1]                               |
| [Apache Maven Install Plugin][56]                      | [Apache-2.0][1]                               |
| [Apache Maven Site Plugin][57]                         | [Apache-2.0][1]                               |

## Project Keeper Shared Test Setup

### Compile Dependencies

| Dependency                                | License                          |
| ----------------------------------------- | -------------------------------- |
| [Project Keeper shared model classes][58] | [The MIT License][59]            |
| [SnakeYAML][63]                           | [Apache License, Version 2.0][8] |
| [Hamcrest][30]                            | [BSD-3-Clause][31]               |
| [Maven Model][65]                         | [Apache-2.0][1]                  |

### Test Dependencies

| Dependency                       | License                           |
| -------------------------------- | --------------------------------- |
| [JUnit Jupiter (Aggregator)][28] | [Eclipse Public License v2.0][29] |

### Plugin Dependencies

| Dependency                                             | License                                     |
| ------------------------------------------------------ | ------------------------------------------- |
| [SonarQube Scanner for Maven][32]                      | [GNU LGPL 3][33]                            |
| [Apache Maven Toolchains Plugin][34]                   | [Apache-2.0][1]                             |
| [Apache Maven Javadoc Plugin][43]                      | [Apache-2.0][1]                             |
| [Apache Maven Compiler Plugin][35]                     | [Apache-2.0][1]                             |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                             |
| [Maven Flatten Plugin][36]                             | [Apache Software License][1]                |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                                   |
| [Maven Surefire Plugin][37]                            | [Apache-2.0][1]                             |
| [Versions Maven Plugin][38]                            | [Apache License, Version 2.0][1]            |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                             |
| [duplicate-finder-maven-plugin Maven Mojo][39]         | [Apache License 2.0][40]                    |
| [Apache Maven Artifact Plugin][41]                     | [Apache-2.0][1]                             |
| [JaCoCo :: Maven Plugin][50]                           | [EPL-2.0][51]                               |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                            |
| [Git Commit Id Maven Plugin][52]                       | [GNU Lesser General Public License 3.0][53] |
| [Apache Maven Clean Plugin][54]                        | [Apache-2.0][1]                             |
| [Apache Maven Resources Plugin][55]                    | [Apache-2.0][1]                             |
| [Apache Maven Install Plugin][56]                      | [Apache-2.0][1]                             |
| [Apache Maven Site Plugin][57]                         | [Apache-2.0][1]                             |

[0]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[1]: https://www.apache.org/licenses/LICENSE-2.0.txt
[2]: https://github.com/itsallcode/openfasttrace-maven-plugin
[3]: https://www.gnu.org/licenses/gpl-3.0.html
[4]: https://maven.apache.org/plugins/maven-deploy-plugin/
[5]: https://github.com/exasol/error-code-crawler-maven-plugin/
[6]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[7]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[8]: http://www.apache.org/licenses/LICENSE-2.0.txt
[9]: https://github.com/eclipse-ee4j/jsonp
[10]: https://projects.eclipse.org/license/epl-2.0
[11]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[12]: https://projects.eclipse.org/projects/ee4j.jsonb/jakarta.json.bind-api
[13]: https://projects.eclipse.org/projects/ee4j.yasson
[14]: http://www.eclipse.org/legal/epl-v20.html
[15]: http://www.eclipse.org/org/documents/edl-v10.php
[16]: https://github.com/exasol/error-reporting-java/
[17]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[18]: https://www.eclipse.org/jgit/
[19]: https://www.eclipse.org/org/documents/edl-v10.php
[20]: https://github.com/itsallcode/junit5-system-extensions
[21]: https://www.jqno.nl/equalsverifier
[22]: https://github.com/jparams/to-string-verifier
[23]: http://www.opensource.org/licenses/mit-license.php
[24]: https://github.com/mockito/mockito
[25]: https://opensource.org/licenses/MIT
[26]: http://www.slf4j.org
[27]: https://opensource.org/license/mit
[28]: https://junit.org/
[29]: https://www.eclipse.org/legal/epl-v20.html
[30]: http://hamcrest.org/JavaHamcrest/
[31]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[32]: https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/developing-a-plugin/plugin-basics/sonar-scanner-maven/sonar-maven-plugin/
[33]: http://www.gnu.org/licenses/lgpl.txt
[34]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[35]: https://maven.apache.org/plugins/maven-compiler-plugin/
[36]: https://www.mojohaus.org/flatten-maven-plugin/
[37]: https://maven.apache.org/surefire/maven-surefire-plugin/
[38]: https://www.mojohaus.org/versions/versions-maven-plugin/
[39]: https://basepom.github.io/duplicate-finder-maven-plugin
[40]: http://www.apache.org/licenses/LICENSE-2.0.html
[41]: https://maven.apache.org/plugins/maven-artifact-plugin/
[42]: https://maven.apache.org/plugins/maven-source-plugin/
[43]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[44]: https://github.com/spdx/spdx-maven-plugin
[45]: https://www.mojohaus.org/build-helper-maven-plugin/
[46]: https://spdx.org/licenses/MIT.txt
[47]: https://maven.apache.org/plugins/maven-gpg-plugin/
[48]: https://central.sonatype.org
[49]: https://maven.apache.org/plugins/maven-dependency-plugin/
[50]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[51]: https://www.eclipse.org/legal/epl-2.0/
[52]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[53]: http://www.gnu.org/licenses/lgpl-3.0.txt
[54]: https://maven.apache.org/plugins/maven-clean-plugin/
[55]: https://maven.apache.org/plugins/maven-resources-plugin/
[56]: https://maven.apache.org/plugins/maven-install-plugin/
[57]: https://maven.apache.org/plugins/maven-site-plugin/
[58]: https://github.com/exasol/project-keeper/
[59]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[60]: https://www.xmlunit.org/
[61]: https://github.com/Steppschuh/Java-Markdown-Generator
[62]: https://github.com/vdurmont/semver4j
[63]: https://bitbucket.org/snakeyaml/snakeyaml
[64]: https://bitbucket.org/snakeyaml/snakeyaml-engine
[65]: https://maven.apache.org/ref/3.9.16/maven-model/
[66]: https://www.jcabi.com/jcabi-github
[67]: https://www.jcabi.com/LICENSE.txt
[68]: https://github.com/exasol/maven-project-version-getter/
[69]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[70]: https://github.com/exasol/maven-plugin-integration-testing/
[71]: https://github.com/exasol/maven-plugin-integration-testing/blob/main/LICENSE
[72]: https://junit-pioneer.org/
[73]: https://maven.apache.org/plugins/maven-jar-plugin/
[74]: https://www.mojohaus.org/exec-maven-plugin
[75]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[76]: https://maven.apache.org/plugins/maven-assembly-plugin/
[77]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[78]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[79]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[80]: https://maven.apache.org/ref/3.9.16/maven-plugin-api/
[81]: https://maven.apache.org/ref/3.9.16/maven-core/
[82]: https://www.eclemma.org/jacoco/index.html
[83]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
