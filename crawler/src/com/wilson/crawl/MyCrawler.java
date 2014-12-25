package com.wilson.crawl;

import org.jsoup.nodes.Document;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.model.Page;

public class MyCrawler extends BreadthCrawler {

	@Override
	public void visit(Page page) {
		String url = page.getUrl();

		Document doc = page.getDoc();
		if (doc == null) {
			return;
		}

		String title = doc.title();
		if (title.startsWith("Re: ")) {
			return;
		}

		String author = getAuthor(doc);

		String content = doc.text();

		String time = getTime(content);

		String ip = getIP(content);

		StringBuilder sb = new StringBuilder();
		sb.append(url);
		sb.append('\t');
		sb.append(title);
		sb.append('\t');
		sb.append(author);
		sb.append('\t');
		sb.append(time);
		sb.append('\t');
		sb.append(ip);
		sb.append('\t');
		sb.append(content);
	}

	private String getAuthor(Document doc) {
		String author = doc.select("pre a").eq(0).text();
		return author;
	}

	private String getIP(String content) {
		int start = content.lastIndexOf("[FROM: ");
		if (start == -1) {
			return null;
		}

		start += 7;
		int end = content.indexOf(']', start);
		if (end == -1) {
			return null;
		}

		String ip = content.substring(start, end);
		return ip;
	}

	private String getTime(String content) {
		int start = content.indexOf("发信站: 饮水思源 (");
		if (start == -1) {
			return null;
		}

		start += 11;
		int end = content.indexOf(' ', start);
		if (end == -1) {
			return null;
		}

		String time = content.substring(start, end);
		return time;
	}

}
