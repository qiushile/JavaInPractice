package com.tecnova;

import com.kiscod.algorithm.CosineSimilarAlgorithm;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class PrewarningCompany2 {

    public static String[] warnName = new String[]{"资质风险", "经营风险", "信用风险", "舆论风险"};
    public static String[] levelName = new String[]{"正常", "黄色预警", "橙色预警", "红色预警"};

    // 资质风险： 未公示
    // 1、平台网址 url 2、高管 gaoguan 3、经营范围 jyfw 4、注册资本 zzzb 5、注册地点 zcdd
    public static String[] zizhi = new String[]{"url", "gaoguan", "jyfw", "zzzb", "zcdd"};
    public static String[] zizhiCn = new String[]{"平台网址", "高管", "经营范围", "注册资本", "注册地点"};

    // 股东+
    public static String[] gudong = new String[]{"gdlx", "gqzb"};
    public static String[] gudongCn = new String[]{"股东类型", "股权占比"};

    // 营业执照注册号与统一社会信用代码与组织机构代码
    public static String[] registerCode = new String[]{"zhizhao", "xinyong", "jigou"};
    public static String[] registerCodeCn = new String[]{"营业执照注册号", "统一社会信用代码", "组织机构代码"};


    // 电话 QQ
    public static String[] dhqq = new String[]{"gfdh", "qq"};
    public static String[] dhqqCn = new String[]{"官方电话", "官方QQ"};


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
//                String sql = "SELECT * FROM company LIMIT 100 ";    //要执行的SQL
                String sql = "SELECT * FROM company WHERE prewarn_level IS NULL OR prewarn_leveldetail IS NULL OR prewarn_info IS NULL OR warn_level IS NULL LIMIT 100 ";    //要执行的SQL
                ResultSet rs = stmt.executeQuery(sql);//创建数据对象
                System.out.println("编号" + "\t" + "平台名称" + "\t" + "公司名称");
                currCount = 0;
                while (rs.next()) {
                    currCount++;
                    int id = rs.getInt("id");
                    String faren = rs.getString("faren");
                    String icpBeian = rs.getString("beian");
                    Integer fznumber = rs.getInt("fznumber");
                    if (fznumber == null) {fznumber = -1;}
                    String zdtb = rs.getString("zdtb");
                    String beijing = rs.getString("beijing");
                    String renzheng = rs.getString("renzheng");
                    String shouyi = rs.getString("shouyi");
                    String aqtd = rs.getString("aqtd");
                    String bzms = rs.getString("bzms");
                    String tzqx = rs.getString("tzqx");
                    String tuoguan = rs.getString("tuoguan");
                    String fxzbj = rs.getString("fxzbj");
                    String sfss = rs.getString("sfss");
                    String zcdd = rs.getString("zcdd");
                    String yydd = rs.getString("yydd");
                    String ftrz = rs.getString("ftrz");


                    boolean isP2P = true;
                    boolean isFznumberOver5 = (fznumber != null) && fznumber >= 5;
                    boolean isZdtb = (zdtb != null && zdtb.trim().equals("支持"));
                    boolean isSiqi = (beijing != null && beijing.trim().startsWith("私营"));
                    boolean isBaiduV = (renzheng != null && renzheng.trim().contains("-"));
                    boolean hasFxzbj = (fxzbj != null && fxzbj.trim().equals("有"));
                    boolean hasSs = (sfss != null && sfss.trim().equals("是"));
                    boolean hasFtrz = !(ftrz == null || ftrz.contains("未") || ftrz.contains("无") || ftrz.contains("否") || ftrz.contains("不"));


                    // 是否使用安全通道： , 使用 使用中 已使用 已认证 找不到 未使用 未使用? 未查到 未认证 查不到
                    boolean isAqtd = (aqtd != null && !(aqtd.startsWith("未") || aqtd.endsWith("不到")));

                    boolean tzqxLessThanOneMonth = (tzqx != null && (tzqx.contains("天标") || tzqx.contains("秒标")));
                    boolean hasTuoguan = !(tuoguan == null || tuoguan.contains("未") || tuoguan.contains("无") || tuoguan.contains("否") || tuoguan.contains("不"));
                    boolean hasCreditRiskWebInfo = false; // TODO 信用风险， 判断 信用中国 网站查询
                    boolean hasCreditRiskMatchWords = false; // TODO 信用风险， 判断 关键字匹配库中数据
                    boolean hasCreditRisk = hasCreditRiskWebInfo || hasCreditRiskMatchWords; // 信用风险
                    boolean hasWebsiteClosed = false; // TODO 关键词匹配：网站关闭、网站打不开、网站不能访问、网站进不去。
                    boolean hasMatchWordsBroken = false; // TODO 关键词匹配：提现困难、失联、跑路。

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

                    String zizhiMessage = getDataLost(rs, zizhi, zizhiCn);
                    String gudongMessage = "股东类型与股东及股权占比同时未公示";
                    String registerCodeMessage = "营业执照注册号与统一社会信用代码与组织机构代码同时未公示";

                    String shouyiOver13Message = (maxShouyi > 13) ? "年收益范围超过13%" : "";
                    String aqtdMessage = isAqtd ? "" : "平台未经过Https安全通道加密传输";
                    String bzmsMessage = containsKeylist(bzms, reservFund) ? "" : "保障模式采用除风险备用金及与风险备用金相组合的模式除外，包括：单纯的平台垫付、第三方担保机构、小额贷款公司担保、非融资性担保公司担保";
                    String basicInfoLostMessage = getDataLost(rs, basicInfo, basicInfoCn);
                    String importantBasicInfoLostMessage = getDataLost(rs, importantBasicInfo, importantBasicInfoCn);
                    String importantBusinessLostMessage = getDataLost(rs, importantBusiness, importantBusinessCn);
                    String contactLostMessage = getDataLost(rs, contact, contactCn);
                    String tzqxMessage = tzqxLessThanOneMonth ? "投资期限<1个月" : "";
                    String tuoguanMessage = hasTuoguan ? "" : "无第三方托管";
                    String fxzbjMessage = (fxzbj != null && fxzbj.trim().equals("有")) ? "" : "无风险准备金";
                    String creditRiskMessage = hasCreditRisk? "信用风险（借贷信息不良，包括从银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录。从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息）": "";
                    String creditRiskWebInfoMessage = hasCreditRiskWebInfo? "通过信用中国或工商系统查证有不良信息记录。从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息": "";
                    String creditRiskMatchWordsMessage = hasCreditRiskMatchWords? "借贷信息不良，包括从银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息": "";
                    String websiteClosedMessage = "关键词匹配：网站关闭、网站打不开、网站不能访问、网站进不去。";
                    String matchWordsBrokenMessage = "关键词匹配：提现困难、失联、跑路";
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
                    //1、资质风险：
                    currWarn = 0;
                    //  黄色预警判断：1 2 3 4 5 9 10
//                     1、平台网址未公示（注：若不是p2p企业，则不预警）
//                    2、高管未公示（注：若不是p2p企业，则不预警）
//                    3、经营范围未公示（注：若不是p2p企业，则不预警）
//                    4、注册资本未公示（注：若不是p2p企业，则不预警）
//                    5、注册地点未公示（注：若不是p2p企业，则不预警）
                    if (isP2P && zizhiMessage.length() > 0) {
                        level[currWarn] = 1;
                        info[currWarn] = zizhiMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(zizhiMessage);
                    }

//                    9、股东类型与股东及股权占比同时未公示（注：若不是p2p企业，则不预警）
                    if (isP2P && isAllDataLost(rs, gudong)) {
                        level[currWarn] = 1;
                        info[currWarn] = gudongMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(gudongMessage);
                    }

//                    10、营业执照注册号与统一社会信用代码与组织机构代码同时未公示
                    if (isAllDataLost(rs, registerCode)) {
                        level[currWarn] = 1;
                        info[currWarn] = registerCodeMessage + " " + info[currWarn];
                        infoList.get(currWarn).add(registerCodeMessage);
                    }

                    //  橙色预警判断： 10
//                    10、经营地址一年更换2次
//                              法人代表一年内更新2次
//                            股东一年内更新2次
//                    // (1) 平台名称name、平台网址url、公司名称company、营业执照注册号zhizhao、社会统一信用代码xinyong、组织机构代码jigou、经营范围jyfw、注册地点zcdd、运营地点yydd、ICP备案号beian重要基础信息缺失
//                    if (importantBasicInfoLostMessage.length() > 0) {
//                        level[currWarn] = 2;
//                        info[currWarn] = importantBasicInfoLostMessage + " " + info[currWarn];
//                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
//                    }

                    // 红色预警判断：3 5 9 12 13 18

                    if (isP2P && (null == faren || "".equals(faren))) {
                        level[currWarn] = 3;
                        info[currWarn] = "法人代表未公示" + " " + info[currWarn];
                        infoList.get(currWarn).add("法人代表未公示");
                    }

                    if (null == icpBeian || "".equals(icpBeian)) {
                        level[currWarn] = 3;
                        info[currWarn] = "ICP备案号未公" + " " + info[currWarn];
                        infoList.get(currWarn).add("ICP备案号未公");
                    }

                    if (isAllDataLost(rs, dhqq)) {
                        level[currWarn] = 3;
                        info[currWarn] = "官方电话及官方QQ同时未公示" + " " + info[currWarn];
                        infoList.get(currWarn).add("官方电话及官方QQ同时未公示");
                    }


//                System.out.println(info[currWarn]);


                    //2、经营风险：
                    currWarn = 1;
                    // 黄色预警判断： 7 11 12 13 14 15
//                    8、分支机构超过5家
//                   11、支持自动投标同时平台背景为私企
//                    12、支持自动投标同时无保障模式
//                    13、支持自动投标同时无HTTPS安全通道
//                    14、百度+V未认证同时没有HTTPS安全通道
//                    15、无保障模式同时无HTTPS安全通道

                    if (isFznumberOver5) {
                        level[currWarn] = 1;
                        info[currWarn] = "分支机构超过5家" + " " + info[currWarn];
                        infoList.get(currWarn).add("分支机构超过5家");
                    }

                    if (isZdtb && isSiqi) {
                        level[currWarn] = 1;
                        info[currWarn] = "支持自动投标同时平台背景为私企" + " " + info[currWarn];
                        infoList.get(currWarn).add("支持自动投标同时平台背景为私企");
                    }

                    if (isZdtb && (bzms == null || bzms.trim().length() == 0)) {
                        level[currWarn] = 1;
                        info[currWarn] = "支持自动投标同时无保障模式" + " " + info[currWarn];
                        infoList.get(currWarn).add("支持自动投标同时无保障模式");
                    }

                    if (isZdtb && !isAqtd) {
                        level[currWarn] = 1;
                        info[currWarn] = "支持自动投标同时无HTTPS安全通道" + " " + info[currWarn];
                        infoList.get(currWarn).add("支持自动投标同时无HTTPS安全通道");
                    }

                    if (isBaiduV && !isAqtd) {
                        level[currWarn] = 1;
                        info[currWarn] = "百度+V未认证同时没有HTTPS安全通道" + " " + info[currWarn];
                        infoList.get(currWarn).add("百度+V未认证同时没有HTTPS安全通道");
                    }

                    if ((bzms == null || bzms.trim().length() == 0) && !isAqtd) {
                        level[currWarn] = 1;
                        info[currWarn] = "无保障模式同时无HTTPS安全通道" + " " + info[currWarn];
                        infoList.get(currWarn).add("无保障模式同时无HTTPS安全通道");
                    }

                    //  橙色预警判断： 2 4 5 6 7 8 9 11 12 13 14 15
//                    2、年收益范围超过13%不超过15%
                    if (maxShouyi > 13 && maxShouyi <= 15) {
                        level[currWarn] = 2;
                        info[currWarn] = "年收益范围超过13%不超过15%" + " " + info[currWarn];
                        infoList.get(currWarn).add("年收益范围超过13%不超过15%");
                    }

                    if (tzqxLessThanOneMonth) {
                        level[currWarn] = 2;
                        info[currWarn] = "投资期限小于一个月" + " " + info[currWarn];
                        infoList.get(currWarn).add("投资期限小于一个月");
                    }

                    if (!hasTuoguan) {
                        level[currWarn] = 2;
                        info[currWarn] = "无第三方托管" + " " + info[currWarn];
                        infoList.get(currWarn).add("无第三方托管");
                    }

                    if (!hasFxzbj) {
                        level[currWarn] = 2;
                        info[currWarn] = "无风险准备金" + " " + info[currWarn];
                        infoList.get(currWarn).add("无风险准备金");
                    }

                    if (!hasSs && isFznumberOver5) {
                        level[currWarn] = 2;
                        info[currWarn] = "不上市同时分支机构超过5家" + " " + info[currWarn];
                        infoList.get(currWarn).add("不上市同时分支机构超过5家");
                    }



                    if (!hasTuoguan && isFznumberOver5) {
                        level[currWarn] = 2;
                        info[currWarn] = "无风投融资同时分支机构超过5家" + " " + info[currWarn];
                        infoList.get(currWarn).add("无风投融资同时分支机构超过5家");
                    }



                    if (!isAqtd && isFznumberOver5) {
                        level[currWarn] = 2;
                        info[currWarn] = "无HTTPS安全通道同时分支机构超过5家" + " " + info[currWarn];
                        infoList.get(currWarn).add("无HTTPS安全通道同时分支机构超过5家");
                    }


                    // 红色预警判断： 8 10 11 14 15 16 17 20

                    if (maxShouyi > 15) {
                        level[currWarn] = 3;
                        info[currWarn] = "年收益范围大于15%" + info[currWarn];
                        infoList.get(currWarn).add("年收益范围大于15%");
                    }

//                System.out.println(info[currWarn]);


                    // 3、信用风险
                    currWarn = 2;
                    //  黄色预警判断： 7
//                    7、运营地点和注册地点相似度为95%以下
                    if (zcdd != null && yydd != null && !zcdd.trim().equals(yydd.trim()) && CosineSimilarAlgorithm.getSimilarity(zcdd, yydd) < 0.95) {
                        level[currWarn] = 1;
                        info[currWarn] = "运营地点和注册地点相似度为95%以下" + " " + info[currWarn];
                        infoList.get(currWarn).add("运营地点和注册地点相似度为95%以下");
                    }

                    //  橙色预警判断： 8
                    // （1）重要基础信息缺失，且存在信用风险（借贷信息不良，包括银行贷款、小额贷款公司借款还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息或通过信用中国或工商系统查证有不良信息记录
                    if (!hasFtrz && isFznumberOver5) {
                        level[currWarn] = 2;
                        info[currWarn] = "无风投融资同时分支机构超过5家" + " " + info[currWarn];
//                        infoList.get(currWarn).add(importantBasicInfoLostMessage);
                        infoList.get(currWarn).add("无风投融资同时分支机构超过5家");
                    }

                    // 红色预警判断： 19 21 22 23 24 25 26 27
                    // (1) "借贷信息不良，包括银行贷款、小额贷款公司借款等还款不及时或无力还款，通过文章匹配关键字：无力还款、信用不良、无力支付本息且通过信用中国或工商系统查证有不良信息记录。 从信用中国系统http://www.creditchina.gov.cn/home信用信息下获取；从国家企业信息信息公示系统http://www.gsxt.gov.cn/index.html获取信用信息。 "
//                    if (hasCreditRisk) {
//                        level[currWarn] = 3;
//                        info[currWarn] = creditRiskMessage + " " + info[currWarn];
//                        infoList.get(currWarn).add(creditRiskMessage);
//                    }

//                System.out.println(info[currWarn]);


                    // 4、舆论风险
                    currWarn = 5;
                    //  黄色预警判断：
                    //  (1) 关键词匹配：网站关闭、网站打不开、网站不能访问、网站进不去。
//                    if (hasWebsiteClosed) {
//                        level[currWarn] = 1;
//                        info[currWarn] = websiteClosedMessage + " " + info[currWarn];
//                        infoList.get(currWarn).add(websiteClosedMessage);
//                    }
                    //  橙色预警判断： 17
//                    if (hasMatchWordsBroken) {
//                        level[currWarn] = 2;
//                        info[currWarn] = matchWordsBrokenMessage + " " + info[currWarn];
//                        infoList.get(currWarn).add(matchWordsBrokenMessage);
//                    }

                    // 红色预警判断：  28
                    // (1) 关键词匹配：提现困难、失联、跑路
//                    if (hasMatchWordsBroken) {
//                        level[currWarn] = 3;
//                        info[currWarn] = matchWordsBrokenMessage + " " + info[currWarn];
//                        infoList.get(currWarn).add(matchWordsBrokenMessage);
//                    }
//                System.out.println(info[currWarn]);

                    System.out.println();

                    int prewarnLevel = 0; // top level
                    int levelsum = 0;
                    String prewarnLevelDetail = "";
                    String prewarnInfo = "";

                    for (int i = 0; i < info.length; i++) {
                        System.out.println("\t" + warnName[i] + ": " + levelName[level[i]]);
                        if (prewarnLevel < level[i]) {
                            prewarnLevel = level[i];
                        }
                        levelsum += level[i];
                        prewarnLevelDetail = prewarnLevelDetail + level[i];
                        if (i < info.length - 1) {
                            prewarnLevelDetail = prewarnLevelDetail + "|";
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

                    // 出现两项及以上黄色预警即为橙色预警
                    // 出现一个黄色预警及以上和一个橙色预警为红色预警
                    // 出现两个橙色预警及以上为红色预警
                    if (prewarnLevel < 3 && prewarnLevel < levelsum) {
                        prewarnLevel ++;
                    }

                    //todo 前端改完要删掉
                    prewarnLevelDetail = prewarnLevelDetail + "|0|0";
                    prewarnInfo = prewarnInfo + "||";

                    System.out.println(prewarnLevel);
                    System.out.println(prewarnLevelDetail);
                    System.out.println(prewarnInfo);

                    //修改数据的代码
                    String sql2 = "UPDATE company SET prewarn_leveldetail=? , prewarn_info=?, prewarn_level=?, warn_level=? WHERE id=? ";
                    PreparedStatement pst = conn.prepareStatement(sql2);
                    pst.setString(1, prewarnLevelDetail);
                    pst.setString(2, prewarnInfo);
                    pst.setInt(3, prewarnLevel);
                    pst.setInt(4, prewarnLevel);
                    pst.setInt(5, id);
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
                currInfo = currInfo + (values[i] + "未公示 ");
            }
        }
        return currInfo.trim().replaceAll(" ", "、");
    }

    public static boolean isAllDataLost(ResultSet rs, String[] keys) {
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
//                currInfo = currInfo + (values[i] + "未公示 ");
            } else {
                return false;
            }
        }
        return true;
    }

        public static boolean containsKeylist(String content, String[] keylist) {
        if (content != null && keylist != null) {
            for (int i = 0; i < keylist.length; i++) {
                if (content.contains(keylist[i])) {
                    return true;
                }
            }
        }
        return false;
    }
}
