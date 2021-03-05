package com.exasol.projectkeeper.validators.changelog;

import java.io.IOException;
import java.io.Reader;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 * This class reads a maven {@link Model} from pom file.
 * <p>
 * In contrast to the plain {@link MavenXpp3Reader} this class also resolves parses the model and resolves properties.
 * </p>
 */
public class MavenModelReader {

    /**
     * Read a maven model.
     * 
     * @param reader reader for the pom.xml file
     * @return read model
     * @throws IOException            if reading fails
     * @throws XmlPullParserException if pom is invalid
     * @throws ModelBuildingException if pom is invalid
     */
    public Model readModel(final Reader reader) throws IOException, XmlPullParserException, ModelBuildingException {
        final Model rawModel = new MavenXpp3Reader().read(reader);
        final ModelBuildingRequest modelRequest = new DefaultModelBuildingRequest();
        modelRequest.setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL);
        modelRequest.setProcessPlugins(true);
        modelRequest.setRawModel(rawModel);
        modelRequest.setSystemProperties(System.getProperties()); // for Java version property
        final DefaultModelBuilder modelBuilder = new DefaultModelBuilderFactory().newInstance();
        final ModelBuildingResult result = modelBuilder.build(modelRequest);
        return result.getEffectiveModel();
    }
}
