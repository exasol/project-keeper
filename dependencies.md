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

| Dependency                     | License                           |
| ------------------------------ | --------------------------------- |
| [JUnit Jupiter Engine][14]     | [Eclipse Public License v2.0][15] |
| [JUnit Jupiter Params][14]     | [Eclipse Public License v2.0][15] |
| [Hamcrest][18]                 | [BSD License 3][19]               |
| [JUnit5 System Extensions][20] | [Eclipse Public License v2.0][9]  |

### Runtime Dependencies

| Dependency                   | License                                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [JSON-P Default Provider][2] | [Eclipse Public License 2.0][3]; [GNU General Public License, version 2 with the GNU Classpath Exception][4] |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [Apache Maven Enforcer Plugin][25]                      | [Apache License, Version 2.0][26]              |
| [Apache Maven Compiler Plugin][27]                      | [Apache License, Version 2.0][26]              |
| [Maven Failsafe Plugin][29]                             | [Apache License, Version 2.0][26]              |
| [Maven Surefire Plugin][31]                             | [Apache License, Version 2.0][26]              |
| [Versions Maven Plugin][33]                             | [Apache License, Version 2.0][26]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][35] | [ASL2][36]                                     |
| [Apache Maven Source Plugin][37]                        | [Apache License, Version 2.0][26]              |
| [Apache Maven Javadoc Plugin][39]                       | [Apache License, Version 2.0][26]              |
| [Apache Maven GPG Plugin][41]                           | [Apache License, Version 2.0][26]              |
| [Lombok Maven Plugin][43]                               | [The MIT License][12]                          |
| [JaCoCo :: Maven Plugin][45]                            | [Eclipse Public License 2.0][46]               |
| [Apache Maven Deploy Plugin][47]                        | [Apache License, Version 2.0][26]              |
| [Nexus Staging Maven Plugin][49]                        | [Eclipse Public License][50]                   |
| [Reproducible Build Maven Plugin][51]                   | [Apache 2.0][36]                               |
| [Maven Flatten Plugin][53]                              | [Apache Software Licenese][36]                 |
| [error-code-crawler-maven-plugin][55]                   | [MIT][12]                                      |
| [Maven Clean Plugin][57]                                | [The Apache Software License, Version 2.0][36] |
| [Maven Resources Plugin][59]                            | [The Apache Software License, Version 2.0][36] |
| [Maven JAR Plugin][61]                                  | [The Apache Software License, Version 2.0][36] |
| [Maven Install Plugin][63]                              | [The Apache Software License, Version 2.0][36] |
| [Maven Site Plugin 3][65]                               | [The Apache Software License, Version 2.0][36] |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project-Keeper shared model classes][67] | [MIT][12]                                      |
| [jaxb-api][69]                            | [CDDL 1.1][70]; [GPL2 w/ CPE][70]              |
| [JAXB Runtime][72]                        | [Eclipse Distribution License - v 1.0][10]     |
| [org.xmlunit:xmlunit-core][74]            | [The Apache Software License, Version 2.0][36] |
| [error-reporting-java][11]                | [MIT][12]                                      |
| [Markdown Generator][78]                  | [The Apache Software License, Version 2.0][36] |
| [semver4j][80]                            | [The MIT License][81]                          |
| [Project Lombok][0]                       | [The MIT License][1]                           |
| [SnakeYAML][84]                           | [Apache License, Version 2.0][36]              |
| [Maven Model][86]                         | [Apache License, Version 2.0][26]              |

### Test Dependencies

| Dependency                              | License                                        |
| --------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][88]      | [MIT][12]                                      |
| [JUnit Jupiter Engine][14]              | [Eclipse Public License v2.0][15]              |
| [JUnit Jupiter Params][14]              | [Eclipse Public License v2.0][15]              |
| [Hamcrest][18]                          | [BSD License 3][19]                            |
| [org.xmlunit:xmlunit-matchers][74]      | [The Apache Software License, Version 2.0][36] |
| [mockito-core][98]                      | [The MIT License][99]                          |
| [Maven Plugin Integration Testing][100] | [MIT][12]                                      |

### Runtime Dependencies

| Dependency                                 | License   |
| ------------------------------------------ | --------- |
| [Project keeper Java project crawler][102] | [MIT][12] |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [Apache Maven Enforcer Plugin][25]                      | [Apache License, Version 2.0][26]              |
| [Apache Maven Compiler Plugin][27]                      | [Apache License, Version 2.0][26]              |
| [Lombok Maven Plugin][43]                               | [The MIT License][12]                          |
| [Maven Failsafe Plugin][29]                             | [Apache License, Version 2.0][26]              |
| [Maven Surefire Plugin][31]                             | [Apache License, Version 2.0][26]              |
| [Versions Maven Plugin][33]                             | [Apache License, Version 2.0][26]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][35] | [ASL2][36]                                     |
| [Apache Maven Source Plugin][37]                        | [Apache License, Version 2.0][26]              |
| [Apache Maven Javadoc Plugin][39]                       | [Apache License, Version 2.0][26]              |
| [Apache Maven GPG Plugin][41]                           | [Apache License, Version 2.0][26]              |
| [JaCoCo :: Maven Plugin][45]                            | [Eclipse Public License 2.0][46]               |
| [Apache Maven Deploy Plugin][47]                        | [Apache License, Version 2.0][26]              |
| [Nexus Staging Maven Plugin][49]                        | [Eclipse Public License][50]                   |
| [Reproducible Build Maven Plugin][51]                   | [Apache 2.0][36]                               |
| [Maven Flatten Plugin][53]                              | [Apache Software Licenese][36]                 |
| [error-code-crawler-maven-plugin][55]                   | [MIT][12]                                      |
| [Apache Maven JAR Plugin][136]                          | [Apache License, Version 2.0][26]              |
| [Maven Clean Plugin][57]                                | [The Apache Software License, Version 2.0][36] |
| [Maven Resources Plugin][59]                            | [The Apache Software License, Version 2.0][36] |
| [Maven Install Plugin][63]                              | [The Apache Software License, Version 2.0][36] |
| [Maven Site Plugin 3][65]                               | [The Apache Software License, Version 2.0][36] |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                 | License                                        |
| ------------------------------------------ | ---------------------------------------------- |
| [Project keeper core][146]                 | [MIT][12]                                      |
| [Maven Plugin Tools Java Annotations][148] | [Apache License, Version 2.0][26]              |
| [Maven Plugin API][150]                    | [Apache License, Version 2.0][26]              |
| [Maven Project Builder][152]               | [The Apache Software License, Version 2.0][36] |
| [Maven Core][154]                          | [Apache License, Version 2.0][26]              |
| [error-reporting-java][11]                 | [MIT][12]                                      |
| [Project Lombok][0]                        | [The MIT License][1]                           |

### Test Dependencies

| Dependency                              | License                                        |
| --------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][88]      | [MIT][12]                                      |
| [JUnit Jupiter Engine][14]              | [Eclipse Public License v2.0][15]              |
| [JUnit Jupiter Params][14]              | [Eclipse Public License v2.0][15]              |
| [Hamcrest][18]                          | [BSD License 3][19]                            |
| [org.xmlunit:xmlunit-matchers][74]      | [The Apache Software License, Version 2.0][36] |
| [mockito-core][98]                      | [The MIT License][99]                          |
| [JaCoCo :: Core][172]                   | [Eclipse Public License 2.0][46]               |
| [Maven Plugin Integration Testing][100] | [MIT][12]                                      |
| [JaCoCo :: Agent][172]                  | [Eclipse Public License 2.0][46]               |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [Apache Maven Enforcer Plugin][25]                      | [Apache License, Version 2.0][26]              |
| [Apache Maven Compiler Plugin][27]                      | [Apache License, Version 2.0][26]              |
| [Apache Maven Dependency Plugin][182]                   | [Apache License, Version 2.0][26]              |
| [Lombok Maven Plugin][43]                               | [The MIT License][12]                          |
| [Maven Failsafe Plugin][29]                             | [Apache License, Version 2.0][26]              |
| [Maven Surefire Plugin][31]                             | [Apache License, Version 2.0][26]              |
| [Versions Maven Plugin][33]                             | [Apache License, Version 2.0][26]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][35] | [ASL2][36]                                     |
| [Apache Maven Source Plugin][37]                        | [Apache License, Version 2.0][26]              |
| [Apache Maven Javadoc Plugin][39]                       | [Apache License, Version 2.0][26]              |
| [Apache Maven GPG Plugin][41]                           | [Apache License, Version 2.0][26]              |
| [JaCoCo :: Maven Plugin][45]                            | [Eclipse Public License 2.0][46]               |
| [Apache Maven Deploy Plugin][47]                        | [Apache License, Version 2.0][26]              |
| [Nexus Staging Maven Plugin][49]                        | [Eclipse Public License][50]                   |
| [Reproducible Build Maven Plugin][51]                   | [Apache 2.0][36]                               |
| [Maven Flatten Plugin][53]                              | [Apache Software Licenese][36]                 |
| [error-code-crawler-maven-plugin][55]                   | [MIT][12]                                      |
| [Maven Plugin Plugin][212]                              | [Apache License, Version 2.0][26]              |
| [Apache Maven JAR Plugin][136]                          | [Apache License, Version 2.0][26]              |
| [Maven Clean Plugin][57]                                | [The Apache Software License, Version 2.0][36] |
| [Maven Resources Plugin][59]                            | [The Apache Software License, Version 2.0][36] |
| [Maven Install Plugin][63]                              | [The Apache Software License, Version 2.0][36] |
| [Maven Site Plugin 3][65]                               | [The Apache Software License, Version 2.0][36] |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                 | License                                        |
| ------------------------------------------ | ---------------------------------------------- |
| [Project-Keeper shared model classes][67]  | [MIT][12]                                      |
| [Maven Plugin Tools Java Annotations][148] | [Apache License, Version 2.0][26]              |
| [Maven Plugin API][150]                    | [Apache License, Version 2.0][26]              |
| [Maven Project Builder][152]               | [The Apache Software License, Version 2.0][36] |
| [error-reporting-java][11]                 | [MIT][12]                                      |
| [JGit - Core][13]                          | Eclipse Distribution License (New BSD License) |
| [semver4j][80]                             | [The MIT License][81]                          |
| [Maven Core][154]                          | [Apache License, Version 2.0][26]              |
| [Apache Commons IO][239]                   | [Apache License, Version 2.0][26]              |

### Test Dependencies

| Dependency                              | License                                        |
| --------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][88]      | [MIT][12]                                      |
| [JUnit Jupiter Engine][14]              | [Eclipse Public License v2.0][15]              |
| [JUnit Jupiter Params][14]              | [Eclipse Public License v2.0][15]              |
| [Hamcrest][18]                          | [BSD License 3][19]                            |
| [org.xmlunit:xmlunit-matchers][74]      | [The Apache Software License, Version 2.0][36] |
| [SLF4J JDK14 Binding][251]              | [MIT License][81]                              |
| [mockito-core][98]                      | [The MIT License][99]                          |
| [JaCoCo :: Agent][172]                  | [Eclipse Public License 2.0][46]               |
| [JaCoCo :: Core][172]                   | [Eclipse Public License 2.0][46]               |
| [Maven Plugin Integration Testing][100] | [MIT][12]                                      |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [Apache Maven Enforcer Plugin][25]                      | [Apache License, Version 2.0][26]              |
| [Apache Maven Compiler Plugin][27]                      | [Apache License, Version 2.0][26]              |
| [Apache Maven Dependency Plugin][182]                   | [Apache License, Version 2.0][26]              |
| [Maven Failsafe Plugin][29]                             | [Apache License, Version 2.0][26]              |
| [Maven Surefire Plugin][31]                             | [Apache License, Version 2.0][26]              |
| [Versions Maven Plugin][33]                             | [Apache License, Version 2.0][26]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][35] | [ASL2][36]                                     |
| [Apache Maven Source Plugin][37]                        | [Apache License, Version 2.0][26]              |
| [Apache Maven Javadoc Plugin][39]                       | [Apache License, Version 2.0][26]              |
| [Apache Maven GPG Plugin][41]                           | [Apache License, Version 2.0][26]              |
| [JaCoCo :: Maven Plugin][45]                            | [Eclipse Public License 2.0][46]               |
| [Apache Maven Deploy Plugin][47]                        | [Apache License, Version 2.0][26]              |
| [Nexus Staging Maven Plugin][49]                        | [Eclipse Public License][50]                   |
| [Reproducible Build Maven Plugin][51]                   | [Apache 2.0][36]                               |
| [Maven Flatten Plugin][53]                              | [Apache Software Licenese][36]                 |
| [error-code-crawler-maven-plugin][55]                   | [MIT][12]                                      |
| [Maven Plugin Plugin][212]                              | [Apache License, Version 2.0][26]              |
| [Maven Clean Plugin][57]                                | [The Apache Software License, Version 2.0][36] |
| [Maven Resources Plugin][59]                            | [The Apache Software License, Version 2.0][36] |
| [Maven JAR Plugin][61]                                  | [The Apache Software License, Version 2.0][36] |
| [Maven Install Plugin][63]                              | [The Apache Software License, Version 2.0][36] |
| [Maven Site Plugin 3][65]                               | [The Apache Software License, Version 2.0][36] |

[172]: https://www.eclemma.org/jacoco/index.html
[9]: http://www.eclipse.org/legal/epl-v20.html
[11]: https://github.com/exasol/error-reporting-java
[86]: https://maven.apache.org/ref/3.8.4/maven-model/
[36]: http://www.apache.org/licenses/LICENSE-2.0.txt
[102]: https://github.com/exasol/project-keeper-maven-plugin/project-keeper-java-project-crawler-generated-parent/project-keeper-java-project-crawler
[0]: https://projectlombok.org
[31]: https://maven.apache.org/surefire/maven-surefire-plugin/
[72]: https://eclipse-ee4j.github.io/jaxb-ri/
[57]: http://maven.apache.org/plugins/maven-clean-plugin/
[5]: https://eclipse-ee4j.github.io/jsonb-api
[12]: https://opensource.org/licenses/MIT
[98]: https://github.com/mockito/mockito
[152]: http://maven.apache.org/
[88]: https://github.com/exasol/maven-project-version-getter
[33]: http://www.mojohaus.org/versions-maven-plugin/
[19]: http://opensource.org/licenses/BSD-3-Clause
[27]: https://maven.apache.org/plugins/maven-compiler-plugin/
[70]: https://oss.oracle.com/licenses/CDDL+GPL-1.1
[78]: https://github.com/Steppschuh/Java-Markdown-Generator
[46]: https://www.eclipse.org/legal/epl-2.0/
[47]: https://maven.apache.org/plugins/maven-deploy-plugin/
[45]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[74]: https://www.xmlunit.org/
[99]: https://github.com/mockito/mockito/blob/main/LICENSE
[1]: https://projectlombok.org/LICENSE
[51]: http://zlika.github.io/reproducible-build-maven-plugin
[239]: https://commons.apache.org/proper/commons-io/
[81]: http://www.opensource.org/licenses/mit-license.php
[69]: https://github.com/eclipse-ee4j/jaxb-api
[14]: https://junit.org/junit5/
[84]: https://bitbucket.org/snakeyaml/snakeyaml
[212]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[53]: https://www.mojohaus.org/flatten-maven-plugin/flatten-maven-plugin
[2]: https://github.com/eclipse-ee4j/jsonp
[37]: https://maven.apache.org/plugins/maven-source-plugin/
[4]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[18]: http://hamcrest.org/JavaHamcrest/
[251]: http://www.slf4j.org
[59]: http://maven.apache.org/plugins/maven-resources-plugin/
[148]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[136]: https://maven.apache.org/plugins/maven-jar-plugin/
[150]: https://maven.apache.org/ref/3.8.4/maven-plugin-api/
[154]: https://maven.apache.org/ref/3.8.4/maven-core/
[80]: https://github.com/vdurmont/semver4j
[49]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[8]: https://projects.eclipse.org/projects/ee4j.yasson
[29]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[50]: http://www.eclipse.org/legal/epl-v10.html
[182]: https://maven.apache.org/plugins/maven-dependency-plugin/
[61]: http://maven.apache.org/plugins/maven-jar-plugin/
[3]: https://projects.eclipse.org/license/epl-2.0
[10]: http://www.eclipse.org/org/documents/edl-v10.php
[26]: https://www.apache.org/licenses/LICENSE-2.0.txt
[25]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[43]: https://awhitford.github.com/lombok.maven/lombok-maven-plugin/
[15]: https://www.eclipse.org/legal/epl-v20.html
[67]: https://github.com/exasol/project-keeper-maven-plugin/project-keeper-shared-model-classes-generated-parent/project-keeper-shared-model-classes
[63]: http://maven.apache.org/plugins/maven-install-plugin/
[35]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[13]: https://www.eclipse.org/jgit/
[41]: https://maven.apache.org/plugins/maven-gpg-plugin/
[20]: https://github.com/itsallcode/junit5-system-extensions
[100]: https://github.com/exasol/maven-plugin-integration-testing
[65]: http://maven.apache.org/plugins/maven-site-plugin/
[39]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[55]: https://github.com/exasol/error-code-crawler-maven-plugin
[146]: https://github.com/exasol/project-keeper-maven-plugin/project-keeper-core-generated-parent/project-keeper-core
