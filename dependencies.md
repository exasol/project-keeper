<!-- @formatter:off -->
# Dependencies

## Project Keeper Root Project

### Plugin Dependencies

| Dependency                           | License                              |
| ------------------------------------ | ------------------------------------ |
| [Apache Maven Enforcer Plugin][0]    | [Apache-2.0][1]                      |
| [OpenFastTrace Maven Plugin][2]      | [GNU General Public License v3.0][3] |
| [Apache Maven Deploy Plugin][4]      | [Apache-2.0][1]                      |
| [error-code-crawler-maven-plugin][5] | [MIT License][6]                     |

## Project Keeper Shared Model Classes

### Compile Dependencies

| Dependency                       | License                                                                                                      |
| -------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [Jakarta JSON Processing API][7] | [Eclipse Public License 2.0][8]; [GNU General Public License, version 2 with the GNU Classpath Exception][9] |
| [JSON-B API][10]                 | [Eclipse Public License 2.0][8]; [GNU General Public License, version 2 with the GNU Classpath Exception][9] |
| [Yasson][11]                     | [Eclipse Public License v. 2.0][12]; [Eclipse Distribution License v. 1.0][13]                               |
| [error-reporting-java][14]       | [MIT License][15]                                                                                            |
| [JGit - Core][16]                | Eclipse Distribution License (New BSD License)                                                               |

### Test Dependencies

| Dependency                                 | License                           |
| ------------------------------------------ | --------------------------------- |
| [JUnit Jupiter Engine][17]                 | [Eclipse Public License v2.0][18] |
| [JUnit Jupiter Params][17]                 | [Eclipse Public License v2.0][18] |
| [Hamcrest][19]                             | [BSD License 3][20]               |
| [JUnit5 System Extensions][21]             | [Eclipse Public License v2.0][12] |
| [EqualsVerifier \| release normal jar][22] | [Apache License, Version 2.0][1]  |
| [to-string-verifier][23]                   | [MIT License][24]                 |
| [mockito-core][25]                         | [MIT][26]                         |
| [SLF4J JDK14 Binding][27]                  | [MIT License][24]                 |

### Plugin Dependencies

| Dependency                                              | License                          |
| ------------------------------------------------------- | -------------------------------- |
| [SonarQube Scanner for Maven][28]                       | [GNU LGPL 3][29]                 |
| [Apache Maven Toolchains Plugin][30]                    | [Apache License, Version 2.0][1] |
| [Apache Maven Compiler Plugin][31]                      | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                       | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][32]                              | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][33] | [ASL2][34]                       |
| [Maven Surefire Plugin][35]                             | [Apache-2.0][1]                  |
| [Versions Maven Plugin][36]                             | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][37]          | [Apache License 2.0][38]         |
| [Apache Maven Deploy Plugin][4]                         | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][39]                           | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][40]                        | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][41]                       | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][42]                        | [Eclipse Public License][43]     |
| [JaCoCo :: Maven Plugin][44]                            | [EPL-2.0][45]                    |
| [error-code-crawler-maven-plugin][5]                    | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][46]                   | [Apache 2.0][34]                 |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project Keeper shared model classes][47] | [The MIT License][48]                          |
| [org.xmlunit:xmlunit-core][49]            | [The Apache Software License, Version 2.0][34] |
| [error-reporting-java][14]                | [MIT License][15]                              |
| [Markdown Generator][50]                  | [The Apache Software License, Version 2.0][34] |
| [semver4j][51]                            | [The MIT License][24]                          |
| [SnakeYAML][52]                           | [Apache License, Version 2.0][34]              |
| [SnakeYAML Engine][53]                    | [Apache License, Version 2.0][34]              |
| [Maven Model][54]                         | [Apache-2.0][1]                                |
| [jcabi-github][55]                        | [BSD][56]                                      |

### Test Dependencies

| Dependency                                 | License                                        |
| ------------------------------------------ | ---------------------------------------------- |
| [Project Keeper shared test setup][47]     | [The MIT License][48]                          |
| [Maven Project Version Getter][57]         | [MIT License][58]                              |
| [JUnit Jupiter Engine][17]                 | [Eclipse Public License v2.0][18]              |
| [JUnit Jupiter Params][17]                 | [Eclipse Public License v2.0][18]              |
| [Hamcrest][19]                             | [BSD License 3][20]                            |
| [org.xmlunit:xmlunit-matchers][49]         | [The Apache Software License, Version 2.0][34] |
| [mockito-junit-jupiter][25]                | [MIT][26]                                      |
| [Maven Plugin Integration Testing][59]     | [MIT License][60]                              |
| [EqualsVerifier \| release normal jar][22] | [Apache License, Version 2.0][1]               |
| [to-string-verifier][23]                   | [MIT License][24]                              |
| [junit-pioneer][61]                        | [Eclipse Public License v2.0][18]              |
| [SLF4J JDK14 Binding][27]                  | [MIT License][24]                              |

### Runtime Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Java project crawler][47] | [The MIT License][48] |

### Plugin Dependencies

| Dependency                                              | License                          |
| ------------------------------------------------------- | -------------------------------- |
| [SonarQube Scanner for Maven][28]                       | [GNU LGPL 3][29]                 |
| [Apache Maven Toolchains Plugin][30]                    | [Apache License, Version 2.0][1] |
| [Apache Maven JAR Plugin][62]                           | [Apache License, Version 2.0][1] |
| [Apache Maven Compiler Plugin][31]                      | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                       | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][32]                              | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][33] | [ASL2][34]                       |
| [Maven Surefire Plugin][35]                             | [Apache-2.0][1]                  |
| [Versions Maven Plugin][36]                             | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][37]          | [Apache License 2.0][38]         |
| [Apache Maven Deploy Plugin][4]                         | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][39]                           | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][40]                        | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][41]                       | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][42]                        | [Eclipse Public License][43]     |
| [Maven Failsafe Plugin][63]                             | [Apache-2.0][1]                  |
| [JaCoCo :: Maven Plugin][44]                            | [EPL-2.0][45]                    |
| [error-code-crawler-maven-plugin][5]                    | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][46]                   | [Apache 2.0][34]                 |

## Project Keeper Command Line Interface

### Compile Dependencies

| Dependency                 | License               |
| -------------------------- | --------------------- |
| [Project Keeper Core][47]  | [The MIT License][48] |
| [error-reporting-java][14] | [MIT License][15]     |
| [Maven Model][54]          | [Apache-2.0][1]       |

### Test Dependencies

| Dependency                             | License                           |
| -------------------------------------- | --------------------------------- |
| [Project Keeper shared test setup][47] | [The MIT License][48]             |
| [JUnit Jupiter Engine][17]             | [Eclipse Public License v2.0][18] |
| [JUnit Jupiter Params][17]             | [Eclipse Public License v2.0][18] |
| [Hamcrest][19]                         | [BSD License 3][20]               |
| [Maven Project Version Getter][57]     | [MIT License][58]                 |

### Runtime Dependencies

| Dependency                | License           |
| ------------------------- | ----------------- |
| [SLF4J JDK14 Binding][27] | [MIT License][24] |

### Plugin Dependencies

| Dependency                                              | License                          |
| ------------------------------------------------------- | -------------------------------- |
| [SonarQube Scanner for Maven][28]                       | [GNU LGPL 3][29]                 |
| [Apache Maven Toolchains Plugin][30]                    | [Apache License, Version 2.0][1] |
| [Apache Maven Compiler Plugin][31]                      | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                       | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][32]                              | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][33] | [ASL2][34]                       |
| [Maven Surefire Plugin][35]                             | [Apache-2.0][1]                  |
| [Versions Maven Plugin][36]                             | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][37]          | [Apache License 2.0][38]         |
| [Apache Maven Assembly Plugin][64]                      | [Apache-2.0][1]                  |
| [Apache Maven JAR Plugin][62]                           | [Apache License, Version 2.0][1] |
| [Artifact reference checker and unifier][65]            | [MIT License][66]                |
| [Apache Maven Deploy Plugin][4]                         | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][39]                           | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][40]                        | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][41]                       | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][42]                        | [Eclipse Public License][43]     |
| [Maven Failsafe Plugin][63]                             | [Apache-2.0][1]                  |
| [JaCoCo :: Maven Plugin][44]                            | [EPL-2.0][45]                    |
| [error-code-crawler-maven-plugin][5]                    | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][46]                   | [Apache 2.0][34]                 |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Core][47]                 | [The MIT License][48] |
| [Maven Plugin Tools Java Annotations][67] | [Apache-2.0][1]       |
| [Maven Plugin API][68]                    | [Apache-2.0][1]       |
| [Maven Core][69]                          | [Apache-2.0][1]       |
| [error-reporting-java][14]                | [MIT License][15]     |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][57]     | [MIT License][58]                              |
| [JUnit Jupiter Engine][17]             | [Eclipse Public License v2.0][18]              |
| [JUnit Jupiter Params][17]             | [Eclipse Public License v2.0][18]              |
| [Hamcrest][19]                         | [BSD License 3][20]                            |
| [org.xmlunit:xmlunit-matchers][49]     | [The Apache Software License, Version 2.0][34] |
| [mockito-core][25]                     | [MIT][26]                                      |
| [Maven Plugin Integration Testing][59] | [MIT License][60]                              |
| [SLF4J JDK14 Binding][27]              | [MIT License][24]                              |
| [JaCoCo :: Agent][70]                  | [Eclipse Public License 2.0][45]               |

### Plugin Dependencies

| Dependency                                              | License                          |
| ------------------------------------------------------- | -------------------------------- |
| [SonarQube Scanner for Maven][28]                       | [GNU LGPL 3][29]                 |
| [Apache Maven Toolchains Plugin][30]                    | [Apache License, Version 2.0][1] |
| [Maven Plugin Plugin][71]                               | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][31]                      | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                       | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][32]                              | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][33] | [ASL2][34]                       |
| [Maven Surefire Plugin][35]                             | [Apache-2.0][1]                  |
| [Versions Maven Plugin][36]                             | [Apache License, Version 2.0][1] |
| [Apache Maven JAR Plugin][62]                           | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][37]          | [Apache License 2.0][38]         |
| [Apache Maven Deploy Plugin][4]                         | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][39]                           | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][40]                        | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][41]                       | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][42]                        | [Eclipse Public License][43]     |
| [Apache Maven Dependency Plugin][72]                    | [Apache-2.0][1]                  |
| [Maven Failsafe Plugin][63]                             | [Apache-2.0][1]                  |
| [JaCoCo :: Maven Plugin][44]                            | [EPL-2.0][45]                    |
| [error-code-crawler-maven-plugin][5]                    | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][46]                   | [Apache 2.0][34]                 |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project Keeper shared model classes][47] | [The MIT License][48]                          |
| [Maven Plugin Tools Java Annotations][67] | [Apache-2.0][1]                                |
| [Maven Plugin API][68]                    | [Apache-2.0][1]                                |
| [error-reporting-java][14]                | [MIT License][15]                              |
| [JGit - Core][16]                         | Eclipse Distribution License (New BSD License) |
| [semver4j][51]                            | [The MIT License][24]                          |
| [Maven Core][69]                          | [Apache-2.0][1]                                |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][57]     | [MIT License][58]                              |
| [JUnit Jupiter Engine][17]             | [Eclipse Public License v2.0][18]              |
| [JUnit Jupiter Params][17]             | [Eclipse Public License v2.0][18]              |
| [Hamcrest][19]                         | [BSD License 3][20]                            |
| [org.xmlunit:xmlunit-matchers][49]     | [The Apache Software License, Version 2.0][34] |
| [SLF4J JDK14 Binding][27]              | [MIT License][24]                              |
| [mockito-core][25]                     | [MIT][26]                                      |
| [mockito-junit-jupiter][25]            | [MIT][26]                                      |
| [Maven Plugin Integration Testing][59] | [MIT License][60]                              |
| [JaCoCo :: Agent][70]                  | [Eclipse Public License 2.0][45]               |

### Plugin Dependencies

| Dependency                                              | License                          |
| ------------------------------------------------------- | -------------------------------- |
| [SonarQube Scanner for Maven][28]                       | [GNU LGPL 3][29]                 |
| [Apache Maven Toolchains Plugin][30]                    | [Apache License, Version 2.0][1] |
| [Apache Maven Compiler Plugin][31]                      | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                       | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][32]                              | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][33] | [ASL2][34]                       |
| [Maven Surefire Plugin][35]                             | [Apache-2.0][1]                  |
| [Versions Maven Plugin][36]                             | [Apache License, Version 2.0][1] |
| [Maven Plugin Plugin][71]                               | [Apache-2.0][1]                  |
| [duplicate-finder-maven-plugin Maven Mojo][37]          | [Apache License 2.0][38]         |
| [Apache Maven Deploy Plugin][4]                         | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][39]                           | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][40]                        | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][41]                       | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][42]                        | [Eclipse Public License][43]     |
| [Apache Maven Dependency Plugin][72]                    | [Apache-2.0][1]                  |
| [Maven Failsafe Plugin][63]                             | [Apache-2.0][1]                  |
| [JaCoCo :: Maven Plugin][44]                            | [EPL-2.0][45]                    |
| [error-code-crawler-maven-plugin][5]                    | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][46]                   | [Apache 2.0][34]                 |

## Project Keeper Shared Test Setup

### Compile Dependencies

| Dependency                                | License                           |
| ----------------------------------------- | --------------------------------- |
| [Project Keeper shared model classes][47] | [The MIT License][48]             |
| [SnakeYAML][52]                           | [Apache License, Version 2.0][34] |
| [Hamcrest][19]                            | [BSD License 3][20]               |
| [Maven Model][54]                         | [Apache-2.0][1]                   |

### Plugin Dependencies

| Dependency                                              | License                          |
| ------------------------------------------------------- | -------------------------------- |
| [SonarQube Scanner for Maven][28]                       | [GNU LGPL 3][29]                 |
| [Apache Maven Toolchains Plugin][30]                    | [Apache License, Version 2.0][1] |
| [Apache Maven Compiler Plugin][31]                      | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                       | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][32]                              | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][33] | [ASL2][34]                       |
| [Maven Surefire Plugin][35]                             | [Apache-2.0][1]                  |
| [Versions Maven Plugin][36]                             | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][37]          | [Apache License 2.0][38]         |
| [JaCoCo :: Maven Plugin][44]                            | [EPL-2.0][45]                    |
| [error-code-crawler-maven-plugin][5]                    | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][46]                   | [Apache 2.0][34]                 |

[0]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[1]: https://www.apache.org/licenses/LICENSE-2.0.txt
[2]: https://github.com/itsallcode/openfasttrace-maven-plugin
[3]: https://www.gnu.org/licenses/gpl-3.0.html
[4]: https://maven.apache.org/plugins/maven-deploy-plugin/
[5]: https://github.com/exasol/error-code-crawler-maven-plugin/
[6]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[7]: https://github.com/eclipse-ee4j/jsonp
[8]: https://projects.eclipse.org/license/epl-2.0
[9]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[10]: https://projects.eclipse.org/projects/ee4j.jsonp
[11]: https://projects.eclipse.org/projects/ee4j.yasson
[12]: http://www.eclipse.org/legal/epl-v20.html
[13]: http://www.eclipse.org/org/documents/edl-v10.php
[14]: https://github.com/exasol/error-reporting-java/
[15]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[16]: https://www.eclipse.org/jgit/
[17]: https://junit.org/junit5/
[18]: https://www.eclipse.org/legal/epl-v20.html
[19]: http://hamcrest.org/JavaHamcrest/
[20]: http://opensource.org/licenses/BSD-3-Clause
[21]: https://github.com/itsallcode/junit5-system-extensions
[22]: https://www.jqno.nl/equalsverifier
[23]: https://github.com/jparams/to-string-verifier
[24]: http://www.opensource.org/licenses/mit-license.php
[25]: https://github.com/mockito/mockito
[26]: https://opensource.org/licenses/MIT
[27]: http://www.slf4j.org
[28]: http://sonarsource.github.io/sonar-scanner-maven/
[29]: http://www.gnu.org/licenses/lgpl.txt
[30]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[31]: https://maven.apache.org/plugins/maven-compiler-plugin/
[32]: https://www.mojohaus.org/flatten-maven-plugin/
[33]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[34]: http://www.apache.org/licenses/LICENSE-2.0.txt
[35]: https://maven.apache.org/surefire/maven-surefire-plugin/
[36]: https://www.mojohaus.org/versions/versions-maven-plugin/
[37]: https://basepom.github.io/duplicate-finder-maven-plugin
[38]: http://www.apache.org/licenses/LICENSE-2.0.html
[39]: https://maven.apache.org/plugins/maven-gpg-plugin/
[40]: https://maven.apache.org/plugins/maven-source-plugin/
[41]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[42]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[43]: http://www.eclipse.org/legal/epl-v10.html
[44]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[45]: https://www.eclipse.org/legal/epl-2.0/
[46]: http://zlika.github.io/reproducible-build-maven-plugin
[47]: https://github.com/exasol/project-keeper/
[48]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[49]: https://www.xmlunit.org/
[50]: https://github.com/Steppschuh/Java-Markdown-Generator
[51]: https://github.com/vdurmont/semver4j
[52]: https://bitbucket.org/snakeyaml/snakeyaml
[53]: https://bitbucket.org/snakeyaml/snakeyaml-engine
[54]: https://maven.apache.org/ref/3.9.6/maven-model/
[55]: https://www.jcabi.com/jcabi-github
[56]: https://www.jcabi.com/LICENSE.txt
[57]: https://github.com/exasol/maven-project-version-getter/
[58]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[59]: https://github.com/exasol/maven-plugin-integration-testing/
[60]: https://github.com/exasol/maven-plugin-integration-testing/blob/main/LICENSE
[61]: https://junit-pioneer.org/
[62]: https://maven.apache.org/plugins/maven-jar-plugin/
[63]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[64]: https://maven.apache.org/plugins/maven-assembly-plugin/
[65]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[66]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[67]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[68]: https://maven.apache.org/ref/3.9.6/maven-plugin-api/
[69]: https://maven.apache.org/ref/3.9.6/maven-core/
[70]: https://www.eclemma.org/jacoco/index.html
[71]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[72]: https://maven.apache.org/plugins/maven-dependency-plugin/
