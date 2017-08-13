package com.kiscod.algorithm;

import com.whos.sa.analysis.Analysis;

/**
 * Created by lance on 2017/5/15.
 * mmseg4j 分词 zg-sa-c1.8 情感分析 encache dom4j
 */
public class Semantic {
    public static void main(String[] args) {
        Analysis analysis = new Analysis();
        System.out.println("1: " + analysis.parse("太倒霉了").getCode());
        System.out.println("1: " + analysis.parse("这是一个普通评论").getCode());
        System.out.println("2: " + analysis.parse("中国加油!").getCode());
        System.out.println("2: " + analysis.parse("赵翔").getCode());
        System.out.println("2: " + analysis.parse("新西兰向我国返还罚没闫永明犯罪所得1.3亿元").getCode());
        System.out.println("2: " + analysis.parse("2016年初，在与新西兰警方联合调查闫永明犯罪所得，并积极准备提请新西兰引渡闫永明的同时，中国警方开始对闫永明开展劝返。在法律震慑和政策感召下，闫永明最后选择退还巨额赃款、缴纳巨额罚金，并接受中新两国法律审判。2016年11月12日，闫永明回到中国投案自首。\n" +
                "2016年12月22日，经依法公开审判，吉林省通化市中级人民法院判决闫永明犯职务侵占罪，判处有期徒刑3年，缓刑3年，罚没犯罪所得收益。\n" +
                "鉴于闫永明回国投案前，新西兰警方已向法院指控其涉嫌洗钱犯罪，应新西兰警方请求，2017年1月12日，中国警方将闫永明移交新西兰警方，由新西兰法院继续对其在新西兰涉嫌洗钱罪进行审判。").getCode());

        String[] test = ("海外网4月11日 电据英国广播公司（BBC）消息，当地时间4月8日下午，来自中国的一家三口于伦敦北部Islington地区遭泼酸性物质，造成男性受害者脸部大面积“无法复原性腐蚀”，妻子及2岁的儿子不同程度受伤。\n" +
                "截至目前，嫌犯仍在逃，因此警方表示，不能确定此案为针对个人的案件，还是随机迫害路人案件。但一位自称认识受害者超过20年的女性称，这是一次有针对性的报复行为。\n" +
                "据当地媒体报道，受害者一家是长期居住在英国的中国家庭，丈夫40岁，妻子36岁，育有两子。事发当日下午，11岁的长子在家并未外出。夫妇俩使用婴儿车推着2岁的弟弟走在离家不远的哥本哈根路上，突然遭人当街泼酸攻击。\n" +
                "据目击者称，嫌犯作案后随即逃离现场，40岁的男子看起来是主要攻击目标，一旁的妻子和2岁的孩子受到了不同程度的腐蚀伤害。\n" +
                "事发地点附近43岁的店主穆拉特称，当时有位女士慌忙冲进店里说，有人被泼酸袭击了，然后路人不断从店里拿瓶装水倒在受伤男性烧伤的部位冲洗。大约五分钟后警车和救护车到达了现场。\n" +
                "伦敦警察厅官方发言人称：“他们最初由救护车送到了北伦敦医院，随后又被转移到了另一家医院。从目前看来，男性受到的伤害并不致命，但可以被视为此伤害足以改变生活（life-changing）。女性和孩子受了轻伤，仍在医院观察。”\n" +
                "据官方声明称，根据医院的诊断，男性皮肤的腐蚀面积高达15%，主要是在手和身体上，基本可以诊断为“毁容”。嫌犯使用的作案工具被定性为有毒强酸性物质。\n" +
                "目前警方正在积极寻找目击者或知情人士。").split("。");
        for (int i = 0; i < test.length; i++) {
            System.out.println(test[i]);
            System.out.println(analysis.parse(test[i]).getCode());
        }
    }
}
