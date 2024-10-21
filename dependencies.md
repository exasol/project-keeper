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
| [Apache Maven Clean Plugin][30]                        | [Apache-2.0][1]                  |
| [Apache Maven Install Plugin][31]                      | [Apache-2.0][1]                  |
| [Apache Maven Resources Plugin][32]                    | [Apache-2.0][1]                  |
| [Apache Maven Site Plugin][33]                         | [Apache License, Version 2.0][1] |
| [SonarQube Scanner for Maven][34]                      | [GNU LGPL 3][35]                 |
| [Apache Maven Toolchains Plugin][36]                   | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][37]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][38]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][39]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][40]                            | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][41]         | [Apache License 2.0][42]         |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][43]                          | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][44]                       | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][45]                      | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][46]                       | [Eclipse Public License][47]     |
| [JaCoCo :: Maven Plugin][48]                           | [EPL-2.0][49]                    |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][50]                  | [Apache 2.0][8]                  |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                       |
| ----------------------------------------- | --------------------------------------------- |
| [Project Keeper shared model classes][51] | [The MIT License][52]                         |
| [org.xmlunit:xmlunit-core][53]            | [The Apache Software License, Version 2.0][8] |
| [error-reporting-java][16]                | [MIT License][17]                             |
| [Markdown Generator][54]                  | [The Apache Software License, Version 2.0][8] |
| [semver4j][55]                            | [The MIT License][26]                         |
| [SnakeYAML][56]                           | [Apache License, Version 2.0][8]              |
| [SnakeYAML Engine][57]                    | [Apache License, Version 2.0][8]              |
| [Maven Model][58]                         | [Apache-2.0][1]                               |
| [jcabi-github][59]                        | [BSD][60]                                     |

### Test Dependencies

| Dependency                                 | License                                       |
| ------------------------------------------ | --------------------------------------------- |
| [Project Keeper shared test setup][51]     | [The MIT License][52]                         |
| [Maven Project Version Getter][61]         | [MIT License][62]                             |
| [JUnit Jupiter Engine][19]                 | [Eclipse Public License v2.0][20]             |
| [JUnit Jupiter Params][19]                 | [Eclipse Public License v2.0][20]             |
| [Hamcrest][21]                             | [BSD-3-Clause][22]                            |
| [org.xmlunit:xmlunit-matchers][53]         | [The Apache Software License, Version 2.0][8] |
| [mockito-junit-jupiter][27]                | [MIT][28]                                     |
| [Maven Plugin Integration Testing][63]     | [MIT License][64]                             |
| [EqualsVerifier \| release normal jar][24] | [Apache License, Version 2.0][1]              |
| [to-string-verifier][25]                   | [MIT License][26]                             |
| [junit-pioneer][65]                        | [Eclipse Public License v2.0][20]             |
| [SLF4J JDK14 Binding][29]                  | [MIT License][26]                             |

### Runtime Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Java project crawler][51] | [The MIT License][52] |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [Apache Maven Clean Plugin][30]                        | [Apache-2.0][1]                  |
| [Apache Maven Install Plugin][31]                      | [Apache-2.0][1]                  |
| [Apache Maven Resources Plugin][32]                    | [Apache-2.0][1]                  |
| [Apache Maven Site Plugin][33]                         | [Apache License, Version 2.0][1] |
| [SonarQube Scanner for Maven][34]                      | [GNU LGPL 3][35]                 |
| [Apache Maven Toolchains Plugin][36]                   | [Apache-2.0][1]                  |
| [Apache Maven JAR Plugin][66]                          | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][37]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][38]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][39]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][40]                            | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][41]         | [Apache License 2.0][42]         |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][43]                          | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][44]                       | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][45]                      | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][46]                       | [Eclipse Public License][47]     |
| [Maven Failsafe Plugin][67]                            | [Apache-2.0][1]                  |
| [JaCoCo :: Maven Plugin][48]                           | [EPL-2.0][49]                    |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][50]                  | [Apache 2.0][8]                  |

## Project Keeper Command Line Interface

### Compile Dependencies

| Dependency                 | License               |
| -------------------------- | --------------------- |
| [Project Keeper Core][51]  | [The MIT License][52] |
| [error-reporting-java][16] | [MIT License][17]     |
| [Maven Model][58]          | [Apache-2.0][1]       |

### Test Dependencies

| Dependency                             | License                           |
| -------------------------------------- | --------------------------------- |
| [Project Keeper shared test setup][51] | [The MIT License][52]             |
| [JUnit Jupiter Engine][19]             | [Eclipse Public License v2.0][20] |
| [JUnit Jupiter Params][19]             | [Eclipse Public License v2.0][20] |
| [Hamcrest][21]                         | [BSD-3-Clause][22]                |
| [Maven Project Version Getter][61]     | [MIT License][62]                 |

### Runtime Dependencies

| Dependency                | License           |
| ------------------------- | ----------------- |
| [SLF4J JDK14 Binding][29] | [MIT License][26] |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [Apache Maven Clean Plugin][30]                        | [Apache-2.0][1]                  |
| [Apache Maven Install Plugin][31]                      | [Apache-2.0][1]                  |
| [Apache Maven Resources Plugin][32]                    | [Apache-2.0][1]                  |
| [Apache Maven Site Plugin][33]                         | [Apache License, Version 2.0][1] |
| [SonarQube Scanner for Maven][34]                      | [GNU LGPL 3][35]                 |
| [Apache Maven Toolchains Plugin][36]                   | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][37]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][38]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][39]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][40]                            | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][41]         | [Apache License 2.0][42]         |
| [Apache Maven Assembly Plugin][68]                     | [Apache-2.0][1]                  |
| [Apache Maven JAR Plugin][66]                          | [Apache-2.0][1]                  |
| [Artifact reference checker and unifier][69]           | [MIT License][70]                |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][43]                          | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][44]                       | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][45]                      | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][46]                       | [Eclipse Public License][47]     |
| [Maven Failsafe Plugin][67]                            | [Apache-2.0][1]                  |
| [JaCoCo :: Maven Plugin][48]                           | [EPL-2.0][49]                    |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][50]                  | [Apache 2.0][8]                  |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Core][51]                 | [The MIT License][52] |
| [Maven Plugin Tools Java Annotations][71] | [Apache-2.0][1]       |
| [Maven Plugin API][72]                    | [Apache-2.0][1]       |
| [Maven Core][73]                          | [Apache-2.0][1]       |
| [error-reporting-java][16]                | [MIT License][17]     |

### Test Dependencies

| Dependency                             | License                                       |
| -------------------------------------- | --------------------------------------------- |
| [Maven Project Version Getter][61]     | [MIT License][62]                             |
| [JUnit Jupiter Engine][19]             | [Eclipse Public License v2.0][20]             |
| [JUnit Jupiter Params][19]             | [Eclipse Public License v2.0][20]             |
| [Hamcrest][21]                         | [BSD-3-Clause][22]                            |
| [org.xmlunit:xmlunit-matchers][53]     | [The Apache Software License, Version 2.0][8] |
| [mockito-core][27]                     | [MIT][28]                                     |
| [Maven Plugin Integration Testing][63] | [MIT License][64]                             |
| [SLF4J JDK14 Binding][29]              | [MIT License][26]                             |
| [JaCoCo :: Agent][74]                  | [EPL-2.0][49]                                 |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [Apache Maven Clean Plugin][30]                        | [Apache-2.0][1]                  |
| [Apache Maven Install Plugin][31]                      | [Apache-2.0][1]                  |
| [Apache Maven Resources Plugin][32]                    | [Apache-2.0][1]                  |
| [Apache Maven Site Plugin][33]                         | [Apache License, Version 2.0][1] |
| [SonarQube Scanner for Maven][34]                      | [GNU LGPL 3][35]                 |
| [Apache Maven Toolchains Plugin][36]                   | [Apache-2.0][1]                  |
| [Maven Plugin Plugin][75]                              | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][37]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][38]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][39]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][40]                            | [Apache License, Version 2.0][1] |
| [Apache Maven JAR Plugin][66]                          | [Apache-2.0][1]                  |
| [duplicate-finder-maven-plugin Maven Mojo][41]         | [Apache License 2.0][42]         |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][43]                          | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][44]                       | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][45]                      | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][46]                       | [Eclipse Public License][47]     |
| [Apache Maven Dependency Plugin][76]                   | [Apache-2.0][1]                  |
| [Maven Failsafe Plugin][67]                            | [Apache-2.0][1]                  |
| [JaCoCo :: Maven Plugin][48]                           | [EPL-2.0][49]                    |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][50]                  | [Apache 2.0][8]                  |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project Keeper shared model classes][51] | [The MIT License][52]                          |
| [Maven Plugin Tools Java Annotations][71] | [Apache-2.0][1]                                |
| [Maven Plugin API][72]                    | [Apache-2.0][1]                                |
| [error-reporting-java][16]                | [MIT License][17]                              |
| [JGit - Core][18]                         | Eclipse Distribution License (New BSD License) |
| [semver4j][55]                            | [The MIT License][26]                          |
| [Maven Core][73]                          | [Apache-2.0][1]                                |

### Test Dependencies

| Dependency                             | License                                       |
| -------------------------------------- | --------------------------------------------- |
| [Maven Project Version Getter][61]     | [MIT License][62]                             |
| [JUnit Jupiter Engine][19]             | [Eclipse Public License v2.0][20]             |
| [JUnit Jupiter Params][19]             | [Eclipse Public License v2.0][20]             |
| [Hamcrest][21]                         | [BSD-3-Clause][22]                            |
| [org.xmlunit:xmlunit-matchers][53]     | [The Apache Software License, Version 2.0][8] |
| [SLF4J JDK14 Binding][29]              | [MIT License][26]                             |
| [mockito-core][27]                     | [MIT][28]                                     |
| [mockito-junit-jupiter][27]            | [MIT][28]                                     |
| [Maven Plugin Integration Testing][63] | [MIT License][64]                             |
| [JaCoCo :: Agent][74]                  | [EPL-2.0][49]                                 |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [Apache Maven Clean Plugin][30]                        | [Apache-2.0][1]                  |
| [Apache Maven Install Plugin][31]                      | [Apache-2.0][1]                  |
| [Apache Maven Resources Plugin][32]                    | [Apache-2.0][1]                  |
| [Apache Maven Site Plugin][33]                         | [Apache License, Version 2.0][1] |
| [SonarQube Scanner for Maven][34]                      | [GNU LGPL 3][35]                 |
| [Apache Maven Toolchains Plugin][36]                   | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][37]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][38]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][39]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][40]                            | [Apache License, Version 2.0][1] |
| [Maven Plugin Plugin][75]                              | [Apache-2.0][1]                  |
| [duplicate-finder-maven-plugin Maven Mojo][41]         | [Apache License 2.0][42]         |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][43]                          | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][44]                       | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][45]                      | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][46]                       | [Eclipse Public License][47]     |
| [Apache Maven Dependency Plugin][76]                   | [Apache-2.0][1]                  |
| [Maven Failsafe Plugin][67]                            | [Apache-2.0][1]                  |
| [JaCoCo :: Maven Plugin][48]                           | [EPL-2.0][49]                    |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][50]                  | [Apache 2.0][8]                  |

## Project Keeper Shared Test Setup

### Compile Dependencies

| Dependency                                | License                          |
| ----------------------------------------- | -------------------------------- |
| [Project Keeper shared model classes][51] | [The MIT License][52]            |
| [SnakeYAML][56]                           | [Apache License, Version 2.0][8] |
| [Hamcrest][21]                            | [BSD-3-Clause][22]               |
| [Maven Model][58]                         | [Apache-2.0][1]                  |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [Apache Maven Clean Plugin][30]                        | [Apache-2.0][1]                  |
| [Apache Maven Install Plugin][31]                      | [Apache-2.0][1]                  |
| [Apache Maven Resources Plugin][32]                    | [Apache-2.0][1]                  |
| [Apache Maven Site Plugin][33]                         | [Apache License, Version 2.0][1] |
| [SonarQube Scanner for Maven][34]                      | [GNU LGPL 3][35]                 |
| [Apache Maven Toolchains Plugin][36]                   | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][37]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][38]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][39]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][40]                            | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][41]         | [Apache License 2.0][42]         |
| [JaCoCo :: Maven Plugin][48]                           | [EPL-2.0][49]                    |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][50]                  | [Apache 2.0][8]                  |

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
[30]: https://maven.apache.org/plugins/maven-clean-plugin/
[31]: https://maven.apache.org/plugins/maven-install-plugin/
[32]: https://maven.apache.org/plugins/maven-resources-plugin/
[33]: https://maven.apache.org/plugins/maven-site-plugin/
[34]: http://sonarsource.github.io/sonar-scanner-maven/
[35]: http://www.gnu.org/licenses/lgpl.txt
[36]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[37]: https://maven.apache.org/plugins/maven-compiler-plugin/
[38]: https://www.mojohaus.org/flatten-maven-plugin/
[39]: https://maven.apache.org/surefire/maven-surefire-plugin/
[40]: https://www.mojohaus.org/versions/versions-maven-plugin/
[41]: https://basepom.github.io/duplicate-finder-maven-plugin
[42]: http://www.apache.org/licenses/LICENSE-2.0.html
[43]: https://maven.apache.org/plugins/maven-gpg-plugin/
[44]: https://maven.apache.org/plugins/maven-source-plugin/
[45]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[46]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[47]: http://www.eclipse.org/legal/epl-v10.html
[48]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[49]: https://www.eclipse.org/legal/epl-2.0/
[50]: http://zlika.github.io/reproducible-build-maven-plugin
[51]: https://github.com/exasol/project-keeper/
[52]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[53]: https://www.xmlunit.org/
[54]: https://github.com/Steppschuh/Java-Markdown-Generator
[55]: https://github.com/vdurmont/semver4j
[56]: https://bitbucket.org/snakeyaml/snakeyaml
[57]: https://bitbucket.org/snakeyaml/snakeyaml-engine
[58]: https://maven.apache.org/ref/3.9.9/maven-model/
[59]: https://www.jcabi.com/jcabi-github
[60]: https://www.jcabi.com/LICENSE.txt
[61]: https://github.com/exasol/maven-project-version-getter/
[62]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[63]: https://github.com/exasol/maven-plugin-integration-testing/
[64]: https://github.com/exasol/maven-plugin-integration-testing/blob/main/LICENSE
[65]: https://junit-pioneer.org/
[66]: https://maven.apache.org/plugins/maven-jar-plugin/
[67]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[68]: https://maven.apache.org/plugins/maven-assembly-plugin/
[69]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[70]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[71]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[72]: https://maven.apache.org/ref/3.9.9/maven-plugin-api/
[73]: https://maven.apache.org/ref/3.9.9/maven-core/
[74]: https://www.eclemma.org/jacoco/index.html
[75]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[76]: https://maven.apache.org/plugins/maven-dependency-plugin/
