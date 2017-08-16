package com.tecnova;

import java.sql.*;
import java.util.*;

public class PrewarningCompany {

    public static String[] warnName = new String[]{"基础信息缺失", "经营风险", "信用风险", "操作风险", "联系方式缺失", "舆论风险"};
    public static String[] levelName = new String[]{"正常", "黄色预警", "橙色预警", "红色预警"};

    // 基础信息缺失：
    // 法人代表faren、高管gaoguan、股东及股权占比gqzb、注册机关zcjg、公司类型gslx、工商注册时间gszcsj、注册资金zzzb、项目类型xmlx信息缺失。
    public static String[] basicInfo = new String[]{"faren", "gaoguan", "gqzb", "zcjg", "gslx", "gszcsj", "zzzb", "xmlx"};
    public static String[] basicInfoCn = new String[]{"法人代表", "高管", "股东及股权占比", "注册机关", "公司类型", "工商注册时间", "注册资金", "项目类型"};

    // 重要基础信息缺失：
    // 平台名称 name、平台网址 url、公司名称 company、营业执照注册号 zhizhao、社会统一信用代码 xinyong、组织机构代码 jigou、经营范围 jyfw、注册地点 zcdd、运营地点 yydd、ICP备案号 beian
    public static String[] importantBasicInfo = new String[]{"name", "url", "company", "zhizhao", "xinyong", "jigou", "jyfw", "zcdd", "yydd", "beian"};
    public static String[] importantBasicInfoCn = new String[]{"平台名称", "平台网址", "公司名称", "营业执照注册号", "社会统一信用代码", "组织机构代码", "经营范围", "注册地点", "运营地点", "ICP备案号"};

    // 经营信息
    // 年收益范围 shouyi、公告及发标频率fbpl、投资期限tzqx
    public static String[] importantBusiness = new String[]{"shouyi", "fbpl", "tzqx"};
    public static String[] importantBusinessCn = new String[]{"年收益范围", "公告及发标频率", "投资期限"};

    // 联系方式缺失
    // 官方QQ qq、官方电话gfdh、官方QQ群qqqun、微信公众号wxpt信息缺失
    public static String[] contact = new String[]{"qq", "gfdh", "qqqun", "wxpt"};
    public static String[] contactCn = new String[]{"官方QQ", "官方电话", "官方QQ群", "微信公众号"};

    // 准备金可能出现的情况
    public static String[] reservFund = new String[]{"平台准备金", "平台备用金", "平台备付金", "风险准备金", "保证金"};

    public static void main(String[] args) {

        calculatePrewarningLevelAndInfo();

    }
    public static void calculatePrewarningLevelAndInfo() {
        long startTime=System.currentTimeMillis();
        long totalCount = 0;
        try {
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载MySQL驱动！");
        } catch (ClassNotFoundException e1) {
            System.out.println("找不到MySQL驱动!");
            e1.printStackTrace();
            return;
        }

        String url = "jdbc:mysql://localhost:3306/webtecnova";    //JDBC的URL
        //调用DriverManager对象的getConnection()方法，获得一个Connection对象
        Connection conn;
        try {
            conn = DriverManager.getConnection(url, "root", "hadoop");
            //创建一个Statement对象
            Statement stmt = conn.createStatement(); //创建Statement对象
            System.out.println("成功连接到数据库！");

            int countPerTime = 100;
            int currCount;
            do {

                //查询数据的代码
                String sql = "SELECT * FROM company WHERE prewarn_level IS NULL LIMIT 100 ";    //要执行的SQL
                ResultSet rs = stmt.executeQuery(sql);//创建数据对象
                System.out.println("编号" + "\t" + "平台名称" + "\t" + "公司名称");
                currCount = 0;
                while (rs.next()) {
                    currCount++;
                    int id = rs.getInt("id");
                    String shouyi = rs.getString("shouyi");
                    String aqtd = rs.getString("aqtd");
                    String bzms = rs.getString("bzms");
                    String tzqx = rs.getString("tzqx");
                    String tuoguan = rs.getString("tuoguan");
                    String fxzbj = rs.getString("fxzbj");

                    // 是否使用安全通道： , 使用 使用中 已使用 已认证 找不到 未使用 未使用? 未查到 未认证 查不到
                    boolean isAqtd = (aqtd != null && !(aqtd.startsWith("未") || aqtd.endsWith("不到")));

                    boolean tzqxLessThanOneMonth = (tzqx != null && (tzqx.contains("天标") || tzqx.contains("秒标")));
                    boolean hasTuoguan = !(tuoguan == null || tuoguan.contains("未") || tuoguan.contains("无") || tuoguan.contains("否") || tuoguan.contains("不"));

                    float maxShouyi = 0;
                    float minShouyi = 0;
                    if (shouyi != null && !shouyi.equals("")) {
                        if (shouyi.contains("~") && !shouyi.contains("?")) {
                            String[] shou_yi = shouyi.split("~");
                            if (shou_yi.length == 2) {
                                shou_yi[0] = shou_yi[0].trim();
                                shou_yi[1] = shou_yi[1].trim();
                                minShouyi = Float.parseFloat(shou_yi[0].substring(0, shou_yi[0].length() - 1));
                                maxShouyi = Float.parseFloat(shou_yi[1].substring(0, shou_yi[1].length() - 1));
                            }
                        } else {
                            if (shouyi.startsWith("<") && shouyi.endsWith("%")) {
                                maxShouyi = Float.parseFloat(shouyi.substring(shouyi.indexOf("<") + 1, shouyi.indexOf("%")));
                            }
                        }
                    }

                    String shouyiOver13Message = (maxShouyi > 13) ? "年收益范围超过13%" : "";
                    String aqtdMessage = isAqtd ? "" : "平台未经过Https安全通道加密传输aqtd";
                    String bzmsMessage = containsKeylist(bzms, reservFund) ? "" : "保障模式采用除风险备用金及与风险备用金相组合的模式除外，包括：单纯的平台垫付、第三方担保机构、小额贷款公司担保、非融资性担保公司担保";
                    String basicInfoLostMessage = getDataLost(rs, basicInfo, basicInfoCn);
                    String importantBasicInfoLostMessage = getDataLost(rs, importantBasicInfo, importantBasicInfoCn);
                    String importantBusinessLostMessage = getDataLost(rs, importantBusiness, importantBusinessCn);
                    String contactLostMessage = getDataLost(rs, contact, contactCn);
                    String tzqxMessage = tzqxLessThanOneMonth ? "投资期限<1个月" : "";
                    String tuoguanMessage = hasTuoguan ? "" : "无第三方托管";
                    String fxzbjMessage = (fxzbj != null && fxzbj.trim().equals("有")) ? "" : "无风险准备金";

//                List<String> list = new ArrayList<>();

                    Comparator<String> comparator = new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.length() - o2.length();
                        }
                    };
//                list.sort(comparator);

                    int[] level = new int[warnName.length]; // 红色:3 橙色:2 黄色:1 无预警:0
                    String[] info = new String[level.length];
                    List<TreeSet<String>> infoList = new ArrayList<TreeSet<String>>(level.length);
                    for (int i = 0; i < level.length; i++) {
                        level[i] = 0;
                        info[i] = "";
                        TreeSet t = new TreeSet<>(comparator);
                        infoList.add(t);
                    }

                    System.out.print(rs.getInt("id") + "\t");
                    System.out.print(rs.getString("name") + "\t");
                    System.out.print(rs.getString("company") + "\t");

//                String currInfo = "";

                    int currWarn = 0;
                    //1、基础信息缺失：
                    currWarn = 0;
                    //  黄色预警判断：
                    //  法人代表faren、高管gaoguan、股东及股权占比gqzb、注册机关zcjg、公司类型gslx、工商注册时间gxzcsj、注册资金zzzb、项目类型xmlx信息缺失。
                    if (basicInfoLostMessage.length() > 0) {
                        level[currWarn] = 1;
                        info[currWarn] = basicInfoLostMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(basicInfoLostMessage);
                    }
                    //  橙色预警判断：
                    // (1) 平台名称name、平台网址url、公司名称company、营业执照注册号zhizhao、社会统一信用代码xinyong、组织机构代码jigou、经营范围jyfw、注册地点zcdd、运营地点yydd、ICP备案号beian重要基础信息缺失
                    if (importantBasicInfoLostMessage.length() > 0) {
                        level[currWarn] = 2;
                        info[currWarn] = importantBasicInfoLostMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                    }
                    // (2) 重要经营信息（年收益范围shouyi、公告及发标频率fbpl、投资期限tzqx）缺失，且重要基础信息缺失
                    if (importantBusinessLostMessage.length() > 0 && importantBasicInfoLostMessage.length() > 0) {
                        level[currWarn] = 2;
                        info[currWarn] = importantBusinessLostMessage + "," + importantBasicInfoLostMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(importantBusinessLostMessage);
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                    }
                    // (3) 重要基础信息缺失，年收益范围超过13% shouyi
                    if (importantBasicInfoLostMessage.length() > 0 && shouyiOver13Message.length() > 0) {
                        level[currWarn] = 2;
                        info[currWarn] = importantBasicInfoLostMessage + "," + shouyiOver13Message + " " + info[currWarn];
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                        infoList.get(currWarn).add(shouyiOver13Message);
                    }
                    // (4) 重要基础信息缺失，且存在信用风险（借贷信息不良，包括银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录
                    // 无法判断信用风险
                    // (5) 平台未经过Https安全通道加密传输，且重要基础信息缺失
                    if (!isAqtd && importantBasicInfoLostMessage.length() > 0) {
                        level[currWarn] = 2;
                        info[currWarn] = aqtdMessage + "," + importantBasicInfoLostMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(aqtdMessage);
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);

                    }
                    // (6) 无基本联系方式，重要基础信息缺失、年收益范围超过13%、投资期限<1个月、存在信用风险。
                    // 无法判断信用风险
                    // (7) 无基本联系方式，重要基础信息缺失，且存在信用风险（借贷信息不良，包括银行贷款、小额贷款公司借款等还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息且通过信用中国或工商系统查证有不良信息记录）
                    // 无法判断信用风险

                    // 红色预警判断：
                    // (1) 重要基础信息缺失，年收益范围超过13% shouyi，且投资期限tzqx<1个月。
                    if (importantBasicInfoLostMessage.length() > 0 && shouyiOver13Message.length() > 0 && tzqxLessThanOneMonth) {
                        level[currWarn] = 3;
                        info[currWarn] = importantBasicInfoLostMessage + "," + shouyiOver13Message + "," + tzqxMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                        infoList.get(currWarn).add(shouyiOver13Message);
                        infoList.get(currWarn).add(tzqxMessage);

                    }
                    // (2) 年收益范围超过13% shouyi ，重要基础信息缺失，且无第三方托管tuoguan、无风险准备金fxzbj
                    if (shouyiOver13Message.length() > 0 && importantBasicInfoLostMessage.length() > 0 && tuoguanMessage.length() > 0 && fxzbjMessage.length() > 0) {
                        level[currWarn] = 3;
                        info[currWarn] = shouyiOver13Message + "," + importantBasicInfoLostMessage + "," + tuoguanMessage + "," + fxzbjMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(shouyiOver13Message);
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                        infoList.get(currWarn).add(tuoguanMessage);
                        infoList.get(currWarn).add(fxzbjMessage);

                    }
                    // (3) 年收益范围超过13% shouyi，重要基础信息缺失，保障模式bzms采用除风险备用金及与风险备用金相组合的模式除外，包括：单纯的平台垫付、第三方担保机构、小额贷款公司担保、非融资性担保公司担保
                    if (shouyiOver13Message.length() > 0 && importantBasicInfoLostMessage.length() > 0 && bzmsMessage.length() > 0) {
                        level[currWarn] = 3;
                        info[currWarn] = shouyiOver13Message + "," + importantBasicInfoLostMessage + "," + bzmsMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(shouyiOver13Message);
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                        infoList.get(currWarn).add(bzmsMessage);

                    }
                    // (4) 重要基础信息缺失，年收益范围超过13% shouyi，投资期限<1个月，且保障模式bzms采用除风险备用金及与风险备用金相组合的模式除外，包括：单纯的平台垫付、第三方担保机构、小额贷款公司担保、非融资性担保公司担保
                    if (importantBasicInfoLostMessage.length() > 0 && shouyiOver13Message.length() > 0 && tzqxLessThanOneMonth && bzmsMessage.length() > 0) {
                        level[currWarn] = 3;
                        info[currWarn] = importantBasicInfoLostMessage + "," + shouyiOver13Message + "," + tzqxMessage + "," + bzmsMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                        infoList.get(currWarn).add(shouyiOver13Message);
                        infoList.get(currWarn).add(tzqxMessage);
                        infoList.get(currWarn).add(bzmsMessage);
                    }
                    // (5) 重要基础信息缺失、年收益范围超过13% shouyi、，投资期限<1个月，公司存在信用风险（借贷信息不良，包括从银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录。从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息）；
                    // 无法判断信用风险
                    // (6) 无基本联系方式，重要基础信息缺失，年收益率超过13% shouyi，且投资期限<1个月。
                    if (contactLostMessage.length() > 0 && importantBasicInfoLostMessage.length() > 0 && shouyiOver13Message.length() > 0 && tzqxLessThanOneMonth) {
                        level[currWarn] = 2;
                        info[currWarn] = contactLostMessage + "," + importantBasicInfoLostMessage + "," + shouyiOver13Message + "," + tzqxMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(contactLostMessage);
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                        infoList.get(currWarn).add(shouyiOver13Message);
                        infoList.get(currWarn).add(tzqxMessage);
                    }
                    // (7) 无基本联系方式、重要基础信息缺失、年收益率超过13% shouyi、投资期限<1个月，平台未经过Https安全通道加密传输 aqtd、存在信用风险（借贷信息不良，包括从银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录。从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息），且保障模式bzms采用除风险备用金及与风险备用金相组合的模式除外，包括：单纯的平台垫付、第三方担保机构、小额贷款公司担保、非融资性担保公司担保；
                    // 无法判断信用风险
//                System.out.println(info[currWarn]);


                    //2、经营风险：
                    currWarn = 1;
                    // 黄色预警判断：
                    // （1）年收益范围shouyi、公告及发标频率fbpl、投资期限tzqx信息缺失；
                    if (importantBusinessLostMessage.length() > 0) {
                        level[currWarn] = 1;
                        info[currWarn] = importantBusinessLostMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(importantBusinessLostMessage);
                    }
                    // （2）年收益范围超过13% shouyi。
                    if (shouyiOver13Message.length() > 0) {
                        level[currWarn] = 1;
                        info[currWarn] = shouyiOver13Message + " " + info[currWarn];
                        infoList.get(currWarn).add(shouyiOver13Message);
                    }
                    //  橙色预警判断
                    // （1）重要经营信息（年收益范围shouyi、公告及发标频率fbpl、投资期限tzqx）缺失，且重要基础信息缺失
                    if (importantBusinessLostMessage.length() > 0 && importantBasicInfoLostMessage.length() > 0) {
                        level[currWarn] = 2;
                        info[currWarn] = importantBusinessLostMessage + "," + importantBasicInfoLostMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(importantBusinessLostMessage);
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                    }
                    // （2）重要基础信息缺失，年收益范围超过13% shouyi
                    if (importantBasicInfoLostMessage.length() > 0 && shouyiOver13Message.length() > 0) {
                        level[currWarn] = 2;
                        info[currWarn] = importantBasicInfoLostMessage + "," + shouyiOver13Message + " " + info[currWarn];
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                        infoList.get(currWarn).add(shouyiOver13Message);
                    }
                    // （3）年收益范围超过13% shouyi，保障模式bzms采用除风险备用金及与风险备用金相组合的模式除外，包括：单纯的平台垫付、第三方担保机构、小额贷款公司担保、非融资性担保公司担保。
                    if (shouyiOver13Message.length() > 0 && bzmsMessage.length() > 0) {
                        level[currWarn] = 2;
                        info[currWarn] = shouyiOver13Message + "," + bzmsMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(shouyiOver13Message);
                        infoList.get(currWarn).add(bzmsMessage);
                    }
                    // （4）平台未经过Https安全通道加密传输aqtd，年收益范围超过13%
                    if (aqtdMessage.length() > 0 && shouyiOver13Message.length() > 0) {
                        level[currWarn] = 2;
                        info[currWarn] = aqtdMessage + "," + shouyiOver13Message + " " + info[currWarn];
                        infoList.get(currWarn).add(aqtdMessage);
                        infoList.get(currWarn).add(shouyiOver13Message);
                    }
                    // （5）无基本联系方式，重要基础信息缺失、年收益范围超过13%、投资期限<1个月、存在信用风险。
                    // 无法判断信用风险

                    // 红色预警判断：
                    // (1) 年收益范围超过13% shouyi，且投资期限<1个月。
                    if (shouyiOver13Message.length() > 0 && tzqxLessThanOneMonth) {
                        level[currWarn] = 3;
                        info[currWarn] = shouyiOver13Message + "," + tzqxMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(shouyiOver13Message);
                        infoList.get(currWarn).add(tzqxMessage);
                    }
                    // (2) 重要基础信息缺失，年收益范围超过13% shouyi，且投资期限<1个月。
                    if (importantBasicInfoLostMessage.length() > 0 && shouyiOver13Message.length() > 0 && tzqxLessThanOneMonth) {
                        level[currWarn] = 3;
                        info[currWarn] = importantBasicInfoLostMessage + "," + shouyiOver13Message + "," + tzqxMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                        infoList.get(currWarn).add(shouyiOver13Message);
                        infoList.get(currWarn).add(tzqxMessage);
                    }
                    // (3) 年收益范围超过13% shouyi ，重要基础信息缺失，且无第三方托管tuoguan、无风险准备金fxzbj
                    if (shouyiOver13Message.length() > 0 && importantBasicInfoLostMessage.length() > 0 && tuoguanMessage.length() > 0 && fxzbjMessage.length() > 0) {
                        level[currWarn] = 3;
                        info[currWarn] = shouyiOver13Message + "," + importantBasicInfoLostMessage + "," + tuoguanMessage + "," + fxzbjMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(shouyiOver13Message);
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                        infoList.get(currWarn).add(tuoguanMessage);
                        infoList.get(currWarn).add(fxzbjMessage);
                    }
                    // (4) 年收益范围超过13% shouyi，重要基础信息缺失，保障模式bzms采用除风险备用金及与风险备用金相组合的模式除外，包括：单纯的平台垫付、第三方担保机构、小额贷款公司担保、非融资性担保公司担保
                    if (shouyiOver13Message.length() > 0 && importantBasicInfoLostMessage.length() > 0 && bzmsMessage.length() > 0) {
                        level[currWarn] = 3;
                        info[currWarn] = shouyiOver13Message + "," + importantBasicInfoLostMessage + "," + bzmsMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(shouyiOver13Message);
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                        infoList.get(currWarn).add(bzmsMessage);
                    }
                    // (5) 重要基础信息缺失，年收益范围超过13% shouyi，投资期限<1个月，且保障模式bzms采用除风险备用金及与风险备用金相组合的模式除外，包括：单纯的平台垫付、第三方担保机构、小额贷款公司担保、非融资性担保公司担保
                    if (importantBasicInfoLostMessage.length() > 0 && shouyiOver13Message.length() > 0 && tzqxLessThanOneMonth && bzmsMessage.length() > 0) {
                        level[currWarn] = 3;
                        info[currWarn] = importantBasicInfoLostMessage + "," + shouyiOver13Message + "," + tzqxMessage + "," + bzmsMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                        infoList.get(currWarn).add(shouyiOver13Message);
                        infoList.get(currWarn).add(tzqxMessage);
                        infoList.get(currWarn).add(bzmsMessage);
                    }
                    // (6) 年收益范围超过13% shouyi，公司存在信用风险（借贷信息不良，包括从银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录。从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息。
                    // 无法判断信用风险
                    // (7) 公司存在信用风险（借贷信息不良，包括从银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录。从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息），年收益范围超过13%  shouyi 且投资期限<1个月。
                    // 无法判断信用风险
                    // (8) 公司存在信用风险（借贷信息不良，包括从银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录。从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息），年收益范围超过13% shouyi，保障模式bzms采用除风险备用金及与风险备用金相组合的模式除外，包括：单纯的平台垫付、第三方担保机构、小额贷款公司担保、非融资性担保公司担保
                    // 无法判断信用风险
                    // (9) 重要基础信息缺失、年收益范围超过13% shouyi、，投资期限<1个月，公司存在信用风险（借贷信息不良，包括从银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录。从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息）；
                    // 无法判断信用风险
                    // (10) 无基本联系方式，重要基础信息缺失，年收益率超过13% shouyi，且投资期限<1个月。
                    if (contactLostMessage.length() > 0 && importantBasicInfoLostMessage.length() > 0 && shouyiOver13Message.length() > 0 && tzqxLessThanOneMonth) {
                        level[currWarn] = 3;
                        info[currWarn] = contactLostMessage + "," + importantBasicInfoLostMessage + "," + shouyiOver13Message + "," + tzqxMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(contactLostMessage);
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                        infoList.get(currWarn).add(shouyiOver13Message);
                        infoList.get(currWarn).add(tzqxMessage);
                    }
                    // (11) 平台未经过Https安全通道加密传输aqtd，年收益范围超过13% shouyi，且投资期限<1个月。
                    if (aqtdMessage.length() > 0 && shouyiOver13Message.length() > 0 && tzqxLessThanOneMonth) {
                        level[currWarn] = 3;
                        info[currWarn] = aqtdMessage + "," + shouyiOver13Message + "," + tzqxMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(aqtdMessage);
                        infoList.get(currWarn).add(shouyiOver13Message);
                        infoList.get(currWarn).add(tzqxMessage);
                    }
                    // (12) 无基本联系方式、重要基础信息缺失、年收益率超过13% shouyi、投资期限<1个月，平台未经过Https安全通道加密传输 aqtd、存在信用风险（借贷信息不良，包括从银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录。从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息），且保障模式bzms采用除风险备用金及与风险备用金相组合的模式除外，包括：单纯的平台垫付、第三方担保机构、小额贷款公司担保、非融资性担保公司担保；
                    // 无法判断信用风险
//                System.out.println(info[currWarn]);


                    // 3、信用风险
                    currWarn = 2;
                    //  黄色预警判断：
                    //  (1) 借贷信息不良，包括银行贷款、小额贷款公司借款等还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息。
                    //  (2) 通过信用中国或工商系统查证有不良信息记录。从信用中国系统 http://www.creditchina.gov.cn/home 信用信息下获取；从国家企业信息信息公示系统 http://www.gsxt.gov.cn/index.html 获取信用信息。

                    //  橙色预警判断
                    // （1）重要基础信息缺失，且存在信用风险（借贷信息不良，包括银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录
                    // （2）无基本联系方式，重要基础信息缺失、年收益范围超过13%、投资期限<1个月、存在信用风险。
                    // （3）无基本联系方式，重要基础信息缺失，且存在信用风险（借贷信息不良，包括银行贷款、小额贷款公司借款等还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息且通过信用中国或工商系统查证有不良信息记录）

                    // 红色预警判断：
                    // (1) "借贷信息不良，包括银行贷款、小额贷款公司借款等还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息且通过信用中国或工商系统查证有不良信息记录。 从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息。 "
                    // (2) 年收益范围超过13% shouyi，公司存在信用风险（借贷信息不良，包括从银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录。从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息。
                    // (3) 公司存在信用风险（借贷信息不良，包括从银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录。从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息），年收益范围超过13%  shouyi 且投资期限<1个月。
                    // (4) 公司存在信用风险（借贷信息不良，包括从银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录。从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息），年收益范围超过13% shouyi，保障模式bzms采用除风险备用金及与风险备用金相组合的模式除外，包括：单纯的平台垫付、第三方担保机构、小额贷款公司担保、非融资性担保公司担保
                    // (5) 重要基础信息缺失、年收益范围超过13% shouyi、，投资期限<1个月，公司存在信用风险（借贷信息不良，包括从银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录。从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息）；
                    // (6) 无基本联系方式、重要基础信息缺失、年收益率超过13% shouyi、投资期限<1个月，平台未经过Https安全通道加密传输 aqtd、存在信用风险（借贷信息不良，包括从银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录。从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息），且保障模式bzms采用除风险备用金及与风险备用金相组合的模式除外，包括：单纯的平台垫付、第三方担保机构、小额贷款公司担保、非融资性担保公司担保；
//                System.out.println(info[currWarn]);

                    // 4、操作风险
                    currWarn = 3;
                    //  黄色预警判断：
                    // (1) 平台未经过Https安全通道加密传输aqtd。
                    if (!isAqtd) {
                        level[currWarn] = 1;
                        info[currWarn] = aqtdMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(aqtdMessage);
                    }
                    //  橙色预警判断
                    // （1）平台未经过Https安全通道加密传输aqtd，且重要基础信息缺失
                    if (!isAqtd && importantBasicInfoLostMessage.length() > 0) {
                        level[currWarn] = 2;
                        info[currWarn] = aqtdMessage + "," + importantBasicInfoLostMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(aqtdMessage);
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                    }
                    // （2）平台未经过Https安全通道加密传输aqtd，年收益范围超过13%
                    if (aqtdMessage.length() > 0 && shouyiOver13Message.length() > 0) {
                        level[currWarn] = 2;
                        info[currWarn] = aqtdMessage + "," + shouyiOver13Message + " " + info[currWarn];
                        infoList.get(currWarn).add(aqtdMessage);
                        infoList.get(currWarn).add(shouyiOver13Message);
                    }
                    // 红色预警判断：
                    // (1) 平台未经过Https安全通道加密传输aqtd，年收益范围超过13% shouyi，且投资期限<1个月。
                    if (aqtdMessage.length() > 0 && shouyiOver13Message.length() > 0 && tzqxLessThanOneMonth) {
                        level[currWarn] = 3;
                        info[currWarn] = aqtdMessage + "," + shouyiOver13Message + "," + tzqxMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(aqtdMessage);
                        infoList.get(currWarn).add(shouyiOver13Message);
                        infoList.get(currWarn).add(tzqxMessage);
                    }
                    // (2) 无基本联系方式、重要基础信息缺失、年收益率超过13% shouyi、投资期限<1个月，平台未经过Https安全通道加密传输 aqtd、存在信用风险（借贷信息不良，包括从银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录。从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息），且保障模式bzms采用除风险备用金及与风险备用金相组合的模式除外，包括：单纯的平台垫付、第三方担保机构、小额贷款公司担保、非融资性担保公司担保；
                    // 无法判断信用风险
//                System.out.println(info[currWarn]);


                    // 5、联系方式缺失
                    currWarn = 4;
                    //  黄色预警判断：
                    //  (1) 官方QQ qq、官方电话gfdh、官方QQ群qqqun、微信公众号wxpt信息缺失。
                    if (contactLostMessage.length() > 0) {
                        level[currWarn] = 1;
                        info[currWarn] = contactLostMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(contactLostMessage);
                    }
                    //  橙色预警判断：
                    // (1) 无基本联系方式，重要基础信息缺失，且存在信用风险（借贷信息不良，包括银行贷款、小额贷款公司借款等还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息且通过信用中国或工商系统查证有不良信息记录）

                    // 红色预警判断：
                    // (1) 无基本联系方式，重要基础信息缺失，年收益率超过13% shouyi，且投资期限<1个月。
                    if (contactLostMessage.length() > 0 && importantBasicInfoLostMessage.length() > 0 && shouyiOver13Message.length() > 0 && tzqxLessThanOneMonth) {
                        level[currWarn] = 3;
                        info[currWarn] = contactLostMessage + "," + importantBasicInfoLostMessage + "," + shouyiOver13Message + "," + tzqxMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(contactLostMessage);
                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                        infoList.get(currWarn).add(shouyiOver13Message);
                        infoList.get(currWarn).add(tzqxMessage);
                    }
                    // (2) 无基本联系方式、重要基础信息缺失、年收益率超过13% shouyi、投资期限<1个月，平台未经过Https安全通道加密传输 aqtd、存在信用风险（借贷信息不良，包括从银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录。从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息），且保障模式bzms采用除风险备用金及与风险备用金相组合的模式除外，包括：单纯的平台垫付、第三方担保机构、小额贷款公司担保、非融资性担保公司担保；
                    // 无法判断信用风险
//                System.out.println(info[currWarn]);


                    // 6、舆论风险
                    currWarn = 5;
                    //  黄色预警判断：
                    //  (1) 关键词匹配：网站关闭、网站打不开、网站不能访问、网站进不去。
                    //  橙色预警判断：

                    // 红色预警判断：
                    // (1) 关键词匹配：提现困难、失联、跑路
//                System.out.println(info[currWarn]);

                    System.out.println();

                    String prewarnLevel = "";
                    String prewarnInfo = "";

                    for (int i = 0; i < info.length; i++) {
                        System.out.println("\t" + warnName[i] + ": " + levelName[level[i]]);
                        prewarnLevel = prewarnLevel + level[i];
                        if (i < info.length - 1) {
                            prewarnLevel = prewarnLevel + "|";
                        }
                        if (level[i] > 0) {
                            System.out.println("\t\t预警内容: ");
                            TreeSet t = infoList.get(i);

                            String currInfo = "";
                            for (String s : infoList.get(i)) {
                                System.out.println("\t\t\t" + s);
                                currInfo = currInfo + " " + s;
                            }
                            prewarnInfo = prewarnInfo + currInfo.trim();
//
//                        String[] currentInfo = info[i].split(" ");
//                        for (int j = 0; j < currentInfo.length; j++) {
//                            System.out.println("\t\t\t(" + (j + 1) + ") " + currentInfo[j]);
//                        }
                        }
                        if (i < info.length - 1) {
                            prewarnInfo = prewarnInfo + "|";
                        }
                    }

                    System.out.println(prewarnLevel);
                    System.out.println(prewarnInfo);

                    //修改数据的代码
                    String sql2 = "UPDATE company SET prewarn_level=? , prewarn_info=? WHERE id=? ";
                    PreparedStatement pst = conn.prepareStatement(sql2);
                    pst.setString(1, prewarnLevel);
                    pst.setString(2, prewarnInfo);
                    pst.setInt(3, id);
                    pst.executeUpdate();

                }

//            //修改数据的代码
//            String sql2 = "update company set name=? where number=?";
//            PreparedStatement pst = conn.prepareStatement(sql2);
//            pst.setString(1,"8888");
//            pst.setInt(2,198);
//            pst.executeUpdate();

//            //删除数据的代码
//            String sql3 = "delete from stu where number=?";
//            pst = conn.prepareStatement(sql3);
//            pst.setInt(1,701);
//            pst.executeUpdate();

//            ResultSet rs2 = stmt.executeQuery(sql);//创建数据对象
//            System.out.println("编号"+"\t"+"姓名"+"\t"+"年龄");
//            while (rs2.next()){
//                System.out.print(rs2.getInt(1) + "\t");
//                System.out.print(rs2.getString(2) + "\t");
//                System.out.print(rs2.getInt(3) + "\t");
//                System.out.println();
//            }

                rs.close();

                totalCount += currCount;
            } while (countPerTime == currCount);
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long endTime=System.currentTimeMillis();
        System.out.println("本次计算条数：\t\t" + totalCount);
        System.out.println("本次运行时间：\t\t" + ( endTime - startTime ) + "ms");

    }

    /**
     * 获取信息缺失详情
     *
     * @param rs     表数据
     * @param keys   字段名
     * @param values 中文名
     * @return 中文详情
     */
    public static String getDataLost(ResultSet rs, String[] keys, String[] values) {
        String currInfo = "";
        for (int i = 0; i < keys.length; i++) {
            String curr = "";
            try {
                curr = rs.getString(keys[i]);
            } catch (SQLException e) {
                e.printStackTrace();
                continue;
            }
            if (null == curr || "".equals(curr)) {
                currInfo = currInfo + (values[i] + "缺失 ");
            }
        }
        return currInfo.trim().replaceAll(" ", "、");
    }

    public static boolean containsKeylist(String content, String[] keylist) {
        if (content == null || keylist == null) {
            return false;
        }
        for (int i = 0; i < keylist.length; i++) {
            if (content.contains(keylist[i])) {
                return true;
            }
        }
        return false;
    }
}
