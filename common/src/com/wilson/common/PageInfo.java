package com.wilson.common;

public class PageInfo {
	private final static char DELIMITER = '\t';

	private String url;
	private String title;
	private String author;
	private String time;
	private String ip;
	private String content;

	public static PageInfo parse(String line) {
		if (line == null) {
			line = "";
		}

		String[] fields = new String[6];
		int start = 0, end = 0;

		for (int i = 0; i < fields.length - 1; i++) {
			end = line.indexOf(DELIMITER, start);

			if (end > start) {
				fields[i] = line.substring(start, end);
				start = end + 1;
			} else {
				break;
			}
		}

		fields[fields.length - 1] = line.substring(start);

		PageInfo info = new PageInfo();
		info.url = fields[0];
		info.title = fields[1];
		info.author = fields[2];
		info.time = fields[3];
		info.ip = fields[4];
		info.content = fields[5];

		return info;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(url);
		sb.append(DELIMITER);

		sb.append(title);
		sb.append(DELIMITER);

		sb.append(author);
		sb.append(DELIMITER);

		sb.append(time);
		sb.append(DELIMITER);

		sb.append(ip);
		sb.append(DELIMITER);

		sb.append(content.replace('\r', '\0').replace('\n', ' '));

		return sb.toString();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
