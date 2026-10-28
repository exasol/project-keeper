package com.exasol.projectkeeper.validators.files;

import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.snakeyaml.engine.v2.api.lowlevel.Compose;
import org.snakeyaml.engine.v2.api.lowlevel.Present;
import org.snakeyaml.engine.v2.api.lowlevel.Serialize;
import org.snakeyaml.engine.v2.common.*;
import org.snakeyaml.engine.v2.nodes.Node;

/**
 * This class provides options for serializing/deserializing GitHub workflows to/from YAML strings.
 */
// [impl->dsn~customize-build-process.preserve-comments~0]
public class GitHubWorkflowIO {

    private final Compose composer;
    private final Serialize serializer;
    private final Present presenter;

    private GitHubWorkflowIO(final Compose composer, final Serialize serializer, final Present presenter) {
        this.composer = composer;
        this.serializer = serializer;
        this.presenter = presenter;
    }

    /**
     * Create a new instance of {@link GitHubWorkflowIO}.
     * 
     * @return new instance
     */
    public static GitHubWorkflowIO create() {
        final LoadSettings loadSettings = LoadSettings.builder().setParseComments(true).build();
        final DumpSettings dumpSettings = createDumpSettings();
        return new GitHubWorkflowIO(new Compose(loadSettings), new Serialize(dumpSettings), new Present(dumpSettings));
    }

    static DumpSettings createDumpSettings() {
        return DumpSettings.builder().setBestLineBreak("\n").setCanonical(false)
                .setDefaultFlowStyle(FlowStyle.AUTO).setDefaultScalarStyle(ScalarStyle.PLAIN).setDumpComments(true)
                .setExplicitEnd(false).setExplicitStart(false) //
                .setIndent(2).setIndentWithIndicator(true).setIndicatorIndent(2) //
                .setWidth(200) //
                .setMultiLineFlow(true).setNonPrintableStyle(NonPrintableStyle.ESCAPE).setUseUnicodeEncoding(true)
                .setSplitLines(false).build();
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

    Node load(final String content) {
        return this.composer.composeString(content)
                .orElseThrow(() -> new IllegalArgumentException("GitHub workflow must not be empty"));
    }

    /**
     * Dump a GitHub workflow to a YAML string.
     * 
     * @param workflow GitHub workflow
     * @return YAML string
     */
    public String dumpWorkflow(final GitHubWorkflow workflow) {
        return dump(workflow.getRawNode());
    }

    String dump(final Node node) {
        return this.presenter.emitToString(this.serializer.serializeOne(node).iterator());
    }
}
