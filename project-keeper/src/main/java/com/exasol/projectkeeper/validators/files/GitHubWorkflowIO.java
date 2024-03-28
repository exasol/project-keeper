package com.exasol.projectkeeper.validators.files;

import org.snakeyaml.engine.v2.api.*;
import org.snakeyaml.engine.v2.common.*;

/**
 * This class provides options for serializing/deserializing GitHub workflows to/from YAML strings.
 */
public class GitHubWorkflowIO {

    private final Load loader;
    private final Dump dumper;

    private GitHubWorkflowIO(final Load loader, final Dump dumper) {
        this.loader = loader;
        this.dumper = dumper;
    }

    /**
     * Create a new instance of {@link GitHubWorkflowIO}.
     * 
     * @return new instance
     */
    public static GitHubWorkflowIO create() {
        final LoadSettings loadSettings = LoadSettings.builder().setParseComments(true).build();
        final DumpSettings dumpSettings = DumpSettings.builder().setBestLineBreak("\n").setCanonical(false)
                .setDefaultFlowStyle(FlowStyle.AUTO).setDefaultScalarStyle(ScalarStyle.PLAIN).setDumpComments(true)
                .setExplicitEnd(false).setExplicitStart(false) //
                .setIndent(2).setIndentWithIndicator(true).setIndicatorIndent(2) //
                .setWidth(200) //
                .setMultiLineFlow(true).setNonPrintableStyle(NonPrintableStyle.ESCAPE).setUseUnicodeEncoding(true)
                .setSplitLines(false).build();
        return new GitHubWorkflowIO(new Load(loadSettings), new Dump(dumpSettings));
    }

    /**
     * Load a GitHub workflow from a YAML string.
     * 
     * @param content YAML string
     * @return GitHub workflow
     */
    public GitHubWorkflow loadWorkflow(final String content) {
        return GitHubWorkflow.create(load(content));
    }

    Object load(final String content) {
        return this.loader.loadFromString(content);
    }

    /**
     * Dump a GitHub workflow to a YAML string.
     * 
     * @param workflow GitHub workflow
     * @return YAML string
     */
    public String dumpWorkflow(final GitHubWorkflow workflow) {
        return dump(workflow.getRawObject());
    }

    String dump(final Object object) {
        return this.dumper.dumpToString(object);
    }
}
