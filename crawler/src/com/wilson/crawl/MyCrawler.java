package com.wilson.crawl;

import java.io.IOException;

import org.jsoup.nodes.Document;

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

		// ����
		String title = doc.title();
		
		// ����
		String content = doc.text();

		// ����
		String author = getAuthor(content);
		if (author == null) {
			return;
		}

		// ʱ��
		String time = getTime(content);
		if (time == null) {
			return;
		}

		// IP
		String ip = getIP(content);
		if (ip == null) {
			return;
		}

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
		sb.append(content.replace('\r', '\0').replace('\n', ' '));

		TextFileWriter writer;

		try {
			writer = getWriter();
			writer.appendLine(sb.toString());
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
		int start = content.indexOf("������: ");
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
		int start = content.indexOf("����վ: ��ˮ˼Դ (");
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
