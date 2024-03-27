package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class YamlIOTest {
    @ParameterizedTest(name = "{0}")
    @MethodSource("testCases")
    void dumpLoad(final String testDescription, final String inputYaml, final String expectedFormattedYaml) {
        final YamlIO yamlIO = YamlIO.create();
        final String dumped = yamlIO.dump(yamlIO.load(inputYaml));
        assertThat(testDescription, dumped, equalTo(expectedFormattedYaml));
    }

    static Stream<Arguments> testCases() {
        return Stream.of( //
                testCase("key-value", "key: value", "{\n  key: value\n}\n"), //
                testCase("comments removed", "key: value # comment", "{\n  key: value\n}\n"), //
                testCase("'on' key not converted to true", "on: value", "{\n  on: value\n}\n"), //
                testCase("'on' value not converted to true", "key: on", "{\n  key: on\n}\n"), //
                testCase("multi-line list", """
                        list:
                          - itemA
                          - itemB
                        """, "list: [\n  itemA,\n  itemB\n]\n"),
                testCase("single-line list", "list: [itemA, itemB]", "list: [\n  itemA,\n  itemB\n]\n"), //
                testCase("single-line list with quotes", "list: ['itemA', \"itemB\"]",
                        "list: [\n  itemA,\n  itemB\n]\n"),
                testCaseUnchanged("ci-build step setup JDK", """
                        jobs:
                          build:
                            steps:
                              - name: Set up JDKs
                                uses: actions/setup-java@v4
                                with:
                                  distribution: temurin
                                  java-version: |
                                    11
                                    17
                                  cache: maven
                        """),
                testCase("ci-build step maven verify",
                        """
                                jobs:
                                  build:
                                    steps:
                                      - name: Run tests and build with Maven
                                        run: |
                                          mvn -T 1C --batch-mode clean verify install \
                                              -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn \
                                              -DtrimStackTrace=false
                                        env:
                                          GITHUB_TOKEN: ${{ github.token }} # Required for integration tests of GitHub access
                                """,
                        """
                                jobs:
                                  build:
                                    steps:
                                      - name: Run tests and build with Maven
                                        run: |
                                          mvn -T 1C --batch-mode clean verify install \
                                              -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn \
                                              -DtrimStackTrace=false
                                        env: {
                                          GITHUB_TOKEN: '${{ github.token }}'
                                        }
                                """)

        );
    }

    private static Arguments testCaseUnchanged(final String description, final String yaml) {
        return testCase(description, yaml, yaml);
    }

    private static Arguments testCase(final String description, final String inputYaml, final String expectedYaml) {
        return Arguments.of(description, inputYaml, expectedYaml);
    }
}
