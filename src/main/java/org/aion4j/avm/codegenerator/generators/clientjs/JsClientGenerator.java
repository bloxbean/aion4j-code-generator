package org.aion4j.avm.codegenerator.generators.clientjs;

import org.aion4j.avm.codegenerator.api.exception.CodeGenerationException;
import org.aion4j.avm.codegenerator.api.generator.BaseGenerator;
import org.aion4j.avm.codegenerator.api.impl.VelocityTemplateGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

public class JsClientGenerator extends BaseGenerator {
    private final Logger logger = LoggerFactory.getLogger(JsClientGenerator.class);

    private final String CONTRACT_DEPLOY_JS_TEMPLATE = "templates/client/js/contract-deploy.js.vm";
    private final String CONTRACT_DEPLOY_WITH_ABI_JS_TEMPLATE = "templates/client/js/contract-deploy-abi.js.vm";
    private final String CONTRACT_CALL_JS_TEMPLATE = "templates/client/js/contract.js.vm";
    private final String CONTRACT_CALL_WITH_ABI_JS_TEMPLATE = "templates/client/js/contract-abi.js.vm";
    private final String PACKAGE_JSON_TEMPLATE = "templates/client/js/package.json.vm";

    public JsClientGenerator(boolean verbose) {
        this.templateGenerator = new VelocityTemplateGenerator();
        this.verbose = verbose;
    }

    @Override
    protected void doGenerate(String baseDir, String packageDir, HashMap<String, Object> data) {
        try {
            data.put("mode", "node");

            //Generate deploy js
            FileWriter contractDeployWriter = new FileWriter(new File(baseDir, "contract-deploy.js"));
            generateFromTemplate(CONTRACT_DEPLOY_JS_TEMPLATE, data, contractDeployWriter);
            //Generate deploy abi js
            FileWriter contractDeployAbiWriter = new FileWriter(new File(baseDir, "contract-deploy-abi.js"));
            generateFromTemplate(CONTRACT_DEPLOY_WITH_ABI_JS_TEMPLATE, data, contractDeployAbiWriter);

            //Generate call js
            FileWriter contractCallWriter = new FileWriter(new File(baseDir, "contract.js"));
            generateFromTemplate(CONTRACT_CALL_JS_TEMPLATE, data, contractCallWriter);
            //Generate call abi js
            FileWriter contractCallAbiWriter = new FileWriter(new File(baseDir, "contract-abi.js"));
            generateFromTemplate(CONTRACT_CALL_WITH_ABI_JS_TEMPLATE, data, contractCallAbiWriter);

            //Generate package json
            File packageJson = new File(baseDir, "package.json");
            if(!packageJson.exists()) {
                generateFromTemplate(PACKAGE_JSON_TEMPLATE, data, new FileWriter(packageJson));
            }
        } catch (Exception e) {
            throw new CodeGenerationException("Error in generting js client code", e);
        }
    }

    @Override
    protected boolean isPackageFolderCreationRequired() {
        return false;
    }
}
