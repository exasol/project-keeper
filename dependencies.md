<!-- @formatter:off -->
# Dependencies

## Project-keeper Shared Model Classes

### Compile Dependencies

| Dependency                       | License                                                                                                      |
| -------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [Project Lombok][0]              | [The MIT License][1]                                                                                         |
| [Jakarta JSON Processing API][2] | [Eclipse Public License 2.0][3]; [GNU General Public License, version 2 with the GNU Classpath Exception][4] |
| [JSON-B API][5]                  | [Eclipse Public License 2.0][3]; [GNU General Public License, version 2 with the GNU Classpath Exception][4] |
| [org.eclipse.yasson][8]          | [Eclipse Public License v. 2.0][9]; [Eclipse Distribution License v. 1.0][10]                                |
| [error-reporting-java][11]       | [MIT][12]                                                                                                    |
| [JGit - Core][13]                | Eclipse Distribution License (New BSD License)                                                               |

### Test Dependencies

| Dependency                                | License                           |
| ----------------------------------------- | --------------------------------- |
| [JUnit Jupiter Engine][14]                | [Eclipse Public License v2.0][15] |
| [JUnit Jupiter Params][14]                | [Eclipse Public License v2.0][15] |
| [Hamcrest][18]                            | [BSD License 3][19]               |
| [JUnit5 System Extensions][20]            | [Eclipse Public License v2.0][9]  |
| [EqualsVerifier | release normal jar][22] | [Apache License, Version 2.0][23] |
| [mockito-core][24]                        | [The MIT License][25]             |

### Runtime Dependencies

| Dependency                   | License                                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [JSON-P Default Provider][2] | [Eclipse Public License 2.0][3]; [GNU General Public License, version 2 with the GNU Classpath Exception][4] |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][29]                       | [GNU LGPL 3][30]                               |
| [Apache Maven Compiler Plugin][31]                      | [Apache License, Version 2.0][23]              |
| [Apache Maven Enforcer Plugin][33]                      | [Apache License, Version 2.0][23]              |
| [Maven Flatten Plugin][35]                              | [Apache Software Licenese][36]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][37] | [ASL2][36]                                     |
| [Reproducible Build Maven Plugin][39]                   | [Apache 2.0][36]                               |
| [Maven Surefire Plugin][41]                             | [Apache License, Version 2.0][23]              |
| [Versions Maven Plugin][43]                             | [Apache License, Version 2.0][23]              |
| [Apache Maven Deploy Plugin][45]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven GPG Plugin][47]                           | [Apache License, Version 2.0][23]              |
| [Apache Maven Source Plugin][49]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven Javadoc Plugin][51]                       | [Apache License, Version 2.0][23]              |
| [Nexus Staging Maven Plugin][53]                        | [Eclipse Public License][54]                   |
| [Lombok Maven Plugin][55]                               | [The MIT License][12]                          |
| [JaCoCo :: Maven Plugin][57]                            | [Eclipse Public License 2.0][58]               |
| [error-code-crawler-maven-plugin][59]                   | [MIT][12]                                      |
| [Maven Clean Plugin][61]                                | [The Apache Software License, Version 2.0][36] |
| [Maven Resources Plugin][63]                            | [The Apache Software License, Version 2.0][36] |
| [Maven JAR Plugin][65]                                  | [The Apache Software License, Version 2.0][36] |
| [Maven Install Plugin][67]                              | [The Apache Software License, Version 2.0][36] |
| [Maven Site Plugin 3][69]                               | [The Apache Software License, Version 2.0][36] |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project-Keeper shared model classes][71] | [The MIT License][72]                          |
| [jaxb-api][73]                            | [CDDL 1.1][74]; [GPL2 w/ CPE][74]              |
| [JAXB Runtime][76]                        | [Eclipse Distribution License - v 1.0][10]     |
| [org.xmlunit:xmlunit-core][78]            | [The Apache Software License, Version 2.0][36] |
| [error-reporting-java][11]                | [MIT][12]                                      |
| [Markdown Generator][82]                  | [The Apache Software License, Version 2.0][36] |
| [semver4j][84]                            | [The MIT License][85]                          |
| [Project Lombok][0]                       | [The MIT License][1]                           |
| [SnakeYAML][88]                           | [Apache License, Version 2.0][36]              |
| [Maven Model][90]                         | [Apache License, Version 2.0][23]              |

### Test Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project Keeper shared test setup][71]    | [The MIT License][72]                          |
| [Maven Project Version Getter][94]        | [MIT][12]                                      |
| [JUnit Jupiter Engine][14]                | [Eclipse Public License v2.0][15]              |
| [JUnit Jupiter Params][14]                | [Eclipse Public License v2.0][15]              |
| [Hamcrest][18]                            | [BSD License 3][19]                            |
| [org.xmlunit:xmlunit-matchers][78]        | [The Apache Software License, Version 2.0][36] |
| [mockito-junit-jupiter][24]               | [The MIT License][25]                          |
| [Maven Plugin Integration Testing][106]   | [MIT][12]                                      |
| [EqualsVerifier | release normal jar][22] | [Apache License, Version 2.0][23]              |

### Runtime Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project keeper Java project crawler][71] | [The MIT License][72] |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][29]                       | [GNU LGPL 3][30]                               |
| [Apache Maven Compiler Plugin][31]                      | [Apache License, Version 2.0][23]              |
| [Apache Maven Enforcer Plugin][33]                      | [Apache License, Version 2.0][23]              |
| [Maven Flatten Plugin][35]                              | [Apache Software Licenese][36]                 |
| [Apache Maven JAR Plugin][120]                          | [Apache License, Version 2.0][23]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][37] | [ASL2][36]                                     |
| [Reproducible Build Maven Plugin][39]                   | [Apache 2.0][36]                               |
| [Maven Surefire Plugin][41]                             | [Apache License, Version 2.0][23]              |
| [Versions Maven Plugin][43]                             | [Apache License, Version 2.0][23]              |
| [Apache Maven Deploy Plugin][45]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven GPG Plugin][47]                           | [Apache License, Version 2.0][23]              |
| [Apache Maven Source Plugin][49]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven Javadoc Plugin][51]                       | [Apache License, Version 2.0][23]              |
| [Nexus Staging Maven Plugin][53]                        | [Eclipse Public License][54]                   |
| [Lombok Maven Plugin][55]                               | [The MIT License][12]                          |
| [Maven Failsafe Plugin][142]                            | [Apache License, Version 2.0][23]              |
| [JaCoCo :: Maven Plugin][57]                            | [Eclipse Public License 2.0][58]               |
| [error-code-crawler-maven-plugin][59]                   | [MIT][12]                                      |
| [Maven Clean Plugin][61]                                | [The Apache Software License, Version 2.0][36] |
| [Maven Resources Plugin][63]                            | [The Apache Software License, Version 2.0][36] |
| [Maven Install Plugin][67]                              | [The Apache Software License, Version 2.0][36] |
| [Maven Site Plugin 3][69]                               | [The Apache Software License, Version 2.0][36] |

## Project Keeper Command Line Interface

### Compile Dependencies

| Dependency                 | License                           |
| -------------------------- | --------------------------------- |
| [Project keeper core][71]  | [The MIT License][72]             |
| [error-reporting-java][11] | [MIT][12]                         |
| [Maven Model][90]          | [Apache License, Version 2.0][23] |

### Test Dependencies

| Dependency                             | License                           |
| -------------------------------------- | --------------------------------- |
| [Project Keeper shared test setup][71] | [The MIT License][72]             |
| [JUnit Jupiter Engine][14]             | [Eclipse Public License v2.0][15] |
| [JUnit Jupiter Params][14]             | [Eclipse Public License v2.0][15] |
| [Hamcrest][18]                         | [BSD License 3][19]               |
| [Maven Project Version Getter][94]     | [MIT][12]                         |

### Runtime Dependencies

| Dependency                 | License           |
| -------------------------- | ----------------- |
| [SLF4J JDK14 Binding][172] | [MIT License][85] |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][29]                       | [GNU LGPL 3][30]                               |
| [Apache Maven Compiler Plugin][31]                      | [Apache License, Version 2.0][23]              |
| [Apache Maven Enforcer Plugin][33]                      | [Apache License, Version 2.0][23]              |
| [Maven Flatten Plugin][35]                              | [Apache Software Licenese][36]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][37] | [ASL2][36]                                     |
| [Reproducible Build Maven Plugin][39]                   | [Apache 2.0][36]                               |
| [Maven Surefire Plugin][41]                             | [Apache License, Version 2.0][23]              |
| [Versions Maven Plugin][43]                             | [Apache License, Version 2.0][23]              |
| [Apache Maven Assembly Plugin][190]                     | [Apache License, Version 2.0][23]              |
| [Apache Maven JAR Plugin][120]                          | [Apache License, Version 2.0][23]              |
| [Artifact reference checker and unifier][194]           | [MIT][12]                                      |
| [Apache Maven Deploy Plugin][45]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven GPG Plugin][47]                           | [Apache License, Version 2.0][23]              |
| [Apache Maven Source Plugin][49]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven Javadoc Plugin][51]                       | [Apache License, Version 2.0][23]              |
| [Nexus Staging Maven Plugin][53]                        | [Eclipse Public License][54]                   |
| [Maven Failsafe Plugin][142]                            | [Apache License, Version 2.0][23]              |
| [JaCoCo :: Maven Plugin][57]                            | [Eclipse Public License 2.0][58]               |
| [error-code-crawler-maven-plugin][59]                   | [MIT][12]                                      |
| [Maven Clean Plugin][61]                                | [The Apache Software License, Version 2.0][36] |
| [Maven Resources Plugin][63]                            | [The Apache Software License, Version 2.0][36] |
| [Maven Install Plugin][67]                              | [The Apache Software License, Version 2.0][36] |
| [Maven Site Plugin 3][69]                               | [The Apache Software License, Version 2.0][36] |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                 | License                                        |
| ------------------------------------------ | ---------------------------------------------- |
| [Project keeper core][71]                  | [The MIT License][72]                          |
| [Maven Plugin Tools Java Annotations][222] | [Apache License, Version 2.0][23]              |
| [Maven Plugin API][224]                    | [Apache License, Version 2.0][23]              |
| [Maven Project Builder][226]               | [The Apache Software License, Version 2.0][36] |
| [Maven Core][228]                          | [Apache License, Version 2.0][23]              |
| [error-reporting-java][11]                 | [MIT][12]                                      |
| [Project Lombok][0]                        | [The MIT License][1]                           |

### Test Dependencies

| Dependency                              | License                                        |
| --------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][94]      | [MIT][12]                                      |
| [JUnit Jupiter Engine][14]              | [Eclipse Public License v2.0][15]              |
| [JUnit Jupiter Params][14]              | [Eclipse Public License v2.0][15]              |
| [Hamcrest][18]                          | [BSD License 3][19]                            |
| [org.xmlunit:xmlunit-matchers][78]      | [The Apache Software License, Version 2.0][36] |
| [mockito-core][24]                      | [The MIT License][25]                          |
| [JaCoCo :: Core][246]                   | [Eclipse Public License 2.0][58]               |
| [Maven Plugin Integration Testing][106] | [MIT][12]                                      |
| [JaCoCo :: Agent][246]                  | [Eclipse Public License 2.0][58]               |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][29]                       | [GNU LGPL 3][30]                               |
| [Apache Maven Compiler Plugin][31]                      | [Apache License, Version 2.0][23]              |
| [Apache Maven Enforcer Plugin][33]                      | [Apache License, Version 2.0][23]              |
| [Maven Flatten Plugin][35]                              | [Apache Software Licenese][36]                 |
| [Maven Plugin Plugin][260]                              | [Apache License, Version 2.0][23]              |
| [Apache Maven JAR Plugin][120]                          | [Apache License, Version 2.0][23]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][37] | [ASL2][36]                                     |
| [Reproducible Build Maven Plugin][39]                   | [Apache 2.0][36]                               |
| [Maven Surefire Plugin][41]                             | [Apache License, Version 2.0][23]              |
| [Versions Maven Plugin][43]                             | [Apache License, Version 2.0][23]              |
| [Apache Maven Deploy Plugin][45]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven GPG Plugin][47]                           | [Apache License, Version 2.0][23]              |
| [Apache Maven Source Plugin][49]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven Javadoc Plugin][51]                       | [Apache License, Version 2.0][23]              |
| [Nexus Staging Maven Plugin][53]                        | [Eclipse Public License][54]                   |
| [Apache Maven Dependency Plugin][282]                   | [Apache License, Version 2.0][23]              |
| [Lombok Maven Plugin][55]                               | [The MIT License][12]                          |
| [Maven Failsafe Plugin][142]                            | [Apache License, Version 2.0][23]              |
| [JaCoCo :: Maven Plugin][57]                            | [Eclipse Public License 2.0][58]               |
| [error-code-crawler-maven-plugin][59]                   | [MIT][12]                                      |
| [Maven Clean Plugin][61]                                | [The Apache Software License, Version 2.0][36] |
| [Maven Resources Plugin][63]                            | [The Apache Software License, Version 2.0][36] |
| [Maven Install Plugin][67]                              | [The Apache Software License, Version 2.0][36] |
| [Maven Site Plugin 3][69]                               | [The Apache Software License, Version 2.0][36] |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                 | License                                        |
| ------------------------------------------ | ---------------------------------------------- |
| [Project-Keeper shared model classes][71]  | [The MIT License][72]                          |
| [Maven Plugin Tools Java Annotations][222] | [Apache License, Version 2.0][23]              |
| [Maven Plugin API][224]                    | [Apache License, Version 2.0][23]              |
| [Maven Project Builder][226]               | [The Apache Software License, Version 2.0][36] |
| [error-reporting-java][11]                 | [MIT][12]                                      |
| [JGit - Core][13]                          | Eclipse Distribution License (New BSD License) |
| [semver4j][84]                             | [The MIT License][85]                          |
| [Maven Core][228]                          | [Apache License, Version 2.0][23]              |
| [Apache Commons IO][315]                   | [Apache License, Version 2.0][23]              |

### Test Dependencies

| Dependency                              | License                                        |
| --------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][94]      | [MIT][12]                                      |
| [JUnit Jupiter Engine][14]              | [Eclipse Public License v2.0][15]              |
| [JUnit Jupiter Params][14]              | [Eclipse Public License v2.0][15]              |
| [Hamcrest][18]                          | [BSD License 3][19]                            |
| [org.xmlunit:xmlunit-matchers][78]      | [The Apache Software License, Version 2.0][36] |
| [SLF4J JDK14 Binding][172]              | [MIT License][85]                              |
| [mockito-core][24]                      | [The MIT License][25]                          |
| [JaCoCo :: Core][246]                   | [Eclipse Public License 2.0][58]               |
| [Maven Plugin Integration Testing][106] | [MIT][12]                                      |
| [JaCoCo :: Agent][246]                  | [Eclipse Public License 2.0][58]               |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][29]                       | [GNU LGPL 3][30]                               |
| [Apache Maven Compiler Plugin][31]                      | [Apache License, Version 2.0][23]              |
| [Apache Maven Enforcer Plugin][33]                      | [Apache License, Version 2.0][23]              |
| [Maven Flatten Plugin][35]                              | [Apache Software Licenese][36]                 |
| [Maven Plugin Plugin][260]                              | [Apache License, Version 2.0][23]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][37] | [ASL2][36]                                     |
| [Reproducible Build Maven Plugin][39]                   | [Apache 2.0][36]                               |
| [Maven Surefire Plugin][41]                             | [Apache License, Version 2.0][23]              |
| [Versions Maven Plugin][43]                             | [Apache License, Version 2.0][23]              |
| [Apache Maven Deploy Plugin][45]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven GPG Plugin][47]                           | [Apache License, Version 2.0][23]              |
| [Apache Maven Source Plugin][49]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven Javadoc Plugin][51]                       | [Apache License, Version 2.0][23]              |
| [Nexus Staging Maven Plugin][53]                        | [Eclipse Public License][54]                   |
| [Apache Maven Dependency Plugin][282]                   | [Apache License, Version 2.0][23]              |
| [Maven Failsafe Plugin][142]                            | [Apache License, Version 2.0][23]              |
| [JaCoCo :: Maven Plugin][57]                            | [Eclipse Public License 2.0][58]               |
| [error-code-crawler-maven-plugin][59]                   | [MIT][12]                                      |
| [Maven Clean Plugin][61]                                | [The Apache Software License, Version 2.0][36] |
| [Maven Resources Plugin][63]                            | [The Apache Software License, Version 2.0][36] |
| [Maven JAR Plugin][65]                                  | [The Apache Software License, Version 2.0][36] |
| [Maven Install Plugin][67]                              | [The Apache Software License, Version 2.0][36] |
| [Maven Site Plugin 3][69]                               | [The Apache Software License, Version 2.0][36] |

## Project Keeper Shared Test Setup

### Compile Dependencies

| Dependency                                | License                           |
| ----------------------------------------- | --------------------------------- |
| [Project-Keeper shared model classes][71] | [The MIT License][72]             |
| [SnakeYAML][88]                           | [Apache License, Version 2.0][36] |
| [Hamcrest][18]                            | [BSD License 3][19]               |
| [Maven Model][90]                         | [Apache License, Version 2.0][23] |
| [Project Lombok][0]                       | [The MIT License][1]              |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][29]                       | [GNU LGPL 3][30]                               |
| [Apache Maven Compiler Plugin][31]                      | [Apache License, Version 2.0][23]              |
| [Apache Maven Enforcer Plugin][33]                      | [Apache License, Version 2.0][23]              |
| [Maven Flatten Plugin][35]                              | [Apache Software Licenese][36]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][37] | [ASL2][36]                                     |
| [Reproducible Build Maven Plugin][39]                   | [Apache 2.0][36]                               |
| [Maven Surefire Plugin][41]                             | [Apache License, Version 2.0][23]              |
| [Versions Maven Plugin][43]                             | [Apache License, Version 2.0][23]              |
| [Lombok Maven Plugin][55]                               | [The MIT License][12]                          |
| [JaCoCo :: Maven Plugin][57]                            | [Eclipse Public License 2.0][58]               |
| [error-code-crawler-maven-plugin][59]                   | [MIT][12]                                      |
| [Maven Clean Plugin][61]                                | [The Apache Software License, Version 2.0][36] |
| [Maven Resources Plugin][63]                            | [The Apache Software License, Version 2.0][36] |
| [Maven JAR Plugin][65]                                  | [The Apache Software License, Version 2.0][36] |
| [Maven Install Plugin][67]                              | [The Apache Software License, Version 2.0][36] |
| [Maven Deploy Plugin][423]                              | [The Apache Software License, Version 2.0][36] |
| [Maven Site Plugin 3][69]                               | [The Apache Software License, Version 2.0][36] |

[246]: https://www.eclemma.org/jacoco/index.html
[9]: http://www.eclipse.org/legal/epl-v20.html
[11]: https://github.com/exasol/error-reporting-java
[36]: http://www.apache.org/licenses/LICENSE-2.0.txt
[0]: https://projectlombok.org
[41]: https://maven.apache.org/surefire/maven-surefire-plugin/
[76]: https://eclipse-ee4j.github.io/jaxb-ri/
[61]: http://maven.apache.org/plugins/maven-clean-plugin/
[5]: https://eclipse-ee4j.github.io/jsonb-api
[12]: https://opensource.org/licenses/MIT
[24]: https://github.com/mockito/mockito
[226]: http://maven.apache.org/
[35]: https://www.mojohaus.org/flatten-maven-plugin/
[94]: https://github.com/exasol/maven-project-version-getter
[43]: http://www.mojohaus.org/versions-maven-plugin/
[71]: https://github.com/exasol/project-keeper/
[19]: http://opensource.org/licenses/BSD-3-Clause
[31]: https://maven.apache.org/plugins/maven-compiler-plugin/
[74]: https://oss.oracle.com/licenses/CDDL+GPL-1.1
[82]: https://github.com/Steppschuh/Java-Markdown-Generator
[58]: https://www.eclipse.org/legal/epl-2.0/
[45]: https://maven.apache.org/plugins/maven-deploy-plugin/
[30]: http://www.gnu.org/licenses/lgpl.txt
[57]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[25]: https://github.com/mockito/mockito/blob/main/LICENSE
[78]: https://www.xmlunit.org/
[1]: https://projectlombok.org/LICENSE
[39]: http://zlika.github.io/reproducible-build-maven-plugin
[315]: https://commons.apache.org/proper/commons-io/
[85]: http://www.opensource.org/licenses/mit-license.php
[29]: http://sonarsource.github.io/sonar-scanner-maven/
[73]: https://github.com/eclipse-ee4j/jaxb-api
[14]: https://junit.org/junit5/
[88]: https://bitbucket.org/snakeyaml/snakeyaml
[260]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[2]: https://github.com/eclipse-ee4j/jsonp
[49]: https://maven.apache.org/plugins/maven-source-plugin/
[4]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[18]: http://hamcrest.org/JavaHamcrest/
[172]: http://www.slf4j.org
[63]: http://maven.apache.org/plugins/maven-resources-plugin/
[222]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[194]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[120]: https://maven.apache.org/plugins/maven-jar-plugin/
[90]: https://maven.apache.org/ref/3.8.5/maven-model/
[84]: https://github.com/vdurmont/semver4j
[53]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[8]: https://projects.eclipse.org/projects/ee4j.yasson
[142]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[224]: https://maven.apache.org/ref/3.8.5/maven-plugin-api/
[54]: http://www.eclipse.org/legal/epl-v10.html
[228]: https://maven.apache.org/ref/3.8.5/maven-core/
[72]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[282]: https://maven.apache.org/plugins/maven-dependency-plugin/
[65]: http://maven.apache.org/plugins/maven-jar-plugin/
[3]: https://projects.eclipse.org/license/epl-2.0
[10]: http://www.eclipse.org/org/documents/edl-v10.php
[23]: https://www.apache.org/licenses/LICENSE-2.0.txt
[22]: https://www.jqno.nl/equalsverifier
[33]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[15]: https://www.eclipse.org/legal/epl-v20.html
[67]: http://maven.apache.org/plugins/maven-install-plugin/
[37]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[13]: https://www.eclipse.org/jgit/
[47]: https://maven.apache.org/plugins/maven-gpg-plugin/
[20]: https://github.com/itsallcode/junit5-system-extensions
[106]: https://github.com/exasol/maven-plugin-integration-testing
[55]: https://anthonywhitford.com/lombok.maven/lombok-maven-plugin/
[69]: http://maven.apache.org/plugins/maven-site-plugin/
[423]: http://maven.apache.org/plugins/maven-deploy-plugin/
[51]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[59]: https://github.com/exasol/error-code-crawler-maven-plugin
[190]: https://maven.apache.org/plugins/maven-assembly-plugin/
