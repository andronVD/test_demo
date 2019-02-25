package com.test.demo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Main {

	public static void main(String[] args) {
		Element original = getOriginalElementById(args[0], args[2]);
		System.out.println(getElemPathIfSimilar(original, args[1]));
	}

	private static String getElemPathIfSimilar(Element original, String filePathToSimilar) {
		File file = new File(filePathToSimilar);
		String result = null;
		try {
			Document doc = Jsoup.parse(file, "UTF-8", "");
			Optional<Element> temp = doc.getElementsByClass("btn").stream().filter(elem -> isElementSimilarToOriginal(elem, original)).findFirst();
			if (temp.isPresent()) {
				result = getFormattedFilePath(temp.get());
			} else {
				result = "There is no similar element on the page";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static boolean isElementSimilarToOriginal(Element temp, Element original) {
		return (temp.attr("onclick").equals(original.attr("onclick")) || temp.text().equals(original.text()) || temp.className().equals(original.className()))
				&& temp.attr("title").equals(original.attr("title"));
	}

	private static Element getOriginalElementById(String filePath, String id) {
		File file = new File(filePath);
		Element res = null;
		try {
			Document doc = Jsoup.parse(file, "UTF-8", "");
			res = doc.getElementById(id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	private static String getFormattedFilePath(Element obj) {
		StringBuilder strBuilder = new StringBuilder();
		obj.parents().stream().collect(Collectors.toCollection(ArrayDeque::new)).descendingIterator().forEachRemaining(elem -> strBuilder.append(elem.tagName()).append("->"));
		return strBuilder.append(obj.tagName()).toString();
	}
	

}
