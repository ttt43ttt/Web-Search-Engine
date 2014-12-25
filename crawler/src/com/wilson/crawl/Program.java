package com.wilson.crawl;

public class Program {

	public static void main(String[] args) {
		MyCrawler crawler = new MyCrawler();

		/* crawlPath����ȡ��Ϣ�洢���ļ��� */
		String crawlPath = "data";
		crawler.setCrawlPath(crawlPath);

		crawler.addSeed("http://bbs.sjtu.edu.cn/php/bbsindex.html");
		crawler.addRegex("http://bbs.sjtu.edu.cn/bbscon.*\\.html");

		/* ��ֹ��ȡ�����ŵ�url */
		crawler.addRegex("-.*#.*");

		/* ��ֹ��ȡͼƬ */
		crawler.addRegex("-.*png.*");
		crawler.addRegex("-.*jpg.*");
		crawler.addRegex("-.*gif.*");
		crawler.addRegex("-.*js.*");
		crawler.addRegex("-.*css.*");

		/* �����߳��� */
		crawler.setThreads(1);

		/* ����Ϊ�ɶϵ���ȡģʽ */
		crawler.setResumable(true);

		try {
			/* �������Ϊ3�Ĺ�ȱ��� */
			crawler.start(3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
