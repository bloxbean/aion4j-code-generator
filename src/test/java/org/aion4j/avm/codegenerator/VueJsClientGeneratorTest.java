package org.aion4j.avm.codegenerator;

import org.aion4j.avm.codegenerator.generators.clientjs.VueJsClientGenerator;
import org.aion4j.avm.codegenerator.util.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class VueJsClientGeneratorTest {

    @Test
    public void testVueClientGenerate() throws IOException {
        Path tempDirWithPrefix = Files.createTempDirectory("aion4jgen");

        File tempDir = tempDirWithPrefix.toFile();
        tempDir.mkdir();
        tempDir.deleteOnExit();

        VueJsClientGenerator vueJsClientGenerator = new VueJsClientGenerator(true);
        vueJsClientGenerator.setIgnoreFormattingError(false);

        String abiStr = FileUtil.readFileFromResource("/abi/vueclient-8.abi");

        Map<String, Object> data = new HashMap<>();
        data.put("projectName", "hello-test");
        data.put("jar", "test.jar");
        vueJsClientGenerator.generate(abiStr, tempDir.getAbsolutePath(), data);

        System.out.println(">>>>>>> " + tempDir.getAbsolutePath());

        File src = new File(tempDir, "src");
        File publicFolder = new File(tempDir, "public");
        File componentFolder = new File(src, "components");

        Assert.assertTrue(new File(tempDir, "babel.config.js").exists());
        Assert.assertTrue(new File(tempDir, "package.json").exists());
        Assert.assertTrue(new File(publicFolder, "index.html").exists());
        Assert.assertTrue(new File(src, "App.vue").exists());
        Assert.assertTrue(new File(src, "contract-abi.js").exists());
        Assert.assertTrue(new File(src, "main.js").exists());
        Assert.assertTrue(new File(componentFolder, "getAddress.vue").exists());
        Assert.assertTrue(new File(componentFolder, "getBoolean.vue").exists());
        Assert.assertTrue(new File(componentFolder, "getShort.vue").exists());
    }
}
