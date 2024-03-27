package com.exasol.projectkeeper.validators.files;

import org.snakeyaml.engine.v2.api.*;
import org.snakeyaml.engine.v2.common.*;

class YamlIO {

    private final Load loader;
    private final Dump dumper;

    private YamlIO(final Load loader, final Dump dumper) {
        this.loader = loader;
        this.dumper = dumper;
    }

    static YamlIO create() {
        final LoadSettings loadSettings = LoadSettings.builder().setParseComments(true).build();
        final DumpSettings dumpSettings = DumpSettings.builder().setBestLineBreak("\n").setCanonical(false)
                .setDefaultFlowStyle(FlowStyle.AUTO).setDefaultScalarStyle(ScalarStyle.PLAIN).setDumpComments(true)
                .setExplicitEnd(false).setExplicitStart(false) //
                .setIndent(2).setIndentWithIndicator(true).setIndicatorIndent(2) //
                .setWidth(200) //
                .setMultiLineFlow(true).setNonPrintableStyle(NonPrintableStyle.ESCAPE).setUseUnicodeEncoding(true)
                .setSplitLines(false).build();
        return new YamlIO(new Load(loadSettings), new Dump(dumpSettings));
    }

    GitHubWorkflow loadGitHubWorkflow(final String content) {
        return GitHubWorkflow.create(load(content));
    }

    Object load(final String content) {
        return this.loader.loadFromString(content);
    }

    String dumpWorkflow(final GitHubWorkflow workflow) {
        return dump(workflow.getRawObject());
    }

    String dump(final Object object) {
        return this.dumper.dumpToString(object);
    }
}
