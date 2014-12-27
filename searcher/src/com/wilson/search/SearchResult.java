package com.wilson.search;

import com.wilson.common.IndexInfo;
import com.wilson.common.PageInfo;

public class SearchResult {
	private IndexInfo indexInfo;
	private PageInfo pageInfo;

	public IndexInfo getIndexInfo() {
		return indexInfo;
	}

	public void setIndexInfo(IndexInfo indexInfo) {
		this.indexInfo = indexInfo;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

}
