package com.example.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatchUtils {
	
	public static boolean isPureNumber(String s)
    { 
		String pattern = "^[1-9]\\d*$";
		 
		// 创建 Pattern 对象
		Pattern r = Pattern.compile(pattern);
		 
		// 现在创建 matcher 对象
		Matcher m = r.matcher(s);
		if (m.find()) {
			return true;
		} else {
			return false;
		}
      
    }
	
	public static boolean hasPureNumber(String s)
    { 
		String pattern = ",[1-9]\\d*,";
		 
		// 创建 Pattern 对象
		Pattern r = Pattern.compile(pattern);
		 
		// 现在创建 matcher 对象
		Matcher m = r.matcher("," + s + ",");
		if (m.find()) {
			return true;
		} else {
			return false;
		}
      
    }
	
	/**
	 * 获取占位符
	 */
	public static String getPlaceholderFromSplit(String s)
    {
		
		String pattern = "\\w+";
		 
		Pattern r = Pattern.compile(pattern);
		
		Matcher m = r.matcher( s );
		if (m.find()) {
			return m.group();
		} else {
			return null;
		}
      
    }
	
	/**
	 * 获取占位符"DELETE FROM $table WHERE name=@name"
	 */
	public static List<String> getPlaceholder(String s, String prefix)
    {
		List<String> result = new ArrayList<String>();
		
		String pattern = "$".equals(prefix) ? "\\$\\w+\\W{0,1}" : "@\\w+\\W{0,1}";
		 
		Pattern r = Pattern.compile(pattern);
		 
		List<String> sl = new ArrayList<String>();
		Matcher m = r.matcher( s );
		while (m.find()) {
//			System.out.println(m.group());
			sl.add(m.group());
		}
		
		for (int i = 0 ; i < sl.size(); i++) {
			String str = getPlaceholderFromSplit(sl.get(i));
			if (str != null) {
				result.add(str);
			}
		}
		
		return result;
      
    }
	
	/**
	 * 匹配abc = &abc
	 */
	public static String getConditionWith(String s, String prefix, String abc)
    {
		// org\b(.*?)\$org(\W*|\b)
		String pattern = "$".equals(prefix) ? abc + "\\b(.*?)\\$" + abc + "(\\W*|\\b)" : abc + "\\b(.*?)@" + abc + "(\\W*|\\b)";
		 
		Pattern r = Pattern.compile(pattern);
		
		Matcher m = r.matcher( s );
		if (m.find()) {
			return m.group();
		} else {
			return null;
		}
      
    }
	
	public static void main(String[] args) {
		List<String> s = getPlaceholder("DELETE FROM $table1, $table2 WHERE name=$name", "$");
		System.out.println(s);
	}

}
