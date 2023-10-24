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
| [mockito-core][17]                         | [MIT][18]                         |
| [SLF4J JDK14 Binding][19]                  | [MIT License][20]                 |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][21]                       | [GNU LGPL 3][22]                  |
| [Apache Maven Compiler Plugin][23]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][24]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][25] | [ASL2][26]                        |
| [Maven Surefire Plugin][27]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][28]                             | [Apache License, Version 2.0][16] |
| [duplicate-finder-maven-plugin Maven Mojo][29]          | [Apache License 2.0][30]          |
| [Apache Maven Deploy Plugin][31]                        | [Apache-2.0][16]                  |
| [Apache Maven GPG Plugin][32]                           | [Apache-2.0][16]                  |
| [Apache Maven Source Plugin][33]                        | [Apache License, Version 2.0][16] |
| [Apache Maven Javadoc Plugin][34]                       | [Apache-2.0][16]                  |
| [Nexus Staging Maven Plugin][35]                        | [Eclipse Public License][36]      |
| [JaCoCo :: Maven Plugin][37]                            | [Eclipse Public License 2.0][38]  |
| [error-code-crawler-maven-plugin][39]                   | [MIT License][40]                 |
| [Reproducible Build Maven Plugin][41]                   | [Apache 2.0][26]                  |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project-Keeper shared model classes][42] | [The MIT License][43]                          |
| [org.xmlunit:xmlunit-core][44]            | [The Apache Software License, Version 2.0][26] |
| [error-reporting-java][7]                 | [MIT License][8]                               |
| [Markdown Generator][45]                  | [The Apache Software License, Version 2.0][26] |
| [semver4j][46]                            | [The MIT License][20]                          |
| [SnakeYAML][47]                           | [Apache License, Version 2.0][26]              |
| [Maven Model][48]                         | [Apache-2.0][16]                               |

### Test Dependencies

| Dependency                                 | License                                        |
| ------------------------------------------ | ---------------------------------------------- |
| [Project Keeper shared test setup][42]     | [The MIT License][43]                          |
| [Maven Project Version Getter][49]         | [MIT License][50]                              |
| [JUnit Jupiter Engine][10]                 | [Eclipse Public License v2.0][11]              |
| [JUnit Jupiter Params][10]                 | [Eclipse Public License v2.0][11]              |
| [Hamcrest][12]                             | [BSD License 3][13]                            |
| [org.xmlunit:xmlunit-matchers][44]         | [The Apache Software License, Version 2.0][26] |
| [mockito-junit-jupiter][17]                | [MIT][18]                                      |
| [Maven Plugin Integration Testing][51]     | [MIT License][52]                              |
| [EqualsVerifier \| release normal jar][15] | [Apache License, Version 2.0][16]              |
| [SLF4J JDK14 Binding][19]                  | [MIT License][20]                              |

### Runtime Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project keeper Java project crawler][42] | [The MIT License][43] |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][21]                       | [GNU LGPL 3][22]                  |
| [Apache Maven Compiler Plugin][23]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][24]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][25] | [ASL2][26]                        |
| [Maven Surefire Plugin][27]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][28]                             | [Apache License, Version 2.0][16] |
| [duplicate-finder-maven-plugin Maven Mojo][29]          | [Apache License 2.0][30]          |
| [Apache Maven Deploy Plugin][31]                        | [Apache-2.0][16]                  |
| [Apache Maven GPG Plugin][32]                           | [Apache-2.0][16]                  |
| [Apache Maven Source Plugin][33]                        | [Apache License, Version 2.0][16] |
| [Apache Maven Javadoc Plugin][34]                       | [Apache-2.0][16]                  |
| [Nexus Staging Maven Plugin][35]                        | [Eclipse Public License][36]      |
| [Maven Failsafe Plugin][53]                             | [Apache-2.0][16]                  |
| [JaCoCo :: Maven Plugin][37]                            | [Eclipse Public License 2.0][38]  |
| [error-code-crawler-maven-plugin][39]                   | [MIT License][40]                 |
| [Reproducible Build Maven Plugin][41]                   | [Apache 2.0][26]                  |
| [Apache Maven JAR Plugin][54]                           | [Apache License, Version 2.0][16] |

## Project Keeper Command Line Interface

### Compile Dependencies

| Dependency                | License               |
| ------------------------- | --------------------- |
| [Project keeper core][42] | [The MIT License][43] |
| [error-reporting-java][7] | [MIT License][8]      |
| [Maven Model][48]         | [Apache-2.0][16]      |

### Test Dependencies

| Dependency                             | License                           |
| -------------------------------------- | --------------------------------- |
| [Project Keeper shared test setup][42] | [The MIT License][43]             |
| [JUnit Jupiter Engine][10]             | [Eclipse Public License v2.0][11] |
| [JUnit Jupiter Params][10]             | [Eclipse Public License v2.0][11] |
| [Hamcrest][12]                         | [BSD License 3][13]               |
| [Maven Project Version Getter][49]     | [MIT License][50]                 |

### Runtime Dependencies

| Dependency                | License           |
| ------------------------- | ----------------- |
| [SLF4J JDK14 Binding][19] | [MIT License][20] |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][21]                       | [GNU LGPL 3][22]                  |
| [Apache Maven Compiler Plugin][23]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][24]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][25] | [ASL2][26]                        |
| [Maven Surefire Plugin][27]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][28]                             | [Apache License, Version 2.0][16] |
| [duplicate-finder-maven-plugin Maven Mojo][29]          | [Apache License 2.0][30]          |
| [Apache Maven Assembly Plugin][55]                      | [Apache-2.0][16]                  |
| [Apache Maven JAR Plugin][54]                           | [Apache License, Version 2.0][16] |
| [Artifact reference checker and unifier][56]            | [MIT License][57]                 |
| [Apache Maven Deploy Plugin][31]                        | [Apache-2.0][16]                  |
| [Apache Maven GPG Plugin][32]                           | [Apache-2.0][16]                  |
| [Apache Maven Source Plugin][33]                        | [Apache License, Version 2.0][16] |
| [Apache Maven Javadoc Plugin][34]                       | [Apache-2.0][16]                  |
| [Nexus Staging Maven Plugin][35]                        | [Eclipse Public License][36]      |
| [Maven Failsafe Plugin][53]                             | [Apache-2.0][16]                  |
| [JaCoCo :: Maven Plugin][37]                            | [Eclipse Public License 2.0][38]  |
| [error-code-crawler-maven-plugin][39]                   | [MIT License][40]                 |
| [Reproducible Build Maven Plugin][41]                   | [Apache 2.0][26]                  |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project keeper core][42]                 | [The MIT License][43] |
| [Maven Plugin Tools Java Annotations][58] | [Apache-2.0][16]      |
| [Maven Plugin API][59]                    | [Apache-2.0][16]      |
| [Maven Core][60]                          | [Apache-2.0][16]      |
| [error-reporting-java][7]                 | [MIT License][8]      |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][49]     | [MIT License][50]                              |
| [JUnit Jupiter Engine][10]             | [Eclipse Public License v2.0][11]              |
| [JUnit Jupiter Params][10]             | [Eclipse Public License v2.0][11]              |
| [Hamcrest][12]                         | [BSD License 3][13]                            |
| [org.xmlunit:xmlunit-matchers][44]     | [The Apache Software License, Version 2.0][26] |
| [mockito-core][17]                     | [MIT][18]                                      |
| [Maven Plugin Integration Testing][51] | [MIT License][52]                              |
| [SLF4J JDK14 Binding][19]              | [MIT License][20]                              |
| [JaCoCo :: Agent][61]                  | [Eclipse Public License 2.0][38]               |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][21]                       | [GNU LGPL 3][22]                  |
| [Apache Maven Compiler Plugin][23]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][24]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][25] | [ASL2][26]                        |
| [Maven Surefire Plugin][27]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][28]                             | [Apache License, Version 2.0][16] |
| [Maven Plugin Plugin][62]                               | [Apache-2.0][16]                  |
| [Apache Maven JAR Plugin][54]                           | [Apache License, Version 2.0][16] |
| [duplicate-finder-maven-plugin Maven Mojo][29]          | [Apache License 2.0][30]          |
| [Apache Maven Deploy Plugin][31]                        | [Apache-2.0][16]                  |
| [Apache Maven GPG Plugin][32]                           | [Apache-2.0][16]                  |
| [Apache Maven Source Plugin][33]                        | [Apache License, Version 2.0][16] |
| [Apache Maven Javadoc Plugin][34]                       | [Apache-2.0][16]                  |
| [Nexus Staging Maven Plugin][35]                        | [Eclipse Public License][36]      |
| [Apache Maven Dependency Plugin][63]                    | [Apache-2.0][16]                  |
| [Maven Failsafe Plugin][53]                             | [Apache-2.0][16]                  |
| [JaCoCo :: Maven Plugin][37]                            | [Eclipse Public License 2.0][38]  |
| [error-code-crawler-maven-plugin][39]                   | [MIT License][40]                 |
| [Reproducible Build Maven Plugin][41]                   | [Apache 2.0][26]                  |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project-Keeper shared model classes][42] | [The MIT License][43]                          |
| [Maven Plugin Tools Java Annotations][58] | [Apache-2.0][16]                               |
| [Maven Plugin API][59]                    | [Apache-2.0][16]                               |
| [error-reporting-java][7]                 | [MIT License][8]                               |
| [JGit - Core][9]                          | Eclipse Distribution License (New BSD License) |
| [semver4j][46]                            | [The MIT License][20]                          |
| [Maven Core][60]                          | [Apache-2.0][16]                               |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][49]     | [MIT License][50]                              |
| [JUnit Jupiter Engine][10]             | [Eclipse Public License v2.0][11]              |
| [JUnit Jupiter Params][10]             | [Eclipse Public License v2.0][11]              |
| [Hamcrest][12]                         | [BSD License 3][13]                            |
| [org.xmlunit:xmlunit-matchers][44]     | [The Apache Software License, Version 2.0][26] |
| [SLF4J JDK14 Binding][19]              | [MIT License][20]                              |
| [mockito-core][17]                     | [MIT][18]                                      |
| [mockito-junit-jupiter][17]            | [MIT][18]                                      |
| [Maven Plugin Integration Testing][51] | [MIT License][52]                              |
| [JaCoCo :: Agent][61]                  | [Eclipse Public License 2.0][38]               |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][21]                       | [GNU LGPL 3][22]                  |
| [Apache Maven Compiler Plugin][23]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][24]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][25] | [ASL2][26]                        |
| [Maven Surefire Plugin][27]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][28]                             | [Apache License, Version 2.0][16] |
| [Maven Plugin Plugin][62]                               | [Apache-2.0][16]                  |
| [duplicate-finder-maven-plugin Maven Mojo][29]          | [Apache License 2.0][30]          |
| [Apache Maven Deploy Plugin][31]                        | [Apache-2.0][16]                  |
| [Apache Maven GPG Plugin][32]                           | [Apache-2.0][16]                  |
| [Apache Maven Source Plugin][33]                        | [Apache License, Version 2.0][16] |
| [Apache Maven Javadoc Plugin][34]                       | [Apache-2.0][16]                  |
| [Nexus Staging Maven Plugin][35]                        | [Eclipse Public License][36]      |
| [Apache Maven Dependency Plugin][63]                    | [Apache-2.0][16]                  |
| [Maven Failsafe Plugin][53]                             | [Apache-2.0][16]                  |
| [JaCoCo :: Maven Plugin][37]                            | [Eclipse Public License 2.0][38]  |
| [error-code-crawler-maven-plugin][39]                   | [MIT License][40]                 |
| [Reproducible Build Maven Plugin][41]                   | [Apache 2.0][26]                  |

## Project Keeper Shared Test Setup

### Compile Dependencies

| Dependency                                | License                           |
| ----------------------------------------- | --------------------------------- |
| [Project-Keeper shared model classes][42] | [The MIT License][43]             |
| [SnakeYAML][47]                           | [Apache License, Version 2.0][26] |
| [Hamcrest][12]                            | [BSD License 3][13]               |
| [Maven Model][48]                         | [Apache-2.0][16]                  |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][21]                       | [GNU LGPL 3][22]                  |
| [Apache Maven Compiler Plugin][23]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][24]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][25] | [ASL2][26]                        |
| [Maven Surefire Plugin][27]                             | [Apache-2.0][16]                  |
| [Versions Maven Plugin][28]                             | [Apache License, Version 2.0][16] |
| [duplicate-finder-maven-plugin Maven Mojo][29]          | [Apache License 2.0][30]          |
| [JaCoCo :: Maven Plugin][37]                            | [Eclipse Public License 2.0][38]  |
| [error-code-crawler-maven-plugin][39]                   | [MIT License][40]                 |
| [Reproducible Build Maven Plugin][41]                   | [Apache 2.0][26]                  |

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
[17]: https://github.com/mockito/mockito
[18]: https://github.com/mockito/mockito/blob/main/LICENSE
[19]: http://www.slf4j.org
[20]: http://www.opensource.org/licenses/mit-license.php
[21]: http://sonarsource.github.io/sonar-scanner-maven/
[22]: http://www.gnu.org/licenses/lgpl.txt
[23]: https://maven.apache.org/plugins/maven-compiler-plugin/
[24]: https://www.mojohaus.org/flatten-maven-plugin/
[25]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[26]: http://www.apache.org/licenses/LICENSE-2.0.txt
[27]: https://maven.apache.org/surefire/maven-surefire-plugin/
[28]: https://www.mojohaus.org/versions/versions-maven-plugin/
[29]: https://basepom.github.io/duplicate-finder-maven-plugin
[30]: http://www.apache.org/licenses/LICENSE-2.0.html
[31]: https://maven.apache.org/plugins/maven-deploy-plugin/
[32]: https://maven.apache.org/plugins/maven-gpg-plugin/
[33]: https://maven.apache.org/plugins/maven-source-plugin/
[34]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[35]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[36]: http://www.eclipse.org/legal/epl-v10.html
[37]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[38]: https://www.eclipse.org/legal/epl-2.0/
[39]: https://github.com/exasol/error-code-crawler-maven-plugin/
[40]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[41]: http://zlika.github.io/reproducible-build-maven-plugin
[42]: https://github.com/exasol/project-keeper/
[43]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[44]: https://www.xmlunit.org/
[45]: https://github.com/Steppschuh/Java-Markdown-Generator
[46]: https://github.com/vdurmont/semver4j
[47]: https://bitbucket.org/snakeyaml/snakeyaml
[48]: https://maven.apache.org/ref/3.9.5/maven-model/
[49]: https://github.com/exasol/maven-project-version-getter/
[50]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[51]: https://github.com/exasol/maven-plugin-integration-testing/
[52]: https://github.com/exasol/maven-plugin-integration-testing/blob/main/LICENSE
[53]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[54]: https://maven.apache.org/plugins/maven-jar-plugin/
[55]: https://maven.apache.org/plugins/maven-assembly-plugin/
[56]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[57]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[58]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[59]: https://maven.apache.org/ref/3.9.5/maven-plugin-api/
[60]: https://maven.apache.org/ref/3.9.5/maven-core/
[61]: https://www.eclemma.org/jacoco/index.html
[62]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[63]: https://maven.apache.org/plugins/maven-dependency-plugin/
