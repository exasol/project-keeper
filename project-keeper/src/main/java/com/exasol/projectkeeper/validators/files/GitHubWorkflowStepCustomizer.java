package com.exasol.projectkeeper.validators.files;

import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.DumperOptions.LineBreak;
import org.yaml.snakeyaml.DumperOptions.NonPrintableStyle;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import com.exasol.projectkeeper.shared.config.BuildOptions;

class GitHubWorkflowStepCustomizer implements ContentCustomizingTemplate.ContentCustomizer {

    private final BuildOptions buildOptions;
    private final Yaml yaml;

    GitHubWorkflowStepCustomizer(final BuildOptions buildOptions) {
        this(configureYaml(), buildOptions);
    }

    GitHubWorkflowStepCustomizer(final Yaml yaml, final BuildOptions buildOptions) {
        this.yaml = yaml;
        this.buildOptions = buildOptions;
    }

    @Override
    public String customizeContent(final String content) {
        final Object object = yaml.load(content);
        return yaml.dump(customizeWorkflow(object));
    }

    private Object customizeWorkflow(final Object object) {
        return object;
    }

    private static Yaml configureYaml() {
        final DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setIndent(2);
        dumperOptions.setCanonical(false);
        dumperOptions.setExplicitEnd(false);
        dumperOptions.setExplicitStart(false);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.AUTO); // Remove quotes
        dumperOptions.setPrettyFlow(true); // Remove curly brackets
        dumperOptions.setLineBreak(LineBreak.UNIX);
        dumperOptions.setSplitLines(false); // Remove line breaks
        dumperOptions.setWidth(400);
        dumperOptions.setProcessComments(true);
        dumperOptions.setAllowUnicode(true);
        dumperOptions.setIndentWithIndicator(false);
        dumperOptions.setIndicatorIndent(2);
        dumperOptions.setMaxSimpleKeyLength(128);
        dumperOptions.setNonPrintableStyle(NonPrintableStyle.ESCAPE);

        final LoaderOptions loaderConfig = new LoaderOptions();
        loaderConfig.setProcessComments(true);
        loaderConfig.setAllowDuplicateKeys(false);
        loaderConfig.setAllowRecursiveKeys(false);
        loaderConfig.setWrappedToRootException(true);
        return new Yaml(new Constructor(loaderConfig), new Representer(dumperOptions));
    }
}
