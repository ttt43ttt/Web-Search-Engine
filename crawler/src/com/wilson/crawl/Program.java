package com.wilson.crawl;

public class Program {

	public static void main(String[] args) {
		MyCrawler crawler = new MyCrawler();

		/* crawlPath����ȡ��Ϣ�洢���ļ��� */
		String crawlPath = "data";
		crawler.setCrawlPath(crawlPath);

		crawler.addSeed("http://bbs.sjtu.edu.cn/php/bbsindex.html");
	
		crawler.addRegex("http://bbs.sjtu.edu.cn/php/bbsindex.html");
		crawler.addRegex("http://bbs.sjtu.edu.cn/bbsdoc\\?board=\\w+");
		crawler.addRegex("http://bbs.sjtu.edu.cn/bbsdoc.*.html");
		crawler.addRegex("http://bbs.sjtu.edu.cn/bbscon.*.html");

		/* ��ֹ��ȡ�����ŵ�url */
		crawler.addRegex("-.*#.*");

		/* ��ֹ��ȡͼƬ */
		crawler.addRegex("-.*ico.*");
		crawler.addRegex("-.*png.*");
		crawler.addRegex("-.*jpg.*");
		crawler.addRegex("-.*gif.*");
		crawler.addRegex("-.*js.*");
		crawler.addRegex("-.*css.*");

		/* �����߳��� */
		crawler.setThreads(100);

		/* ����Ϊ�ɶϵ���ȡģʽ */
		crawler.setResumable(true);

		try {
			/* �������Ϊ100�Ĺ�ȱ��� */
			crawler.start(100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
