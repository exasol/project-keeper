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

### Runtime Dependencies

| Dependency                   | License                                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [JSON-P Default Provider][2] | [Eclipse Public License 2.0][3]; [GNU General Public License, version 2 with the GNU Classpath Exception][4] |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][27]                       | [GNU LGPL 3][28]                               |
| [Apache Maven Compiler Plugin][29]                      | [Apache License, Version 2.0][23]              |
| [Apache Maven Enforcer Plugin][31]                      | [Apache License, Version 2.0][23]              |
| [Maven Flatten Plugin][33]                              | [Apache Software Licenese][34]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][35] | [ASL2][34]                                     |
| [Reproducible Build Maven Plugin][37]                   | [Apache 2.0][34]                               |
| [Maven Surefire Plugin][39]                             | [Apache License, Version 2.0][23]              |
| [Versions Maven Plugin][41]                             | [Apache License, Version 2.0][23]              |
| [Apache Maven Deploy Plugin][43]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven GPG Plugin][45]                           | [Apache License, Version 2.0][23]              |
| [Apache Maven Source Plugin][47]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven Javadoc Plugin][49]                       | [Apache License, Version 2.0][23]              |
| [Nexus Staging Maven Plugin][51]                        | [Eclipse Public License][52]                   |
| [Lombok Maven Plugin][53]                               | [The MIT License][12]                          |
| [JaCoCo :: Maven Plugin][55]                            | [Eclipse Public License 2.0][56]               |
| [error-code-crawler-maven-plugin][57]                   | [MIT][12]                                      |
| [Maven Clean Plugin][59]                                | [The Apache Software License, Version 2.0][34] |
| [Maven Resources Plugin][61]                            | [The Apache Software License, Version 2.0][34] |
| [Maven JAR Plugin][63]                                  | [The Apache Software License, Version 2.0][34] |
| [Maven Install Plugin][65]                              | [The Apache Software License, Version 2.0][34] |
| [Maven Site Plugin 3][67]                               | [The Apache Software License, Version 2.0][34] |

## Project Keeper Core

### Compile Dependencies

| Dependency                                | License                                        |
| ----------------------------------------- | ---------------------------------------------- |
| [Project-Keeper shared model classes][69] | [The MIT License][70]                          |
| [jaxb-api][71]                            | [CDDL 1.1][72]; [GPL2 w/ CPE][72]              |
| [JAXB Runtime][74]                        | [Eclipse Distribution License - v 1.0][10]     |
| [org.xmlunit:xmlunit-core][76]            | [The Apache Software License, Version 2.0][34] |
| [error-reporting-java][11]                | [MIT][12]                                      |
| [Markdown Generator][80]                  | [The Apache Software License, Version 2.0][34] |
| [semver4j][82]                            | [The MIT License][83]                          |
| [Project Lombok][0]                       | [The MIT License][1]                           |
| [SnakeYAML][86]                           | [Apache License, Version 2.0][34]              |
| [Maven Model][88]                         | [Apache License, Version 2.0][23]              |

### Test Dependencies

| Dependency                              | License                                        |
| --------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][90]      | [MIT][12]                                      |
| [JUnit Jupiter Engine][14]              | [Eclipse Public License v2.0][15]              |
| [JUnit Jupiter Params][14]              | [Eclipse Public License v2.0][15]              |
| [Hamcrest][18]                          | [BSD License 3][19]                            |
| [org.xmlunit:xmlunit-matchers][76]      | [The Apache Software License, Version 2.0][34] |
| [mockito-core][100]                     | [The MIT License][101]                         |
| [Maven Plugin Integration Testing][102] | [MIT][12]                                      |

### Runtime Dependencies

| Dependency                                | License               |
| ----------------------------------------- | --------------------- |
| [Project keeper Java project crawler][69] | [The MIT License][70] |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][27]                       | [GNU LGPL 3][28]                               |
| [Apache Maven Compiler Plugin][29]                      | [Apache License, Version 2.0][23]              |
| [Apache Maven Enforcer Plugin][31]                      | [Apache License, Version 2.0][23]              |
| [Maven Flatten Plugin][33]                              | [Apache Software Licenese][34]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][35] | [ASL2][34]                                     |
| [Reproducible Build Maven Plugin][37]                   | [Apache 2.0][34]                               |
| [Maven Surefire Plugin][39]                             | [Apache License, Version 2.0][23]              |
| [Versions Maven Plugin][41]                             | [Apache License, Version 2.0][23]              |
| [Apache Maven Deploy Plugin][43]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven GPG Plugin][45]                           | [Apache License, Version 2.0][23]              |
| [Apache Maven Source Plugin][47]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven Javadoc Plugin][49]                       | [Apache License, Version 2.0][23]              |
| [Nexus Staging Maven Plugin][51]                        | [Eclipse Public License][52]                   |
| [Lombok Maven Plugin][53]                               | [The MIT License][12]                          |
| [Maven Failsafe Plugin][134]                            | [Apache License, Version 2.0][23]              |
| [JaCoCo :: Maven Plugin][55]                            | [Eclipse Public License 2.0][56]               |
| [error-code-crawler-maven-plugin][57]                   | [MIT][12]                                      |
| [Apache Maven JAR Plugin][140]                          | [Apache License, Version 2.0][23]              |
| [Maven Clean Plugin][59]                                | [The Apache Software License, Version 2.0][34] |
| [Maven Resources Plugin][61]                            | [The Apache Software License, Version 2.0][34] |
| [Maven Install Plugin][65]                              | [The Apache Software License, Version 2.0][34] |
| [Maven Site Plugin 3][67]                               | [The Apache Software License, Version 2.0][34] |

## Project Keeper Command Line Interface

### Compile Dependencies

| Dependency                 | License               |
| -------------------------- | --------------------- |
| [Project keeper core][69]  | [The MIT License][70] |
| [error-reporting-java][11] | [MIT][12]             |

### Test Dependencies

| Dependency                 | License                           |
| -------------------------- | --------------------------------- |
| [JUnit Jupiter Engine][14] | [Eclipse Public License v2.0][15] |
| [JUnit Jupiter Params][14] | [Eclipse Public License v2.0][15] |
| [Hamcrest][18]             | [BSD License 3][19]               |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][27]                       | [GNU LGPL 3][28]                               |
| [Apache Maven Compiler Plugin][29]                      | [Apache License, Version 2.0][23]              |
| [Apache Maven Enforcer Plugin][31]                      | [Apache License, Version 2.0][23]              |
| [Maven Flatten Plugin][33]                              | [Apache Software Licenese][34]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][35] | [ASL2][34]                                     |
| [Reproducible Build Maven Plugin][37]                   | [Apache 2.0][34]                               |
| [Maven Surefire Plugin][39]                             | [Apache License, Version 2.0][23]              |
| [Versions Maven Plugin][41]                             | [Apache License, Version 2.0][23]              |
| [Apache Maven Assembly Plugin][176]                     | [Apache License, Version 2.0][23]              |
| [Apache Maven JAR Plugin][140]                          | [Apache License, Version 2.0][23]              |
| [Artifact reference checker and unifier][180]           | [MIT][12]                                      |
| [Apache Maven Deploy Plugin][43]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven GPG Plugin][45]                           | [Apache License, Version 2.0][23]              |
| [Apache Maven Source Plugin][47]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven Javadoc Plugin][49]                       | [Apache License, Version 2.0][23]              |
| [Nexus Staging Maven Plugin][51]                        | [Eclipse Public License][52]                   |
| [Maven Failsafe Plugin][134]                            | [Apache License, Version 2.0][23]              |
| [JaCoCo :: Maven Plugin][55]                            | [Eclipse Public License 2.0][56]               |
| [error-code-crawler-maven-plugin][57]                   | [MIT][12]                                      |
| [Maven Clean Plugin][59]                                | [The Apache Software License, Version 2.0][34] |
| [Maven Resources Plugin][61]                            | [The Apache Software License, Version 2.0][34] |
| [Maven Install Plugin][65]                              | [The Apache Software License, Version 2.0][34] |
| [Maven Site Plugin 3][67]                               | [The Apache Software License, Version 2.0][34] |

## Project Keeper Maven Plugin

### Compile Dependencies

| Dependency                                 | License                                        |
| ------------------------------------------ | ---------------------------------------------- |
| [Project keeper core][69]                  | [The MIT License][70]                          |
| [Maven Plugin Tools Java Annotations][208] | [Apache License, Version 2.0][23]              |
| [Maven Plugin API][210]                    | [Apache License, Version 2.0][23]              |
| [Maven Project Builder][212]               | [The Apache Software License, Version 2.0][34] |
| [Maven Core][214]                          | [Apache License, Version 2.0][23]              |
| [error-reporting-java][11]                 | [MIT][12]                                      |
| [Project Lombok][0]                        | [The MIT License][1]                           |

### Test Dependencies

| Dependency                              | License                                        |
| --------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][90]      | [MIT][12]                                      |
| [JUnit Jupiter Engine][14]              | [Eclipse Public License v2.0][15]              |
| [JUnit Jupiter Params][14]              | [Eclipse Public License v2.0][15]              |
| [Hamcrest][18]                          | [BSD License 3][19]                            |
| [org.xmlunit:xmlunit-matchers][76]      | [The Apache Software License, Version 2.0][34] |
| [mockito-core][100]                     | [The MIT License][101]                         |
| [JaCoCo :: Core][232]                   | [Eclipse Public License 2.0][56]               |
| [Maven Plugin Integration Testing][102] | [MIT][12]                                      |
| [JaCoCo :: Agent][232]                  | [Eclipse Public License 2.0][56]               |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][27]                       | [GNU LGPL 3][28]                               |
| [Apache Maven Compiler Plugin][29]                      | [Apache License, Version 2.0][23]              |
| [Apache Maven Enforcer Plugin][31]                      | [Apache License, Version 2.0][23]              |
| [Maven Flatten Plugin][33]                              | [Apache Software Licenese][34]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][35] | [ASL2][34]                                     |
| [Reproducible Build Maven Plugin][37]                   | [Apache 2.0][34]                               |
| [Maven Surefire Plugin][39]                             | [Apache License, Version 2.0][23]              |
| [Versions Maven Plugin][41]                             | [Apache License, Version 2.0][23]              |
| [Apache Maven Deploy Plugin][43]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven GPG Plugin][45]                           | [Apache License, Version 2.0][23]              |
| [Apache Maven Source Plugin][47]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven Javadoc Plugin][49]                       | [Apache License, Version 2.0][23]              |
| [Nexus Staging Maven Plugin][51]                        | [Eclipse Public License][52]                   |
| [Apache Maven Dependency Plugin][264]                   | [Apache License, Version 2.0][23]              |
| [Lombok Maven Plugin][53]                               | [The MIT License][12]                          |
| [Maven Failsafe Plugin][134]                            | [Apache License, Version 2.0][23]              |
| [JaCoCo :: Maven Plugin][55]                            | [Eclipse Public License 2.0][56]               |
| [error-code-crawler-maven-plugin][57]                   | [MIT][12]                                      |
| [Maven Plugin Plugin][274]                              | [Apache License, Version 2.0][23]              |
| [Apache Maven JAR Plugin][140]                          | [Apache License, Version 2.0][23]              |
| [Maven Clean Plugin][59]                                | [The Apache Software License, Version 2.0][34] |
| [Maven Resources Plugin][61]                            | [The Apache Software License, Version 2.0][34] |
| [Maven Install Plugin][65]                              | [The Apache Software License, Version 2.0][34] |
| [Maven Site Plugin 3][67]                               | [The Apache Software License, Version 2.0][34] |

## Project Keeper Java Project Crawler

### Compile Dependencies

| Dependency                                 | License                                        |
| ------------------------------------------ | ---------------------------------------------- |
| [Project-Keeper shared model classes][69]  | [The MIT License][70]                          |
| [Maven Plugin Tools Java Annotations][208] | [Apache License, Version 2.0][23]              |
| [Maven Plugin API][210]                    | [Apache License, Version 2.0][23]              |
| [Maven Project Builder][212]               | [The Apache Software License, Version 2.0][34] |
| [error-reporting-java][11]                 | [MIT][12]                                      |
| [JGit - Core][13]                          | Eclipse Distribution License (New BSD License) |
| [semver4j][82]                             | [The MIT License][83]                          |
| [Maven Core][214]                          | [Apache License, Version 2.0][23]              |
| [Apache Commons IO][301]                   | [Apache License, Version 2.0][23]              |

### Test Dependencies

| Dependency                              | License                                        |
| --------------------------------------- | ---------------------------------------------- |
| [Maven Project Version Getter][90]      | [MIT][12]                                      |
| [JUnit Jupiter Engine][14]              | [Eclipse Public License v2.0][15]              |
| [JUnit Jupiter Params][14]              | [Eclipse Public License v2.0][15]              |
| [Hamcrest][18]                          | [BSD License 3][19]                            |
| [org.xmlunit:xmlunit-matchers][76]      | [The Apache Software License, Version 2.0][34] |
| [SLF4J JDK14 Binding][313]              | [MIT License][83]                              |
| [mockito-core][100]                     | [The MIT License][101]                         |
| [JaCoCo :: Core][232]                   | [Eclipse Public License 2.0][56]               |
| [Maven Plugin Integration Testing][102] | [MIT][12]                                      |
| [JaCoCo :: Agent][232]                  | [Eclipse Public License 2.0][56]               |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][27]                       | [GNU LGPL 3][28]                               |
| [Apache Maven Compiler Plugin][29]                      | [Apache License, Version 2.0][23]              |
| [Apache Maven Enforcer Plugin][31]                      | [Apache License, Version 2.0][23]              |
| [Maven Flatten Plugin][33]                              | [Apache Software Licenese][34]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][35] | [ASL2][34]                                     |
| [Reproducible Build Maven Plugin][37]                   | [Apache 2.0][34]                               |
| [Maven Surefire Plugin][39]                             | [Apache License, Version 2.0][23]              |
| [Versions Maven Plugin][41]                             | [Apache License, Version 2.0][23]              |
| [Apache Maven Deploy Plugin][43]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven GPG Plugin][45]                           | [Apache License, Version 2.0][23]              |
| [Apache Maven Source Plugin][47]                        | [Apache License, Version 2.0][23]              |
| [Apache Maven Javadoc Plugin][49]                       | [Apache License, Version 2.0][23]              |
| [Nexus Staging Maven Plugin][51]                        | [Eclipse Public License][52]                   |
| [Apache Maven Dependency Plugin][264]                   | [Apache License, Version 2.0][23]              |
| [Maven Failsafe Plugin][134]                            | [Apache License, Version 2.0][23]              |
| [JaCoCo :: Maven Plugin][55]                            | [Eclipse Public License 2.0][56]               |
| [error-code-crawler-maven-plugin][57]                   | [MIT][12]                                      |
| [Maven Plugin Plugin][274]                              | [Apache License, Version 2.0][23]              |
| [Maven Clean Plugin][59]                                | [The Apache Software License, Version 2.0][34] |
| [Maven Resources Plugin][61]                            | [The Apache Software License, Version 2.0][34] |
| [Maven JAR Plugin][63]                                  | [The Apache Software License, Version 2.0][34] |
| [Maven Install Plugin][65]                              | [The Apache Software License, Version 2.0][34] |
| [Maven Site Plugin 3][67]                               | [The Apache Software License, Version 2.0][34] |

[232]: https://www.eclemma.org/jacoco/index.html
[9]: http://www.eclipse.org/legal/epl-v20.html
[11]: https://github.com/exasol/error-reporting-java
[34]: http://www.apache.org/licenses/LICENSE-2.0.txt
[0]: https://projectlombok.org
[39]: https://maven.apache.org/surefire/maven-surefire-plugin/
[74]: https://eclipse-ee4j.github.io/jaxb-ri/
[59]: http://maven.apache.org/plugins/maven-clean-plugin/
[5]: https://eclipse-ee4j.github.io/jsonb-api
[12]: https://opensource.org/licenses/MIT
[100]: https://github.com/mockito/mockito
[212]: http://maven.apache.org/
[90]: https://github.com/exasol/maven-project-version-getter
[41]: http://www.mojohaus.org/versions-maven-plugin/
[69]: https://github.com/exasol/project-keeper/
[19]: http://opensource.org/licenses/BSD-3-Clause
[29]: https://maven.apache.org/plugins/maven-compiler-plugin/
[72]: https://oss.oracle.com/licenses/CDDL+GPL-1.1
[80]: https://github.com/Steppschuh/Java-Markdown-Generator
[56]: https://www.eclipse.org/legal/epl-2.0/
[43]: https://maven.apache.org/plugins/maven-deploy-plugin/
[28]: http://www.gnu.org/licenses/lgpl.txt
[55]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[76]: https://www.xmlunit.org/
[101]: https://github.com/mockito/mockito/blob/main/LICENSE
[1]: https://projectlombok.org/LICENSE
[37]: http://zlika.github.io/reproducible-build-maven-plugin
[301]: https://commons.apache.org/proper/commons-io/
[83]: http://www.opensource.org/licenses/mit-license.php
[27]: http://sonarsource.github.io/sonar-scanner-maven/
[71]: https://github.com/eclipse-ee4j/jaxb-api
[14]: https://junit.org/junit5/
[86]: https://bitbucket.org/snakeyaml/snakeyaml
[274]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[33]: https://www.mojohaus.org/flatten-maven-plugin/flatten-maven-plugin
[2]: https://github.com/eclipse-ee4j/jsonp
[47]: https://maven.apache.org/plugins/maven-source-plugin/
[4]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[18]: http://hamcrest.org/JavaHamcrest/
[313]: http://www.slf4j.org
[61]: http://maven.apache.org/plugins/maven-resources-plugin/
[208]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[180]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[140]: https://maven.apache.org/plugins/maven-jar-plugin/
[88]: https://maven.apache.org/ref/3.8.5/maven-model/
[82]: https://github.com/vdurmont/semver4j
[51]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[8]: https://projects.eclipse.org/projects/ee4j.yasson
[134]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[210]: https://maven.apache.org/ref/3.8.5/maven-plugin-api/
[52]: http://www.eclipse.org/legal/epl-v10.html
[214]: https://maven.apache.org/ref/3.8.5/maven-core/
[70]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[264]: https://maven.apache.org/plugins/maven-dependency-plugin/
[63]: http://maven.apache.org/plugins/maven-jar-plugin/
[3]: https://projects.eclipse.org/license/epl-2.0
[10]: http://www.eclipse.org/org/documents/edl-v10.php
[23]: https://www.apache.org/licenses/LICENSE-2.0.txt
[22]: https://www.jqno.nl/equalsverifier
[31]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[53]: https://awhitford.github.com/lombok.maven/lombok-maven-plugin/
[15]: https://www.eclipse.org/legal/epl-v20.html
[65]: http://maven.apache.org/plugins/maven-install-plugin/
[35]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[13]: https://www.eclipse.org/jgit/
[45]: https://maven.apache.org/plugins/maven-gpg-plugin/
[20]: https://github.com/itsallcode/junit5-system-extensions
[102]: https://github.com/exasol/maven-plugin-integration-testing
[67]: http://maven.apache.org/plugins/maven-site-plugin/
[49]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[57]: https://github.com/exasol/error-code-crawler-maven-plugin
[176]: https://maven.apache.org/plugins/maven-assembly-plugin/
