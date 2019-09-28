package org.aion4j.avm.codegenerator;

import org.aion4j.avm.codegenerator.generators.clientjs.JsClientGenerator;
import org.aion4j.avm.codegenerator.util.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class JsClientGeneratorTest {

    @Test
    public void testDeployJsGenerate() throws IOException {
        Path tempDirWithPrefix = Files.createTempDirectory("aion4jgen");

        File tempDir = tempDirWithPrefix.toFile();
        tempDir.mkdir();
        tempDir.deleteOnExit();

        System.out.println("** " +tempDir);

        JsClientGenerator jsClientGenerator = new JsClientGenerator(true);
        jsClientGenerator.setIgnoreFormattingError(false);

        String abiStr = FileUtil.readFileFromResource("/abi/5.abi");

        Map<String, Object> data = new HashMap<>();
        data.put("jar", "test.jar");
        jsClientGenerator.generate(abiStr, tempDir.getAbsolutePath(), data);

        Assert.assertTrue(new File(tempDir, "contract-deploy-abi.js" ).exists());
        Assert.assertTrue(new File(tempDir, "contract.js" ).exists());

        String content = FileUtil.readFile(tempDir + File.separator + "contract-deploy-abi.js");
        Assert.assertTrue("web3.avm.contract.deploy() is generated", content.contains("let res = await  web3.avm.contract.deploy(jarPath).args([arg1,arg2]).initSend()"));
        Assert.assertTrue(content.contains("let jarPath = path.join(__dirname,'test.jar')"));

        String callContent = FileUtil.readFile(tempDir + File.separator + "contract.js");
        Assert.assertTrue(callContent.contains("async function setValues_call(arg1, arg2, arg3, arg4, arg5)"));
        Assert.assertTrue(callContent.contains("let data = web3.avm.contract.method('setValues').input([\"short[]\", \"int[][]\", \"BigInteger\", \"long[][]\", \"boolean\"], [arg1, arg2, arg3, arg4, arg5])"));
    }

    @Test
    public void testDeployJsGenerate2() throws IOException {
        Path tempDirWithPrefix = Files.createTempDirectory("aion4jgen");

        File tempDir = tempDirWithPrefix.toFile();
        tempDir.mkdir();
        tempDir.deleteOnExit();

        System.out.println("** " +tempDir);

        JsClientGenerator jsClientGenerator = new JsClientGenerator(true);
        jsClientGenerator.setIgnoreFormattingError(false);

        String abiStr = FileUtil.readFileFromResource("/abi/6.abi");

        Map<String, Object> data = new HashMap<>();
        data.put("jar", "contract.jar");
        jsClientGenerator.generate(abiStr, tempDir.getAbsolutePath(), data);

        Assert.assertTrue(new File(tempDir, "contract-deploy-abi.js" ).exists());
        Assert.assertTrue(new File(tempDir, "contract.js" ).exists());

        String deployContent = FileUtil.readFile(tempDir + File.separator + "contract-deploy-abi.js");
        Assert.assertTrue(deployContent.contains("let jarPath = path.join(__dirname,'contract.jar')"));

        String callContent = FileUtil.readFile(tempDir + File.separator + "contract.js");
        Assert.assertTrue(callContent.contains("async function setValues_call(arg1, arg2, arg3, arg4, arg5)"));
    }

    @Test
    public void testClientAbiJsGenerate() throws IOException {
        Path tempDirWithPrefix = Files.createTempDirectory("aion4jgen");

        File tempDir = tempDirWithPrefix.toFile();
        tempDir.mkdir();
        tempDir.deleteOnExit();

        System.out.println("** " +tempDir);

        JsClientGenerator jsClientGenerator = new JsClientGenerator(true);
        jsClientGenerator.setIgnoreFormattingError(false);

        String abiStr = FileUtil.readFileFromResource("/abi/jsclient-7.abi");

        Map<String, Object> data = new HashMap<>();
        data.put("jar", "contract.jar");
        jsClientGenerator.generate(abiStr, tempDir.getAbsolutePath(), data);

        Assert.assertTrue(new File(tempDir, "contract-abi.js" ).exists());

        String content = FileUtil.readFile(tempDir + File.separator + "contract-abi.js");
        System.out.println(content);
        Assert.assertTrue(content.contains("async function setValues(value,arg1,arg2,arg3,arg4,arg5)"));
    }
}
