package com.wilson.search;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.wilson.common.IndexInfo;
import com.wilson.common.PageInfo;

public class SearcherTest {
	@Test
	public void test() throws IOException {
		KeywordSearcher searcher = new KeywordSearcher();
		List<SearchResult> result = searcher.search("±±¾©");
		System.out.println(result.size());

		for (SearchResult record : result) {
			IndexInfo index = record.getIndexInfo();
			PageInfo page = record.getPageInfo();
			Assert.assertEquals(index.getUrl(), page.getUrl());

			System.out.println(page.getUrl());
		}
	}
}
