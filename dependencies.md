<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                               | License                                        |
| ---------------------------------------- | ---------------------------------------------- |
| [Maven Plugin Tools Java Annotations][0] | [Apache License, Version 2.0][1]               |
| [Maven Plugin API][2]                    | [Apache License, Version 2.0][1]               |
| [Maven Project Builder][4]               | [The Apache Software License, Version 2.0][5]  |
| [ClassGraph][6]                          | [The MIT License (MIT)][7]                     |
| [jaxb-api][8]                            | [CDDL 1.1][9]; [GPL2 w/ CPE][9]                |
| [JAXB Runtime][11]                       | [Eclipse Distribution License - v 1.0][12]     |
| [org.xmlunit:xmlunit-core][13]           | [The Apache Software License, Version 2.0][5]  |
| [error-reporting-java][15]               | [MIT][16]                                      |
| [JGit - Core][17]                        | Eclipse Distribution License (New BSD License) |
| [Markdown Generator][18]                 | [The Apache Software License, Version 2.0][5]  |
| [Maven Core][20]                         | [Apache License, Version 2.0][1]               |
| [maven-plugin-integration-testing][22]   | [MIT][16]                                      |

## Test Dependencies

| Dependency                         | License                                       |
| ---------------------------------- | --------------------------------------------- |
| [maven-project-version-getter][24] | [MIT][16]                                     |
| [JUnit][26]                        | [Eclipse Public License 1.0][27]              |
| [JUnit Jupiter Engine][28]         | [Eclipse Public License v2.0][29]             |
| [JUnit Jupiter Params][28]         | [Eclipse Public License v2.0][29]             |
| [Hamcrest][32]                     | [BSD License 3][33]                           |
| [org.xmlunit:xmlunit-matchers][13] | [The Apache Software License, Version 2.0][5] |
| [SLF4J JDK14 Binding][36]          | [MIT License][37]                             |
| [mockito-core][38]                 | [The MIT License][39]                         |
| [JaCoCo :: Core][40]               | [Eclipse Public License 2.0][41]              |

## Runtime Dependencies

| Dependency            | License                          |
| --------------------- | -------------------------------- |
| [JaCoCo :: Agent][40] | [Eclipse Public License 2.0][41] |

## Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [Apache Maven Compiler Plugin][44]                      | [Apache License, Version 2.0][1]              |
| [Maven Plugin Plugin][46]                               | [Apache License, Version 2.0][1]              |
| [Maven Failsafe Plugin][48]                             | [Apache License, Version 2.0][1]              |
| [Maven Surefire Plugin][50]                             | [Apache License, Version 2.0][1]              |
| [Versions Maven Plugin][52]                             | [Apache License, Version 2.0][1]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][54] | [ASL2][5]                                     |
| [Apache Maven Source Plugin][56]                        | [Apache License, Version 2.0][1]              |
| [Apache Maven Javadoc Plugin][58]                       | [Apache License, Version 2.0][1]              |
| [Apache Maven GPG Plugin][60]                           | [Apache License, Version 2.0][5]              |
| [JaCoCo :: Maven Plugin][40]                            | [Eclipse Public License 2.0][41]              |
| [Apache Maven Enforcer Plugin][64]                      | [Apache License, Version 2.0][1]              |
| [Project keeper maven plugin][66]                       | [MIT][16]                                     |
| [Maven Deploy Plugin][68]                               | [The Apache Software License, Version 2.0][5] |
| [Nexus Staging Maven Plugin][70]                        | [Eclipse Public License][27]                  |
| [error-code-crawler-maven-plugin][72]                   | [MIT][16]                                     |
| [Reproducible Build Maven Plugin][74]                   | [Apache 2.0][5]                               |
| [Maven Dependency Plugin][76]                           | [The Apache Software License, Version 2.0][5] |
| [Maven Clean Plugin][78]                                | [The Apache Software License, Version 2.0][5] |
| [Maven Resources Plugin][80]                            | [The Apache Software License, Version 2.0][5] |
| [Maven JAR Plugin][82]                                  | [The Apache Software License, Version 2.0][5] |
| [Maven Install Plugin][84]                              | [The Apache Software License, Version 2.0][5] |
| [Maven Site Plugin 3][86]                               | [The Apache Software License, Version 2.0][5] |

[40]: https://www.eclemma.org/jacoco/index.html
[66]: https://github.com/exasol/project-keeper-maven-plugin
[15]: https://github.com/exasol/error-reporting-java
[5]: http://www.apache.org/licenses/LICENSE-2.0.txt
[11]: https://eclipse-ee4j.github.io/jaxb-ri/
[50]: https://maven.apache.org/surefire/maven-surefire-plugin/
[78]: http://maven.apache.org/plugins/maven-clean-plugin/
[16]: https://opensource.org/licenses/MIT
[38]: https://github.com/mockito/mockito
[4]: http://maven.apache.org/
[24]: https://github.com/exasol/maven-project-version-getter
[52]: http://www.mojohaus.org/versions-maven-plugin/
[33]: http://opensource.org/licenses/BSD-3-Clause
[44]: https://maven.apache.org/plugins/maven-compiler-plugin/
[9]: https://oss.oracle.com/licenses/CDDL+GPL-1.1
[60]: http://maven.apache.org/plugins/maven-gpg-plugin/
[18]: https://github.com/Steppschuh/Java-Markdown-Generator
[26]: http://junit.org
[41]: https://www.eclipse.org/legal/epl-2.0/
[13]: https://www.xmlunit.org/
[20]: https://maven.apache.org/ref/3.6.3/maven-core/
[74]: http://zlika.github.io/reproducible-build-maven-plugin
[37]: http://www.opensource.org/licenses/mit-license.php
[6]: https://github.com/classgraph/classgraph
[39]: https://github.com/mockito/mockito/blob/release/3.x/LICENSE
[8]: https://github.com/eclipse-ee4j/jaxb-api
[28]: https://junit.org/junit5/
[46]: https://maven.apache.org/plugin-tools/maven-plugin-plugin
[56]: https://maven.apache.org/plugins/maven-source-plugin/
[32]: http://hamcrest.org/JavaHamcrest/
[36]: http://www.slf4j.org
[80]: http://maven.apache.org/plugins/maven-resources-plugin/
[0]: https://maven.apache.org/plugin-tools/maven-plugin-annotations
[70]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[48]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[76]: http://maven.apache.org/plugins/maven-dependency-plugin/
[7]: http://opensource.org/licenses/MIT
[27]: http://www.eclipse.org/legal/epl-v10.html
[2]: https://maven.apache.org/ref/3.6.3/maven-plugin-api/
[82]: http://maven.apache.org/plugins/maven-jar-plugin/
[12]: http://www.eclipse.org/org/documents/edl-v10.php
[1]: https://www.apache.org/licenses/LICENSE-2.0.txt
[64]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[29]: https://www.eclipse.org/legal/epl-v20.html
[84]: http://maven.apache.org/plugins/maven-install-plugin/
[54]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[17]: https://www.eclipse.org/jgit/
[22]: https://github.com/exasol/maven-plugin-integration-testing
[68]: http://maven.apache.org/plugins/maven-deploy-plugin/
[86]: http://maven.apache.org/plugins/maven-site-plugin/
[58]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[72]: https://github.com/exasol/error-code-crawler-maven-plugin
