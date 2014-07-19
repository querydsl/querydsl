package com.mysema.query.jpa.codegen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mysema.codegen.SimpleCompiler;
import junit.framework.Assert;

public class CompileUtils {

    private static final SimpleCompiler compiler = new SimpleCompiler();

    private static List<String> getFiles(String path) {
        List<String> classes = new ArrayList<String>();
        for (File file : new File(path).listFiles()) {
            if (file.getName().endsWith(".java")) {
                classes.add(file.getPath());
            } else if (file.isDirectory() && !file.getName().startsWith(".")) {
                classes.addAll(getFiles(file.getAbsolutePath()));
            }
        }
        return classes;
    }

    public static void compile(String target) throws IOException {
        List<String> options = new ArrayList<String>();
        options.addAll(getFiles(target));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        int compilationResult = compiler.run(null, out, err, options.toArray(new String[options.size()]));

        if (compilationResult != 0) {
            System.out.println(new String(out.toByteArray()));
            System.out.println(new String(err.toByteArray()));
            Assert.fail("Compilation Failed");
        }
    }
}
