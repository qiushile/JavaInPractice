package com.kiscod.algorithm;

import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by lance on 2017/5/7.
 */
public class CharTest {

    public static void main(String[] args) {

        System.out.println("2012-03-45".substring(8, 10));



        String s =
                "A2-桐桐、B10-瑶瑶、B3-丫丫、A1-语涵、A3-祎祎、B8-宝宝 、B6-白宝、A1-大山、A1-彤彤、A5-柠柠"
                ;
        s = s.replaceAll(" ", "")
//                .replace("、", "\r")
//                .replace("-", "\r")
        ;

        String[] ss = s.split("、");
        List<String> s2 = Arrays.asList(ss);
        s2.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        for (String s1 : s2) {


            System.out.println(s1.split("-")[0] + "\t" + s1.split("-")[1]);

        }
//        System.out.println(s);


//
//
//        for (int i = Character.MIN_VALUE; i <= Character.MAX_VALUE; ++i) {
//            System.out.println(i + "    " + (char)i);
//        }
    }

    // 过滤特殊字符
    public   static   String StringFilter(String   str)   throws PatternSyntaxException {
        // 只允许字母和数字
        // String   regEx  =  "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }

    @Test
    public    void   testStringFilter()   throws   PatternSyntaxException   {
        String   str   =   "*adCVs*34_a _09_b5*[/435^*&城池()^$$&*).{}+.|.)%%*(*.中国}34{45[]12.fd'*&999下面是中文的字符￥……{}【】。，；’“‘”？";
        System.out.println(str);
        System.out.println(StringFilter(str));
    }
}
