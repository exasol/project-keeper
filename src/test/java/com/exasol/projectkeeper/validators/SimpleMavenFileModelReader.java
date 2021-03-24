package com.exasol.projectkeeper.validators;

import java.io.*;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.pom.MavenFileModelReader;

/**
 * Simplified {@link MavenFileModelReader} that works without injected dependencies.
 */
public class SimpleMavenFileModelReader implements MavenFileModelReader {
    @Override
    public Model readModel(final File pomFile) throws ReadFailedException {
        try (final FileReader reader = new FileReader(pomFile)) {
            final Model rawModel = new MavenXpp3Reader().read(reader);
            final ModelBuildingRequest modelRequest = new DefaultModelBuildingRequest();
            modelRequest.setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL);
            modelRequest.setProcessPlugins(true);
            modelRequest.setRawModel(rawModel);
            modelRequest.setSystemProperties(System.getProperties()); // for Java version property
            final DefaultModelBuilder modelBuilder = new DefaultModelBuilderFactory().newInstance();
            final ModelBuildingResult result = modelBuilder.build(modelRequest);
            return result.getEffectiveModel();
        } catch (final IOException | ModelBuildingException | XmlPullParserException exception) {
            throw new ReadFailedException(
                    ExaError.messageBuilder("E-PK-47").message("Failed to build maven model.").toString(), exception);
        }
    }
}
