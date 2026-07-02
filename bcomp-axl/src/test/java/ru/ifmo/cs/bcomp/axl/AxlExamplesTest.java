package ru.ifmo.cs.bcomp.axl;

import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AxlExamplesTest {

    @Test
    public void everyExampleCompilesAndAssembles() throws Exception {
        File dir = new File("examples");
        assertTrue("examples directory not found at " + dir.getAbsolutePath(), dir.isDirectory());
        File[] files = dir.listFiles(new java.io.FilenameFilter() {
            @Override
            public boolean accept(File d, String name) {
                return name.endsWith(".axl");
            }
        });
        assertNotNull(files);
        assertTrue("no .axl examples found", files.length > 0);
        for (File f : files) {
            String src = new String(Files.readAllBytes(f.toPath()), Charset.forName("UTF-8"));
            String asm = AxlTestSupport.compileToAsm(src);
            AxlTestSupport.assemble(asm);
        }
    }
}
