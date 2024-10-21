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
| [JSON-B API][12]                 | [Eclipse Public License 2.0][10]; [GNU General Public License, version 2 with the GNU Classpath Exception][11] |
| [Yasson][13]                     | [Eclipse Public License v. 2.0][14]; [Eclipse Distribution License v. 1.0][15]                                 |
| [error-reporting-java][16]       | [MIT License][17]                                                                                              |
| [JGit - Core][18]                | Eclipse Distribution License (New BSD License)                                                                 |

### Test Dependencies

| Dependency                                 | License                           |
| ------------------------------------------ | --------------------------------- |
| [JUnit Jupiter Engine][19]                 | [Eclipse Public License v2.0][20] |
| [JUnit Jupiter Params][19]                 | [Eclipse Public License v2.0][20] |
| [Hamcrest][21]                             | [BSD-3-Clause][22]                |
| [JUnit5 System Extensions][23]             | [Eclipse Public License v2.0][14] |
| [EqualsVerifier \| release normal jar][24] | [Apache License, Version 2.0][1]  |
| [to-string-verifier][25]                   | [MIT License][26]                 |
| [mockito-core][27]                         | [MIT][28]                         |
| [SLF4J JDK14 Binding][29]                  | [MIT License][26]                 |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [SonarQube Scanner for Maven][30]                      | [GNU LGPL 3][31]                 |
| [Apache Maven Toolchains Plugin][32]                   | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][33]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][34]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][35]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][36]                            | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][37]         | [Apache License 2.0][38]         |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][39]                          | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][40]                       | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][41]                      | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][42]                       | [Eclipse Public License][43]     |
| [JaCoCo :: Maven Plugin][44]                           | [EPL-2.0][45]                    |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][46]                  | [Apache 2.0][8]                  |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                       |
| ----------------------------------------- | --------------------------------------------- |
| [Project Keeper shared model classes][47] | [The MIT License][48]                         |
| [org.xmlunit:xmlunit-core][49]            | [The Apache Software License, Version 2.0][8] |
| [error-reporting-java][16]                | [MIT License][17]                             |
| [Markdown Generator][50]                  | [The Apache Software License, Version 2.0][8] |
| [semver4j][51]                            | [The MIT License][26]                         |
| [SnakeYAML][52]                           | [Apache License, Version 2.0][8]              |
| [SnakeYAML Engine][53]                    | [Apache License, Version 2.0][8]              |
| [Maven Model][54]                         | [Apache-2.0][1]                               |
| [jcabi-github][55]                        | [BSD][56]                                     |

### Test Dependencies

| Dependency                                 | License                                       |
| ------------------------------------------ | --------------------------------------------- |
| [Project Keeper shared test setup][47]     | [The MIT License][48]                         |
| [Maven Project Version Getter][57]         | [MIT License][58]                             |
| [JUnit Jupiter Engine][19]                 | [Eclipse Public License v2.0][20]             |
| [JUnit Jupiter Params][19]                 | [Eclipse Public License v2.0][20]             |
| [Hamcrest][21]                             | [BSD-3-Clause][22]                            |
| [org.xmlunit:xmlunit-matchers][49]         | [The Apache Software License, Version 2.0][8] |
| [mockito-junit-jupiter][27]                | [MIT][28]                                     |
| [Maven Plugin Integration Testing][59]     | [MIT License][60]                             |
| [EqualsVerifier \| release normal jar][24] | [Apache License, Version 2.0][1]              |
| [to-string-verifier][25]                   | [MIT License][26]                             |
| [junit-pioneer][61]                        | [Eclipse Public License v2.0][20]             |
| [SLF4J JDK14 Binding][29]                  | [MIT License][26]                             |

### Runtime Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Java project crawler][47] | [The MIT License][48] |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [SonarQube Scanner for Maven][30]                      | [GNU LGPL 3][31]                 |
| [Apache Maven Toolchains Plugin][32]                   | [Apache-2.0][1]                  |
| [Apache Maven JAR Plugin][62]                          | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][33]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][34]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][35]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][36]                            | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][37]         | [Apache License 2.0][38]         |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][39]                          | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][40]                       | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][41]                      | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][42]                       | [Eclipse Public License][43]     |
| [Maven Failsafe Plugin][63]                            | [Apache-2.0][1]                  |
| [JaCoCo :: Maven Plugin][44]                           | [EPL-2.0][45]                    |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][46]                  | [Apache 2.0][8]                  |

## Project Keeper Command Line Interface

### Compile Dependencies

| Dependency                 | License               |
| -------------------------- | --------------------- |
| [Project Keeper Core][47]  | [The MIT License][48] |
| [error-reporting-java][16] | [MIT License][17]     |
| [Maven Model][54]          | [Apache-2.0][1]       |

### Test Dependencies

| Dependency                             | License                           |
| -------------------------------------- | --------------------------------- |
| [Project Keeper shared test setup][47] | [The MIT License][48]             |
| [JUnit Jupiter Engine][19]             | [Eclipse Public License v2.0][20] |
| [JUnit Jupiter Params][19]             | [Eclipse Public License v2.0][20] |
| [Hamcrest][21]                         | [BSD-3-Clause][22]                |
| [Maven Project Version Getter][57]     | [MIT License][58]                 |

### Runtime Dependencies

| Dependency                | License           |
| ------------------------- | ----------------- |
| [SLF4J JDK14 Binding][29] | [MIT License][26] |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [SonarQube Scanner for Maven][30]                      | [GNU LGPL 3][31]                 |
| [Apache Maven Toolchains Plugin][32]                   | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][33]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][34]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][35]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][36]                            | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][37]         | [Apache License 2.0][38]         |
| [Apache Maven Assembly Plugin][64]                     | [Apache-2.0][1]                  |
| [Apache Maven JAR Plugin][62]                          | [Apache-2.0][1]                  |
| [Artifact reference checker and unifier][65]           | [MIT License][66]                |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][39]                          | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][40]                       | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][41]                      | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][42]                       | [Eclipse Public License][43]     |
| [Maven Failsafe Plugin][63]                            | [Apache-2.0][1]                  |
| [JaCoCo :: Maven Plugin][44]                           | [EPL-2.0][45]                    |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][46]                  | [Apache 2.0][8]                  |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Core][47]                 | [The MIT License][48] |
| [Maven Plugin Tools Java Annotations][67] | [Apache-2.0][1]       |
| [Maven Plugin API][68]                    | [Apache-2.0][1]       |
| [Maven Core][69]                          | [Apache-2.0][1]       |
| [error-reporting-java][16]                | [MIT License][17]     |

### Test Dependencies

| Dependency                             | License                                       |
| -------------------------------------- | --------------------------------------------- |
| [Maven Project Version Getter][57]     | [MIT License][58]                             |
| [JUnit Jupiter Engine][19]             | [Eclipse Public License v2.0][20]             |
| [JUnit Jupiter Params][19]             | [Eclipse Public License v2.0][20]             |
| [Hamcrest][21]                         | [BSD-3-Clause][22]                            |
| [org.xmlunit:xmlunit-matchers][49]     | [The Apache Software License, Version 2.0][8] |
| [mockito-core][27]                     | [MIT][28]                                     |
| [Maven Plugin Integration Testing][59] | [MIT License][60]                             |
| [SLF4J JDK14 Binding][29]              | [MIT License][26]                             |
| [JaCoCo :: Agent][70]                  | [EPL-2.0][45]                                 |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [SonarQube Scanner for Maven][30]                      | [GNU LGPL 3][31]                 |
| [Apache Maven Toolchains Plugin][32]                   | [Apache-2.0][1]                  |
| [Maven Plugin Plugin][71]                              | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][33]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][34]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][35]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][36]                            | [Apache License, Version 2.0][1] |
| [Apache Maven JAR Plugin][62]                          | [Apache-2.0][1]                  |
| [duplicate-finder-maven-plugin Maven Mojo][37]         | [Apache License 2.0][38]         |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][39]                          | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][40]                       | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][41]                      | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][42]                       | [Eclipse Public License][43]     |
| [Apache Maven Dependency Plugin][72]                   | [Apache-2.0][1]                  |
| [Maven Failsafe Plugin][63]                            | [Apache-2.0][1]                  |
| [JaCoCo :: Maven Plugin][44]                           | [EPL-2.0][45]                    |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][46]                  | [Apache 2.0][8]                  |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project Keeper shared model classes][47] | [The MIT License][48]                          |
| [Maven Plugin Tools Java Annotations][67] | [Apache-2.0][1]                                |
| [Maven Plugin API][68]                    | [Apache-2.0][1]                                |
| [error-reporting-java][16]                | [MIT License][17]                              |
| [JGit - Core][18]                         | Eclipse Distribution License (New BSD License) |
| [semver4j][51]                            | [The MIT License][26]                          |
| [Maven Core][69]                          | [Apache-2.0][1]                                |

### Test Dependencies

| Dependency                             | License                                       |
| -------------------------------------- | --------------------------------------------- |
| [Maven Project Version Getter][57]     | [MIT License][58]                             |
| [JUnit Jupiter Engine][19]             | [Eclipse Public License v2.0][20]             |
| [JUnit Jupiter Params][19]             | [Eclipse Public License v2.0][20]             |
| [Hamcrest][21]                         | [BSD-3-Clause][22]                            |
| [org.xmlunit:xmlunit-matchers][49]     | [The Apache Software License, Version 2.0][8] |
| [SLF4J JDK14 Binding][29]              | [MIT License][26]                             |
| [mockito-core][27]                     | [MIT][28]                                     |
| [mockito-junit-jupiter][27]            | [MIT][28]                                     |
| [Maven Plugin Integration Testing][59] | [MIT License][60]                             |
| [JaCoCo :: Agent][70]                  | [EPL-2.0][45]                                 |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [SonarQube Scanner for Maven][30]                      | [GNU LGPL 3][31]                 |
| [Apache Maven Toolchains Plugin][32]                   | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][33]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][34]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][35]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][36]                            | [Apache License, Version 2.0][1] |
| [Maven Plugin Plugin][71]                              | [Apache-2.0][1]                  |
| [duplicate-finder-maven-plugin Maven Mojo][37]         | [Apache License 2.0][38]         |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][39]                          | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][40]                       | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][41]                      | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][42]                       | [Eclipse Public License][43]     |
| [Apache Maven Dependency Plugin][72]                   | [Apache-2.0][1]                  |
| [Maven Failsafe Plugin][63]                            | [Apache-2.0][1]                  |
| [JaCoCo :: Maven Plugin][44]                           | [EPL-2.0][45]                    |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][46]                  | [Apache 2.0][8]                  |

## Project Keeper Shared Test Setup

### Compile Dependencies

| Dependency                                | License                          |
| ----------------------------------------- | -------------------------------- |
| [Project Keeper shared model classes][47] | [The MIT License][48]            |
| [SnakeYAML][52]                           | [Apache License, Version 2.0][8] |
| [Hamcrest][21]                            | [BSD-3-Clause][22]               |
| [Maven Model][54]                         | [Apache-2.0][1]                  |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [SonarQube Scanner for Maven][30]                      | [GNU LGPL 3][31]                 |
| [Apache Maven Toolchains Plugin][32]                   | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][33]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][34]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][35]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][36]                            | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][37]         | [Apache License 2.0][38]         |
| [JaCoCo :: Maven Plugin][44]                           | [EPL-2.0][45]                    |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][46]                  | [Apache 2.0][8]                  |

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
[12]: https://jakartaee.github.io/jsonb-api
[13]: https://projects.eclipse.org/projects/ee4j.yasson
[14]: http://www.eclipse.org/legal/epl-v20.html
[15]: http://www.eclipse.org/org/documents/edl-v10.php
[16]: https://github.com/exasol/error-reporting-java/
[17]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[18]: https://www.eclipse.org/jgit/
[19]: https://junit.org/junit5/
[20]: https://www.eclipse.org/legal/epl-v20.html
[21]: http://hamcrest.org/JavaHamcrest/
[22]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[23]: https://github.com/itsallcode/junit5-system-extensions
[24]: https://www.jqno.nl/equalsverifier
[25]: https://github.com/jparams/to-string-verifier
[26]: http://www.opensource.org/licenses/mit-license.php
[27]: https://github.com/mockito/mockito
[28]: https://opensource.org/licenses/MIT
[29]: http://www.slf4j.org
[30]: http://sonarsource.github.io/sonar-scanner-maven/
[31]: http://www.gnu.org/licenses/lgpl.txt
[32]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[33]: https://maven.apache.org/plugins/maven-compiler-plugin/
[34]: https://www.mojohaus.org/flatten-maven-plugin/
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
[54]: https://maven.apache.org/ref/3.9.9/maven-model/
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
[68]: https://maven.apache.org/ref/3.9.9/maven-plugin-api/
[69]: https://maven.apache.org/ref/3.9.9/maven-core/
[70]: https://www.eclemma.org/jacoco/index.html
[71]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[72]: https://maven.apache.org/plugins/maven-dependency-plugin/
