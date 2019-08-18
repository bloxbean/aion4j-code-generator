/*
 * MIT License
 *
 * Copyright (c) 2019 Aion4j Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.aion4j.avm.codegenerator;

import org.aion4j.avm.codegenerator.api.abi.ABI;
import org.aion4j.avm.codegenerator.api.abi.ABIParserHelper;
import org.aion4j.avm.codegenerator.util.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ABIParserTest {

    @Test
    public void testSampleABI1() {
        String abiStr = "1\r\n" +
                "com.test.HelloAvm\r\n" +
                "Clinit: ()\r\n" +
                "public static void sayHello()\r\n" +
                "public static String greet(String)\r\n" +
                "public static void setString(String)\r\n" +
                "public static String[][] setValues(String[], int,BigInteger  , boolean)";

        System.out.println(abiStr);
        ABI abi = null;
        try {
            abi = ABIParserHelper.parse(abiStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals("1", abi.getVersion());
        Assert.assertEquals("com.test.HelloAvm", abi.getContractClass());
        Assert.assertEquals(0, abi.getClint().size());

        Assert.assertEquals(4, abi.getMethods().size());

        Assert.assertEquals("sayHello", abi.getMethods().get(0).getMethodName());
        Assert.assertEquals("void", abi.getMethods().get(0).getReturnType());
        Assert.assertEquals(0, abi.getMethods().get(0).getParameters().size());

        Assert.assertEquals("greet", abi.getMethods().get(1).getMethodName());
        Assert.assertEquals("String", abi.getMethods().get(1).getReturnType());
        Assert.assertEquals(1, abi.getMethods().get(1).getParameters().size());
        Assert.assertEquals("String", abi.getMethods().get(1).getParameters().get(0));

        Assert.assertEquals("setString", abi.getMethods().get(2).getMethodName());
        Assert.assertEquals("void", abi.getMethods().get(2).getReturnType());
        Assert.assertEquals(1, abi.getMethods().get(2).getParameters().size());
        Assert.assertEquals("String", abi.getMethods().get(2).getParameters().get(0));

        Assert.assertEquals("setValues", abi.getMethods().get(3).getMethodName());
        Assert.assertEquals("String[][]", abi.getMethods().get(3).getReturnType());
        Assert.assertEquals(4, abi.getMethods().get(3).getParameters().size());
        Assert.assertEquals("String[]", abi.getMethods().get(3).getParameters().get(0));
        Assert.assertEquals("int", abi.getMethods().get(3).getParameters().get(1));
        Assert.assertEquals("BigInteger", abi.getMethods().get(3).getParameters().get(2));
        Assert.assertEquals("boolean", abi.getMethods().get(3).getParameters().get(3));

    }

    @Test
    public void testSampleABIeWithClint() {
        String abiStr = "1.12\r\n" +
                "com.test.HelloAvm\r\n" +
                "Clinit: (int, boolean, Address)\r\n" +
                "public static void sayHello()\r\n" +
                "public static String greet(String)\r\n" +
                "public static void setString(String)\r\n" +
                "public static String[][] setValues(String[], int,BigInteger  , boolean)";

        System.out.println(abiStr);
        ABI abi = null;
        try {
            abi = ABIParserHelper.parse(abiStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals("1.12", abi.getVersion());
        Assert.assertEquals("com.test.HelloAvm", abi.getContractClass());

        Assert.assertEquals("int", abi.getClint().get(0));
        Assert.assertEquals("boolean", abi.getClint().get(1));
        Assert.assertEquals("Address", abi.getClint().get(2));

        Assert.assertEquals(4, abi.getMethods().size());

        Assert.assertEquals("sayHello", abi.getMethods().get(0).getMethodName());
        Assert.assertEquals("void", abi.getMethods().get(0).getReturnType());
        Assert.assertEquals(0, abi.getMethods().get(0).getParameters().size());

        Assert.assertEquals("greet", abi.getMethods().get(1).getMethodName());
        Assert.assertEquals("String", abi.getMethods().get(1).getReturnType());
        Assert.assertEquals(1, abi.getMethods().get(1).getParameters().size());
        Assert.assertEquals("String", abi.getMethods().get(1).getParameters().get(0));

        Assert.assertEquals("setString", abi.getMethods().get(2).getMethodName());
        Assert.assertEquals("void", abi.getMethods().get(2).getReturnType());
        Assert.assertEquals(1, abi.getMethods().get(2).getParameters().size());
        Assert.assertEquals("String", abi.getMethods().get(2).getParameters().get(0));

        Assert.assertEquals("setValues", abi.getMethods().get(3).getMethodName());
        Assert.assertEquals("String[][]", abi.getMethods().get(3).getReturnType());
        Assert.assertEquals(4, abi.getMethods().get(3).getParameters().size());
        Assert.assertEquals("String[]", abi.getMethods().get(3).getParameters().get(0));
        Assert.assertEquals("int", abi.getMethods().get(3).getParameters().get(1));
        Assert.assertEquals("BigInteger", abi.getMethods().get(3).getParameters().get(2));
        Assert.assertEquals("boolean", abi.getMethods().get(3).getParameters().get(3));

    }

    @Test
    public void testSampleABI1FromFile() throws IOException {
        String abiStr = FileUtil.readFileFromResource("/abi/1.abi");

        System.out.println(abiStr);
        ABI abi = null;
        try {
            abi = ABIParserHelper.parse(abiStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals("1", abi.getVersion());
        Assert.assertEquals("com.test.HelloAvm", abi.getContractClass());
        Assert.assertEquals(0, abi.getClint().size());

        Assert.assertEquals(4, abi.getMethods().size());

        Assert.assertEquals("sayHello", abi.getMethods().get(0).getMethodName());
        Assert.assertEquals("void", abi.getMethods().get(0).getReturnType());
        Assert.assertEquals(0, abi.getMethods().get(0).getParameters().size());

        Assert.assertEquals("greet", abi.getMethods().get(1).getMethodName());
        Assert.assertEquals("String", abi.getMethods().get(1).getReturnType());
        Assert.assertEquals(1, abi.getMethods().get(1).getParameters().size());
        Assert.assertEquals("String", abi.getMethods().get(1).getParameters().get(0));

        Assert.assertEquals("setString", abi.getMethods().get(2).getMethodName());
        Assert.assertEquals("void", abi.getMethods().get(2).getReturnType());
        Assert.assertEquals(1, abi.getMethods().get(2).getParameters().size());
        Assert.assertEquals("String", abi.getMethods().get(2).getParameters().get(0));

        Assert.assertEquals("setValues", abi.getMethods().get(3).getMethodName());
        Assert.assertEquals("String[][]", abi.getMethods().get(3).getReturnType());
        Assert.assertEquals(5, abi.getMethods().get(3).getParameters().size());
        Assert.assertEquals("short[]", abi.getMethods().get(3).getParameters().get(0));
        Assert.assertEquals("int[][]", abi.getMethods().get(3).getParameters().get(1));
        Assert.assertEquals("BigInteger", abi.getMethods().get(3).getParameters().get(2));
        Assert.assertEquals("long[][]", abi.getMethods().get(3).getParameters().get(3));
        Assert.assertEquals("boolean", abi.getMethods().get(3).getParameters().get(4));

    }

    @Test
    public void testSampleABI2FromFile() throws IOException {
        String abiStr = FileUtil.readFileFromResource("/abi/2.abi");

        System.out.println(abiStr);
        ABI abi = null;
        try {
            abi = ABIParserHelper.parse(abiStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals("1.11", abi.getVersion());
        Assert.assertEquals("com.test.HelloAvm", abi.getContractClass());

        Assert.assertEquals(3, abi.getClint().size());
        Assert.assertEquals("int[]", abi.getClint().get(0));
        Assert.assertEquals("boolean[][]", abi.getClint().get(1));
        Assert.assertEquals("Address", abi.getClint().get(2));

        Assert.assertEquals(2, abi.getMethods().size());

        Assert.assertEquals("setString", abi.getMethods().get(0).getMethodName());
        Assert.assertEquals("void", abi.getMethods().get(0).getReturnType());
        Assert.assertEquals(5, abi.getMethods().get(0).getParameters().size());
        Assert.assertEquals("String", abi.getMethods().get(0).getParameters().get(0));
        Assert.assertEquals("int", abi.getMethods().get(0).getParameters().get(1));
        Assert.assertEquals("boolean", abi.getMethods().get(0).getParameters().get(2));
        Assert.assertEquals("Address", abi.getMethods().get(0).getParameters().get(3));
        Assert.assertEquals("long", abi.getMethods().get(0).getParameters().get(4));

        Assert.assertEquals("setValues", abi.getMethods().get(1).getMethodName());
        Assert.assertEquals("String[][]", abi.getMethods().get(1).getReturnType());
        Assert.assertEquals(6, abi.getMethods().get(1).getParameters().size());
        Assert.assertEquals("short[]", abi.getMethods().get(1).getParameters().get(0));
        Assert.assertEquals("int[][]", abi.getMethods().get(1).getParameters().get(1));
        Assert.assertEquals("BigInteger", abi.getMethods().get(1).getParameters().get(2));
        Assert.assertEquals("long[][]", abi.getMethods().get(1).getParameters().get(3));
        Assert.assertEquals("boolean", abi.getMethods().get(1).getParameters().get(4));
        Assert.assertEquals("short", abi.getMethods().get(1).getParameters().get(5));

    }


}
