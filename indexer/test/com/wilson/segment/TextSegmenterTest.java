package com.wilson.segment;

import java.util.List;

import org.junit.Test;
import org.wltea.analyzer.core.Lexeme;

public class TextSegmenterTest {

	@Test
	public void test() {
		String content = "��˰ģʽ�����ͳ����ó�� ȴ�ɿ羳���̷�չƿ��";
		List<Lexeme> lexemes = TextSegmenter.getLexemes(content);

		for (Lexeme l : lexemes) {
			System.out.println(l.getLexemeType() + "," + l.getLexemeText() + "," + l.getBeginPosition());
		}
	}
}
