package com.kiscod.jit;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author qiushile <qiushile@sina.com>
 * @date 2023/1/10
 */
public class RunJava {

    public void compileTheJavaSrcFile(String sourceCode, String className) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {

        long n = ThreadLocalRandom.current().nextLong();
        if (n < 0) {
            n = -n;
        }
        String filepath = "/Users/lance/WorkStation/IdeaProjects/JavaInPractice/target/jit/" + n;
        File files = saveCode(sourceCode, className, filepath);
        compileCode(files);
        Class clazz = loadClass(className, filepath);
        execute(clazz, "main");
    }

    /**
     * 源代码保存到文件中/
     * @param sourceCode    源码
     * @param className     类名
     * @param filepath      保存路径
     * @return              文件
     * @throws IOException
     */
    private static File saveCode(String sourceCode, String className, String filepath) throws IOException {
        File file = new File(filepath);
        file.mkdirs();
        File files = new File(file, className + ".java");
        FileOutputStream out = new FileOutputStream(files);
        out.write(sourceCode.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
        return files;
    }

    /**
     * 编译代码
     * @param files 待编译文件
     * @throws IOException
     */
    private static void compileCode(File files) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileMgr = compiler.getStandardFileManager(null, null, null);
        JavaCompiler.CompilationTask t = compiler.getTask(null, fileMgr, null, null, null, fileMgr.getJavaFileObjects(files));
        t.call();
        fileMgr.close();
    }

    /**
     * 类加载，需要使用自定义类加载器，并且指定上面编译路径为加载路径///
     * @param className     the class name
     * @param filepath      the file path
     * @return              the Class object
     * @throws MalformedURLException
     * @throws ClassNotFoundException
     */
    private static Class loadClass(String className, String filepath) throws MalformedURLException, ClassNotFoundException {
        URLClassLoader lo = URLClassLoader.newInstance(new URL[]{new URL("file://" +
                filepath + "/")});
        Class clazz = lo.loadClass(className);
        return clazz;
    }

    /**
     * 执行代码
     * @param clazz                     the compiled class
     * @param methodName                the method to execute
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static void execute(Class clazz, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Method m = clazz.getMethod("toString");
        Object result = m.invoke(clazz.newInstance(), null);
        System.out.println(result);

        m = clazz.getMethod("print");
        result = m.invoke(null);
        System.out.println(result);

        m = clazz.getDeclaredMethod(methodName, String[].class);
        //Object obj = clazz.newInstance();
        //result = m.invoke(null, new String[]{"温斌nb"});
        String[] args = new String[]{"温斌nb"};
        result = m.invoke(null, new Object[]{args});
        System.out.println(result);
    }
}
