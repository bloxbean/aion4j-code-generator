package org.aion4j.avm.codegenerator.api.generator;

import org.aion4j.avm.codegenerator.api.TemplateGenerator;
import org.aion4j.avm.codegenerator.api.abi.ABI;
import org.aion4j.avm.codegenerator.api.abi.ABIParserHelper;
import org.aion4j.avm.codegenerator.api.exception.CodeGenerationException;
import org.aion4j.avm.codegenerator.api.impl.VelocityTemplateGenerator;
import org.aion4j.avm.codegenerator.util.StringUtils;
import org.aion4j.avm.codegenerator.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseGenerator implements Generator {
    private final Logger logger = LoggerFactory.getLogger(BaseGenerator.class);
    protected TemplateGenerator templateGenerator;
    protected boolean verbose;
    protected boolean ignoreFormattingError = true;

    public void generate(String abiString, String folder) throws IOException {
        generate(abiString, folder, Collections.emptyMap());
    }

    public void generate(String abiString, String folder, Map<String, Object> templateData) throws IOException {
        ABI abi = parse(abiString);

        if(abi == null)
            throw new CodeGenerationException("Invalid ABI : " + abiString);

        Tuple<String, String> classInfo = getContractPackageName(abi.getContractClass());

        HashMap<String, Object> data = new HashMap<>();
        data.put("package", classInfo._1());
        data.put("className", classInfo._2());
        data.put("methods", abi.getMethods());
        data.put("abi", abi);
        data.put("abi_str", abiString.trim());

        populateData(data);
        data.putAll(templateData);

        File baseFolder = new File(folder);
        if(!baseFolder.exists()) {
            throw new CodeGenerationException("Base folder for code generation doesn't exist : " + folder);
        }

        String packagePath = classInfo._1().replace('.', File.separatorChar);
        File packageDir = new File(baseFolder, packagePath);
        if(packageDir.mkdirs() || packageDir.exists()) {

        } else {
            throw new CodeGenerationException("Package folder could not be created. May be permission issue: " + packageDir.getAbsolutePath());
        }

        doGenerate(folder, packageDir.getAbsolutePath(), data);
    }

    public void setVerbose(boolean flag) {
        verbose = flag;
    }

    public void setIgnoreFormattingError(boolean flag) {
        this.ignoreFormattingError = flag;
    }

    public void generateFromTemplate(String  template, HashMap<String, Object> data, Writer writer) throws IOException {

        if(templateGenerator == null)
            templateGenerator = new VelocityTemplateGenerator();

        StringWriter mywriter = new StringWriter();
        templateGenerator.generate(template, data, mywriter);

        if(verbose || logger.isDebugEnabled())
            logger.info(mywriter.toString());

        String formattedSource = formatSource(mywriter.toString());

        writer.append(formattedSource);
        writer.flush();
        writer.close();
    }

    protected String formatSource(String source) {
        return source;
    }

    protected void populateData(Map<String, Object> data) {

    }

    private ABI parse(String abi) {
        return ABIParserHelper.parse(abi);
    }

    private Tuple<String, String> getContractPackageName(String fqName) {
        String packageName = null;
        String className = null;
        if(StringUtils.isEmpty(fqName)) {
            throw new CodeGenerationException("Contract classname cannot be empty or null");
        } if(!fqName.contains(".")) {
            packageName = "";
            className = fqName;
        } else {

            int index = fqName.lastIndexOf(".");
            packageName = fqName.substring(0, index);
            className = fqName.substring(index + 1);
        }

        return new Tuple<>(packageName, className);
    }

    protected abstract void doGenerate(String baseDir, String packageDir, HashMap<String, Object> data );
}
