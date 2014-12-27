package com.wilson.crawl;

import java.io.IOException;

import org.jsoup.nodes.Document;

import com.wilson.common.PageInfo;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.model.Page;

public class MyCrawler extends BreadthCrawler {
	private final String fileName = "data/bbs.txt";
	private TextFileWriter writer;

	@Override
	public void visit(Page page) {
		String url = page.getUrl();
		if (!url.endsWith(".html")) {
			return;
		}

		Document doc = page.getDoc();
		if (doc == null) {
			return;
		}

		// 标题
		String title = doc.title();

		// 内容
		String content = doc.text();

		// 作者
		String author = getAuthor(content);
		if (author == null) {
			return;
		}

		// 时间
		String time = getTime(content);
		if (time == null) {
			return;
		}

		// IP
		String ip = getIP(content);
		if (ip == null) {
			return;
		}

		PageInfo info = new PageInfo();
		info.setUrl(url);
		info.setTitle(title);
		info.setAuthor(author);
		info.setTime(time);
		info.setIp(ip);
		info.setContent(content);

		String line = info.toString();

		try {
			getWriter().appendLine(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private TextFileWriter getWriter() throws IOException {
		if (writer == null) {
			writer = TextFileWriter.getFileWriter(fileName);
		}

		return writer;
	}

	private String getAuthor(String content) {
		// String author = doc.select("pre a").eq(0).text();
		int start = content.indexOf("发信人: ");
		if (start == -1) {
			return null;
		}

		start += 5;

		int end = content.indexOf('(', start);
		if (end == -1) {
			return null;
		}

		String author = content.substring(start, end);
		return author.trim();
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
