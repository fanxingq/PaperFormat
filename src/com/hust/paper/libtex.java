package com.hust.paper;

import java.util.regex.Pattern;

import com.hust.utils.PatternMatch;

public class libtex {
	String author = null;
	String title = null;
	String jounal = null;
	Boolean isjounal = true;

	public Boolean getIsjounal() {
		return isjounal;
	}

	public void setIsjounal(Boolean isjounal) {
		this.isjounal = isjounal;
	}

	public void setTitle(String title) {
		
		System.out.println(title);
		
		
		this.title = title;
	}

	public libtex() {
		// TODO Auto-generated constructor stub
	}

	public void setAuthor(String author) {
		// 如果含有等等字样，啥都不做，肯定小于等于三

		String[] authorlist = null;
		String newAuthor = "";
		if (author.contains("et al")) {
			authorlist = (author.substring(0, author.length() - 5)).split(",");
		}else if (author.contains(", 等")) {
			authorlist = (author.substring(0, author.length() - 1)).split(",");
		} else {
			authorlist = author.split(",");
		}

		if (authorlist.length >= 3) {
			// 大于三做事
			String string = authorlist[0].trim() + ", " + authorlist[1].trim() + ", "
					+ authorlist[2].trim();
			if (Pattern.compile(PatternMatch.findChineseCharacters).matcher(string).find()) {
				newAuthor = string + ", 等";
			}else {
				newAuthor = string + ", et al";
			}
		} else if (authorlist.length == 2) {
			newAuthor = authorlist[0].trim() + ", " + authorlist[1].trim();
		} else if (authorlist.length == 1) {
			newAuthor = authorlist[0].trim();
		} else {
			System.out.println("异常:没有作者。");
		}

//		System.out.println(newAuthor);
		this.author = newAuthor;
	}

	public void setJounal(String jounal,String mark) {
		
		//Zhou M. An OGS-Based Dynamic Time Warping Algorithm for Time Series Data[J]. Innovation in the High-Tech Economy, 2014: 115.
		//待修改
		String newjouStrings = "";
		String[] jouStrings = null;
		if (jounal.contains("-")) {
		//判断-是否是页码处的
		boolean matches = Pattern.compile(PatternMatch.findNumToNum).matcher(jounal).find();
		System.out.println(matches);
		if (matches) {
			jounal = jounal.replace('-', '~');
		}
		}
		if (jounal.endsWith(".")) {
			jounal = jounal.substring(0, jounal.length()-1);         
		}
		switch (mark) {
		case "J":
//			break;
		case "D":
			//第一个匹配示例3):   第二个匹配示例2014:
//			boolean find = Pattern.compile("\\d\\):").matcher(jounal).find();
//			System.out.println(find);
//			boolean find2 = Pattern.compile("\\d{4}:").matcher(jounal).find();
//			System.out.println(find2);
			if (!Pattern.compile(PatternMatch.findNumAndBracketsColon).matcher(jounal).find() 
					&& Pattern.compile(PatternMatch.findNumAndColon).matcher(jounal).find()) {   

				jounal = jounal.replace(':', ',');
			}
			jouStrings = jounal.split(",");
			for (int i = 0; i < jouStrings.length; i++) {
				if (i!=jouStrings.length-1) {
					newjouStrings = newjouStrings + jouStrings[i].trim() + ", ";
				}else {
					newjouStrings = newjouStrings + jouStrings[i].trim();
				}
			}
			break;
		case "C":
			
			jounal = jounal.replace(':','.');
			jouStrings = jounal.split(",");
			for (int i = 0; i < jouStrings.length; i++) {
				if (i!=jouStrings.length-1) {
					newjouStrings = newjouStrings + jouStrings[i].trim() + ", ";
				}else {
					newjouStrings = newjouStrings + jouStrings[i].trim();
				}
			}
			
			break;

		default:              //不用修改
			newjouStrings = jounal;
			break;
		}
		
		this.jounal = newjouStrings;
	}

	public String getAuthor() {
		return author;
	}

	public String getJounal() {
		return jounal;
	}

	public String getTitle() {
		return title;
	}

	public String merge() {
		String resultString = author + ". " + title + ". " + jounal;
		return resultString;

	}
}
