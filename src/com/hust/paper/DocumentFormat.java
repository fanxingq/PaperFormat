package com.hust.paper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hust.utils.PatternMatch;

/**
 * 论文文献格式，以英文文献为主，中文文献为辅。
 * 主要包括：[J],[D],[C]
 * 
 * @author zheng 
 * 
 * 要求：1）以英文句号为分隔符，分为author，title和期刊名（会议名）。 
 * 2）author：人名以逗号分隔，三个以上用”, et al”表示，所有逗号后都有空格。 
 * 3）title：[J]删除 4）期刊：期刊名，年号，页码之间用~。
 * 
 * 注意：本项目只适用从谷歌学术、百度学术、知网等引用文献。
 * 
 * 中文论文[D]和英文期刊[J]格式基本相同。
 * [D]中要添加“: [硕士/博士学位论文]. ”，所有年份后面要有标点。
 * 
 * 所有标点符号为英文格式，且每个标点后有一个空格。
 * 
 * 针对会议[C]引用，需要添加会议地点，时间以及出版社(需手动)。
 * 
 * 目前该程序还有两个问题：1）形如“3):”，冒号后面容易没有空格；2）[C]文献在修改后的"in: "后容易多空格* 
 */
public class DocumentFormat {

	public static void main(String[] args) {

		while (true) {
			/*System.out.println("请输入所要修改的英文文献引用：");
			Scanner sc = new Scanner(System.in);
			String string = sc.nextLine();
			System.out.println("输入的内容为：" + string);*/
			String str = null;
			BufferedReader bre = null;
			
			System.out.println("请输入要读的txt文件路径和名称：（格式示例：path/paper.txt）"); // C:/Users/zheng/Desktop/paper.txt
			Scanner sc = new Scanner(System.in);
			String readPath = sc.nextLine();
			System.out.println("请输入要写入的txt文件路径和名称：");   // C:/Users/zheng/Desktop/result.txt
			String writePath = sc.nextLine();
			boolean flag = false;
			System.out.println("是否覆盖写入文件已有内容：  1. 覆盖    2. 不覆盖   （请输入1或者2）");
			String f = sc.nextLine();
			if (f.equals("1")) {
				flag = false;
			}else if (f.equals("2")) {
				flag = true;
			}else {
				System.out.println("请按要求输入！");
				System.exit(0);
			}
			try {
				 bre = new BufferedReader(new InputStreamReader(new FileInputStream(readPath),"gbk"));
//				 FileWriter fw = new FileWriter(writePath, flag);
				 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writePath),"gbk"));
				while ((str = bre.readLine()) != null) // 判断最后一行不存在，为空结束循环
				{
					System.out.println(str);// 原样输出读到的内容
					
					String newpaper = processPaper(str);
					bw.write(newpaper+"\r\n");
					
				}
				
				bw.flush();
				bre.close();
				bw.close();
//				fw.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static String processPaper(String str) {
		
		libtex libtex1 = new libtex();
		String[] strlist = str.split("\\.");
//		for (String stri : strlist) {
//			System.out.println(stri);
//		}
		libtex1.setAuthor(strlist[0].trim());
		String newstring1 = null;
		//判断是否为中文
		Pattern p = Pattern.compile(PatternMatch.findChineseCharacters);
        Matcher m = p.matcher(str);
		if (m.find() && strlist[1].contains("[D]")) {
			newstring1 = strlist[1].substring(0, strlist[1].length() - 3) + ": [硕士学位论文]";
			libtex1.setJounal(strlist[2].trim() + ". ", "D");
		} else {
			// 如果期刊名那里含有[J]删掉
			if (strlist[1].contains("[J]")) {
				newstring1 = strlist[1].substring(0,
						strlist[1].length() - 3);
				libtex1.setJounal(strlist[2].trim(), "J");
			} else if (strlist[1].contains("[C]")) {
				libtex1.setIsjounal(false);
				newstring1 = strlist[1].replace("[C]//", ". in: ");
				libtex1.setJounal(strlist[2].trim(), "C");
			} else {
				newstring1 = strlist[1];
				libtex1.setJounal(strlist[2].trim(),"");
			}
		}
		
//		System.out.println(newstring1);
		libtex1.setTitle(newstring1.trim());
		
		System.out.println("修改后的结果为：");
		System.out.println(libtex1.merge());
		
		
		return libtex1.merge();
	}

}
