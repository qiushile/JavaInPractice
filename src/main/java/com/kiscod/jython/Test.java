package com.kiscod.jython;

import org.python.util.PythonInterpreter;

/**
 * Created by lance on 2017/4/19.
 */
public class Test {

    public static void main(String[] args) {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("a = \"hello, Jython\"");
        interpreter.exec("print a");
    }

}
