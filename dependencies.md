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
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][26]              |
| [Apache Maven Enforcer Plugin][27]                      | [Apache License, Version 2.0][26]              |
| [Maven Flatten Plugin][29]                              | [Apache Software Licenese][30]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][31] | [ASL2][30]                                     |
| [Reproducible Build Maven Plugin][33]                   | [Apache 2.0][30]                               |
| [Maven Surefire Plugin][35]                             | [Apache License, Version 2.0][26]              |
| [Versions Maven Plugin][37]                             | [Apache License, Version 2.0][26]              |
| [Apache Maven Deploy Plugin][39]                        | [Apache License, Version 2.0][26]              |
| [Apache Maven GPG Plugin][41]                           | [Apache License, Version 2.0][26]              |
| [Apache Maven Source Plugin][43]                        | [Apache License, Version 2.0][26]              |
| [Apache Maven Javadoc Plugin][45]                       | [Apache License, Version 2.0][26]              |
| [Nexus Staging Maven Plugin][47]                        | [Eclipse Public License][48]                   |
| [Lombok Maven Plugin][49]                               | [The MIT License][12]                          |
| [JaCoCo :: Maven Plugin][51]                            | [Eclipse Public License 2.0][52]               |
| [error-code-crawler-maven-plugin][53]                   | [MIT][12]                                      |
| [Maven Clean Plugin][55]                                | [The Apache Software License, Version 2.0][30] |
| [Maven Resources Plugin][57]                            | [The Apache Software License, Version 2.0][30] |
| [Maven JAR Plugin][59]                                  | [The Apache Software License, Version 2.0][30] |
| [Maven Install Plugin][61]                              | [The Apache Software License, Version 2.0][30] |
| [Maven Site Plugin 3][63]                               | [The Apache Software License, Version 2.0][30] |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project-Keeper shared model classes][65] | [MIT][12]                                      |
| [jaxb-api][67]                            | [CDDL 1.1][68]; [GPL2 w/ CPE][68]              |
| [JAXB Runtime][70]                        | [Eclipse Distribution License - v 1.0][10]     |
| [org.xmlunit:xmlunit-core][72]            | [The Apache Software License, Version 2.0][30] |
| [error-reporting-java][11]                | [MIT][12]                                      |
| [Markdown Generator][76]                  | [The Apache Software License, Version 2.0][30] |
| [semver4j][78]                            | [The MIT License][79]                          |
| [Project Lombok][0]                       | [The MIT License][1]                           |
| [SnakeYAML][82]                           | [Apache License, Version 2.0][30]              |
| [Maven Model][84]                         | [Apache License, Version 2.0][26]              |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][86]     | [MIT][12]                                      |
| [JUnit Jupiter Engine][14]             | [Eclipse Public License v2.0][15]              |
| [JUnit Jupiter Params][14]             | [Eclipse Public License v2.0][15]              |
| [Hamcrest][18]                         | [BSD License 3][19]                            |
| [org.xmlunit:xmlunit-matchers][72]     | [The Apache Software License, Version 2.0][30] |
| [mockito-core][96]                     | [The MIT License][97]                          |
| [Maven Plugin Integration Testing][98] | [MIT][12]                                      |

### Runtime Dependencies

| Dependency                                 | License   |
| ------------------------------------------ | --------- |
| [Project keeper Java project crawler][100] | [MIT][12] |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][26]              |
| [Apache Maven Enforcer Plugin][27]                      | [Apache License, Version 2.0][26]              |
| [Maven Flatten Plugin][29]                              | [Apache Software Licenese][30]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][31] | [ASL2][30]                                     |
| [Reproducible Build Maven Plugin][33]                   | [Apache 2.0][30]                               |
| [Maven Surefire Plugin][35]                             | [Apache License, Version 2.0][26]              |
| [Versions Maven Plugin][37]                             | [Apache License, Version 2.0][26]              |
| [Apache Maven Deploy Plugin][39]                        | [Apache License, Version 2.0][26]              |
| [Apache Maven GPG Plugin][41]                           | [Apache License, Version 2.0][26]              |
| [Apache Maven Source Plugin][43]                        | [Apache License, Version 2.0][26]              |
| [Apache Maven Javadoc Plugin][45]                       | [Apache License, Version 2.0][26]              |
| [Nexus Staging Maven Plugin][47]                        | [Eclipse Public License][48]                   |
| [Lombok Maven Plugin][49]                               | [The MIT License][12]                          |
| [Maven Failsafe Plugin][128]                            | [Apache License, Version 2.0][26]              |
| [JaCoCo :: Maven Plugin][51]                            | [Eclipse Public License 2.0][52]               |
| [error-code-crawler-maven-plugin][53]                   | [MIT][12]                                      |
| [Apache Maven JAR Plugin][134]                          | [Apache License, Version 2.0][26]              |
| [Maven Clean Plugin][55]                                | [The Apache Software License, Version 2.0][30] |
| [Maven Resources Plugin][57]                            | [The Apache Software License, Version 2.0][30] |
| [Maven Install Plugin][61]                              | [The Apache Software License, Version 2.0][30] |
| [Maven Site Plugin 3][63]                               | [The Apache Software License, Version 2.0][30] |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                 | License                                        |
| ------------------------------------------ | ---------------------------------------------- |
| [Project keeper core][100]                 | [MIT][12]                                      |
| [Maven Plugin Tools Java Annotations][146] | [Apache License, Version 2.0][26]              |
| [Maven Plugin API][148]                    | [Apache License, Version 2.0][26]              |
| [Maven Project Builder][150]               | [The Apache Software License, Version 2.0][30] |
| [Maven Core][152]                          | [Apache License, Version 2.0][26]              |
| [error-reporting-java][11]                 | [MIT][12]                                      |
| [Project Lombok][0]                        | [The MIT License][1]                           |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][86]     | [MIT][12]                                      |
| [JUnit Jupiter Engine][14]             | [Eclipse Public License v2.0][15]              |
| [JUnit Jupiter Params][14]             | [Eclipse Public License v2.0][15]              |
| [Hamcrest][18]                         | [BSD License 3][19]                            |
| [org.xmlunit:xmlunit-matchers][72]     | [The Apache Software License, Version 2.0][30] |
| [mockito-core][96]                     | [The MIT License][97]                          |
| [JaCoCo :: Core][170]                  | [Eclipse Public License 2.0][52]               |
| [Maven Plugin Integration Testing][98] | [MIT][12]                                      |
| [JaCoCo :: Agent][170]                 | [Eclipse Public License 2.0][52]               |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][26]              |
| [Apache Maven Enforcer Plugin][27]                      | [Apache License, Version 2.0][26]              |
| [Maven Flatten Plugin][29]                              | [Apache Software Licenese][30]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][31] | [ASL2][30]                                     |
| [Reproducible Build Maven Plugin][33]                   | [Apache 2.0][30]                               |
| [Maven Surefire Plugin][35]                             | [Apache License, Version 2.0][26]              |
| [Versions Maven Plugin][37]                             | [Apache License, Version 2.0][26]              |
| [Apache Maven Deploy Plugin][39]                        | [Apache License, Version 2.0][26]              |
| [Apache Maven GPG Plugin][41]                           | [Apache License, Version 2.0][26]              |
| [Apache Maven Source Plugin][43]                        | [Apache License, Version 2.0][26]              |
| [Apache Maven Javadoc Plugin][45]                       | [Apache License, Version 2.0][26]              |
| [Nexus Staging Maven Plugin][47]                        | [Eclipse Public License][48]                   |
| [Apache Maven Dependency Plugin][200]                   | [Apache License, Version 2.0][26]              |
| [Lombok Maven Plugin][49]                               | [The MIT License][12]                          |
| [Maven Failsafe Plugin][128]                            | [Apache License, Version 2.0][26]              |
| [JaCoCo :: Maven Plugin][51]                            | [Eclipse Public License 2.0][52]               |
| [error-code-crawler-maven-plugin][53]                   | [MIT][12]                                      |
| [Maven Plugin Plugin][210]                              | [Apache License, Version 2.0][26]              |
| [Apache Maven JAR Plugin][134]                          | [Apache License, Version 2.0][26]              |
| [Maven Clean Plugin][55]                                | [The Apache Software License, Version 2.0][30] |
| [Maven Resources Plugin][57]                            | [The Apache Software License, Version 2.0][30] |
| [Maven Install Plugin][61]                              | [The Apache Software License, Version 2.0][30] |
| [Maven Site Plugin 3][63]                               | [The Apache Software License, Version 2.0][30] |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                 | License                                        |
| ------------------------------------------ | ---------------------------------------------- |
| [Project-Keeper shared model classes][65]  | [MIT][12]                                      |
| [Maven Plugin Tools Java Annotations][146] | [Apache License, Version 2.0][26]              |
| [Maven Plugin API][148]                    | [Apache License, Version 2.0][26]              |
| [Maven Project Builder][150]               | [The Apache Software License, Version 2.0][30] |
| [error-reporting-java][11]                 | [MIT][12]                                      |
| [JGit - Core][13]                          | Eclipse Distribution License (New BSD License) |
| [semver4j][78]                             | [The MIT License][79]                          |
| [Maven Core][152]                          | [Apache License, Version 2.0][26]              |
| [Apache Commons IO][237]                   | [Apache License, Version 2.0][26]              |

### Test Dependencies

| Dependency                             | License                                        |
| -------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][86]     | [MIT][12]                                      |
| [JUnit Jupiter Engine][14]             | [Eclipse Public License v2.0][15]              |
| [JUnit Jupiter Params][14]             | [Eclipse Public License v2.0][15]              |
| [Hamcrest][18]                         | [BSD License 3][19]                            |
| [org.xmlunit:xmlunit-matchers][72]     | [The Apache Software License, Version 2.0][30] |
| [SLF4J JDK14 Binding][249]             | [MIT License][79]                              |
| [mockito-core][96]                     | [The MIT License][97]                          |
| [JaCoCo :: Core][170]                  | [Eclipse Public License 2.0][52]               |
| [Maven Plugin Integration Testing][98] | [MIT][12]                                      |
| [JaCoCo :: Agent][170]                 | [Eclipse Public License 2.0][52]               |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][26]              |
| [Apache Maven Enforcer Plugin][27]                      | [Apache License, Version 2.0][26]              |
| [Maven Flatten Plugin][29]                              | [Apache Software Licenese][30]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][31] | [ASL2][30]                                     |
| [Reproducible Build Maven Plugin][33]                   | [Apache 2.0][30]                               |
| [Maven Surefire Plugin][35]                             | [Apache License, Version 2.0][26]              |
| [Versions Maven Plugin][37]                             | [Apache License, Version 2.0][26]              |
| [Apache Maven Deploy Plugin][39]                        | [Apache License, Version 2.0][26]              |
| [Apache Maven GPG Plugin][41]                           | [Apache License, Version 2.0][26]              |
| [Apache Maven Source Plugin][43]                        | [Apache License, Version 2.0][26]              |
| [Apache Maven Javadoc Plugin][45]                       | [Apache License, Version 2.0][26]              |
| [Nexus Staging Maven Plugin][47]                        | [Eclipse Public License][48]                   |
| [Apache Maven Dependency Plugin][200]                   | [Apache License, Version 2.0][26]              |
| [Maven Failsafe Plugin][128]                            | [Apache License, Version 2.0][26]              |
| [JaCoCo :: Maven Plugin][51]                            | [Eclipse Public License 2.0][52]               |
| [error-code-crawler-maven-plugin][53]                   | [MIT][12]                                      |
| [Maven Plugin Plugin][210]                              | [Apache License, Version 2.0][26]              |
| [Maven Clean Plugin][55]                                | [The Apache Software License, Version 2.0][30] |
| [Maven Resources Plugin][57]                            | [The Apache Software License, Version 2.0][30] |
| [Maven JAR Plugin][59]                                  | [The Apache Software License, Version 2.0][30] |
| [Maven Install Plugin][61]                              | [The Apache Software License, Version 2.0][30] |
| [Maven Site Plugin 3][63]                               | [The Apache Software License, Version 2.0][30] |

[170]: https://www.eclemma.org/jacoco/index.html
[9]: http://www.eclipse.org/legal/epl-v20.html
[11]: https://github.com/exasol/error-reporting-java
[84]: https://maven.apache.org/ref/3.8.4/maven-model/
[30]: http://www.apache.org/licenses/LICENSE-2.0.txt
[0]: https://projectlombok.org
[35]: https://maven.apache.org/surefire/maven-surefire-plugin/
[70]: https://eclipse-ee4j.github.io/jaxb-ri/
[55]: http://maven.apache.org/plugins/maven-clean-plugin/
[5]: https://eclipse-ee4j.github.io/jsonb-api
[12]: https://opensource.org/licenses/MIT
[96]: https://github.com/mockito/mockito
[150]: http://maven.apache.org/
[86]: https://github.com/exasol/maven-project-version-getter
[37]: http://www.mojohaus.org/versions-maven-plugin/
[19]: http://opensource.org/licenses/BSD-3-Clause
[25]: https://maven.apache.org/plugins/maven-compiler-plugin/
[68]: https://oss.oracle.com/licenses/CDDL+GPL-1.1
[76]: https://github.com/Steppschuh/Java-Markdown-Generator
[52]: https://www.eclipse.org/legal/epl-2.0/
[39]: https://maven.apache.org/plugins/maven-deploy-plugin/
[51]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[72]: https://www.xmlunit.org/
[97]: https://github.com/mockito/mockito/blob/main/LICENSE
[1]: https://projectlombok.org/LICENSE
[33]: http://zlika.github.io/reproducible-build-maven-plugin
[237]: https://commons.apache.org/proper/commons-io/
[79]: http://www.opensource.org/licenses/mit-license.php
[67]: https://github.com/eclipse-ee4j/jaxb-api
[14]: https://junit.org/junit5/
[82]: https://bitbucket.org/snakeyaml/snakeyaml
[210]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[29]: https://www.mojohaus.org/flatten-maven-plugin/flatten-maven-plugin
[2]: https://github.com/eclipse-ee4j/jsonp
[43]: https://maven.apache.org/plugins/maven-source-plugin/
[4]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[18]: http://hamcrest.org/JavaHamcrest/
[249]: http://www.slf4j.org
[57]: http://maven.apache.org/plugins/maven-resources-plugin/
[146]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[134]: https://maven.apache.org/plugins/maven-jar-plugin/
[148]: https://maven.apache.org/ref/3.8.4/maven-plugin-api/
[100]: https://github.com/exasol/project-keeper
[152]: https://maven.apache.org/ref/3.8.4/maven-core/
[78]: https://github.com/vdurmont/semver4j
[47]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[8]: https://projects.eclipse.org/projects/ee4j.yasson
[128]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[48]: http://www.eclipse.org/legal/epl-v10.html
[65]: https://github.com/exasol/project-keeper/project-keeper-shared-model-classes-generated-parent/project-keeper-shared-model-classes
[200]: https://maven.apache.org/plugins/maven-dependency-plugin/
[59]: http://maven.apache.org/plugins/maven-jar-plugin/
[3]: https://projects.eclipse.org/license/epl-2.0
[10]: http://www.eclipse.org/org/documents/edl-v10.php
[26]: https://www.apache.org/licenses/LICENSE-2.0.txt
[27]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[49]: https://awhitford.github.com/lombok.maven/lombok-maven-plugin/
[15]: https://www.eclipse.org/legal/epl-v20.html
[61]: http://maven.apache.org/plugins/maven-install-plugin/
[31]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[13]: https://www.eclipse.org/jgit/
[41]: https://maven.apache.org/plugins/maven-gpg-plugin/
[20]: https://github.com/itsallcode/junit5-system-extensions
[98]: https://github.com/exasol/maven-plugin-integration-testing
[63]: http://maven.apache.org/plugins/maven-site-plugin/
[45]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[53]: https://github.com/exasol/error-code-crawler-maven-plugin
