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
| [JGit - Core][18]                | [BSD-3-Clause][19]                                                                                             |

### Test Dependencies

| Dependency                                 | License                           |
| ------------------------------------------ | --------------------------------- |
| [JUnit Jupiter Engine][20]                 | [Eclipse Public License v2.0][21] |
| [JUnit Jupiter Params][20]                 | [Eclipse Public License v2.0][21] |
| [Hamcrest][22]                             | [BSD-3-Clause][23]                |
| [JUnit5 System Extensions][24]             | [Eclipse Public License v2.0][14] |
| [EqualsVerifier \| release normal jar][25] | [Apache License, Version 2.0][1]  |
| [to-string-verifier][26]                   | [MIT License][27]                 |
| [mockito-core][28]                         | [MIT][29]                         |
| [SLF4J JDK14 Provider][30]                 | [MIT License][27]                 |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [Apache Maven Clean Plugin][31]                        | [Apache-2.0][1]                  |
| [Apache Maven Install Plugin][32]                      | [Apache-2.0][1]                  |
| [Apache Maven Resources Plugin][33]                    | [Apache-2.0][1]                  |
| [Apache Maven Site Plugin][34]                         | [Apache License, Version 2.0][1] |
| [SonarQube Scanner for Maven][35]                      | [GNU LGPL 3][36]                 |
| [Apache Maven Toolchains Plugin][37]                   | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][38]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][39]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][40]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][41]                            | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][42]         | [Apache License 2.0][43]         |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][44]                          | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][45]                       | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][46]                      | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][47]                       | [Eclipse Public License][48]     |
| [JaCoCo :: Maven Plugin][49]                           | [EPL-2.0][50]                    |
| [Quality Summarizer Maven Plugin][51]                  | [MIT License][52]                |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][53]                  | [Apache 2.0][8]                  |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                       |
| ----------------------------------------- | --------------------------------------------- |
| [Project Keeper shared model classes][54] | [The MIT License][55]                         |
| [org.xmlunit:xmlunit-core][56]            | [The Apache Software License, Version 2.0][8] |
| [error-reporting-java][16]                | [MIT License][17]                             |
| [Markdown Generator][57]                  | [The Apache Software License, Version 2.0][8] |
| [semver4j][58]                            | [The MIT License][27]                         |
| [SnakeYAML][59]                           | [Apache License, Version 2.0][8]              |
| [SnakeYAML Engine][60]                    | [Apache License, Version 2.0][8]              |
| [Maven Model][61]                         | [Apache-2.0][1]                               |
| [jcabi-github][62]                        | [BSD][63]                                     |

### Test Dependencies

| Dependency                                 | License                                       |
| ------------------------------------------ | --------------------------------------------- |
| [Project Keeper shared test setup][54]     | [The MIT License][55]                         |
| [Maven Project Version Getter][64]         | [MIT License][65]                             |
| [JUnit Jupiter Engine][20]                 | [Eclipse Public License v2.0][21]             |
| [JUnit Jupiter Params][20]                 | [Eclipse Public License v2.0][21]             |
| [Hamcrest][22]                             | [BSD-3-Clause][23]                            |
| [org.xmlunit:xmlunit-matchers][56]         | [The Apache Software License, Version 2.0][8] |
| [mockito-junit-jupiter][28]                | [MIT][29]                                     |
| [Maven Plugin Integration Testing][66]     | [MIT License][67]                             |
| [EqualsVerifier \| release normal jar][25] | [Apache License, Version 2.0][1]              |
| [to-string-verifier][26]                   | [MIT License][27]                             |
| [junit-pioneer][68]                        | [Eclipse Public License v2.0][21]             |
| [SLF4J JDK14 Provider][30]                 | [MIT License][27]                             |

### Runtime Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Java project crawler][54] | [The MIT License][55] |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [Apache Maven Clean Plugin][31]                        | [Apache-2.0][1]                  |
| [Apache Maven Install Plugin][32]                      | [Apache-2.0][1]                  |
| [Apache Maven Resources Plugin][33]                    | [Apache-2.0][1]                  |
| [Apache Maven Site Plugin][34]                         | [Apache License, Version 2.0][1] |
| [SonarQube Scanner for Maven][35]                      | [GNU LGPL 3][36]                 |
| [Apache Maven Toolchains Plugin][37]                   | [Apache-2.0][1]                  |
| [Apache Maven JAR Plugin][69]                          | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][38]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][39]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][40]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][41]                            | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][42]         | [Apache License 2.0][43]         |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][44]                          | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][45]                       | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][46]                      | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][47]                       | [Eclipse Public License][48]     |
| [Maven Failsafe Plugin][70]                            | [Apache-2.0][1]                  |
| [JaCoCo :: Maven Plugin][49]                           | [EPL-2.0][50]                    |
| [Quality Summarizer Maven Plugin][51]                  | [MIT License][52]                |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][53]                  | [Apache 2.0][8]                  |

## Project Keeper Command Line Interface

### Compile Dependencies

| Dependency                 | License               |
| -------------------------- | --------------------- |
| [Project Keeper Core][54]  | [The MIT License][55] |
| [error-reporting-java][16] | [MIT License][17]     |
| [Maven Model][61]          | [Apache-2.0][1]       |

### Test Dependencies

| Dependency                             | License                           |
| -------------------------------------- | --------------------------------- |
| [Project Keeper shared test setup][54] | [The MIT License][55]             |
| [JUnit Jupiter Engine][20]             | [Eclipse Public License v2.0][21] |
| [JUnit Jupiter Params][20]             | [Eclipse Public License v2.0][21] |
| [Hamcrest][22]                         | [BSD-3-Clause][23]                |
| [Maven Project Version Getter][64]     | [MIT License][65]                 |

### Runtime Dependencies

| Dependency                 | License           |
| -------------------------- | ----------------- |
| [SLF4J API Module][30]     | [MIT License][27] |
| [SLF4J JDK14 Provider][30] | [MIT License][27] |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [Apache Maven Clean Plugin][31]                        | [Apache-2.0][1]                  |
| [Apache Maven Install Plugin][32]                      | [Apache-2.0][1]                  |
| [Apache Maven Resources Plugin][33]                    | [Apache-2.0][1]                  |
| [Apache Maven Site Plugin][34]                         | [Apache License, Version 2.0][1] |
| [SonarQube Scanner for Maven][35]                      | [GNU LGPL 3][36]                 |
| [Apache Maven Toolchains Plugin][37]                   | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][38]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][39]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][40]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][41]                            | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][42]         | [Apache License 2.0][43]         |
| [Apache Maven Assembly Plugin][71]                     | [Apache-2.0][1]                  |
| [Apache Maven JAR Plugin][69]                          | [Apache-2.0][1]                  |
| [Artifact reference checker and unifier][72]           | [MIT License][73]                |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][44]                          | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][45]                       | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][46]                      | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][47]                       | [Eclipse Public License][48]     |
| [Maven Failsafe Plugin][70]                            | [Apache-2.0][1]                  |
| [JaCoCo :: Maven Plugin][49]                           | [EPL-2.0][50]                    |
| [Quality Summarizer Maven Plugin][51]                  | [MIT License][52]                |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][53]                  | [Apache 2.0][8]                  |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper Core][54]                 | [The MIT License][55] |
| [Maven Plugin Tools Java Annotations][74] | [Apache-2.0][1]       |
| [Maven Plugin API][75]                    | [Apache-2.0][1]       |
| [Maven Core][76]                          | [Apache-2.0][1]       |
| [error-reporting-java][16]                | [MIT License][17]     |

### Test Dependencies

| Dependency                             | License                                       |
| -------------------------------------- | --------------------------------------------- |
| [Maven Project Version Getter][64]     | [MIT License][65]                             |
| [JUnit Jupiter Engine][20]             | [Eclipse Public License v2.0][21]             |
| [JUnit Jupiter Params][20]             | [Eclipse Public License v2.0][21]             |
| [Hamcrest][22]                         | [BSD-3-Clause][23]                            |
| [org.xmlunit:xmlunit-matchers][56]     | [The Apache Software License, Version 2.0][8] |
| [mockito-core][28]                     | [MIT][29]                                     |
| [Maven Plugin Integration Testing][66] | [MIT License][67]                             |
| [SLF4J JDK14 Provider][30]             | [MIT License][27]                             |
| [JaCoCo :: Agent][77]                  | [EPL-2.0][50]                                 |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [Apache Maven Clean Plugin][31]                        | [Apache-2.0][1]                  |
| [Apache Maven Install Plugin][32]                      | [Apache-2.0][1]                  |
| [Apache Maven Resources Plugin][33]                    | [Apache-2.0][1]                  |
| [Apache Maven Site Plugin][34]                         | [Apache License, Version 2.0][1] |
| [SonarQube Scanner for Maven][35]                      | [GNU LGPL 3][36]                 |
| [Apache Maven Toolchains Plugin][37]                   | [Apache-2.0][1]                  |
| [Maven Plugin Plugin][78]                              | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][38]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][39]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][40]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][41]                            | [Apache License, Version 2.0][1] |
| [Apache Maven JAR Plugin][69]                          | [Apache-2.0][1]                  |
| [duplicate-finder-maven-plugin Maven Mojo][42]         | [Apache License 2.0][43]         |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][44]                          | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][45]                       | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][46]                      | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][47]                       | [Eclipse Public License][48]     |
| [Apache Maven Dependency Plugin][79]                   | [Apache-2.0][1]                  |
| [Maven Failsafe Plugin][70]                            | [Apache-2.0][1]                  |
| [JaCoCo :: Maven Plugin][49]                           | [EPL-2.0][50]                    |
| [Quality Summarizer Maven Plugin][51]                  | [MIT License][52]                |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][53]                  | [Apache 2.0][8]                  |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project Keeper shared model classes][54] | [The MIT License][55] |
| [Maven Plugin Tools Java Annotations][74] | [Apache-2.0][1]       |
| [Maven Plugin API][75]                    | [Apache-2.0][1]       |
| [error-reporting-java][16]                | [MIT License][17]     |
| [JGit - Core][18]                         | [BSD-3-Clause][19]    |
| [semver4j][58]                            | [The MIT License][27] |
| [Maven Core][76]                          | [Apache-2.0][1]       |

### Test Dependencies

| Dependency                             | License                                       |
| -------------------------------------- | --------------------------------------------- |
| [Maven Project Version Getter][64]     | [MIT License][65]                             |
| [JUnit Jupiter Engine][20]             | [Eclipse Public License v2.0][21]             |
| [JUnit Jupiter Params][20]             | [Eclipse Public License v2.0][21]             |
| [Hamcrest][22]                         | [BSD-3-Clause][23]                            |
| [org.xmlunit:xmlunit-matchers][56]     | [The Apache Software License, Version 2.0][8] |
| [SLF4J JDK14 Provider][30]             | [MIT License][27]                             |
| [mockito-core][28]                     | [MIT][29]                                     |
| [mockito-junit-jupiter][28]            | [MIT][29]                                     |
| [Maven Plugin Integration Testing][66] | [MIT License][67]                             |
| [JaCoCo :: Agent][77]                  | [EPL-2.0][50]                                 |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [Apache Maven Clean Plugin][31]                        | [Apache-2.0][1]                  |
| [Apache Maven Install Plugin][32]                      | [Apache-2.0][1]                  |
| [Apache Maven Resources Plugin][33]                    | [Apache-2.0][1]                  |
| [Apache Maven Site Plugin][34]                         | [Apache License, Version 2.0][1] |
| [SonarQube Scanner for Maven][35]                      | [GNU LGPL 3][36]                 |
| [Apache Maven Toolchains Plugin][37]                   | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][38]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][39]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][40]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][41]                            | [Apache License, Version 2.0][1] |
| [Maven Plugin Plugin][78]                              | [Apache-2.0][1]                  |
| [duplicate-finder-maven-plugin Maven Mojo][42]         | [Apache License 2.0][43]         |
| [Apache Maven Deploy Plugin][4]                        | [Apache-2.0][1]                  |
| [Apache Maven GPG Plugin][44]                          | [Apache-2.0][1]                  |
| [Apache Maven Source Plugin][45]                       | [Apache License, Version 2.0][1] |
| [Apache Maven Javadoc Plugin][46]                      | [Apache-2.0][1]                  |
| [Nexus Staging Maven Plugin][47]                       | [Eclipse Public License][48]     |
| [Apache Maven Dependency Plugin][79]                   | [Apache-2.0][1]                  |
| [Maven Failsafe Plugin][70]                            | [Apache-2.0][1]                  |
| [JaCoCo :: Maven Plugin][49]                           | [EPL-2.0][50]                    |
| [Quality Summarizer Maven Plugin][51]                  | [MIT License][52]                |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][53]                  | [Apache 2.0][8]                  |

## Project Keeper Shared Test Setup

### Compile Dependencies

| Dependency                                | License                          |
| ----------------------------------------- | -------------------------------- |
| [Project Keeper shared model classes][54] | [The MIT License][55]            |
| [SnakeYAML][59]                           | [Apache License, Version 2.0][8] |
| [Hamcrest][22]                            | [BSD-3-Clause][23]               |
| [Maven Model][61]                         | [Apache-2.0][1]                  |

### Plugin Dependencies

| Dependency                                             | License                          |
| ------------------------------------------------------ | -------------------------------- |
| [Apache Maven Clean Plugin][31]                        | [Apache-2.0][1]                  |
| [Apache Maven Install Plugin][32]                      | [Apache-2.0][1]                  |
| [Apache Maven Resources Plugin][33]                    | [Apache-2.0][1]                  |
| [Apache Maven Site Plugin][34]                         | [Apache License, Version 2.0][1] |
| [SonarQube Scanner for Maven][35]                      | [GNU LGPL 3][36]                 |
| [Apache Maven Toolchains Plugin][37]                   | [Apache-2.0][1]                  |
| [Apache Maven Compiler Plugin][38]                     | [Apache-2.0][1]                  |
| [Apache Maven Enforcer Plugin][0]                      | [Apache-2.0][1]                  |
| [Maven Flatten Plugin][39]                             | [Apache Software Licenese][1]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][7] | [ASL2][8]                        |
| [Maven Surefire Plugin][40]                            | [Apache-2.0][1]                  |
| [Versions Maven Plugin][41]                            | [Apache License, Version 2.0][1] |
| [duplicate-finder-maven-plugin Maven Mojo][42]         | [Apache License 2.0][43]         |
| [JaCoCo :: Maven Plugin][49]                           | [EPL-2.0][50]                    |
| [Quality Summarizer Maven Plugin][51]                  | [MIT License][52]                |
| [error-code-crawler-maven-plugin][5]                   | [MIT License][6]                 |
| [Reproducible Build Maven Plugin][53]                  | [Apache 2.0][8]                  |

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
[19]: https://www.eclipse.org/org/documents/edl-v10.php
[20]: https://junit.org/junit5/
[21]: https://www.eclipse.org/legal/epl-v20.html
[22]: http://hamcrest.org/JavaHamcrest/
[23]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[24]: https://github.com/itsallcode/junit5-system-extensions
[25]: https://www.jqno.nl/equalsverifier
[26]: https://github.com/jparams/to-string-verifier
[27]: http://www.opensource.org/licenses/mit-license.php
[28]: https://github.com/mockito/mockito
[29]: https://opensource.org/licenses/MIT
[30]: http://www.slf4j.org
[31]: https://maven.apache.org/plugins/maven-clean-plugin/
[32]: https://maven.apache.org/plugins/maven-install-plugin/
[33]: https://maven.apache.org/plugins/maven-resources-plugin/
[34]: https://maven.apache.org/plugins/maven-site-plugin/
[35]: http://docs.sonarqube.org/display/PLUG/Plugin+Library/sonar-maven-plugin
[36]: http://www.gnu.org/licenses/lgpl.txt
[37]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[38]: https://maven.apache.org/plugins/maven-compiler-plugin/
[39]: https://www.mojohaus.org/flatten-maven-plugin/
[40]: https://maven.apache.org/surefire/maven-surefire-plugin/
[41]: https://www.mojohaus.org/versions/versions-maven-plugin/
[42]: https://basepom.github.io/duplicate-finder-maven-plugin
[43]: http://www.apache.org/licenses/LICENSE-2.0.html
[44]: https://maven.apache.org/plugins/maven-gpg-plugin/
[45]: https://maven.apache.org/plugins/maven-source-plugin/
[46]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[47]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[48]: http://www.eclipse.org/legal/epl-v10.html
[49]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[50]: https://www.eclipse.org/legal/epl-2.0/
[51]: https://github.com/exasol/quality-summarizer-maven-plugin/
[52]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[53]: http://zlika.github.io/reproducible-build-maven-plugin
[54]: https://github.com/exasol/project-keeper/
[55]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[56]: https://www.xmlunit.org/
[57]: https://github.com/Steppschuh/Java-Markdown-Generator
[58]: https://github.com/vdurmont/semver4j
[59]: https://bitbucket.org/snakeyaml/snakeyaml
[60]: https://bitbucket.org/snakeyaml/snakeyaml-engine
[61]: https://maven.apache.org/ref/3.9.9/maven-model/
[62]: https://www.jcabi.com/jcabi-github
[63]: https://www.jcabi.com/LICENSE.txt
[64]: https://github.com/exasol/maven-project-version-getter/
[65]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[66]: https://github.com/exasol/maven-plugin-integration-testing/
[67]: https://github.com/exasol/maven-plugin-integration-testing/blob/main/LICENSE
[68]: https://junit-pioneer.org/
[69]: https://maven.apache.org/plugins/maven-jar-plugin/
[70]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[71]: https://maven.apache.org/plugins/maven-assembly-plugin/
[72]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[73]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[74]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[75]: https://maven.apache.org/ref/3.9.9/maven-plugin-api/
[76]: https://maven.apache.org/ref/3.9.9/maven-core/
[77]: https://www.eclemma.org/jacoco/index.html
[78]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[79]: https://maven.apache.org/plugins/maven-dependency-plugin/
