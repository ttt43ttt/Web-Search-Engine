package com.wilson.segment;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class TextSegmenter {

	public static List<Lexeme> getLexemes(String content) {
		List<Lexeme> list = new ArrayList<Lexeme>();
		
		if(content == null){
			return list;
		}
		
		StringReader input = new StringReader(content.trim());

		// true智能分词，false细粒度
		IKSegmenter ikSeg = new IKSegmenter(input, false);
		Lexeme lexeme;

		try {
			while ((lexeme = ikSeg.next()) != null) {
				list.add(lexeme);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			input.close();
		}

		return list;
	}
}
