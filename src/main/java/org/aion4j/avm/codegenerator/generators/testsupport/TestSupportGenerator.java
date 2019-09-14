package org.aion4j.avm.codegenerator.generators.testsupport;

import org.aion4j.avm.codegenerator.api.exception.CodeGenerationException;
import org.aion4j.avm.codegenerator.api.impl.VelocityTemplateGenerator;
import org.aion4j.avm.codegenerator.api.generator.BaseGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

public class TestSupportGenerator extends BaseGenerator {
    private final Logger logger = LoggerFactory.getLogger(TestSupportGenerator.class);

    private final String TEST_IMPL_TEMPLATE = "templates/test/TestImpl.java.vm";
    private final String REQUEST_CONTEXT_TEMPLATE = "templates/test/RequestContext.java.vm";
    private final String RESPONSE_CONTEXT_TEMPLATE = "templates/test/ResponseContext.java.vm";

    public TestSupportGenerator(boolean verbose) {
        this.templateGenerator = new VelocityTemplateGenerator();
        this.verbose = verbose;
    }

    @Override
    protected void doGenerate(String baseDir, String packageDir, HashMap<String, Object> data) {
        try {
            //Generate main testImpl class
            FileWriter contractTestImplWriter = new FileWriter(new File(packageDir, data.get("className") + "TestImpl.java"));
            generateFromTemplate(TEST_IMPL_TEMPLATE, data, contractTestImplWriter);

            //Generate RequestContext
            FileWriter requestContextWriter = new FileWriter(new File(packageDir, "RequestContext.java"));
            generateFromTemplate(REQUEST_CONTEXT_TEMPLATE, data, requestContextWriter);

            //Generate ResponseContext
            FileWriter responseContextWriter = new FileWriter(new File(packageDir, "ResponseContext.java"));
            generateFromTemplate(RESPONSE_CONTEXT_TEMPLATE, data, responseContextWriter);
        } catch (Exception e) {
            throw new CodeGenerationException("Error in generting test support code", e);
        }
    }
}
