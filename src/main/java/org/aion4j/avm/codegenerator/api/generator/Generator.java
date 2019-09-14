package org.aion4j.avm.codegenerator.api.generator;

import java.io.IOException;

public interface Generator {
    public void generate(String abiString, String folder) throws IOException;
}
