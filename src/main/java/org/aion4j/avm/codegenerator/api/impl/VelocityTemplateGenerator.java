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

package org.aion4j.avm.codegenerator.api.impl;

import org.aion4j.avm.codegenerator.api.TemplateGenerator;
import org.aion4j.avm.codegenerator.api.exception.TemplateNotFoundException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class VelocityTemplateGenerator implements TemplateGenerator {
    VelocityEngine velocityEngine = null;

    public VelocityTemplateGenerator() {
        init();
    }

    public void init() {
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
    }

    @Override
    public void generate(String template, HashMap<String, Object> data, Writer writer) {
        Template t = null;

        try {
            t = velocityEngine.getTemplate(template);
        } catch (ResourceNotFoundException e) {
            throw new TemplateNotFoundException("Template not found : " + template, e);
        }

        if(t == null) {
            throw new TemplateNotFoundException("Template not found : " + template);
        }

        VelocityContext context = new VelocityContext();

        if(data != null) {

            for(Map.Entry<String, Object> entry: data.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }
        }

        t.merge( context, writer );
    }
}

