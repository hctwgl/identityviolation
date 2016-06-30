package com.mapbar.traffic.score.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeijingOrcHelper {

	private static Map<String, String[]> zhongwenReplaceMap = new HashMap<String, String[]>();
	static {
		zhongwenReplaceMap.put("洗", new String[] { "三芫" });
		zhongwenReplaceMap.put("通", new String[] { "擅" });
		zhongwenReplaceMap.put("符", new String[] { "萝杳" });
		zhongwenReplaceMap.put("设", new String[] { "之曼" });
		zhongwenReplaceMap.put("心", new String[] { "」5、" });
		zhongwenReplaceMap.put("验", new String[] { "骂企" });
		zhongwenReplaceMap.put("时", new String[] { "日立" });
		zhongwenReplaceMap.put("辆", new String[] { "荃薄", "镛" });
		zhongwenReplaceMap.put("限", new String[] { "冈蔓", "肾艮", "膏艮", "艮" });
		zhongwenReplaceMap.put("汇", new String[] { "三仁", "豪L" });
		zhongwenReplaceMap.put("以", new String[] { "贮人", "仁/\\" });
		zhongwenReplaceMap.put("向", new String[] { "L遭司" });
		zhongwenReplaceMap.put("线", new String[] { "多易" });
		zhongwenReplaceMap.put("标", new String[] { "难圣", "糠圣" });
		zhongwenReplaceMap.put("叭", new String[] { "醒`", "皿/\\", "/\\", "八" });
		zhongwenReplaceMap.put("低", new String[] { "巨鬼", "儡氏" });
		zhongwenReplaceMap.put("驶", new String[] { "马塑", "暑迟", "赡", "碾" });
		zhongwenReplaceMap.put("道", new String[] { "芗亘", "殖", "澶", "遣" });
		zhongwenReplaceMap.put("行", new String[] { "不哥", "付", "狩" });
		zhongwenReplaceMap.put("放", new String[] { "右夕" });
		zhongwenReplaceMap.put("醒", new String[] { "酉星" });
		zhongwenReplaceMap.put("意", new String[] { "营、", "童" });
		zhongwenReplaceMap.put("域", new String[] { "土芗酋", "土蓼", "碱" });
		zhongwenReplaceMap.put("提", new String[] { "茅畏", "棣" });
		zhongwenReplaceMap.put("准", new String[] { "外呈" });
		zhongwenReplaceMap.put("慎", new String[] { "`寮熹" });
		zhongwenReplaceMap.put("动", new String[] { "云刁", "远刀", "云力", "珀" });
		zhongwenReplaceMap.put("头", new String[] { "三大" });
		zhongwenReplaceMap.put("指", new String[] { "茎旨" });
		zhongwenReplaceMap.put("接", new String[] { "苷妾", "媛" });
		zhongwenReplaceMap.put("〉、", new String[] { "入" });
		zhongwenReplaceMap.put("或", new String[] { "藁" });
		zhongwenReplaceMap.put("减", new String[] { "曦", "臧" });
		zhongwenReplaceMap.put("转", new String[] { "荃舌", "车专" });
		zhongwenReplaceMap.put("路", new String[] { "矗" });
		zhongwenReplaceMap.put("表", new String[] { "耒", "袁" });
		zhongwenReplaceMap.put("禁", new String[] { "鬣", "纂", "赢" });
		zhongwenReplaceMap.put("续", new String[] { "鲑" });
		zhongwenReplaceMap.put("专", new String[] { "蔓", "亏" });
		zhongwenReplaceMap.put("方", new String[] { "畜", "宽" });
		zhongwenReplaceMap.put("解", new String[] { "邂" });
		zhongwenReplaceMap.put("各", new String[] { "答", "备" });
		zhongwenReplaceMap.put("合", new String[] { "舍", "台" });
		zhongwenReplaceMap.put("慢", new String[] { "l}曼", "曼" });
		zhongwenReplaceMap.put("注", new String[] { "淀", "塞" });
		zhongwenReplaceMap.put("慢", new String[] { "曼" });
		zhongwenReplaceMap.put("处", new String[] { "灶", "铿" });
		zhongwenReplaceMap.put("多", new String[] { "芗" });
		zhongwenReplaceMap.put("驾", new String[] { "骂", "萱" });
		zhongwenReplaceMap.put("连", new String[] { "襄", "萱" });
		zhongwenReplaceMap.put("切", new String[] { "′乒刀", "乒刀", "刀" });
		zhongwenReplaceMap.put("傍", new String[] { "旁" });
		zhongwenReplaceMap.put("该", new String[] { "谦" });
		zhongwenReplaceMap.put("乘", new String[] { "冢" });
		zhongwenReplaceMap.put("右", new String[] { "疱" });
		zhongwenReplaceMap.put("桥", new String[] { "轿" });
		zhongwenReplaceMap.put("示", new String[] { "呆" });
		zhongwenReplaceMap.put("车", new String[] { "在" });
		zhongwenReplaceMap.put("弯", new String[] { "雪" });
		zhongwenReplaceMap.put("落", new String[] { "鳝" });
		zhongwenReplaceMap.put("直", new String[] { "亘" });
		zhongwenReplaceMap.put("掉", new String[] { "挂" });
		zhongwenReplaceMap.put("横", new String[] { "霸", "幢", "桔" });
		zhongwenReplaceMap.put("享", new String[] { "夏" });
		zhongwenReplaceMap.put("况", new String[] { "吼" });
		zhongwenReplaceMap.put("检", new String[] { "榆" });
		zhongwenReplaceMap.put("离", new String[] { "禺" });
		zhongwenReplaceMap.put("供", new String[] { "殃" });
		zhongwenReplaceMap.put("某", new String[] { "蒙" });
		zhongwenReplaceMap.put("度", new String[] { "震" });
		zhongwenReplaceMap.put("机", new String[] { "祝" });
		zhongwenReplaceMap.put("警", new String[] { "兽" });
		zhongwenReplaceMap.put("的", new String[] { "台券" });
		zhongwenReplaceMap.put("汐", new String[] { "毒彝" });
		zhongwenReplaceMap.put("前", new String[] { "言亓" });
		zhongwenReplaceMap.put("用", new String[] { "闰" });
		zhongwenReplaceMap.put("停", new String[] { "儡亭" });
		zhongwenReplaceMap.put("停", new String[] { "儡亭" });
		zhongwenReplaceMap.put("段", new String[] { "鬃复", "鬟复" });

	}

	public static String dealZhongwen(String source, String imgDes) {
		source = source.replace("三告", "浩");
		source = source.replace("/ \\", "八");
		// zhongwenReplaceMap.put("符",new String[]{""});
		char[] des = imgDes.toCharArray();
		List<String[]> replacelist = new ArrayList<String[]>();
		List<String> toReplaces = new ArrayList<String>();
		for (char c : des) {
			if (zhongwenReplaceMap.get(String.valueOf(c)) != null && !toReplaces.contains(String.valueOf(c))) {
				replacelist.add(zhongwenReplaceMap.get(String.valueOf(c)));
				toReplaces.add(String.valueOf(c));
			}
		}
		//System.out.println(toReplaces);
		//System.out.println(replacelist);
		for (int i = 0; i < toReplaces.size(); i++) {
			String s = toReplaces.get(i);
			String[] sss = replacelist.get(i);
			//System.out.println(sss);
			for (String str : sss) {
				source = source.replace(str, s);
			}
		}
		return source;
	}

}
