package com.exasol.projectkeeper.sources.analyze.golang;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class GoSourceAnalyzerTest {
    private static final Logger LOGGER = Logger.getLogger(GoSourceAnalyzerTest.class.getName());

    @Test
    void test() {
        final String name = getProjectNameFromGit(Paths.get("c:/Huge/Workspaces/Git/overview/"));
        System.out.println(name);
    }

    private static final Pattern PATTERN = Pattern.compile(".*/([^/]*)\\.git$");

    private String getProjectNameFromGit(final Path projectDir) {
        final SimpleProcess process = SimpleProcess.start(projectDir,
                Arrays.asList("git", "config", "--get", "remote.origin.url"));
        final String url = process.getOutputStreamContent().trim();
        final Matcher matcher = PATTERN.matcher(url);
        if (!matcher.matches()) {
            return url;
        }
        return matcher.group(1);
    }

}
