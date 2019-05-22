package com.hust.utils;

/**
 * 正则表达式
 * @author zheng
 *
 */
public class PatternMatch {

	//冒号正则表达式    匹配示例        2014:
	public static final String findNumAndColon = "\\d{4}:";
	
	//匹配示例    3): 
	public static final String findNumAndBracketsColon = "\\d\\):";
	
	//匹配中文字符   
	public static final String findChineseCharacters = "[\u4e00-\u9fa5]";
	
	//匹配示例：  1-2
	public static final String findNumToNum = "\\d{1}-\\d{1}";
	
	
}
