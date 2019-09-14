package org.aion4j.avm.codegenerator;

import com.google.googlejavaformat.java.Formatter;
import org.aion4j.avm.codegenerator.api.abi.ABI;
import org.aion4j.avm.codegenerator.api.abi.ABIParserHelper;
import org.aion4j.avm.codegenerator.generators.testsupport.TestSupportGenerator;
import org.aion4j.avm.codegenerator.util.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class AvmTestSupportTest {

    @Test
    public void testContractTestImplClassGeneration() throws Exception {
        String abiStr = FileUtil.readFileFromResource("/abi/3.abi");

        System.out.println(abiStr);
        ABI abi = null;
        try {
            abi = ABIParserHelper.parse(abiStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("package", "com.test");
        data.put("className", "HelloAvm");
        data.put("methods", abi.getMethods());
        data.put("abi", abi);

        StringWriter writer = new StringWriter();

        TestSupportGenerator testSupportGenerator = new TestSupportGenerator(true);
        testSupportGenerator.setIgnoreFormattingError(false);
        testSupportGenerator.generateFromTemplate("templates/test/TestImpl.java.vm",data, writer);

        System.out.println(writer.toString());

        System.out.println("****** formatted ********");
        String formattedSource = new Formatter().formatSource(writer.toString());
        System.out.println(formattedSource);

        Assert.assertTrue(formattedSource.contains("package com.test"));
        Assert.assertTrue(formattedSource.contains("class HelloAvmTestImpl"));
        Assert.assertTrue(formattedSource.contains("return setValues(newRequestContext(), arg1, arg2, arg3, arg4, arg5);"));
        Assert.assertTrue(formattedSource.contains("byte[] txData = ABIUtil.encodeMethodArguments(\"setValues\", arg1, arg2, arg3, arg4, arg5);"));
    }

    @Test
    public void testContractTestGeneration3() throws IOException { //All classes
        Path tempDirWithPrefix = Files.createTempDirectory("aion4jgen");

        File tempDir = tempDirWithPrefix.toFile();
        tempDir.mkdir();
        tempDir.deleteOnExit();

        System.out.println("** " +tempDir);

        TestSupportGenerator testSupportGenerator = new TestSupportGenerator(true);
        testSupportGenerator.setIgnoreFormattingError(false);

        String abiStr = FileUtil.readFileFromResource("/abi/3.abi");
        testSupportGenerator.generate(abiStr, tempDir.getAbsolutePath());

        Assert.assertTrue(new File(tempDir, "com" + File.separator + "test" + File.separator + "HelloAvmTestImpl.java" ).exists());
    }

    @Test
    public void testContractTestGeneration4() throws IOException { //All classes
        Path tempDirWithPrefix = Files.createTempDirectory("aion4jgen");

        File tempDir = tempDirWithPrefix.toFile();
        tempDir.mkdir();
        tempDir.deleteOnExit();

        System.out.println("** " +tempDir);

        TestSupportGenerator testSupportGenerator = new TestSupportGenerator(true);
        testSupportGenerator.setIgnoreFormattingError(false);

        String abiStr = FileUtil.readFileFromResource("/abi/4.abi");
        testSupportGenerator.generate(abiStr, tempDir.getAbsolutePath());

        Assert.assertTrue(new File(tempDir, "com" + File.separator + "test" + File.separator + "HelloAvmTestImpl.java" ).exists());
    }

    @Test
    public void testContractTestGeneration1() throws IOException { //All classes
        Path tempDirWithPrefix = Files.createTempDirectory("aion4jgen");

        File tempDir = tempDirWithPrefix.toFile();
        tempDir.mkdir();
        tempDir.deleteOnExit();

        System.out.println("** " +tempDir);

        TestSupportGenerator testSupportGenerator = new TestSupportGenerator(true);
        testSupportGenerator.setIgnoreFormattingError(false);

        String abiStr = FileUtil.readFileFromResource("/abi/1.abi");
        testSupportGenerator.generate(abiStr, tempDir.getAbsolutePath());

        Assert.assertTrue(new File(tempDir, "com" + File.separator + "test" + File.separator + "HelloAvmTestImpl.java" ).exists());
    }

    @Test
    public void testContractTestGeneration2() throws IOException { //All classes
        Path tempDirWithPrefix = Files.createTempDirectory("aion4jgen");

        File tempDir = tempDirWithPrefix.toFile();
        tempDir.mkdir();
        tempDir.deleteOnExit();

        System.out.println("** " +tempDir);

        TestSupportGenerator testSupportGenerator = new TestSupportGenerator(true);
        testSupportGenerator.setIgnoreFormattingError(false);

        String abiStr = FileUtil.readFileFromResource("/abi/2.abi");
        testSupportGenerator.generate(abiStr, tempDir.getAbsolutePath());

        Assert.assertTrue(new File(tempDir, "com" + File.separator + "test" + File.separator + "HelloAvmTestImpl.java" ).exists());
    }
}
