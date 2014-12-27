package com.wilson.segment;

import java.util.List;

import org.junit.Test;
import org.wltea.analyzer.core.Lexeme;

public class TextSegmenterTest {

	@Test
	public void test() {
		String content = "保税模式冲击传统进口贸易 却成跨境电商发展瓶颈";
		List<Lexeme> lexemes = TextSegmenter.getLexemes(content);

		for (Lexeme l : lexemes) {
			System.out.println(l.getLexemeType() + "," + l.getLexemeText() + "," + l.getBeginPosition());
		}
	}
}
