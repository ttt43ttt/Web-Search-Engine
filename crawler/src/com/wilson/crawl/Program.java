package com.wilson.crawl;

public class Program {

	public static void main(String[] args) {
		MyCrawler crawler = new MyCrawler();

		/* crawlPath是爬取信息存储的文件夹 */
		String crawlPath = "data";
		crawler.setCrawlPath(crawlPath);

		crawler.addSeed("http://bbs.sjtu.edu.cn/php/bbsindex.html");
	
		crawler.addRegex("http://bbs.sjtu.edu.cn/php/bbsindex.html");
		crawler.addRegex("http://bbs.sjtu.edu.cn/bbsdoc\\?board=\\w+");
		crawler.addRegex("http://bbs.sjtu.edu.cn/bbsdoc.*.html");
		crawler.addRegex("http://bbs.sjtu.edu.cn/bbscon.*.html");

		/* 禁止爬取带井号的url */
		crawler.addRegex("-.*#.*");

		/* 禁止爬取图片 */
		crawler.addRegex("-.*ico.*");
		crawler.addRegex("-.*png.*");
		crawler.addRegex("-.*jpg.*");
		crawler.addRegex("-.*gif.*");
		crawler.addRegex("-.*js.*");
		crawler.addRegex("-.*css.*");

		/* 设置线程数 */
		crawler.setThreads(100);

		/* 设置为可断点爬取模式 */
		crawler.setResumable(true);

		try {
			/* 进行深度为100的广度遍历 */
			crawler.start(100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
