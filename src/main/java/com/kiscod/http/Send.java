package com.kiscod.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lance on 2017/7/7.
 */
public class Send {

    public static String sendGet(String url, String token) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Authorization", token);
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }



    public static void main(String[] args) {

        System.out.println(format(1499994646, "yyyy-MM-dd HH:mm:ss"));
//        System.out.println(format(1487560749, "yyyy-MM-dd HH:mm:ss"));

        String res = sendGet("http://123.135.111.2:8050/audit/audit/getNumber", "n40blPVgSC7M0O4Ph2Up");

    }


    public static String format(long t, String format) {
        if(t <= 0L) {
            return Long.toString(t);
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);

                return sdf.format(new Date(t));
            } catch (Exception var5) {
                return "";
            }
        }
    }
}
