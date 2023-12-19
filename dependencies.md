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
| [JGit - Core][9]                 | Eclipse Distribution License (New BSD License)                                                               |

### Test Dependencies

| Dependency                                 | License                           |
| ------------------------------------------ | --------------------------------- |
| [JUnit Jupiter Engine][10]                 | [Eclipse Public License v2.0][11] |
| [JUnit Jupiter Params][10]                 | [Eclipse Public License v2.0][11] |
| [Hamcrest][12]                             | [BSD License 3][13]               |
| [JUnit5 System Extensions][14]             | [Eclipse Public License v2.0][5]  |
| [EqualsVerifier \| release normal jar][15] | [Apache License, Version 2.0][16] |
| [to-string-verifier][17]                   | [MIT License][18]                 |
| [mockito-core][19]                         | [MIT][20]                         |
| [SLF4J JDK14 Binding][21]                  | [MIT License][18]                 |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][22]                       | [GNU LGPL 3][23]                  |
| [Apache Maven Toolchains Plugin][24]                    | [Apache License, Version 2.0][16] |
| [Apache Maven Compiler Plugin][25]                      | [Apache-2.0][16]                  |
| [Apache Maven Enforcer Plugin][26]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][28] | [ASL2][29]                        |
| [Maven Surefire Plugin][30]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][16] |
| [duplicate-finder-maven-plugin Maven Mojo][32]          | [Apache License 2.0][33]          |
| [Apache Maven Deploy Plugin][34]                        | [Apache-2.0][16]                  |
| [Apache Maven GPG Plugin][35]                           | [Apache-2.0][16]                  |
| [Apache Maven Source Plugin][36]                        | [Apache License, Version 2.0][16] |
| [Apache Maven Javadoc Plugin][37]                       | [Apache-2.0][16]                  |
| [Nexus Staging Maven Plugin][38]                        | [Eclipse Public License][39]      |
| [JaCoCo :: Maven Plugin][40]                            | [Eclipse Public License 2.0][41]  |
| [error-code-crawler-maven-plugin][42]                   | [MIT License][43]                 |
| [Reproducible Build Maven Plugin][44]                   | [Apache 2.0][29]                  |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project Keeper shared model classes][45] | [The MIT License][46]                          |
| [org.xmlunit:xmlunit-core][47]            | [The Apache Software License, Version 2.0][29] |
| [error-reporting-java][7]                 | [MIT License][8]                               |
| [Markdown Generator][48]                  | [The Apache Software License, Version 2.0][29] |
| [semver4j][49]                            | [The MIT License][18]                          |
| [SnakeYAML][50]                           | [Apache License, Version 2.0][29]              |
| [Maven Model][51]                         | [Apache-2.0][16]                               |

### Test Dependencies

| Dependency                                 | License                                        |
| ------------------------------------------ | ---------------------------------------------- |
| [Project Keeper shared test setup][45]     | [The MIT License][46]                          |
| [Maven Project Version Getter][52]         | [MIT License][53]                              |
| [JUnit Jupiter Engine][10]                 | [Eclipse Public License v2.0][11]              |
| [JUnit Jupiter Params][10]                 | [Eclipse Public License v2.0][11]              |
| [Hamcrest][12]                             | [BSD License 3][13]                            |
| [org.xmlunit:xmlunit-matchers][47]         | [The Apache Software License, Version 2.0][29] |
| [mockito-junit-jupiter][19]                | [MIT][20]                                      |
| [Maven Plugin Integration Testing][54]     | [MIT License][55]                              |
| [EqualsVerifier \| release normal jar][15] | [Apache License, Version 2.0][16]              |
| [to-string-verifier][17]                   | [MIT License][18]                              |
| [SLF4J JDK14 Binding][21]                  | [MIT License][18]                              |

### Runtime Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Java project crawler][45] | [The MIT License][46] |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][22]                       | [GNU LGPL 3][23]                  |
| [Apache Maven Toolchains Plugin][24]                    | [Apache License, Version 2.0][16] |
| [Apache Maven Compiler Plugin][25]                      | [Apache-2.0][16]                  |
| [Apache Maven Enforcer Plugin][26]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][28] | [ASL2][29]                        |
| [Maven Surefire Plugin][30]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][16] |
| [duplicate-finder-maven-plugin Maven Mojo][32]          | [Apache License 2.0][33]          |
| [Apache Maven Deploy Plugin][34]                        | [Apache-2.0][16]                  |
| [Apache Maven GPG Plugin][35]                           | [Apache-2.0][16]                  |
| [Apache Maven Source Plugin][36]                        | [Apache License, Version 2.0][16] |
| [Apache Maven Javadoc Plugin][37]                       | [Apache-2.0][16]                  |
| [Nexus Staging Maven Plugin][38]                        | [Eclipse Public License][39]      |
| [Maven Failsafe Plugin][56]                             | [Apache-2.0][16]                  |
| [JaCoCo :: Maven Plugin][40]                            | [Eclipse Public License 2.0][41]  |
| [error-code-crawler-maven-plugin][42]                   | [MIT License][43]                 |
| [Reproducible Build Maven Plugin][44]                   | [Apache 2.0][29]                  |
| [Apache Maven JAR Plugin][57]                           | [Apache License, Version 2.0][16] |

## Project Keeper Command Line Interface

### Compile Dependencies

| Dependency                | License               |
| ------------------------- | --------------------- |
| [Project Keeper Core][45] | [The MIT License][46] |
| [error-reporting-java][7] | [MIT License][8]      |
| [Maven Model][51]         | [Apache-2.0][16]      |

### Test Dependencies

| Dependency                             | License                           |
| -------------------------------------- | --------------------------------- |
| [Project Keeper shared test setup][45] | [The MIT License][46]             |
| [JUnit Jupiter Engine][10]             | [Eclipse Public License v2.0][11] |
| [JUnit Jupiter Params][10]             | [Eclipse Public License v2.0][11] |
| [Hamcrest][12]                         | [BSD License 3][13]               |
| [Maven Project Version Getter][52]     | [MIT License][53]                 |

### Runtime Dependencies

| Dependency                | License           |
| ------------------------- | ----------------- |
| [SLF4J JDK14 Binding][21] | [MIT License][18] |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][22]                       | [GNU LGPL 3][23]                  |
| [Apache Maven Toolchains Plugin][24]                    | [Apache License, Version 2.0][16] |
| [Apache Maven Compiler Plugin][25]                      | [Apache-2.0][16]                  |
| [Apache Maven Enforcer Plugin][26]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][28] | [ASL2][29]                        |
| [Maven Surefire Plugin][30]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][16] |
| [duplicate-finder-maven-plugin Maven Mojo][32]          | [Apache License 2.0][33]          |
| [Apache Maven Assembly Plugin][58]                      | [Apache-2.0][16]                  |
| [Apache Maven JAR Plugin][57]                           | [Apache License, Version 2.0][16] |
| [Artifact reference checker and unifier][59]            | [MIT License][60]                 |
| [Apache Maven Deploy Plugin][34]                        | [Apache-2.0][16]                  |
| [Apache Maven GPG Plugin][35]                           | [Apache-2.0][16]                  |
| [Apache Maven Source Plugin][36]                        | [Apache License, Version 2.0][16] |
| [Apache Maven Javadoc Plugin][37]                       | [Apache-2.0][16]                  |
| [Nexus Staging Maven Plugin][38]                        | [Eclipse Public License][39]      |
| [Maven Failsafe Plugin][56]                             | [Apache-2.0][16]                  |
| [JaCoCo :: Maven Plugin][40]                            | [Eclipse Public License 2.0][41]  |
| [error-code-crawler-maven-plugin][42]                   | [MIT License][43]                 |
| [Reproducible Build Maven Plugin][44]                   | [Apache 2.0][29]                  |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Core][45]                 | [The MIT License][46] |
| [Maven Plugin Tools Java Annotations][61] | [Apache-2.0][16]      |
| [Maven Plugin API][62]                    | [Apache-2.0][16]      |
| [Maven Core][63]                          | [Apache-2.0][16]      |
| [error-reporting-java][7]                 | [MIT License][8]      |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][52]     | [MIT License][53]                              |
| [JUnit Jupiter Engine][10]             | [Eclipse Public License v2.0][11]              |
| [JUnit Jupiter Params][10]             | [Eclipse Public License v2.0][11]              |
| [Hamcrest][12]                         | [BSD License 3][13]                            |
| [org.xmlunit:xmlunit-matchers][47]     | [The Apache Software License, Version 2.0][29] |
| [mockito-core][19]                     | [MIT][20]                                      |
| [Maven Plugin Integration Testing][54] | [MIT License][55]                              |
| [SLF4J JDK14 Binding][21]              | [MIT License][18]                              |
| [JaCoCo :: Agent][64]                  | [Eclipse Public License 2.0][41]               |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][22]                       | [GNU LGPL 3][23]                  |
| [Apache Maven Toolchains Plugin][24]                    | [Apache License, Version 2.0][16] |
| [Apache Maven Compiler Plugin][25]                      | [Apache-2.0][16]                  |
| [Apache Maven Enforcer Plugin][26]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][28] | [ASL2][29]                        |
| [Maven Surefire Plugin][30]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][16] |
| [Maven Plugin Plugin][65]                               | [Apache-2.0][16]                  |
| [Apache Maven JAR Plugin][57]                           | [Apache License, Version 2.0][16] |
| [duplicate-finder-maven-plugin Maven Mojo][32]          | [Apache License 2.0][33]          |
| [Apache Maven Deploy Plugin][34]                        | [Apache-2.0][16]                  |
| [Apache Maven GPG Plugin][35]                           | [Apache-2.0][16]                  |
| [Apache Maven Source Plugin][36]                        | [Apache License, Version 2.0][16] |
| [Apache Maven Javadoc Plugin][37]                       | [Apache-2.0][16]                  |
| [Nexus Staging Maven Plugin][38]                        | [Eclipse Public License][39]      |
| [Apache Maven Dependency Plugin][66]                    | [Apache-2.0][16]                  |
| [Maven Failsafe Plugin][56]                             | [Apache-2.0][16]                  |
| [JaCoCo :: Maven Plugin][40]                            | [Eclipse Public License 2.0][41]  |
| [error-code-crawler-maven-plugin][42]                   | [MIT License][43]                 |
| [Reproducible Build Maven Plugin][44]                   | [Apache 2.0][29]                  |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project Keeper shared model classes][45] | [The MIT License][46]                          |
| [Maven Plugin Tools Java Annotations][61] | [Apache-2.0][16]                               |
| [Maven Plugin API][62]                    | [Apache-2.0][16]                               |
| [error-reporting-java][7]                 | [MIT License][8]                               |
| [JGit - Core][9]                          | Eclipse Distribution License (New BSD License) |
| [semver4j][49]                            | [The MIT License][18]                          |
| [Maven Core][63]                          | [Apache-2.0][16]                               |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][52]     | [MIT License][53]                              |
| [JUnit Jupiter Engine][10]             | [Eclipse Public License v2.0][11]              |
| [JUnit Jupiter Params][10]             | [Eclipse Public License v2.0][11]              |
| [Hamcrest][12]                         | [BSD License 3][13]                            |
| [org.xmlunit:xmlunit-matchers][47]     | [The Apache Software License, Version 2.0][29] |
| [SLF4J JDK14 Binding][21]              | [MIT License][18]                              |
| [mockito-core][19]                     | [MIT][20]                                      |
| [mockito-junit-jupiter][19]            | [MIT][20]                                      |
| [Maven Plugin Integration Testing][54] | [MIT License][55]                              |
| [JaCoCo :: Agent][64]                  | [Eclipse Public License 2.0][41]               |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][22]                       | [GNU LGPL 3][23]                  |
| [Apache Maven Toolchains Plugin][24]                    | [Apache License, Version 2.0][16] |
| [Apache Maven Compiler Plugin][25]                      | [Apache-2.0][16]                  |
| [Apache Maven Enforcer Plugin][26]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][28] | [ASL2][29]                        |
| [Maven Surefire Plugin][30]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][16] |
| [Maven Plugin Plugin][65]                               | [Apache-2.0][16]                  |
| [duplicate-finder-maven-plugin Maven Mojo][32]          | [Apache License 2.0][33]          |
| [Apache Maven Deploy Plugin][34]                        | [Apache-2.0][16]                  |
| [Apache Maven GPG Plugin][35]                           | [Apache-2.0][16]                  |
| [Apache Maven Source Plugin][36]                        | [Apache License, Version 2.0][16] |
| [Apache Maven Javadoc Plugin][37]                       | [Apache-2.0][16]                  |
| [Nexus Staging Maven Plugin][38]                        | [Eclipse Public License][39]      |
| [Apache Maven Dependency Plugin][66]                    | [Apache-2.0][16]                  |
| [Maven Failsafe Plugin][56]                             | [Apache-2.0][16]                  |
| [JaCoCo :: Maven Plugin][40]                            | [Eclipse Public License 2.0][41]  |
| [error-code-crawler-maven-plugin][42]                   | [MIT License][43]                 |
| [Reproducible Build Maven Plugin][44]                   | [Apache 2.0][29]                  |

## Project Keeper Shared Test Setup

### Compile Dependencies

| Dependency                                | License                           |
| ----------------------------------------- | --------------------------------- |
| [Project Keeper shared model classes][45] | [The MIT License][46]             |
| [SnakeYAML][50]                           | [Apache License, Version 2.0][29] |
| [Hamcrest][12]                            | [BSD License 3][13]               |
| [Maven Model][51]                         | [Apache-2.0][16]                  |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][22]                       | [GNU LGPL 3][23]                  |
| [Apache Maven Toolchains Plugin][24]                    | [Apache License, Version 2.0][16] |
| [Apache Maven Compiler Plugin][25]                      | [Apache-2.0][16]                  |
| [Apache Maven Enforcer Plugin][26]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][28] | [ASL2][29]                        |
| [Maven Surefire Plugin][30]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][16] |
| [duplicate-finder-maven-plugin Maven Mojo][32]          | [Apache License 2.0][33]          |
| [JaCoCo :: Maven Plugin][40]                            | [Eclipse Public License 2.0][41]  |
| [error-code-crawler-maven-plugin][42]                   | [MIT License][43]                 |
| [Reproducible Build Maven Plugin][44]                   | [Apache 2.0][29]                  |

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
[10]: https://junit.org/junit5/
[11]: https://www.eclipse.org/legal/epl-v20.html
[12]: http://hamcrest.org/JavaHamcrest/
[13]: http://opensource.org/licenses/BSD-3-Clause
[14]: https://github.com/itsallcode/junit5-system-extensions
[15]: https://www.jqno.nl/equalsverifier
[16]: https://www.apache.org/licenses/LICENSE-2.0.txt
[17]: https://github.com/jparams/to-string-verifier
[18]: http://www.opensource.org/licenses/mit-license.php
[19]: https://github.com/mockito/mockito
[20]: https://opensource.org/licenses/MIT
[21]: http://www.slf4j.org
[22]: http://sonarsource.github.io/sonar-scanner-maven/
[23]: http://www.gnu.org/licenses/lgpl.txt
[24]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[25]: https://maven.apache.org/plugins/maven-compiler-plugin/
[26]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[27]: https://www.mojohaus.org/flatten-maven-plugin/
[28]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[29]: http://www.apache.org/licenses/LICENSE-2.0.txt
[30]: https://maven.apache.org/surefire/maven-surefire-plugin/
[31]: https://www.mojohaus.org/versions/versions-maven-plugin/
[32]: https://basepom.github.io/duplicate-finder-maven-plugin
[33]: http://www.apache.org/licenses/LICENSE-2.0.html
[34]: https://maven.apache.org/plugins/maven-deploy-plugin/
[35]: https://maven.apache.org/plugins/maven-gpg-plugin/
[36]: https://maven.apache.org/plugins/maven-source-plugin/
[37]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[38]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[39]: http://www.eclipse.org/legal/epl-v10.html
[40]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[41]: https://www.eclipse.org/legal/epl-2.0/
[42]: https://github.com/exasol/error-code-crawler-maven-plugin/
[43]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[44]: http://zlika.github.io/reproducible-build-maven-plugin
[45]: https://github.com/exasol/project-keeper/
[46]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[47]: https://www.xmlunit.org/
[48]: https://github.com/Steppschuh/Java-Markdown-Generator
[49]: https://github.com/vdurmont/semver4j
[50]: https://bitbucket.org/snakeyaml/snakeyaml
[51]: https://maven.apache.org/ref/3.9.6/maven-model/
[52]: https://github.com/exasol/maven-project-version-getter/
[53]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[54]: https://github.com/exasol/maven-plugin-integration-testing/
[55]: https://github.com/exasol/maven-plugin-integration-testing/blob/main/LICENSE
[56]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[57]: https://maven.apache.org/plugins/maven-jar-plugin/
[58]: https://maven.apache.org/plugins/maven-assembly-plugin/
[59]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[60]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[61]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[62]: https://maven.apache.org/ref/3.9.6/maven-plugin-api/
[63]: https://maven.apache.org/ref/3.9.6/maven-core/
[64]: https://www.eclemma.org/jacoco/index.html
[65]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[66]: https://maven.apache.org/plugins/maven-dependency-plugin/
