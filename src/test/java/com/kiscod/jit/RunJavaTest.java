package com.kiscod.jit;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class RunJavaTest {

    private RunJava runJavaUnderTest;

    @Before
    public void setUp() {
        runJavaUnderTest = new RunJava();
    }

    @Test
    public void testCompileTheJavaSrcFile() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        // Setup
        // Run the test
        //String className = "com.kiscod.not.previous.jit.MyTest";
        String className = "MyTest";
        String filepath = "/Users/lance/WorkStation/IdeaProjects/JavaInPractice/src/main/java/com/kiscod/jit/Test.java";
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filepath));
            StringBuilder sb = new StringBuilder();
            if (in.ready()) {
                // change package name
                //String str =
                        in.readLine();
                //String replace = str.replace(".jit", ".not.previous.jit");
                //sb.append(replace);
            }
            while (in.ready()) {
                sb.append(in.readLine().replace("public class Test {", "public class MyTest {"));
            }
            System.out.println(sb);
            in.close();

            runJavaUnderTest.compileTheJavaSrcFile(sb.toString(), className);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Verify the results
    }
}
