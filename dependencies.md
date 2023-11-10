<!-- @formatter:off -->
# Dependencies

## Project-keeper Shared Model Classes

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
| [Apache Maven Compiler Plugin][24]                      | [Apache-2.0][16]                  |
| [Apache Maven Enforcer Plugin][25]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][26]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][27] | [ASL2][28]                        |
| [Maven Surefire Plugin][29]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][30]                             | [Apache License, Version 2.0][16] |
| [duplicate-finder-maven-plugin Maven Mojo][31]          | [Apache License 2.0][32]          |
| [Apache Maven Deploy Plugin][33]                        | [Apache-2.0][16]                  |
| [Apache Maven GPG Plugin][34]                           | [Apache-2.0][16]                  |
| [Apache Maven Source Plugin][35]                        | [Apache License, Version 2.0][16] |
| [Apache Maven Javadoc Plugin][36]                       | [Apache-2.0][16]                  |
| [Nexus Staging Maven Plugin][37]                        | [Eclipse Public License][38]      |
| [JaCoCo :: Maven Plugin][39]                            | [Eclipse Public License 2.0][40]  |
| [error-code-crawler-maven-plugin][41]                   | [MIT License][42]                 |
| [Reproducible Build Maven Plugin][43]                   | [Apache 2.0][28]                  |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project-Keeper shared model classes][44] | [The MIT License][45]                          |
| [org.xmlunit:xmlunit-core][46]            | [The Apache Software License, Version 2.0][28] |
| [error-reporting-java][7]                 | [MIT License][8]                               |
| [Markdown Generator][47]                  | [The Apache Software License, Version 2.0][28] |
| [semver4j][48]                            | [The MIT License][18]                          |
| [SnakeYAML][49]                           | [Apache License, Version 2.0][28]              |
| [Maven Model][50]                         | [Apache-2.0][16]                               |

### Test Dependencies

| Dependency                                 | License                                        |
| ------------------------------------------ | ---------------------------------------------- |
| [Project Keeper shared test setup][44]     | [The MIT License][45]                          |
| [Maven Project Version Getter][51]         | [MIT License][52]                              |
| [JUnit Jupiter Engine][10]                 | [Eclipse Public License v2.0][11]              |
| [JUnit Jupiter Params][10]                 | [Eclipse Public License v2.0][11]              |
| [Hamcrest][12]                             | [BSD License 3][13]                            |
| [org.xmlunit:xmlunit-matchers][46]         | [The Apache Software License, Version 2.0][28] |
| [mockito-junit-jupiter][19]                | [MIT][20]                                      |
| [Maven Plugin Integration Testing][53]     | [MIT License][54]                              |
| [EqualsVerifier \| release normal jar][15] | [Apache License, Version 2.0][16]              |
| [to-string-verifier][17]                   | [MIT License][18]                              |
| [SLF4J JDK14 Binding][21]                  | [MIT License][18]                              |

### Runtime Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project keeper Java project crawler][44] | [The MIT License][45] |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][22]                       | [GNU LGPL 3][23]                  |
| [Apache Maven Compiler Plugin][24]                      | [Apache-2.0][16]                  |
| [Apache Maven Enforcer Plugin][25]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][26]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][27] | [ASL2][28]                        |
| [Maven Surefire Plugin][29]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][30]                             | [Apache License, Version 2.0][16] |
| [duplicate-finder-maven-plugin Maven Mojo][31]          | [Apache License 2.0][32]          |
| [Apache Maven Deploy Plugin][33]                        | [Apache-2.0][16]                  |
| [Apache Maven GPG Plugin][34]                           | [Apache-2.0][16]                  |
| [Apache Maven Source Plugin][35]                        | [Apache License, Version 2.0][16] |
| [Apache Maven Javadoc Plugin][36]                       | [Apache-2.0][16]                  |
| [Nexus Staging Maven Plugin][37]                        | [Eclipse Public License][38]      |
| [Maven Failsafe Plugin][55]                             | [Apache-2.0][16]                  |
| [JaCoCo :: Maven Plugin][39]                            | [Eclipse Public License 2.0][40]  |
| [error-code-crawler-maven-plugin][41]                   | [MIT License][42]                 |
| [Reproducible Build Maven Plugin][43]                   | [Apache 2.0][28]                  |
| [Apache Maven JAR Plugin][56]                           | [Apache License, Version 2.0][16] |

## Project Keeper Command Line Interface

### Compile Dependencies

| Dependency                | License               |
| ------------------------- | --------------------- |
| [Project keeper core][44] | [The MIT License][45] |
| [error-reporting-java][7] | [MIT License][8]      |
| [Maven Model][50]         | [Apache-2.0][16]      |

### Test Dependencies

| Dependency                             | License                           |
| -------------------------------------- | --------------------------------- |
| [Project Keeper shared test setup][44] | [The MIT License][45]             |
| [JUnit Jupiter Engine][10]             | [Eclipse Public License v2.0][11] |
| [JUnit Jupiter Params][10]             | [Eclipse Public License v2.0][11] |
| [Hamcrest][12]                         | [BSD License 3][13]               |
| [Maven Project Version Getter][51]     | [MIT License][52]                 |

### Runtime Dependencies

| Dependency                | License           |
| ------------------------- | ----------------- |
| [SLF4J JDK14 Binding][21] | [MIT License][18] |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][22]                       | [GNU LGPL 3][23]                  |
| [Apache Maven Compiler Plugin][24]                      | [Apache-2.0][16]                  |
| [Apache Maven Enforcer Plugin][25]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][26]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][27] | [ASL2][28]                        |
| [Maven Surefire Plugin][29]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][30]                             | [Apache License, Version 2.0][16] |
| [duplicate-finder-maven-plugin Maven Mojo][31]          | [Apache License 2.0][32]          |
| [Apache Maven Assembly Plugin][57]                      | [Apache-2.0][16]                  |
| [Apache Maven JAR Plugin][56]                           | [Apache License, Version 2.0][16] |
| [Artifact reference checker and unifier][58]            | [MIT License][59]                 |
| [Apache Maven Deploy Plugin][33]                        | [Apache-2.0][16]                  |
| [Apache Maven GPG Plugin][34]                           | [Apache-2.0][16]                  |
| [Apache Maven Source Plugin][35]                        | [Apache License, Version 2.0][16] |
| [Apache Maven Javadoc Plugin][36]                       | [Apache-2.0][16]                  |
| [Nexus Staging Maven Plugin][37]                        | [Eclipse Public License][38]      |
| [Maven Failsafe Plugin][55]                             | [Apache-2.0][16]                  |
| [JaCoCo :: Maven Plugin][39]                            | [Eclipse Public License 2.0][40]  |
| [error-code-crawler-maven-plugin][41]                   | [MIT License][42]                 |
| [Reproducible Build Maven Plugin][43]                   | [Apache 2.0][28]                  |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project keeper core][44]                 | [The MIT License][45] |
| [Maven Plugin Tools Java Annotations][60] | [Apache-2.0][16]      |
| [Maven Plugin API][61]                    | [Apache-2.0][16]      |
| [Maven Core][62]                          | [Apache-2.0][16]      |
| [error-reporting-java][7]                 | [MIT License][8]      |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][51]     | [MIT License][52]                              |
| [JUnit Jupiter Engine][10]             | [Eclipse Public License v2.0][11]              |
| [JUnit Jupiter Params][10]             | [Eclipse Public License v2.0][11]              |
| [Hamcrest][12]                         | [BSD License 3][13]                            |
| [org.xmlunit:xmlunit-matchers][46]     | [The Apache Software License, Version 2.0][28] |
| [mockito-core][19]                     | [MIT][20]                                      |
| [Maven Plugin Integration Testing][53] | [MIT License][54]                              |
| [SLF4J JDK14 Binding][21]              | [MIT License][18]                              |
| [JaCoCo :: Agent][63]                  | [Eclipse Public License 2.0][40]               |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][22]                       | [GNU LGPL 3][23]                  |
| [Apache Maven Compiler Plugin][24]                      | [Apache-2.0][16]                  |
| [Apache Maven Enforcer Plugin][25]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][26]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][27] | [ASL2][28]                        |
| [Maven Surefire Plugin][29]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][30]                             | [Apache License, Version 2.0][16] |
| [Maven Plugin Plugin][64]                               | [Apache-2.0][16]                  |
| [Apache Maven JAR Plugin][56]                           | [Apache License, Version 2.0][16] |
| [duplicate-finder-maven-plugin Maven Mojo][31]          | [Apache License 2.0][32]          |
| [Apache Maven Deploy Plugin][33]                        | [Apache-2.0][16]                  |
| [Apache Maven GPG Plugin][34]                           | [Apache-2.0][16]                  |
| [Apache Maven Source Plugin][35]                        | [Apache License, Version 2.0][16] |
| [Apache Maven Javadoc Plugin][36]                       | [Apache-2.0][16]                  |
| [Nexus Staging Maven Plugin][37]                        | [Eclipse Public License][38]      |
| [Apache Maven Dependency Plugin][65]                    | [Apache-2.0][16]                  |
| [Maven Failsafe Plugin][55]                             | [Apache-2.0][16]                  |
| [JaCoCo :: Maven Plugin][39]                            | [Eclipse Public License 2.0][40]  |
| [error-code-crawler-maven-plugin][41]                   | [MIT License][42]                 |
| [Reproducible Build Maven Plugin][43]                   | [Apache 2.0][28]                  |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project-Keeper shared model classes][44] | [The MIT License][45]                          |
| [Maven Plugin Tools Java Annotations][60] | [Apache-2.0][16]                               |
| [Maven Plugin API][61]                    | [Apache-2.0][16]                               |
| [error-reporting-java][7]                 | [MIT License][8]                               |
| [JGit - Core][9]                          | Eclipse Distribution License (New BSD License) |
| [semver4j][48]                            | [The MIT License][18]                          |
| [Maven Core][62]                          | [Apache-2.0][16]                               |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][51]     | [MIT License][52]                              |
| [JUnit Jupiter Engine][10]             | [Eclipse Public License v2.0][11]              |
| [JUnit Jupiter Params][10]             | [Eclipse Public License v2.0][11]              |
| [Hamcrest][12]                         | [BSD License 3][13]                            |
| [org.xmlunit:xmlunit-matchers][46]     | [The Apache Software License, Version 2.0][28] |
| [SLF4J JDK14 Binding][21]              | [MIT License][18]                              |
| [mockito-core][19]                     | [MIT][20]                                      |
| [mockito-junit-jupiter][19]            | [MIT][20]                                      |
| [Maven Plugin Integration Testing][53] | [MIT License][54]                              |
| [JaCoCo :: Agent][63]                  | [Eclipse Public License 2.0][40]               |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][22]                       | [GNU LGPL 3][23]                  |
| [Apache Maven Compiler Plugin][24]                      | [Apache-2.0][16]                  |
| [Apache Maven Enforcer Plugin][25]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][26]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][27] | [ASL2][28]                        |
| [Maven Surefire Plugin][29]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][30]                             | [Apache License, Version 2.0][16] |
| [Maven Plugin Plugin][64]                               | [Apache-2.0][16]                  |
| [duplicate-finder-maven-plugin Maven Mojo][31]          | [Apache License 2.0][32]          |
| [Apache Maven Deploy Plugin][33]                        | [Apache-2.0][16]                  |
| [Apache Maven GPG Plugin][34]                           | [Apache-2.0][16]                  |
| [Apache Maven Source Plugin][35]                        | [Apache License, Version 2.0][16] |
| [Apache Maven Javadoc Plugin][36]                       | [Apache-2.0][16]                  |
| [Nexus Staging Maven Plugin][37]                        | [Eclipse Public License][38]      |
| [Apache Maven Dependency Plugin][65]                    | [Apache-2.0][16]                  |
| [Maven Failsafe Plugin][55]                             | [Apache-2.0][16]                  |
| [JaCoCo :: Maven Plugin][39]                            | [Eclipse Public License 2.0][40]  |
| [error-code-crawler-maven-plugin][41]                   | [MIT License][42]                 |
| [Reproducible Build Maven Plugin][43]                   | [Apache 2.0][28]                  |

## Project Keeper Shared Test Setup

### Compile Dependencies

| Dependency                                | License                           |
| ----------------------------------------- | --------------------------------- |
| [Project-Keeper shared model classes][44] | [The MIT License][45]             |
| [SnakeYAML][49]                           | [Apache License, Version 2.0][28] |
| [Hamcrest][12]                            | [BSD License 3][13]               |
| [Maven Model][50]                         | [Apache-2.0][16]                  |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][22]                       | [GNU LGPL 3][23]                  |
| [Apache Maven Compiler Plugin][24]                      | [Apache-2.0][16]                  |
| [Apache Maven Enforcer Plugin][25]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][26]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][27] | [ASL2][28]                        |
| [Maven Surefire Plugin][29]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][30]                             | [Apache License, Version 2.0][16] |
| [duplicate-finder-maven-plugin Maven Mojo][31]          | [Apache License 2.0][32]          |
| [JaCoCo :: Maven Plugin][39]                            | [Eclipse Public License 2.0][40]  |
| [error-code-crawler-maven-plugin][41]                   | [MIT License][42]                 |
| [Reproducible Build Maven Plugin][43]                   | [Apache 2.0][28]                  |

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
[24]: https://maven.apache.org/plugins/maven-compiler-plugin/
[25]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[26]: https://www.mojohaus.org/flatten-maven-plugin/
[27]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[28]: http://www.apache.org/licenses/LICENSE-2.0.txt
[29]: https://maven.apache.org/surefire/maven-surefire-plugin/
[30]: https://www.mojohaus.org/versions/versions-maven-plugin/
[31]: https://basepom.github.io/duplicate-finder-maven-plugin
[32]: http://www.apache.org/licenses/LICENSE-2.0.html
[33]: https://maven.apache.org/plugins/maven-deploy-plugin/
[34]: https://maven.apache.org/plugins/maven-gpg-plugin/
[35]: https://maven.apache.org/plugins/maven-source-plugin/
[36]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[37]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[38]: http://www.eclipse.org/legal/epl-v10.html
[39]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[40]: https://www.eclipse.org/legal/epl-2.0/
[41]: https://github.com/exasol/error-code-crawler-maven-plugin/
[42]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[43]: http://zlika.github.io/reproducible-build-maven-plugin
[44]: https://github.com/exasol/project-keeper/
[45]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[46]: https://www.xmlunit.org/
[47]: https://github.com/Steppschuh/Java-Markdown-Generator
[48]: https://github.com/vdurmont/semver4j
[49]: https://bitbucket.org/snakeyaml/snakeyaml
[50]: https://maven.apache.org/ref/3.9.5/maven-model/
[51]: https://github.com/exasol/maven-project-version-getter/
[52]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[53]: https://github.com/exasol/maven-plugin-integration-testing/
[54]: https://github.com/exasol/maven-plugin-integration-testing/blob/main/LICENSE
[55]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[56]: https://maven.apache.org/plugins/maven-jar-plugin/
[57]: https://maven.apache.org/plugins/maven-assembly-plugin/
[58]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[59]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[60]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[61]: https://maven.apache.org/ref/3.9.5/maven-plugin-api/
[62]: https://maven.apache.org/ref/3.9.5/maven-core/
[63]: https://www.eclemma.org/jacoco/index.html
[64]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[65]: https://maven.apache.org/plugins/maven-dependency-plugin/
