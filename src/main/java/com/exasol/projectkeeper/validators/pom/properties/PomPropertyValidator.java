package com.exasol.projectkeeper.validators.pom.properties;

import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.validators.pom.AbstractPomValidator;
import com.exasol.projectkeeper.validators.pom.PomValidator;
import com.exasol.projectkeeper.xpath.XPathSplitter;

/**
 * This class validates that the pom.xml file contains a certain property with a given value.
 */
public class PomPropertyValidator extends AbstractPomValidator implements PomValidator {
    private final String propertyXPath;
    private final String expectedValue;
    private final ProjectKeeperModule module;

    /**
     * Create a new instance of {@link PomPropertyValidator}.
     * 
     * @param propertyXPath path of the property
     * @param expectedValue expected value
     */
    public PomPropertyValidator(final String propertyXPath, final String expectedValue,
            final ProjectKeeperModule module) {
        this.propertyXPath = propertyXPath;
        this.expectedValue = expectedValue;
        this.module = module;
    }

    @Override
    public void validate(final Document pom, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
        final Node property = runXPath(pom, this.propertyXPath);
        if (property != null) {
            verifyPropertyValue(findingConsumer, property);
        } else {
            findingConsumer.accept(ValidationFinding.withMessage(ExaError.messageBuilder("E-PK-72")
                    .message("Missing required property {{property}} in pom.xml.")
                    .mitigation("Set the required property {{property}} to {{value}}.")
                    .parameter("property", this.propertyXPath).parameter("value", this.expectedValue).toString())
                    .andFix(log -> createMissingProperty(pom)).build());
        }
    }

    private void verifyPropertyValue(final Consumer<ValidationFinding> findingConsumer, final Node property) {
        final String actualValue = property.getTextContent();
        if (!this.expectedValue.equals(actualValue)) {
            findingConsumer.accept(ValidationFinding.withMessage(ExaError.messageBuilder("E-PK-73")
                    .message("The required property {{property}} pom.xml has an illegal value.")
                    .mitigation("Set the required property {{property}} to {{value}}.")
                    .parameter("property", this.propertyXPath).parameter("value", this.expectedValue).toString())
                    .andFix(log -> property.setNodeValue(this.expectedValue)).build());
        }
    }

    private void createMissingProperty(final Document pom) {
        createObjectPathIfNotExists(pom, XPathSplitter.split(this.propertyXPath));
        runXPath(pom, this.propertyXPath).setTextContent(this.expectedValue);
    }

    @Override
    public ProjectKeeperModule getModule() {
        return this.module;
    }

    @Override
    public boolean isExcluded(final Collection<String> excludedPlugins) {
        return false;
    }
}
