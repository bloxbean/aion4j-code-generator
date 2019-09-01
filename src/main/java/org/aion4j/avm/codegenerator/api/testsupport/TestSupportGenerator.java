package org.aion4j.avm.codegenerator.api.testsupport;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import org.aion4j.avm.codegenerator.api.TemplateGenerator;
import org.aion4j.avm.codegenerator.api.abi.ABI;
import org.aion4j.avm.codegenerator.api.abi.ABIParserHelper;
import org.aion4j.avm.codegenerator.api.exception.CodeGenerationException;
import org.aion4j.avm.codegenerator.api.impl.VelocityTemplateGenerator;
import org.aion4j.avm.codegenerator.util.StringUtils;
import org.aion4j.avm.codegenerator.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;

public class TestSupportGenerator {
    private final Logger logger = LoggerFactory.getLogger(TestSupportGenerator.class);

    private final String TEST_IMPL_TEMPLATE = "templates/test/TestImpl.java.vm";
    private final String REQUEST_CONTEXT_TEMPLATE = "templates/test/RequestContext.java.vm";
    private final String RESPONSE_CONTEXT_TEMPLATE = "templates/test/ResponseContext.java.vm";

    private TemplateGenerator templateGenerator;
    private boolean verbose;
    private boolean ignoreFormattingError = true;

    public TestSupportGenerator(boolean verbose) {
        this.templateGenerator = new VelocityTemplateGenerator();
        this.verbose = verbose;
    }

    public void generate(String abiString, String folder) throws IOException {
        ABI abi = parse(abiString);

        if(abi == null)
            throw new CodeGenerationException("Invalid ABI : " + abiString);

        Tuple<String, String> classInfo = getContractPackageName(abi.getContractClass());

        HashMap<String, Object> data = new HashMap<>();
        data.put("package", classInfo._1());
        data.put("className", classInfo._2());
        data.put("methods", abi.getMethods());
        data.put("abi", abi);

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

        //Generate main testImpl class
        FileWriter contractTestImplWriter = new FileWriter(new File(packageDir, classInfo._2() + "TestImpl.java"));
        generateFromTemplate(TEST_IMPL_TEMPLATE, data, contractTestImplWriter);

        //Generate RequestContext
        FileWriter requestContextWriter = new FileWriter(new File(packageDir, "RequestContext.java"));
        generateFromTemplate(REQUEST_CONTEXT_TEMPLATE, data, requestContextWriter);

        //Generate ResponseContext
        FileWriter responseContextWriter = new FileWriter(new File(packageDir, "ResponseContext.java"));
        generateFromTemplate(RESPONSE_CONTEXT_TEMPLATE, data, responseContextWriter);
    }

    public void generateFromTemplate(String  template, HashMap<String, Object> data, Writer writer) throws IOException {
        StringWriter mywriter = new StringWriter();
        templateGenerator.generate(template, data, mywriter);

        if(verbose || logger.isDebugEnabled())
            logger.info(mywriter.toString());

        String formattedSource = null;
        try {
            formattedSource = new Formatter().formatSource(mywriter.toString());
        } catch (FormatterException e) {
            if(verbose)
                logger.info("Error formatting generated source : \n" + mywriter.toString(), e);
            formattedSource = mywriter.toString();
            if(!ignoreFormattingError)
                throw new CodeGenerationException("Some error in generated code", e);
        }

        writer.append(formattedSource);
        writer.flush();
        writer.close();
    }

    public void setIgnoreFormattingError(boolean flag) {
        this.ignoreFormattingError = flag;
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
}
