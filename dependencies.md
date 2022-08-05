<!-- @formatter:off -->
# Dependencies

## Project-keeper Shared Model Classes

### Compile Dependencies

| Dependency                       | License                                                                                                      |
| -------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [Project Lombok][0]              | [The MIT License][1]                                                                                         |
| [Jakarta JSON Processing API][2] | [Eclipse Public License 2.0][3]; [GNU General Public License, version 2 with the GNU Classpath Exception][4] |
| [JSON-B API][5]                  | [Eclipse Public License 2.0][3]; [GNU General Public License, version 2 with the GNU Classpath Exception][4] |
| [org.eclipse.yasson][6]          | [Eclipse Public License v. 2.0][7]; [Eclipse Distribution License v. 1.0][8]                                 |
| [error-reporting-java][9]        | [MIT][10]                                                                                                    |
| [JGit - Core][11]                | Eclipse Distribution License (New BSD License)                                                               |

### Test Dependencies

| Dependency                                | License                           |
| ----------------------------------------- | --------------------------------- |
| [JUnit Jupiter Engine][12]                | [Eclipse Public License v2.0][13] |
| [JUnit Jupiter Params][12]                | [Eclipse Public License v2.0][13] |
| [Hamcrest][14]                            | [BSD License 3][15]               |
| [JUnit5 System Extensions][16]            | [Eclipse Public License v2.0][7]  |
| [EqualsVerifier | release normal jar][17] | [Apache License, Version 2.0][18] |
| [mockito-core][19]                        | [The MIT License][20]             |
| [SLF4J JDK14 Binding][21]                 | [MIT License][22]                 |

### Runtime Dependencies

| Dependency                   | License                                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [JSON-P Default Provider][2] | [Eclipse Public License 2.0][3]; [GNU General Public License, version 2 with the GNU Classpath Exception][4] |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                               |
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][18]              |
| [Apache Maven Enforcer Plugin][26]                      | [Apache License, Version 2.0][18]              |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][28]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][29] | [ASL2][28]                                     |
| [Maven Surefire Plugin][30]                             | [Apache License, Version 2.0][18]              |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][18]              |
| [Apache Maven Deploy Plugin][32]                        | [Apache License, Version 2.0][18]              |
| [Apache Maven GPG Plugin][33]                           | [Apache License, Version 2.0][18]              |
| [Apache Maven Source Plugin][34]                        | [Apache License, Version 2.0][18]              |
| [Apache Maven Javadoc Plugin][35]                       | [Apache License, Version 2.0][18]              |
| [Nexus Staging Maven Plugin][36]                        | [Eclipse Public License][37]                   |
| [Lombok Maven Plugin][38]                               | [The MIT License][10]                          |
| [JaCoCo :: Maven Plugin][39]                            | [Eclipse Public License 2.0][40]               |
| [error-code-crawler-maven-plugin][41]                   | [MIT License][42]                              |
| [Reproducible Build Maven Plugin][43]                   | [Apache 2.0][28]                               |
| [Maven Clean Plugin][44]                                | [The Apache Software License, Version 2.0][28] |
| [Maven Resources Plugin][45]                            | [The Apache Software License, Version 2.0][28] |
| [Maven JAR Plugin][46]                                  | [The Apache Software License, Version 2.0][28] |
| [Maven Install Plugin][47]                              | [The Apache Software License, Version 2.0][28] |
| [Maven Site Plugin 3][48]                               | [The Apache Software License, Version 2.0][28] |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project-Keeper shared model classes][49] | [The MIT License][50]                          |
| [jaxb-api][51]                            | [CDDL 1.1][52]; [GPL2 w/ CPE][52]              |
| [JAXB Runtime][53]                        | [Eclipse Distribution License - v 1.0][8]      |
| [org.xmlunit:xmlunit-core][54]            | [The Apache Software License, Version 2.0][28] |
| [error-reporting-java][9]                 | [MIT][10]                                      |
| [Markdown Generator][55]                  | [The Apache Software License, Version 2.0][28] |
| [semver4j][56]                            | [The MIT License][22]                          |
| [Project Lombok][0]                       | [The MIT License][1]                           |
| [SnakeYAML][57]                           | [Apache License, Version 2.0][28]              |
| [Maven Model][58]                         | [Apache License, Version 2.0][18]              |

### Test Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project Keeper shared test setup][49]    | [The MIT License][50]                          |
| [Maven Project Version Getter][59]        | [MIT][10]                                      |
| [JUnit Jupiter Engine][12]                | [Eclipse Public License v2.0][13]              |
| [JUnit Jupiter Params][12]                | [Eclipse Public License v2.0][13]              |
| [Hamcrest][14]                            | [BSD License 3][15]                            |
| [org.xmlunit:xmlunit-matchers][54]        | [The Apache Software License, Version 2.0][28] |
| [mockito-junit-jupiter][19]               | [The MIT License][20]                          |
| [Maven Plugin Integration Testing][60]    | [MIT License][61]                              |
| [EqualsVerifier | release normal jar][17] | [Apache License, Version 2.0][18]              |
| [SLF4J JDK14 Binding][21]                 | [MIT License][22]                              |

### Runtime Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project keeper Java project crawler][49] | [The MIT License][50] |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                               |
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][18]              |
| [Apache Maven Enforcer Plugin][26]                      | [Apache License, Version 2.0][18]              |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][28]                 |
| [Apache Maven JAR Plugin][62]                           | [Apache License, Version 2.0][18]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][29] | [ASL2][28]                                     |
| [Maven Surefire Plugin][30]                             | [Apache License, Version 2.0][18]              |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][18]              |
| [Apache Maven Deploy Plugin][32]                        | [Apache License, Version 2.0][18]              |
| [Apache Maven GPG Plugin][33]                           | [Apache License, Version 2.0][18]              |
| [Apache Maven Source Plugin][34]                        | [Apache License, Version 2.0][18]              |
| [Apache Maven Javadoc Plugin][35]                       | [Apache License, Version 2.0][18]              |
| [Nexus Staging Maven Plugin][36]                        | [Eclipse Public License][37]                   |
| [Lombok Maven Plugin][38]                               | [The MIT License][10]                          |
| [Maven Failsafe Plugin][63]                             | [Apache License, Version 2.0][18]              |
| [JaCoCo :: Maven Plugin][39]                            | [Eclipse Public License 2.0][40]               |
| [error-code-crawler-maven-plugin][41]                   | [MIT License][42]                              |
| [Reproducible Build Maven Plugin][43]                   | [Apache 2.0][28]                               |
| [Maven Clean Plugin][44]                                | [The Apache Software License, Version 2.0][28] |
| [Maven Resources Plugin][45]                            | [The Apache Software License, Version 2.0][28] |
| [Maven Install Plugin][47]                              | [The Apache Software License, Version 2.0][28] |
| [Maven Site Plugin 3][48]                               | [The Apache Software License, Version 2.0][28] |

## Project Keeper Command Line Interface

### Compile Dependencies

| Dependency                | License                           |
| ------------------------- | --------------------------------- |
| [Project keeper core][49] | [The MIT License][50]             |
| [error-reporting-java][9] | [MIT][10]                         |
| [Maven Model][58]         | [Apache License, Version 2.0][18] |

### Test Dependencies

| Dependency                             | License                           |
| -------------------------------------- | --------------------------------- |
| [Project Keeper shared test setup][49] | [The MIT License][50]             |
| [JUnit Jupiter Engine][12]             | [Eclipse Public License v2.0][13] |
| [JUnit Jupiter Params][12]             | [Eclipse Public License v2.0][13] |
| [Hamcrest][14]                         | [BSD License 3][15]               |
| [Maven Project Version Getter][59]     | [MIT][10]                         |

### Runtime Dependencies

| Dependency                | License           |
| ------------------------- | ----------------- |
| [SLF4J JDK14 Binding][21] | [MIT License][22] |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                               |
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][18]              |
| [Apache Maven Enforcer Plugin][26]                      | [Apache License, Version 2.0][18]              |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][28]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][29] | [ASL2][28]                                     |
| [Maven Surefire Plugin][30]                             | [Apache License, Version 2.0][18]              |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][18]              |
| [Apache Maven Assembly Plugin][64]                      | [Apache License, Version 2.0][18]              |
| [Apache Maven JAR Plugin][62]                           | [Apache License, Version 2.0][18]              |
| [Artifact reference checker and unifier][65]            | [MIT][10]                                      |
| [Apache Maven Deploy Plugin][32]                        | [Apache License, Version 2.0][18]              |
| [Apache Maven GPG Plugin][33]                           | [Apache License, Version 2.0][18]              |
| [Apache Maven Source Plugin][34]                        | [Apache License, Version 2.0][18]              |
| [Apache Maven Javadoc Plugin][35]                       | [Apache License, Version 2.0][18]              |
| [Nexus Staging Maven Plugin][36]                        | [Eclipse Public License][37]                   |
| [Maven Failsafe Plugin][63]                             | [Apache License, Version 2.0][18]              |
| [JaCoCo :: Maven Plugin][39]                            | [Eclipse Public License 2.0][40]               |
| [error-code-crawler-maven-plugin][41]                   | [MIT License][42]                              |
| [Reproducible Build Maven Plugin][43]                   | [Apache 2.0][28]                               |
| [Maven Clean Plugin][44]                                | [The Apache Software License, Version 2.0][28] |
| [Maven Resources Plugin][45]                            | [The Apache Software License, Version 2.0][28] |
| [Maven Install Plugin][47]                              | [The Apache Software License, Version 2.0][28] |
| [Maven Site Plugin 3][48]                               | [The Apache Software License, Version 2.0][28] |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project keeper core][49]                 | [The MIT License][50]                          |
| [Maven Plugin Tools Java Annotations][66] | [Apache License, Version 2.0][18]              |
| [Maven Plugin API][67]                    | [Apache License, Version 2.0][18]              |
| [Maven Project Builder][68]               | [The Apache Software License, Version 2.0][28] |
| [Maven Core][69]                          | [Apache License, Version 2.0][18]              |
| [error-reporting-java][9]                 | [MIT][10]                                      |
| [Project Lombok][0]                       | [The MIT License][1]                           |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][59]     | [MIT][10]                                      |
| [JUnit Jupiter Engine][12]             | [Eclipse Public License v2.0][13]              |
| [JUnit Jupiter Params][12]             | [Eclipse Public License v2.0][13]              |
| [Hamcrest][14]                         | [BSD License 3][15]                            |
| [org.xmlunit:xmlunit-matchers][54]     | [The Apache Software License, Version 2.0][28] |
| [mockito-core][19]                     | [The MIT License][20]                          |
| [JaCoCo :: Core][70]                   | [Eclipse Public License 2.0][40]               |
| [Maven Plugin Integration Testing][60] | [MIT License][61]                              |
| [SLF4J JDK14 Binding][21]              | [MIT License][22]                              |
| [JaCoCo :: Agent][70]                  | [Eclipse Public License 2.0][40]               |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                               |
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][18]              |
| [Apache Maven Enforcer Plugin][26]                      | [Apache License, Version 2.0][18]              |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][28]                 |
| [Maven Plugin Plugin][71]                               | [Apache License, Version 2.0][18]              |
| [Apache Maven JAR Plugin][62]                           | [Apache License, Version 2.0][18]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][29] | [ASL2][28]                                     |
| [Maven Surefire Plugin][30]                             | [Apache License, Version 2.0][18]              |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][18]              |
| [Apache Maven Deploy Plugin][32]                        | [Apache License, Version 2.0][18]              |
| [Apache Maven GPG Plugin][33]                           | [Apache License, Version 2.0][18]              |
| [Apache Maven Source Plugin][34]                        | [Apache License, Version 2.0][18]              |
| [Apache Maven Javadoc Plugin][35]                       | [Apache License, Version 2.0][18]              |
| [Nexus Staging Maven Plugin][36]                        | [Eclipse Public License][37]                   |
| [Apache Maven Dependency Plugin][72]                    | [Apache License, Version 2.0][18]              |
| [Lombok Maven Plugin][38]                               | [The MIT License][10]                          |
| [Maven Failsafe Plugin][63]                             | [Apache License, Version 2.0][18]              |
| [JaCoCo :: Maven Plugin][39]                            | [Eclipse Public License 2.0][40]               |
| [error-code-crawler-maven-plugin][41]                   | [MIT License][42]                              |
| [Reproducible Build Maven Plugin][43]                   | [Apache 2.0][28]                               |
| [Maven Clean Plugin][44]                                | [The Apache Software License, Version 2.0][28] |
| [Maven Resources Plugin][45]                            | [The Apache Software License, Version 2.0][28] |
| [Maven Install Plugin][47]                              | [The Apache Software License, Version 2.0][28] |
| [Maven Site Plugin 3][48]                               | [The Apache Software License, Version 2.0][28] |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project-Keeper shared model classes][49] | [The MIT License][50]                          |
| [Maven Plugin Tools Java Annotations][66] | [Apache License, Version 2.0][18]              |
| [Maven Plugin API][67]                    | [Apache License, Version 2.0][18]              |
| [Maven Project Builder][68]               | [The Apache Software License, Version 2.0][28] |
| [error-reporting-java][9]                 | [MIT][10]                                      |
| [JGit - Core][11]                         | Eclipse Distribution License (New BSD License) |
| [semver4j][56]                            | [The MIT License][22]                          |
| [Maven Core][69]                          | [Apache License, Version 2.0][18]              |
| [Apache Commons IO][73]                   | [Apache License, Version 2.0][18]              |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][59]     | [MIT][10]                                      |
| [JUnit Jupiter Engine][12]             | [Eclipse Public License v2.0][13]              |
| [JUnit Jupiter Params][12]             | [Eclipse Public License v2.0][13]              |
| [Hamcrest][14]                         | [BSD License 3][15]                            |
| [org.xmlunit:xmlunit-matchers][54]     | [The Apache Software License, Version 2.0][28] |
| [SLF4J JDK14 Binding][21]              | [MIT License][22]                              |
| [mockito-core][19]                     | [The MIT License][20]                          |
| [JaCoCo :: Core][70]                   | [Eclipse Public License 2.0][40]               |
| [Maven Plugin Integration Testing][60] | [MIT License][61]                              |
| [JaCoCo :: Agent][70]                  | [Eclipse Public License 2.0][40]               |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                               |
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][18]              |
| [Apache Maven Enforcer Plugin][26]                      | [Apache License, Version 2.0][18]              |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][28]                 |
| [Maven Plugin Plugin][71]                               | [Apache License, Version 2.0][18]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][29] | [ASL2][28]                                     |
| [Maven Surefire Plugin][30]                             | [Apache License, Version 2.0][18]              |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][18]              |
| [Apache Maven Deploy Plugin][32]                        | [Apache License, Version 2.0][18]              |
| [Apache Maven GPG Plugin][33]                           | [Apache License, Version 2.0][18]              |
| [Apache Maven Source Plugin][34]                        | [Apache License, Version 2.0][18]              |
| [Apache Maven Javadoc Plugin][35]                       | [Apache License, Version 2.0][18]              |
| [Nexus Staging Maven Plugin][36]                        | [Eclipse Public License][37]                   |
| [Apache Maven Dependency Plugin][72]                    | [Apache License, Version 2.0][18]              |
| [Maven Failsafe Plugin][63]                             | [Apache License, Version 2.0][18]              |
| [JaCoCo :: Maven Plugin][39]                            | [Eclipse Public License 2.0][40]               |
| [error-code-crawler-maven-plugin][41]                   | [MIT License][42]                              |
| [Reproducible Build Maven Plugin][43]                   | [Apache 2.0][28]                               |
| [Maven Clean Plugin][44]                                | [The Apache Software License, Version 2.0][28] |
| [Maven Resources Plugin][45]                            | [The Apache Software License, Version 2.0][28] |
| [Maven JAR Plugin][46]                                  | [The Apache Software License, Version 2.0][28] |
| [Maven Install Plugin][47]                              | [The Apache Software License, Version 2.0][28] |
| [Maven Site Plugin 3][48]                               | [The Apache Software License, Version 2.0][28] |

## Project Keeper Shared Test Setup

### Compile Dependencies

| Dependency                                | License                           |
| ----------------------------------------- | --------------------------------- |
| [Project-Keeper shared model classes][49] | [The MIT License][50]             |
| [SnakeYAML][57]                           | [Apache License, Version 2.0][28] |
| [Hamcrest][14]                            | [BSD License 3][15]               |
| [Maven Model][58]                         | [Apache License, Version 2.0][18] |
| [Project Lombok][0]                       | [The MIT License][1]              |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                               |
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][18]              |
| [Apache Maven Enforcer Plugin][26]                      | [Apache License, Version 2.0][18]              |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][28]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][29] | [ASL2][28]                                     |
| [Maven Surefire Plugin][30]                             | [Apache License, Version 2.0][18]              |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][18]              |
| [Lombok Maven Plugin][38]                               | [The MIT License][10]                          |
| [JaCoCo :: Maven Plugin][39]                            | [Eclipse Public License 2.0][40]               |
| [error-code-crawler-maven-plugin][41]                   | [MIT License][42]                              |
| [Reproducible Build Maven Plugin][43]                   | [Apache 2.0][28]                               |
| [Maven Clean Plugin][44]                                | [The Apache Software License, Version 2.0][28] |
| [Maven Resources Plugin][45]                            | [The Apache Software License, Version 2.0][28] |
| [Maven JAR Plugin][46]                                  | [The Apache Software License, Version 2.0][28] |
| [Maven Install Plugin][47]                              | [The Apache Software License, Version 2.0][28] |
| [Maven Deploy Plugin][74]                               | [The Apache Software License, Version 2.0][28] |
| [Maven Site Plugin 3][48]                               | [The Apache Software License, Version 2.0][28] |

[0]: https://projectlombok.org
[1]: https://projectlombok.org/LICENSE
[2]: https://github.com/eclipse-ee4j/jsonp
[3]: https://projects.eclipse.org/license/epl-2.0
[4]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[5]: https://eclipse-ee4j.github.io/jsonb-api
[6]: https://projects.eclipse.org/projects/ee4j.yasson
[7]: http://www.eclipse.org/legal/epl-v20.html
[8]: http://www.eclipse.org/org/documents/edl-v10.php
[9]: https://github.com/exasol/error-reporting-java
[10]: https://opensource.org/licenses/MIT
[11]: https://www.eclipse.org/jgit/
[12]: https://junit.org/junit5/
[13]: https://www.eclipse.org/legal/epl-v20.html
[14]: http://hamcrest.org/JavaHamcrest/
[15]: http://opensource.org/licenses/BSD-3-Clause
[16]: https://github.com/itsallcode/junit5-system-extensions
[17]: https://www.jqno.nl/equalsverifier
[18]: https://www.apache.org/licenses/LICENSE-2.0.txt
[19]: https://github.com/mockito/mockito
[20]: https://github.com/mockito/mockito/blob/main/LICENSE
[21]: http://www.slf4j.org
[22]: http://www.opensource.org/licenses/mit-license.php
[23]: http://sonarsource.github.io/sonar-scanner-maven/
[24]: http://www.gnu.org/licenses/lgpl.txt
[25]: https://maven.apache.org/plugins/maven-compiler-plugin/
[26]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[27]: https://www.mojohaus.org/flatten-maven-plugin/
[28]: http://www.apache.org/licenses/LICENSE-2.0.txt
[29]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[30]: https://maven.apache.org/surefire/maven-surefire-plugin/
[31]: http://www.mojohaus.org/versions-maven-plugin/
[32]: https://maven.apache.org/plugins/maven-deploy-plugin/
[33]: https://maven.apache.org/plugins/maven-gpg-plugin/
[34]: https://maven.apache.org/plugins/maven-source-plugin/
[35]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[36]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[37]: http://www.eclipse.org/legal/epl-v10.html
[38]: https://anthonywhitford.com/lombok.maven/lombok-maven-plugin/
[39]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[40]: https://www.eclipse.org/legal/epl-2.0/
[41]: https://github.com/exasol/error-code-crawler-maven-plugin/
[42]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[43]: http://zlika.github.io/reproducible-build-maven-plugin
[44]: http://maven.apache.org/plugins/maven-clean-plugin/
[45]: http://maven.apache.org/plugins/maven-resources-plugin/
[46]: http://maven.apache.org/plugins/maven-jar-plugin/
[47]: http://maven.apache.org/plugins/maven-install-plugin/
[48]: http://maven.apache.org/plugins/maven-site-plugin/
[49]: https://github.com/exasol/project-keeper/
[50]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[51]: https://github.com/eclipse-ee4j/jaxb-api
[52]: https://oss.oracle.com/licenses/CDDL+GPL-1.1
[53]: https://eclipse-ee4j.github.io/jaxb-ri/
[54]: https://www.xmlunit.org/
[55]: https://github.com/Steppschuh/Java-Markdown-Generator
[56]: https://github.com/vdurmont/semver4j
[57]: https://bitbucket.org/snakeyaml/snakeyaml
[58]: https://maven.apache.org/ref/3.8.6/maven-model/
[59]: https://github.com/exasol/maven-project-version-getter
[60]: https://github.com/exasol/maven-plugin-integration-testing/
[61]: https://github.com/exasol/maven-plugin-integration-testing/blob/main/LICENSE
[62]: https://maven.apache.org/plugins/maven-jar-plugin/
[63]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[64]: https://maven.apache.org/plugins/maven-assembly-plugin/
[65]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[66]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[67]: https://maven.apache.org/ref/3.8.6/maven-plugin-api/
[68]: http://maven.apache.org/
[69]: https://maven.apache.org/ref/3.8.6/maven-core/
[70]: https://www.eclemma.org/jacoco/index.html
[71]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[72]: https://maven.apache.org/plugins/maven-dependency-plugin/
[73]: https://commons.apache.org/proper/commons-io/
[74]: http://maven.apache.org/plugins/maven-deploy-plugin/
