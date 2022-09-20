package com.exasol.projectkeeper.sources.analyze.golang;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectNameReader {

    static final String GIT_CONFIG = ".git/config";
    private static final String BLANK = "\\p{Blank}*";
    private static final Pattern GIT_URL = Pattern.compile(BLANK + "url" + BLANK + "=" + BLANK + ".*/([^/]*)\\.git$");

    /**
     * @param projectDir project's root folder
     * @return name of project either from root folder or from file
     */
    public static String getProjectName(final Path projectDir) {
        final String defaultResult = projectDir.getFileName().toString();
        try (BufferedReader reader = Files.newBufferedReader(projectDir.resolve(GIT_CONFIG))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                final Matcher matcher = GIT_URL.matcher(line);
                if (matcher.matches()) {
                    return matcher.group(1);
                }
            }
        } catch (final IOException exception) {
            return defaultResult;
        }
        return defaultResult;
    }
}
