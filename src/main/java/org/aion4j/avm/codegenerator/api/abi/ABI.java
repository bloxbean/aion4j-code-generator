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

package org.aion4j.avm.codegenerator.api.abi;

import java.util.ArrayList;
import java.util.List;

public class ABI {
    private String version;
    private String contractClass;
    private List<String> clint;
    private List<ABIMethod> methods;

    public ABI() {
        this.clint = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContractClass() {
        return contractClass;
    }

    public void setContractClass(String contractClass) {
        this.contractClass = contractClass;
    }

    public List<ABIMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<ABIMethod> methods) {
        this.methods = methods;
    }

    public List<String> getClint() {
        return clint;
    }

    public void setClint(List<String> clint) {
        this.clint = clint;
    }
}
