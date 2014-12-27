package com.wilson.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.wltea.analyzer.core.Lexeme;

import com.wilson.segment.TextSegmenter;

public class IndexingTest {
	private static List<String[]> mapResults = new ArrayList<>();

	private String url;
	private String title;
	private String author;
	private String time;
	private String ip;
	private String content;

	@Test
	public void testMap() {
		String line = "http://bbs.sjtu.edu.cn/bbscon,board,Garden,file,M.1419301917.A.html	Re: ��㷢��ͼ - ��ˮ˼Դ	bonnieangel	2014��12��23��10:31:57	116.228.53.35	Re: ��㷢��ͼ - ��ˮ˼Դ ��ˮ˼Դ - �����Ķ�����[������: Garden] [ת��/�Ƽ�][ת��][ɾ��][�޸�][���ÿ�RE����][��һƪ][����������][��һƪ][������][ͬ�����б�][ͬ�����Ķ�][������չ��] ������: bonnieangel(������), ����: Garden ��  ��: Re: ��㷢��ͼ ����վ: ��ˮ˼Դ (2014��12��23��10:31:57 ���ڶ�)  ˭˵���ĳ��ˣ�����������ô�õ������������ڼ������Ÿ��������գ������ϰࡣ�� �� �� cmm �Ĵ������ᵽ: �� : ��Ȼ���ĳ� : ����Ҫʱ��ʱ��ˢˢ���ڸе�~ : �������ͼ����ǰ��ʱ���ĵ��� : �����ҵ���̨���絽���Ѿ�û��һ������������Ŀ : http://bbs.sjtu.edu.cn/file/Garden/141929895566390.jpg : http://bbs.sjtu.edu.cn/file/Garden/1419298967181260.jpg : http://bbs.sjtu.edu.cn/file/Garden/1419298970100090.jpg : http://bbs.sjtu.edu.cn/file/Garden/1419298972253300.jpg : http://bbs.sjtu.edu.cn/file/Garden/1419298974117580.jpg : http://bbs.sjtu.edu.cn/file/Garden/1419298983254550.jpg : http://bbs.sjtu.edu.cn/file/Garden/141929898884940.jpg : http://bbs.sjtu.edu.cn/file/Garden/1419298993213900.jpg : http://bbs.sjtu.edu.cn/file/Garden/1419298999100470.jpg : http://bbs.sjtu.edu.cn/file/Garden/1419299006295271.jpg : http://bbs.sjtu.edu.cn/file/Garden/1419299022250060.jpg : http://bbs.sjtu.edu.cn/file/Garden/1419299026230870.jpg : http://bbs.sjtu.edu.cn/file/Garden/1419299032118220.jpg : http://bbs.sjtu.edu.cn/file/Garden/1419299043251650.jpg  -- ���������ϩǩ���    ������  bonnieangel  ���ѣ�                  ��  ��  �����ϩǩ���                                                 ��  ��  ��  ����  ��           Garden�� �������� �ƺ�                ��  ��  �������贈��                                                 ��  ��  ������������        ������ΪGarden��ķ�չ������׿Խ���ף�   ��  ��  ����  ������    �ط���֤�����ʹ�����                         ��  �� ��Դ:����ˮ˼Դ bbs.sjtu.edu.cn��[FROM: 116.228.53.35]  [ת��/�Ƽ�][ת��][ɾ��][�޸�][���ÿ�RE����][��һƪ][����������][��һƪ][������][ͬ�����б�][ͬ�����Ķ�][������չ��]";

		// parse line
		parseLine(line);

		if (url == null || title == null || content == null) {
			return;
		}

		// segment words
		List<Lexeme> lexemes = TextSegmenter.getLexemes(content);
		for (Lexeme lexeme : lexemes) {
			// key
			String kw = lexeme.getLexemeText();

			if (lexeme.getLexemeType() == Lexeme.TYPE_CNWORD && kw.length() > 1) {
				System.out.println(kw);

				// value
				StringBuilder sb = new StringBuilder();
				sb.append(url);
				sb.append(';');
				sb.append(1);
				sb.append(';');
				sb.append(lexeme.getBeginPosition());

				System.out.println(sb.toString());
				mapResults.add(new String[] { kw, sb.toString() });
			}
		}
	}

	private void parseLine(String line) {
		String[] fields = new String[6];
		char delimiter = '\t';
		int start = 0, end = 0;

		for (int i = 0; i < fields.length - 1; i++) {
			end = line.indexOf(delimiter, start);

			if (end > start) {
				fields[i] = line.substring(start, end);
				start = end + 1;
			} else {
				break;
			}
		}

		fields[fields.length - 1] = line.substring(start);

		url = fields[0];
		title = fields[1];
		author = fields[2];
		time = fields[3];
		ip = fields[4];
		content = fields[5];
	}

	/*-------------------------------------------------*/

	private HashSet<String> urls = new HashSet<>();
	private HashMap<String, Integer> ranks = new HashMap<>();
	private HashMap<String, StringBuilder> positions = new HashMap<>();

	@Test
	public void testReduce() {
		for (String[] result : mapResults) {
			String key = result[0];
			String value = result[1];
			String[] list = value.split(";");

			if (list.length != 3) {
				continue;
			}

			String url = list[0];
			urls.add(url);

			Integer rank = ranks.get(url);
			if (rank == null)
				rank = 0;
			rank += Integer.parseInt(list[1]);
			ranks.put(url, rank);

			StringBuilder sb = positions.get(url);
			if (sb == null) {
				sb = new StringBuilder();
				positions.put(url, sb);
			}

			sb.append(",");
			sb.append(list[2]);
		}

		StringBuilder sb = new StringBuilder();

		for (String url : urls) {
			sb.append(url);
			sb.append(';');
			sb.append(ranks.get(url));
			sb.append(';');
			sb.append(positions.get(url).toString());
			sb.append(';');
		}

		System.out.println(sb.toString());
	}

}
