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

import org.aion4j.codegenerator.abi.antlr4.*;
import org.aion4j.codegenerator.abi.antlr4.ABIParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.List;

public class ABIParserHelper {
    public static ABI parse(String abi) {

        org.aion4j.codegenerator.abi.antlr4.ABILexer abiLexer =
                new org.aion4j.codegenerator.abi.antlr4.ABILexer(CharStreams.fromString(abi));

        CommonTokenStream tokens = new CommonTokenStream(abiLexer);
        org.aion4j.codegenerator.abi.antlr4.ABIParser parser = new org.aion4j.codegenerator.abi.antlr4.ABIParser(tokens);
        ParseTree tree = parser.abi_content();

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();

        ABIParserListenerImpl abiListener = new ABIParserListenerImpl();

        parseTreeWalker.walk(abiListener, tree);

        return abiListener.getABI();

    }

    public static void main(String[] args) {
        String abiStr = "0.0\n" +
                "org.aion.web3.Counter\n" +
                "Clinit: ()\n" +
                "public static void incrementCounter(int)\n" +
                "public static void decrementCounter(int)\n" +
                "public static int getCount()\n" +
                " ";

        ABI abi = parse(abiStr);
    }

}

class ABIParserListenerImpl extends org.aion4j.codegenerator.abi.antlr4.ABIBaseListener {
    ABI abi;

    public ABIParserListenerImpl() {
        abi = new ABI();
    }

    @Override
    public void enterVersion(org.aion4j.codegenerator.abi.antlr4.ABIParser.VersionContext ctx) {
        System.out.println("version: " + ctx.getText());
        abi.setVersion(ctx.getText());
    }

    @Override
    public void exitContract_name(org.aion4j.codegenerator.abi.antlr4.ABIParser.Contract_nameContext ctx) {
        System.out.println("Contract class: " + ctx.getText());
        abi.setContractClass(ctx.getText());
    }

    @Override
    public void exitClinit(org.aion4j.codegenerator.abi.antlr4.ABIParser.ClinitContext ctx) {
        System.out.println("clinit: " + ctx.getText());

        List<ABIParser.ParamTypeContext> paramTypeContextList = ctx.paramType();

        for(ABIParser.ParamTypeContext paramType: paramTypeContextList) {
            String paramTypeText = paramType.getText();
            abi.getClint().add(paramTypeText);
        }
    }

    @Override
    public void exitMethodDeclaration(ABIParser.MethodDeclarationContext ctx) {
        String methodName = ctx.methodName().getText();

        ABIMethod abiMethod = new ABIMethod();
        abiMethod.setSignature(ctx.getText());
        abiMethod.setMethodName(methodName);

        List<ABIParser.ParamTypeContext> paramTypeContextList = ctx.paramType();

        for(ABIParser.ParamTypeContext paramType: paramTypeContextList) {
            String paramTypeText = paramType.getText();
            abiMethod.getParameters().add(paramTypeText);
        }

        abiMethod.setReturnType(ctx.returnType().getText());

        abi.getMethods().add(abiMethod);
    }

    public ABI getABI() {
        return abi;
    }
}
