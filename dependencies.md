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
| [SonarQube Scanner for Maven][25]                       | [GNU LGPL 3][26]                               |
| [Apache Maven Compiler Plugin][27]                      | [Apache License, Version 2.0][28]              |
| [Apache Maven Enforcer Plugin][29]                      | [Apache License, Version 2.0][28]              |
| [Maven Flatten Plugin][31]                              | [Apache Software Licenese][32]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][33] | [ASL2][32]                                     |
| [Reproducible Build Maven Plugin][35]                   | [Apache 2.0][32]                               |
| [Maven Surefire Plugin][37]                             | [Apache License, Version 2.0][28]              |
| [Versions Maven Plugin][39]                             | [Apache License, Version 2.0][28]              |
| [Apache Maven Deploy Plugin][41]                        | [Apache License, Version 2.0][28]              |
| [Apache Maven GPG Plugin][43]                           | [Apache License, Version 2.0][28]              |
| [Apache Maven Source Plugin][45]                        | [Apache License, Version 2.0][28]              |
| [Apache Maven Javadoc Plugin][47]                       | [Apache License, Version 2.0][28]              |
| [Nexus Staging Maven Plugin][49]                        | [Eclipse Public License][50]                   |
| [Lombok Maven Plugin][51]                               | [The MIT License][12]                          |
| [JaCoCo :: Maven Plugin][53]                            | [Eclipse Public License 2.0][54]               |
| [error-code-crawler-maven-plugin][55]                   | [MIT][12]                                      |
| [Maven Clean Plugin][57]                                | [The Apache Software License, Version 2.0][32] |
| [Maven Resources Plugin][59]                            | [The Apache Software License, Version 2.0][32] |
| [Maven JAR Plugin][61]                                  | [The Apache Software License, Version 2.0][32] |
| [Maven Install Plugin][63]                              | [The Apache Software License, Version 2.0][32] |
| [Maven Site Plugin 3][65]                               | [The Apache Software License, Version 2.0][32] |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project-Keeper shared model classes][67] | [MIT][12]                                      |
| [jaxb-api][69]                            | [CDDL 1.1][70]; [GPL2 w/ CPE][70]              |
| [JAXB Runtime][72]                        | [Eclipse Distribution License - v 1.0][10]     |
| [org.xmlunit:xmlunit-core][74]            | [The Apache Software License, Version 2.0][32] |
| [error-reporting-java][11]                | [MIT][12]                                      |
| [Markdown Generator][78]                  | [The Apache Software License, Version 2.0][32] |
| [semver4j][80]                            | [The MIT License][81]                          |
| [Project Lombok][0]                       | [The MIT License][1]                           |
| [SnakeYAML][84]                           | [Apache License, Version 2.0][32]              |
| [Maven Model][86]                         | [Apache License, Version 2.0][28]              |

### Test Dependencies

| Dependency                              | License                                        |
| --------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][88]      | [MIT][12]                                      |
| [JUnit Jupiter Engine][14]              | [Eclipse Public License v2.0][15]              |
| [JUnit Jupiter Params][14]              | [Eclipse Public License v2.0][15]              |
| [Hamcrest][18]                          | [BSD License 3][19]                            |
| [org.xmlunit:xmlunit-matchers][74]      | [The Apache Software License, Version 2.0][32] |
| [mockito-core][98]                      | [The MIT License][99]                          |
| [Maven Plugin Integration Testing][100] | [MIT][12]                                      |

### Runtime Dependencies

| Dependency                                | License   |
| ----------------------------------------- | --------- |
| [Project keeper Java project crawler][67] | [MIT][12] |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][25]                       | [GNU LGPL 3][26]                               |
| [Apache Maven Compiler Plugin][27]                      | [Apache License, Version 2.0][28]              |
| [Apache Maven Enforcer Plugin][29]                      | [Apache License, Version 2.0][28]              |
| [Maven Flatten Plugin][31]                              | [Apache Software Licenese][32]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][33] | [ASL2][32]                                     |
| [Reproducible Build Maven Plugin][35]                   | [Apache 2.0][32]                               |
| [Maven Surefire Plugin][37]                             | [Apache License, Version 2.0][28]              |
| [Versions Maven Plugin][39]                             | [Apache License, Version 2.0][28]              |
| [Apache Maven Deploy Plugin][41]                        | [Apache License, Version 2.0][28]              |
| [Apache Maven GPG Plugin][43]                           | [Apache License, Version 2.0][28]              |
| [Apache Maven Source Plugin][45]                        | [Apache License, Version 2.0][28]              |
| [Apache Maven Javadoc Plugin][47]                       | [Apache License, Version 2.0][28]              |
| [Nexus Staging Maven Plugin][49]                        | [Eclipse Public License][50]                   |
| [Lombok Maven Plugin][51]                               | [The MIT License][12]                          |
| [Maven Failsafe Plugin][132]                            | [Apache License, Version 2.0][28]              |
| [JaCoCo :: Maven Plugin][53]                            | [Eclipse Public License 2.0][54]               |
| [error-code-crawler-maven-plugin][55]                   | [MIT][12]                                      |
| [Apache Maven JAR Plugin][138]                          | [Apache License, Version 2.0][28]              |
| [Maven Clean Plugin][57]                                | [The Apache Software License, Version 2.0][32] |
| [Maven Resources Plugin][59]                            | [The Apache Software License, Version 2.0][32] |
| [Maven Install Plugin][63]                              | [The Apache Software License, Version 2.0][32] |
| [Maven Site Plugin 3][65]                               | [The Apache Software License, Version 2.0][32] |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                 | License                                        |
| ------------------------------------------ | ---------------------------------------------- |
| [Project keeper core][67]                  | [MIT][12]                                      |
| [Maven Plugin Tools Java Annotations][150] | [Apache License, Version 2.0][28]              |
| [Maven Plugin API][152]                    | [Apache License, Version 2.0][28]              |
| [Maven Project Builder][154]               | [The Apache Software License, Version 2.0][32] |
| [Maven Core][156]                          | [Apache License, Version 2.0][28]              |
| [error-reporting-java][11]                 | [MIT][12]                                      |
| [Project Lombok][0]                        | [The MIT License][1]                           |

### Test Dependencies

| Dependency                              | License                                        |
| --------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][88]      | [MIT][12]                                      |
| [JUnit Jupiter Engine][14]              | [Eclipse Public License v2.0][15]              |
| [JUnit Jupiter Params][14]              | [Eclipse Public License v2.0][15]              |
| [Hamcrest][18]                          | [BSD License 3][19]                            |
| [org.xmlunit:xmlunit-matchers][74]      | [The Apache Software License, Version 2.0][32] |
| [mockito-core][98]                      | [The MIT License][99]                          |
| [JaCoCo :: Core][174]                   | [Eclipse Public License 2.0][54]               |
| [Maven Plugin Integration Testing][100] | [MIT][12]                                      |
| [JaCoCo :: Agent][174]                  | [Eclipse Public License 2.0][54]               |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][25]                       | [GNU LGPL 3][26]                               |
| [Apache Maven Compiler Plugin][27]                      | [Apache License, Version 2.0][28]              |
| [Apache Maven Enforcer Plugin][29]                      | [Apache License, Version 2.0][28]              |
| [Maven Flatten Plugin][31]                              | [Apache Software Licenese][32]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][33] | [ASL2][32]                                     |
| [Reproducible Build Maven Plugin][35]                   | [Apache 2.0][32]                               |
| [Maven Surefire Plugin][37]                             | [Apache License, Version 2.0][28]              |
| [Versions Maven Plugin][39]                             | [Apache License, Version 2.0][28]              |
| [Apache Maven Deploy Plugin][41]                        | [Apache License, Version 2.0][28]              |
| [Apache Maven GPG Plugin][43]                           | [Apache License, Version 2.0][28]              |
| [Apache Maven Source Plugin][45]                        | [Apache License, Version 2.0][28]              |
| [Apache Maven Javadoc Plugin][47]                       | [Apache License, Version 2.0][28]              |
| [Nexus Staging Maven Plugin][49]                        | [Eclipse Public License][50]                   |
| [Apache Maven Dependency Plugin][206]                   | [Apache License, Version 2.0][28]              |
| [Lombok Maven Plugin][51]                               | [The MIT License][12]                          |
| [Maven Failsafe Plugin][132]                            | [Apache License, Version 2.0][28]              |
| [JaCoCo :: Maven Plugin][53]                            | [Eclipse Public License 2.0][54]               |
| [error-code-crawler-maven-plugin][55]                   | [MIT][12]                                      |
| [Maven Plugin Plugin][216]                              | [Apache License, Version 2.0][28]              |
| [Apache Maven JAR Plugin][138]                          | [Apache License, Version 2.0][28]              |
| [Maven Clean Plugin][57]                                | [The Apache Software License, Version 2.0][32] |
| [Maven Resources Plugin][59]                            | [The Apache Software License, Version 2.0][32] |
| [Maven Install Plugin][63]                              | [The Apache Software License, Version 2.0][32] |
| [Maven Site Plugin 3][65]                               | [The Apache Software License, Version 2.0][32] |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                 | License                                        |
| ------------------------------------------ | ---------------------------------------------- |
| [Project-Keeper shared model classes][67]  | [MIT][12]                                      |
| [Maven Plugin Tools Java Annotations][150] | [Apache License, Version 2.0][28]              |
| [Maven Plugin API][152]                    | [Apache License, Version 2.0][28]              |
| [Maven Project Builder][154]               | [The Apache Software License, Version 2.0][32] |
| [error-reporting-java][11]                 | [MIT][12]                                      |
| [JGit - Core][13]                          | Eclipse Distribution License (New BSD License) |
| [semver4j][80]                             | [The MIT License][81]                          |
| [Maven Core][156]                          | [Apache License, Version 2.0][28]              |
| [Apache Commons IO][243]                   | [Apache License, Version 2.0][28]              |

### Test Dependencies

| Dependency                              | License                                        |
| --------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][88]      | [MIT][12]                                      |
| [JUnit Jupiter Engine][14]              | [Eclipse Public License v2.0][15]              |
| [JUnit Jupiter Params][14]              | [Eclipse Public License v2.0][15]              |
| [Hamcrest][18]                          | [BSD License 3][19]                            |
| [org.xmlunit:xmlunit-matchers][74]      | [The Apache Software License, Version 2.0][32] |
| [SLF4J JDK14 Binding][255]              | [MIT License][81]                              |
| [mockito-core][98]                      | [The MIT License][99]                          |
| [JaCoCo :: Core][174]                   | [Eclipse Public License 2.0][54]               |
| [Maven Plugin Integration Testing][100] | [MIT][12]                                      |
| [JaCoCo :: Agent][174]                  | [Eclipse Public License 2.0][54]               |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][25]                       | [GNU LGPL 3][26]                               |
| [Apache Maven Compiler Plugin][27]                      | [Apache License, Version 2.0][28]              |
| [Apache Maven Enforcer Plugin][29]                      | [Apache License, Version 2.0][28]              |
| [Maven Flatten Plugin][31]                              | [Apache Software Licenese][32]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][33] | [ASL2][32]                                     |
| [Reproducible Build Maven Plugin][35]                   | [Apache 2.0][32]                               |
| [Maven Surefire Plugin][37]                             | [Apache License, Version 2.0][28]              |
| [Versions Maven Plugin][39]                             | [Apache License, Version 2.0][28]              |
| [Apache Maven Deploy Plugin][41]                        | [Apache License, Version 2.0][28]              |
| [Apache Maven GPG Plugin][43]                           | [Apache License, Version 2.0][28]              |
| [Apache Maven Source Plugin][45]                        | [Apache License, Version 2.0][28]              |
| [Apache Maven Javadoc Plugin][47]                       | [Apache License, Version 2.0][28]              |
| [Nexus Staging Maven Plugin][49]                        | [Eclipse Public License][50]                   |
| [Apache Maven Dependency Plugin][206]                   | [Apache License, Version 2.0][28]              |
| [Maven Failsafe Plugin][132]                            | [Apache License, Version 2.0][28]              |
| [JaCoCo :: Maven Plugin][53]                            | [Eclipse Public License 2.0][54]               |
| [error-code-crawler-maven-plugin][55]                   | [MIT][12]                                      |
| [Maven Plugin Plugin][216]                              | [Apache License, Version 2.0][28]              |
| [Maven Clean Plugin][57]                                | [The Apache Software License, Version 2.0][32] |
| [Maven Resources Plugin][59]                            | [The Apache Software License, Version 2.0][32] |
| [Maven JAR Plugin][61]                                  | [The Apache Software License, Version 2.0][32] |
| [Maven Install Plugin][63]                              | [The Apache Software License, Version 2.0][32] |
| [Maven Site Plugin 3][65]                               | [The Apache Software License, Version 2.0][32] |

[174]: https://www.eclemma.org/jacoco/index.html
[9]: http://www.eclipse.org/legal/epl-v20.html
[11]: https://github.com/exasol/error-reporting-java
[86]: https://maven.apache.org/ref/3.8.4/maven-model/
[32]: http://www.apache.org/licenses/LICENSE-2.0.txt
[0]: https://projectlombok.org
[37]: https://maven.apache.org/surefire/maven-surefire-plugin/
[72]: https://eclipse-ee4j.github.io/jaxb-ri/
[57]: http://maven.apache.org/plugins/maven-clean-plugin/
[5]: https://eclipse-ee4j.github.io/jsonb-api
[12]: https://opensource.org/licenses/MIT
[98]: https://github.com/mockito/mockito
[154]: http://maven.apache.org/
[88]: https://github.com/exasol/maven-project-version-getter
[39]: http://www.mojohaus.org/versions-maven-plugin/
[67]: https://github.com/exasol/project-keeper/
[19]: http://opensource.org/licenses/BSD-3-Clause
[27]: https://maven.apache.org/plugins/maven-compiler-plugin/
[70]: https://oss.oracle.com/licenses/CDDL+GPL-1.1
[78]: https://github.com/Steppschuh/Java-Markdown-Generator
[54]: https://www.eclipse.org/legal/epl-2.0/
[41]: https://maven.apache.org/plugins/maven-deploy-plugin/
[26]: http://www.gnu.org/licenses/lgpl.txt
[53]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[74]: https://www.xmlunit.org/
[99]: https://github.com/mockito/mockito/blob/main/LICENSE
[1]: https://projectlombok.org/LICENSE
[35]: http://zlika.github.io/reproducible-build-maven-plugin
[243]: https://commons.apache.org/proper/commons-io/
[81]: http://www.opensource.org/licenses/mit-license.php
[25]: http://sonarsource.github.io/sonar-scanner-maven/
[69]: https://github.com/eclipse-ee4j/jaxb-api
[14]: https://junit.org/junit5/
[84]: https://bitbucket.org/snakeyaml/snakeyaml
[216]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[31]: https://www.mojohaus.org/flatten-maven-plugin/flatten-maven-plugin
[2]: https://github.com/eclipse-ee4j/jsonp
[45]: https://maven.apache.org/plugins/maven-source-plugin/
[4]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[18]: http://hamcrest.org/JavaHamcrest/
[255]: http://www.slf4j.org
[59]: http://maven.apache.org/plugins/maven-resources-plugin/
[150]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[138]: https://maven.apache.org/plugins/maven-jar-plugin/
[80]: https://github.com/vdurmont/semver4j
[49]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[8]: https://projects.eclipse.org/projects/ee4j.yasson
[132]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[152]: https://maven.apache.org/ref/3.8.5/maven-plugin-api/
[50]: http://www.eclipse.org/legal/epl-v10.html
[156]: https://maven.apache.org/ref/3.8.5/maven-core/
[206]: https://maven.apache.org/plugins/maven-dependency-plugin/
[61]: http://maven.apache.org/plugins/maven-jar-plugin/
[3]: https://projects.eclipse.org/license/epl-2.0
[10]: http://www.eclipse.org/org/documents/edl-v10.php
[28]: https://www.apache.org/licenses/LICENSE-2.0.txt
[29]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[51]: https://awhitford.github.com/lombok.maven/lombok-maven-plugin/
[15]: https://www.eclipse.org/legal/epl-v20.html
[63]: http://maven.apache.org/plugins/maven-install-plugin/
[33]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[13]: https://www.eclipse.org/jgit/
[43]: https://maven.apache.org/plugins/maven-gpg-plugin/
[20]: https://github.com/itsallcode/junit5-system-extensions
[100]: https://github.com/exasol/maven-plugin-integration-testing
[65]: http://maven.apache.org/plugins/maven-site-plugin/
[47]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[55]: https://github.com/exasol/error-code-crawler-maven-plugin
