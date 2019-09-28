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

import org.aion4j.avm.codegenerator.api.TemplateGenerator;
import org.aion4j.avm.codegenerator.api.impl.VelocityTemplateGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.HashMap;

public class TemplateGeneratorTest {

    @Test
    public void testSampleGenerate() {
        TemplateGenerator generator = new VelocityTemplateGenerator();

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", "Avm");

        StringWriter writer = new StringWriter();
        generator.generate("templates/js/hello.vm", data, writer);

        Assert.assertEquals("Hello Avm", writer.toString().trim());
    }

    @Test
    public void testContractClientJsGenerate() {
        TemplateGenerator generator = new VelocityTemplateGenerator();

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", "Avm");

        StringWriter writer = new StringWriter();
        generator.generate("templates/client/js/contract.js.vm", data, writer);

        System.out.println(writer.toString());

        Assert.assertEquals(true, writer.toString().length() > 0);
    }
}
