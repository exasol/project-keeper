package com.exasol.projectkeeper;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class matches files to exclude from validation.
 */
public class ExcludedFilesMatcher {
    private static final FileSystem FILE_SYSTEM = FileSystems.getDefault();
    private final List<PathMatcher> matchers;

    /**
     * Create a new instance of {@link ExcludedFilesMatcher}.
     * 
     * @param excludedFilePatterns list of glob patterns to exclude
     */
    public ExcludedFilesMatcher(final Collection<String> excludedFilePatterns) {
        this.matchers = excludedFilePatterns.stream().map(ExcludedFilesMatcher::removeTrailingSlash)
                .map(fileName -> FILE_SYSTEM.getPathMatcher("glob:" + fileName)).collect(Collectors.toList());
    }

    private static String removeTrailingSlash(final String fileName) {
        if (fileName.startsWith("/")) {
            return fileName.substring(1);
        } else {
            return fileName;
        }
    }

    /**
     * Get if a give file was excluded.
     * 
     * @param file Path of the file to check relative to the project directory
     * @return {@code} true if the file was excluded
     */
    public boolean isFileExcluded(final Path file) {
        return this.matchers.stream().anyMatch(matcher -> matcher.matches(file));
    }
}
