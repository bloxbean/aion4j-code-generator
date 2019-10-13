package org.aion4j.avm.codegenerator.generators.clientjs;

import org.aion4j.avm.codegenerator.api.abi.ABI;
import org.aion4j.avm.codegenerator.api.abi.ABIMethod;
import org.aion4j.avm.codegenerator.api.exception.CodeGenerationException;
import org.aion4j.avm.codegenerator.api.generator.BaseGenerator;
import org.aion4j.avm.codegenerator.api.impl.VelocityTemplateGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VueJsClientGenerator extends BaseGenerator {
    private final Logger logger = LoggerFactory.getLogger(VueJsClientGenerator.class);

    private final String CONTRACT_CALL_WITH_ABI_JS_TEMPLATE = "templates/client/js/contract-abi.js.vm";
    private final String CONTRACT_CALL_JS_TEMPLATE = "templates/client/js/contract.js.vm";
    private final String PACKAGE_JSON_TEMPLATE = "templates/client/js/vue/package.json.vm";
    private final String BABEL_CONFIG_JS_VM = "templates/client/js/vue/babel.config.js.vm";
    private final String GITIGNORE_VM = "templates/client/js/vue/.gitignore.vm";
    private final String MAIN_JS_VM = "templates/client/js/vue/src/main.js.vm";
    private final String APP_VUE_VM = "templates/client/js/vue/src/App.vue.vm";
    private final String COMPONENT_VM = "templates/client/js/vue/src/components/component.vue.vm";
    private final String INDEX_HTML_VM = "templates/client/js/vue/public/index.html.vm";

    public VueJsClientGenerator(boolean verbose) {
        this.templateGenerator = new VelocityTemplateGenerator();
        this.verbose = verbose;
    }

    @Override
    protected void doGenerate(String baseDir, String packageDir, HashMap<String, Object> data) {
        try {
            data.put("mode", "vue");

            JsHelper jsHelper = new JsHelper();
            ABI abi = (ABI)data.get("abi");
            List<ABIMethod> methods = abi.getMethods();

            data.put("_jshelper", jsHelper);

            /** create folders **/
            createFolders(baseDir);

            File baseDirFile = new File(baseDir);
            File srcDir = new File(baseDir, "src");
            File componentsDir = new File(srcDir, "components");
            File publicDir = new File(baseDir, "public");

            /** package.json **/
            createFile(PACKAGE_JSON_TEMPLATE, data, baseDirFile, "package.json");

            /** babel.config.js **/
            createFile(BABEL_CONFIG_JS_VM, data, baseDirFile, "babel.config.js");

            //src folder files
            createFile(MAIN_JS_VM, data, srcDir, "main.js");

            /*** app.vue ***/
            //Create a temp data map for app.vue
            HashMap<String, Object> appVueData = new HashMap<>();
            appVueData.putAll(data);
            List<String> components = new ArrayList<>();
            for(ABIMethod abiMethod: abi.getMethods()) {
                String componentJsName = jsHelper.methodNameToComponentName(abiMethod.getMethodName());
                components.add(componentJsName);
            }
            appVueData.put("components", components);
            createFile(APP_VUE_VM, appVueData, srcDir, "App.vue");

            /** contract-abi.js **/
            createFile(CONTRACT_CALL_WITH_ABI_JS_TEMPLATE, data, srcDir, "contract-abi.js");

            /** contract.js **/
            //createFile(CONTRACT_CALL_JS_TEMPLATE, data, srcDir, "contract.js");

            /** component js **/
            for(ABIMethod method: methods) {
                String methodName = method.getMethodName();
                String componentName = jsHelper.methodNameToComponentName(methodName);

                //Create a copy of data map
                HashMap<String, Object> tempData = new HashMap<>();
                tempData.putAll(data);
                tempData.put("method", method);
                tempData.put("component", componentName);

                createFile(COMPONENT_VM, tempData, componentsDir, componentName + ".vue");
            }

            createFile(INDEX_HTML_VM, data, publicDir, "index.html");


        } catch (Exception e) {
            throw new CodeGenerationException("Error in generting vuejs client code", e);
        }
    }

    private void createFolders(String baseDir) {
        File src = new File(baseDir, "src");
        src.mkdirs();

        File components = new File(src, "components");
        components.mkdirs();

        File publicFolder = new File(baseDir, "public");
        publicFolder.mkdirs();
    }

    private void createFile(String templateName, HashMap<String, Object> data, File destFolder, String destFile) throws Exception{
        FileWriter contractCallAbiWriter = new FileWriter(new File(destFolder, destFile));
        generateFromTemplate(templateName, data, contractCallAbiWriter);
    }

    @Override
    protected boolean isPackageFolderCreationRequired() {
        return false;
    }
}
