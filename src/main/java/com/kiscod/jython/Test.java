package com.kiscod.jython;

import org.python.antlr.ast.Str;
import org.python.core.Py;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import java.io.*;

/**
 * Created by lance on 2017/4/19.
 */
public class Test {

    public static void main(String[] args) throws IOException, InterruptedException {

        String c = "<p><p>原标题：皇太子膝下无子，日本天皇的退位没那么简单！</p>\\n<p>撰文/袁野</p>\\n<!-- IFENG_DOC_ADVERT --><p>今天（4月14日），据日媒报道，日本政府已经决定在有关天皇陛下退位的特例法案中，将明确写入以下内容，即天皇从《特例法》付诸实施之日起退位。据报道，退位的具体日期将由日本首相在事先听取皇室会议意见的基础上，在《特例法》颁布后的3年之内，以政令的形式做出规定。</p>\\n<p>据信，这一日期很可能将是2019年元旦。该法案一旦获得国会通过，日本天皇“生前退位”的方式方法就将从此大局底定。</p>\\n<p>众所周知，天皇任期是终身制，很少出现提前退位的情况，除非有身体健康和政治方面的特殊考虑。明仁天皇生于1933年12月，1989年其父昭和天皇逝世后继任皇位，目前在任17年。日本天皇退位需要修改《皇室典范》，以便继承人能在天皇生前继承皇位。因此，没有特殊情况，天皇是不会生前退位的。</p>\\n<p>“生前退位”引波澜</p>\\n<p>2016年7月13日，日本NHK电视台在当晚7点黄金时间播出重磅消息：天皇明仁已表示打算“生前退位”，时间约在今后数年，并已经向宫内厅表达了这一意愿。有趣的是，当天深夜日本宫内厅的次长山本信一郎就紧急召集记者会，全盘否认关于天皇“生前退位”的相关报道，并斩钉截铁地说称“绝无此类事实”。</p>\\n<p>天皇“生前退位”是日本近二百年来所未见的，二战后制定的日本宪法和《皇室典范》中根本没有关于天皇让位的规定，因此若要实现，必须修改法律。</p>\\n<p>“海运仓内参”（ID：hycplb）注意到，日本政府在起草、修订法律文件时的谨慎和繁琐颇为著名，二战末期起草天皇《终战诏书》的过程就是典型案例，即使美国在长崎投掷第二颗原子弹也未能使起草过程提速。最终的《停战诏书》字句经过反复改动，以至于已来不及誊写，而是以添加行间小字和贴附小纸条的方式进行修改，令人哭笑不得。此次为天皇退位而修法，日本政府也遇到了类似的麻烦。</p>\\n<p>";
        c = c.replace("\\n", "");
        int left = c.indexOf("<");
        int right = c.indexOf(">");
        while (left >= 0 && right >= 0) {
            String repl = c.substring(left, right + 1);
            System.out.println(repl);
            c = c.replace(repl, "");
            left = c.indexOf("<");
            right = c.indexOf(">");
        }
        String nn = "";
        int dotp = -1;
//        c.indexOf("。");
//        nn = c.substring(0, dotp+1);

        do {
            dotp = c.indexOf("。", dotp + 1);
            nn = c.substring(0, dotp + 1);
        } while ( dotp < 50 && dotp > 0) ;


        System.out.println(c);
        System.out.println(nn);

        if ( 2>1 )return;





        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("a = \"hello, Jython\"");
        interpreter.exec("print a");

        interpreter.exec("import sys");
        interpreter.exec("print sys.path");


        Process proc = Runtime.getRuntime().exec("python /Users/lance/WorkStation/python/test2.py a");

        BufferedReader in = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        in.close();
        proc.waitFor();
        System.out.println("end");


//        interpreter.exec("pip install numpy");

//        interpreter.execfile("/Users/lance/Documents/Tencent Files/516517474/FileRecv/test0.py");

        PySystemState sys = Py.getSystemState();
        sys.path.add("/Library/Frameworks/Python.framework/Versions/2.7/lib/python2.7/site-packages");
        System.out.println(sys.path.toString());

        InputStream filepy = new FileInputStream("/Users/lance/WorkStation/python/test0.py");
        interpreter.execfile(filepy);  ///执行python py文件
        filepy.close();

        interpreter.exec("import urllib");

        interpreter.exec("import numpy");
        interpreter.exec("yn = numpy.zeros((2, 3))");
        interpreter.exec("print(yn)");
        interpreter.exec("print a");
    }

}
