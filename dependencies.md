<!-- @formatter:off -->
# Dependencies

## Project Keeper Root Project

### Plugin Dependencies

| Dependency                                             | License                              |
| ------------------------------------------------------ | ------------------------------------ |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                      |
| [Central Publishing Maven Plugin][2]                   | [The Apache License, Version 2.0][1] |
| [OpenFastTrace Maven Plugin][3]                        | [GNU General Public License v3.0][4] |
| [Apache Maven Deploy Plugin][5]                        | [Apache-2.0][1]                      |
| [error-code-crawler-maven-plugin][6]                   | [MIT License][7]                     |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][8] | [ASL2][9]                            |

## Project Keeper Shared Model Classes

### Compile Dependencies

| Dependency                        | License                                                                                                        |
| --------------------------------- | -------------------------------------------------------------------------------------------------------------- |
| [Jakarta JSON Processing API][10] | [Eclipse Public License 2.0][11]; [GNU General Public License, version 2 with the GNU Classpath Exception][12] |
| [JSON-B API][13]                  | [Eclipse Public License 2.0][11]; [GNU General Public License, version 2 with the GNU Classpath Exception][12] |
| [Yasson][14]                      | [Eclipse Public License v. 2.0][15]; [Eclipse Distribution License v. 1.0][16]                                 |
| [error-reporting-java][17]        | [MIT License][18]                                                                                              |
| [JGit - Core][19]                 | [BSD-3-Clause][20]                                                                                             |

### Test Dependencies

| Dependency                                 | License                           |
| ------------------------------------------ | --------------------------------- |
| [JUnit Jupiter Params][21]                 | [Eclipse Public License v2.0][22] |
| [Hamcrest][23]                             | [BSD-3-Clause][24]                |
| [JUnit5 System Extensions][25]             | [Eclipse Public License v2.0][15] |
| [EqualsVerifier \| release normal jar][26] | [Apache License, Version 2.0][1]  |
| [to-string-verifier][27]                   | [MIT License][28]                 |
| [mockito-core][29]                         | [MIT][30]                         |
| [SLF4J JDK14 Provider][31]                 | [MIT][32]                         |

### Plugin Dependencies

| Dependency                                             | License                                     |
| ------------------------------------------------------ | ------------------------------------------- |
| [Apache Maven Clean Plugin][33]                        | [Apache-2.0][1]                             |
| [Apache Maven Install Plugin][34]                      | [Apache-2.0][1]                             |
| [Apache Maven Resources Plugin][35]                    | [Apache-2.0][1]                             |
| [Apache Maven Site Plugin][36]                         | [Apache-2.0][1]                             |
| [SonarQube Scanner for Maven][37]                      | [GNU LGPL 3][38]                            |
| [Apache Maven Toolchains Plugin][39]                   | [Apache-2.0][1]                             |
| [Apache Maven Compiler Plugin][40]                     | [Apache-2.0][1]                             |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                             |
| [Maven Flatten Plugin][41]                             | [Apache Software Licenese][1]               |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][8] | [ASL2][9]                                   |
| [Maven Surefire Plugin][42]                            | [Apache-2.0][1]                             |
| [Versions Maven Plugin][43]                            | [Apache License, Version 2.0][1]            |
| [duplicate-finder-maven-plugin Maven Mojo][44]         | [Apache License 2.0][45]                    |
| [Apache Maven Artifact Plugin][46]                     | [Apache-2.0][1]                             |
| [Apache Maven Deploy Plugin][5]                        | [Apache-2.0][1]                             |
| [Apache Maven GPG Plugin][47]                          | [Apache-2.0][1]                             |
| [Apache Maven Source Plugin][48]                       | [Apache License, Version 2.0][1]            |
| [Apache Maven Javadoc Plugin][49]                      | [Apache-2.0][1]                             |
| [Central Publishing Maven Plugin][2]                   | [The Apache License, Version 2.0][1]        |
| [JaCoCo :: Maven Plugin][50]                           | [EPL-2.0][51]                               |
| [Quality Summarizer Maven Plugin][52]                  | [MIT License][53]                           |
| [error-code-crawler-maven-plugin][6]                   | [MIT License][7]                            |
| [Git Commit Id Maven Plugin][54]                       | [GNU Lesser General Public License 3.0][55] |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                       |
| ----------------------------------------- | --------------------------------------------- |
| [Project Keeper shared model classes][56] | [The MIT License][57]                         |
| [org.xmlunit:xmlunit-core][58]            | [The Apache Software License, Version 2.0][9] |
| [error-reporting-java][17]                | [MIT License][18]                             |
| [Markdown Generator][59]                  | [The Apache Software License, Version 2.0][9] |
| [semver4j][60]                            | [The MIT License][28]                         |
| [SnakeYAML][61]                           | [Apache License, Version 2.0][9]              |
| [SnakeYAML Engine][62]                    | [Apache License, Version 2.0][9]              |
| [Maven Model][63]                         | [Apache-2.0][1]                               |
| [jcabi-github][64]                        | [BSD][65]                                     |

### Test Dependencies

| Dependency                                 | License                                       |
| ------------------------------------------ | --------------------------------------------- |
| [Project Keeper shared test setup][56]     | [The MIT License][57]                         |
| [Maven Project Version Getter][66]         | [MIT License][67]                             |
| [JUnit Jupiter Params][21]                 | [Eclipse Public License v2.0][22]             |
| [Hamcrest][23]                             | [BSD-3-Clause][24]                            |
| [org.xmlunit:xmlunit-matchers][58]         | [The Apache Software License, Version 2.0][9] |
| [mockito-junit-jupiter][29]                | [MIT][30]                                     |
| [Maven Plugin Integration Testing][68]     | [MIT License][69]                             |
| [EqualsVerifier \| release normal jar][26] | [Apache License, Version 2.0][1]              |
| [to-string-verifier][27]                   | [MIT License][28]                             |
| [junit-pioneer][70]                        | [Eclipse Public License v2.0][22]             |
| [SLF4J JDK14 Provider][31]                 | [MIT][32]                                     |

### Runtime Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Java project crawler][56] | [The MIT License][57] |

### Plugin Dependencies

| Dependency                                             | License                                     |
| ------------------------------------------------------ | ------------------------------------------- |
| [Apache Maven Clean Plugin][33]                        | [Apache-2.0][1]                             |
| [Apache Maven Install Plugin][34]                      | [Apache-2.0][1]                             |
| [Apache Maven Resources Plugin][35]                    | [Apache-2.0][1]                             |
| [Apache Maven Site Plugin][36]                         | [Apache-2.0][1]                             |
| [SonarQube Scanner for Maven][37]                      | [GNU LGPL 3][38]                            |
| [Apache Maven Toolchains Plugin][39]                   | [Apache-2.0][1]                             |
| [Apache Maven JAR Plugin][71]                          | [Apache-2.0][1]                             |
| [Apache Maven Compiler Plugin][40]                     | [Apache-2.0][1]                             |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                             |
| [Maven Flatten Plugin][41]                             | [Apache Software Licenese][1]               |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][8] | [ASL2][9]                                   |
| [Maven Surefire Plugin][42]                            | [Apache-2.0][1]                             |
| [Versions Maven Plugin][43]                            | [Apache License, Version 2.0][1]            |
| [duplicate-finder-maven-plugin Maven Mojo][44]         | [Apache License 2.0][45]                    |
| [Apache Maven Artifact Plugin][46]                     | [Apache-2.0][1]                             |
| [Apache Maven Deploy Plugin][5]                        | [Apache-2.0][1]                             |
| [Apache Maven GPG Plugin][47]                          | [Apache-2.0][1]                             |
| [Apache Maven Source Plugin][48]                       | [Apache License, Version 2.0][1]            |
| [Apache Maven Javadoc Plugin][49]                      | [Apache-2.0][1]                             |
| [Central Publishing Maven Plugin][2]                   | [The Apache License, Version 2.0][1]        |
| [Maven Failsafe Plugin][72]                            | [Apache-2.0][1]                             |
| [JaCoCo :: Maven Plugin][50]                           | [EPL-2.0][51]                               |
| [Quality Summarizer Maven Plugin][52]                  | [MIT License][53]                           |
| [error-code-crawler-maven-plugin][6]                   | [MIT License][7]                            |
| [Git Commit Id Maven Plugin][54]                       | [GNU Lesser General Public License 3.0][55] |

## Project Keeper Command Line Interface

### Compile Dependencies

| Dependency                 | License               |
| -------------------------- | --------------------- |
| [Project Keeper Core][56]  | [The MIT License][57] |
| [error-reporting-java][17] | [MIT License][18]     |
| [Maven Model][63]          | [Apache-2.0][1]       |

### Test Dependencies

| Dependency                             | License                           |
| -------------------------------------- | --------------------------------- |
| [Project Keeper shared test setup][56] | [The MIT License][57]             |
| [JUnit Jupiter Params][21]             | [Eclipse Public License v2.0][22] |
| [Hamcrest][23]                         | [BSD-3-Clause][24]                |
| [Maven Project Version Getter][66]     | [MIT License][67]                 |

### Runtime Dependencies

| Dependency                 | License   |
| -------------------------- | --------- |
| [SLF4J API Module][31]     | [MIT][32] |
| [SLF4J JDK14 Provider][31] | [MIT][32] |

### Plugin Dependencies

| Dependency                                             | License                                     |
| ------------------------------------------------------ | ------------------------------------------- |
| [Apache Maven Clean Plugin][33]                        | [Apache-2.0][1]                             |
| [Apache Maven Install Plugin][34]                      | [Apache-2.0][1]                             |
| [Apache Maven Resources Plugin][35]                    | [Apache-2.0][1]                             |
| [Apache Maven Site Plugin][36]                         | [Apache-2.0][1]                             |
| [SonarQube Scanner for Maven][37]                      | [GNU LGPL 3][38]                            |
| [Apache Maven Toolchains Plugin][39]                   | [Apache-2.0][1]                             |
| [Apache Maven Compiler Plugin][40]                     | [Apache-2.0][1]                             |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                             |
| [Maven Flatten Plugin][41]                             | [Apache Software Licenese][1]               |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][8] | [ASL2][9]                                   |
| [Maven Surefire Plugin][42]                            | [Apache-2.0][1]                             |
| [Versions Maven Plugin][43]                            | [Apache License, Version 2.0][1]            |
| [duplicate-finder-maven-plugin Maven Mojo][44]         | [Apache License 2.0][45]                    |
| [Apache Maven Artifact Plugin][46]                     | [Apache-2.0][1]                             |
| [Apache Maven Assembly Plugin][73]                     | [Apache-2.0][1]                             |
| [Apache Maven JAR Plugin][71]                          | [Apache-2.0][1]                             |
| [Artifact reference checker and unifier][74]           | [MIT License][75]                           |
| [Apache Maven Deploy Plugin][5]                        | [Apache-2.0][1]                             |
| [Apache Maven GPG Plugin][47]                          | [Apache-2.0][1]                             |
| [Apache Maven Source Plugin][48]                       | [Apache License, Version 2.0][1]            |
| [Apache Maven Javadoc Plugin][49]                      | [Apache-2.0][1]                             |
| [Central Publishing Maven Plugin][2]                   | [The Apache License, Version 2.0][1]        |
| [Maven Failsafe Plugin][72]                            | [Apache-2.0][1]                             |
| [JaCoCo :: Maven Plugin][50]                           | [EPL-2.0][51]                               |
| [Quality Summarizer Maven Plugin][52]                  | [MIT License][53]                           |
| [error-code-crawler-maven-plugin][6]                   | [MIT License][7]                            |
| [Git Commit Id Maven Plugin][54]                       | [GNU Lesser General Public License 3.0][55] |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Core][56]                 | [The MIT License][57] |
| [Maven Plugin Tools Java Annotations][76] | [Apache-2.0][1]       |
| [Maven Plugin API][77]                    | [Apache-2.0][1]       |
| [Maven Core][78]                          | [Apache-2.0][1]       |
| [error-reporting-java][17]                | [MIT License][18]     |

### Test Dependencies

| Dependency                             | License                                       |
| -------------------------------------- | --------------------------------------------- |
| [Maven Project Version Getter][66]     | [MIT License][67]                             |
| [JUnit Jupiter Params][21]             | [Eclipse Public License v2.0][22]             |
| [Hamcrest][23]                         | [BSD-3-Clause][24]                            |
| [org.xmlunit:xmlunit-matchers][58]     | [The Apache Software License, Version 2.0][9] |
| [mockito-core][29]                     | [MIT][30]                                     |
| [Maven Plugin Integration Testing][68] | [MIT License][69]                             |
| [SLF4J JDK14 Provider][31]             | [MIT][32]                                     |
| [JaCoCo :: Agent][79]                  | [EPL-2.0][51]                                 |

### Plugin Dependencies

| Dependency                                             | License                                     |
| ------------------------------------------------------ | ------------------------------------------- |
| [Apache Maven Clean Plugin][33]                        | [Apache-2.0][1]                             |
| [Apache Maven Install Plugin][34]                      | [Apache-2.0][1]                             |
| [Apache Maven Resources Plugin][35]                    | [Apache-2.0][1]                             |
| [Apache Maven Site Plugin][36]                         | [Apache-2.0][1]                             |
| [SonarQube Scanner for Maven][37]                      | [GNU LGPL 3][38]                            |
| [Apache Maven Toolchains Plugin][39]                   | [Apache-2.0][1]                             |
| [Maven Plugin Plugin][80]                              | [Apache-2.0][1]                             |
| [Apache Maven Compiler Plugin][40]                     | [Apache-2.0][1]                             |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                             |
| [Maven Flatten Plugin][41]                             | [Apache Software Licenese][1]               |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][8] | [ASL2][9]                                   |
| [Maven Surefire Plugin][42]                            | [Apache-2.0][1]                             |
| [Versions Maven Plugin][43]                            | [Apache License, Version 2.0][1]            |
| [Apache Maven JAR Plugin][71]                          | [Apache-2.0][1]                             |
| [duplicate-finder-maven-plugin Maven Mojo][44]         | [Apache License 2.0][45]                    |
| [Apache Maven Artifact Plugin][46]                     | [Apache-2.0][1]                             |
| [Apache Maven Deploy Plugin][5]                        | [Apache-2.0][1]                             |
| [Apache Maven GPG Plugin][47]                          | [Apache-2.0][1]                             |
| [Apache Maven Source Plugin][48]                       | [Apache License, Version 2.0][1]            |
| [Apache Maven Javadoc Plugin][49]                      | [Apache-2.0][1]                             |
| [Central Publishing Maven Plugin][2]                   | [The Apache License, Version 2.0][1]        |
| [Apache Maven Dependency Plugin][81]                   | [Apache-2.0][1]                             |
| [Maven Failsafe Plugin][72]                            | [Apache-2.0][1]                             |
| [JaCoCo :: Maven Plugin][50]                           | [EPL-2.0][51]                               |
| [Quality Summarizer Maven Plugin][52]                  | [MIT License][53]                           |
| [error-code-crawler-maven-plugin][6]                   | [MIT License][7]                            |
| [Git Commit Id Maven Plugin][54]                       | [GNU Lesser General Public License 3.0][55] |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper shared model classes][56] | [The MIT License][57] |
| [Maven Plugin Tools Java Annotations][76] | [Apache-2.0][1]       |
| [Maven Plugin API][77]                    | [Apache-2.0][1]       |
| [error-reporting-java][17]                | [MIT License][18]     |
| [JGit - Core][19]                         | [BSD-3-Clause][20]    |
| [semver4j][60]                            | [The MIT License][28] |
| [Maven Core][78]                          | [Apache-2.0][1]       |

### Test Dependencies

| Dependency                             | License                                       |
| -------------------------------------- | --------------------------------------------- |
| [Project Keeper shared test setup][56] | [The MIT License][57]                         |
| [Maven Project Version Getter][66]     | [MIT License][67]                             |
| [JUnit Jupiter Params][21]             | [Eclipse Public License v2.0][22]             |
| [Hamcrest][23]                         | [BSD-3-Clause][24]                            |
| [org.xmlunit:xmlunit-matchers][58]     | [The Apache Software License, Version 2.0][9] |
| [SLF4J JDK14 Provider][31]             | [MIT][32]                                     |
| [mockito-core][29]                     | [MIT][30]                                     |
| [mockito-junit-jupiter][29]            | [MIT][30]                                     |
| [Maven Plugin Integration Testing][68] | [MIT License][69]                             |
| [JaCoCo :: Agent][79]                  | [EPL-2.0][51]                                 |

### Plugin Dependencies

| Dependency                                             | License                                     |
| ------------------------------------------------------ | ------------------------------------------- |
| [Apache Maven Clean Plugin][33]                        | [Apache-2.0][1]                             |
| [Apache Maven Install Plugin][34]                      | [Apache-2.0][1]                             |
| [Apache Maven Resources Plugin][35]                    | [Apache-2.0][1]                             |
| [Apache Maven Site Plugin][36]                         | [Apache-2.0][1]                             |
| [SonarQube Scanner for Maven][37]                      | [GNU LGPL 3][38]                            |
| [Apache Maven Toolchains Plugin][39]                   | [Apache-2.0][1]                             |
| [Apache Maven Compiler Plugin][40]                     | [Apache-2.0][1]                             |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                             |
| [Maven Flatten Plugin][41]                             | [Apache Software Licenese][1]               |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][8] | [ASL2][9]                                   |
| [Maven Surefire Plugin][42]                            | [Apache-2.0][1]                             |
| [Versions Maven Plugin][43]                            | [Apache License, Version 2.0][1]            |
| [Maven Plugin Plugin][80]                              | [Apache-2.0][1]                             |
| [duplicate-finder-maven-plugin Maven Mojo][44]         | [Apache License 2.0][45]                    |
| [Apache Maven Artifact Plugin][46]                     | [Apache-2.0][1]                             |
| [Apache Maven Deploy Plugin][5]                        | [Apache-2.0][1]                             |
| [Apache Maven GPG Plugin][47]                          | [Apache-2.0][1]                             |
| [Apache Maven Source Plugin][48]                       | [Apache License, Version 2.0][1]            |
| [Apache Maven Javadoc Plugin][49]                      | [Apache-2.0][1]                             |
| [Central Publishing Maven Plugin][2]                   | [The Apache License, Version 2.0][1]        |
| [Apache Maven Dependency Plugin][81]                   | [Apache-2.0][1]                             |
| [Maven Failsafe Plugin][72]                            | [Apache-2.0][1]                             |
| [JaCoCo :: Maven Plugin][50]                           | [EPL-2.0][51]                               |
| [Quality Summarizer Maven Plugin][52]                  | [MIT License][53]                           |
| [error-code-crawler-maven-plugin][6]                   | [MIT License][7]                            |
| [Git Commit Id Maven Plugin][54]                       | [GNU Lesser General Public License 3.0][55] |

## Project Keeper Shared Test Setup

### Compile Dependencies

| Dependency                                | License                          |
| ----------------------------------------- | -------------------------------- |
| [Project Keeper shared model classes][56] | [The MIT License][57]            |
| [SnakeYAML][61]                           | [Apache License, Version 2.0][9] |
| [Hamcrest][23]                            | [BSD-3-Clause][24]               |
| [Maven Model][63]                         | [Apache-2.0][1]                  |

### Plugin Dependencies

| Dependency                                             | License                                     |
| ------------------------------------------------------ | ------------------------------------------- |
| [Apache Maven Clean Plugin][33]                        | [Apache-2.0][1]                             |
| [Apache Maven Install Plugin][34]                      | [Apache-2.0][1]                             |
| [Apache Maven Resources Plugin][35]                    | [Apache-2.0][1]                             |
| [Apache Maven Site Plugin][36]                         | [Apache-2.0][1]                             |
| [SonarQube Scanner for Maven][37]                      | [GNU LGPL 3][38]                            |
| [Apache Maven Toolchains Plugin][39]                   | [Apache-2.0][1]                             |
| [Apache Maven Javadoc Plugin][49]                      | [Apache-2.0][1]                             |
| [Apache Maven Compiler Plugin][40]                     | [Apache-2.0][1]                             |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                             |
| [Maven Flatten Plugin][41]                             | [Apache Software Licenese][1]               |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][8] | [ASL2][9]                                   |
| [Maven Surefire Plugin][42]                            | [Apache-2.0][1]                             |
| [Versions Maven Plugin][43]                            | [Apache License, Version 2.0][1]            |
| [Apache Maven Deploy Plugin][5]                        | [Apache-2.0][1]                             |
| [duplicate-finder-maven-plugin Maven Mojo][44]         | [Apache License 2.0][45]                    |
| [Apache Maven Artifact Plugin][46]                     | [Apache-2.0][1]                             |
| [JaCoCo :: Maven Plugin][50]                           | [EPL-2.0][51]                               |
| [Quality Summarizer Maven Plugin][52]                  | [MIT License][53]                           |
| [error-code-crawler-maven-plugin][6]                   | [MIT License][7]                            |
| [Git Commit Id Maven Plugin][54]                       | [GNU Lesser General Public License 3.0][55] |

[0]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[1]: https://www.apache.org/licenses/LICENSE-2.0.txt
[2]: https://central.sonatype.org
[3]: https://github.com/itsallcode/openfasttrace-maven-plugin
[4]: https://www.gnu.org/licenses/gpl-3.0.html
[5]: https://maven.apache.org/plugins/maven-deploy-plugin/
[6]: https://github.com/exasol/error-code-crawler-maven-plugin/
[7]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[8]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[9]: http://www.apache.org/licenses/LICENSE-2.0.txt
[10]: https://github.com/eclipse-ee4j/jsonp
[11]: https://projects.eclipse.org/license/epl-2.0
[12]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[13]: https://jakartaee.github.io/jsonb-api
[14]: https://projects.eclipse.org/projects/ee4j.yasson
[15]: http://www.eclipse.org/legal/epl-v20.html
[16]: http://www.eclipse.org/org/documents/edl-v10.php
[17]: https://github.com/exasol/error-reporting-java/
[18]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[19]: https://www.eclipse.org/jgit/
[20]: https://www.eclipse.org/org/documents/edl-v10.php
[21]: https://junit.org/junit5/
[22]: https://www.eclipse.org/legal/epl-v20.html
[23]: http://hamcrest.org/JavaHamcrest/
[24]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[25]: https://github.com/itsallcode/junit5-system-extensions
[26]: https://www.jqno.nl/equalsverifier
[27]: https://github.com/jparams/to-string-verifier
[28]: http://www.opensource.org/licenses/mit-license.php
[29]: https://github.com/mockito/mockito
[30]: https://opensource.org/licenses/MIT
[31]: http://www.slf4j.org
[32]: https://opensource.org/license/mit
[33]: https://maven.apache.org/plugins/maven-clean-plugin/
[34]: https://maven.apache.org/plugins/maven-install-plugin/
[35]: https://maven.apache.org/plugins/maven-resources-plugin/
[36]: https://maven.apache.org/plugins/maven-site-plugin/
[37]: http://docs.sonarqube.org/display/PLUG/Plugin+Library/sonar-scanner-maven/sonar-maven-plugin
[38]: http://www.gnu.org/licenses/lgpl.txt
[39]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[40]: https://maven.apache.org/plugins/maven-compiler-plugin/
[41]: https://www.mojohaus.org/flatten-maven-plugin/
[42]: https://maven.apache.org/surefire/maven-surefire-plugin/
[43]: https://www.mojohaus.org/versions/versions-maven-plugin/
[44]: https://basepom.github.io/duplicate-finder-maven-plugin
[45]: http://www.apache.org/licenses/LICENSE-2.0.html
[46]: https://maven.apache.org/plugins/maven-artifact-plugin/
[47]: https://maven.apache.org/plugins/maven-gpg-plugin/
[48]: https://maven.apache.org/plugins/maven-source-plugin/
[49]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[50]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[51]: https://www.eclipse.org/legal/epl-2.0/
[52]: https://github.com/exasol/quality-summarizer-maven-plugin/
[53]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[54]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[55]: http://www.gnu.org/licenses/lgpl-3.0.txt
[56]: https://github.com/exasol/project-keeper/
[57]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[58]: https://www.xmlunit.org/
[59]: https://github.com/Steppschuh/Java-Markdown-Generator
[60]: https://github.com/vdurmont/semver4j
[61]: https://bitbucket.org/snakeyaml/snakeyaml
[62]: https://bitbucket.org/snakeyaml/snakeyaml-engine
[63]: https://maven.apache.org/ref/3.9.10/maven-model/
[64]: https://www.jcabi.com/jcabi-github
[65]: https://www.jcabi.com/LICENSE.txt
[66]: https://github.com/exasol/maven-project-version-getter/
[67]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[68]: https://github.com/exasol/maven-plugin-integration-testing/
[69]: https://github.com/exasol/maven-plugin-integration-testing/blob/main/LICENSE
[70]: https://junit-pioneer.org/
[71]: https://maven.apache.org/plugins/maven-jar-plugin/
[72]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[73]: https://maven.apache.org/plugins/maven-assembly-plugin/
[74]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[75]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[76]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[77]: https://maven.apache.org/ref/3.9.10/maven-plugin-api/
[78]: https://maven.apache.org/ref/3.9.10/maven-core/
[79]: https://www.eclemma.org/jacoco/index.html
[80]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[81]: https://maven.apache.org/plugins/maven-dependency-plugin/
