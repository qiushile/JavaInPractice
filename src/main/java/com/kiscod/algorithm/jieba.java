package com.kiscod.algorithm;

import com.huaban.analysis.jieba.JiebaSegmenter;

import java.io.IOException;
import java.util.*;

/**
 * Created by lance on 2017/5/4.
 */
public class jieba {

    public static void  main(String[] args) throws IOException {
        String text1 =  "大黄河南部";
        String text =
                "五五开黑节就要来了，自诩为老司机的你是否已经摩拳擦掌准备大展拳脚了呢，是否有心仪的男神/女神，你一直渴望带她/他征服王者峡谷确不知如何开口呢，我们开启了“寻找峡谷老司机”活动，解锁成为老司机的正确姿势！\n" +
                "目前进度：\n" +
                "为了保证活动的稳定性，我们会逐步开放各个区的参与资格，目前仅开放了iOS微信区的活动资格，玩家们可能会遇到以下情况：\n" +
                "1. 新玩家在除了iOS微信区的服务器登录时，无法输入邀请码。这是由于在其他区服还未开放活动造成的：全面开放后，4月27日之后注册的新玩家均可以在活动页面绑定对应平台的邀请码（微信区的邀请码只能在微信区绑定，手Q区的邀请码只能在手Q区绑定）\n" +
                "2. 部分玩家能够看到好友召回的活动，但是无法看到其他活动，并且无法兑换积分，这是由于活动还未完全开放造成的：玩家通过召回获得的积分在活动完全开放之后可以继续使用。\n" +
                "活动持续时间为4月27日~5月14日，非常充裕，请大家适度游戏，劳逸结合，才能成为更稳的司机哦！\n" +
                " \n" +
                "活动入口：\n" +
                "活动面板-限时活动里即可看到四个页签：萌新输入邀请码（只对新玩家开放），邀请新玩家，召回老玩家，积分兑换&排行榜，即为本次的四个主题活动！";
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<String> list = segmenter.sentenceProcess(text1);
//        Arrays.sort(Arrays.asList(list));
        System.out.println(list);
        System.out.println(processSentence(text));
    }


    public static List<Map<String, String>> processSentence(String text) {
        if (text == null) {
            return null;
        }
        List<String> words = segmenter(text);
        Map<String, Integer> cntMap = new HashMap<>();
        for (String word : words) {
            if (cntMap.containsKey(word)) {
                cntMap.put(word, cntMap.get(word)+1);
            } else {
                cntMap.put(word, 1);
            }
        }

        List<Map<String, String>> res = new ArrayList<>(cntMap.size());
        for (String name : cntMap.keySet()) {
            String value = "" + cntMap.get(name);
            Map<String, String> map = new HashMap<>(2);
            map.put("name", name);
            map.put("value", value);
            res.add(map);
        }

        res.sort(new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                return Integer.parseInt(o2.get("value").toString()) - Integer.parseInt(o1.get("value").toString());
            }
        });

        return res;
    }

    public static List<String> segmenter(String text) {
        if (text == null) {
            return null;
        }
//        String text = "前任拉甘送苏宁首败落后恒大6分争冠难了,前任拉甘送苏宁首败落后恒大6分争冠难了。";
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<String> list = segmenter.sentenceProcess(text);
//        Arrays.sort(Arrays.asList(list));
//        System.out.println(segmenter.sentenceProcess(text));

        return list;
    }
}
