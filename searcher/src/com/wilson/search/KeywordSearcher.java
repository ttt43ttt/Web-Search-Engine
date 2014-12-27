package com.wilson.search;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.wilson.common.IndexInfo;
import com.wilson.common.PageInfo;

public class KeywordSearcher {
	private final static int INDEX_DATA_COUNT = 10;
	private final static int BBS_DADA_COUNT = 10;

	private final static int MAX_RESULT_COUNT = 20;

	private String INDEX_INDEX_PATH = "/index.index";
	private String INDEX_DATA_PREFIX = "/index/part-";

	private String BBS_INDEX_PATH = "/bbs.index";
	private String BBS_DATA_PREFIX = "/bbs/part-";

	private DecimalFormat formatter = new DecimalFormat("00000");

	public KeywordSearcher(String baseDir) {
		INDEX_INDEX_PATH = baseDir.concat(INDEX_INDEX_PATH);
		INDEX_DATA_PREFIX = baseDir.concat(INDEX_DATA_PREFIX);
		BBS_INDEX_PATH = baseDir.concat(BBS_INDEX_PATH);
		BBS_DATA_PREFIX = baseDir.concat(BBS_DATA_PREFIX);
	}

	public List<SearchResult> search(String keyword) throws IOException {
		List<SearchResult> result = new ArrayList<>();

		if (keyword == null || keyword.isEmpty()) {
			return result;
		}

		List<IndexInfo> indexInfoList = getIndexInfo(keyword);

		for (int i = 0; i < MAX_RESULT_COUNT && i < indexInfoList.size(); i++) {
			IndexInfo indexInfo = indexInfoList.get(i);
			PageInfo pageInfo = getPageInfo(indexInfo.getUrl());

			SearchResult searchResult = new SearchResult();
			searchResult.setIndexInfo(indexInfo);
			searchResult.setPageInfo(pageInfo);

			result.add(searchResult);
		}

		return result;
	}

	private List<IndexInfo> getIndexInfo(String keyword) throws IOException {
		// get file name
		String filePath = getFilePath(INDEX_DATA_PREFIX, INDEX_DATA_COUNT, keyword);

		// get position
		long position = getPosition(INDEX_INDEX_PATH, keyword);

		// get line
		String line = readLine(filePath, position);

		// indexes
		int tabIndex = line == null ? -1 : line.indexOf('\t');
		if (tabIndex > -1) {
			line = line.substring(tabIndex + 1);
		}

		return IndexInfo.parse(line);
	}

	private PageInfo getPageInfo(String url) throws IOException {
		// get file name
		String filePath = getFilePath(BBS_DATA_PREFIX, BBS_DADA_COUNT, url);

		// get position
		long position = getPosition(BBS_INDEX_PATH, url);

		// get line
		String line = readLine(filePath, position);

		// page
		return PageInfo.parse(line);
	}

	private String getFilePath(String prefix, int totalFiles, String key) {
		String name = formatter.format((key.hashCode() & Integer.MAX_VALUE) % totalFiles);
		return prefix.concat(name);
	}

	private long getPosition(String filePath, String key) throws IOException {
		key += '\t';
		BufferedReader reader = null;
		long position = -1;

		try {
			reader = FileUtil.read(filePath);
			String line;

			while ((line = reader.readLine()) != null) {
				if (line.startsWith(key)) {
					int start = key.length();
					String text = line.substring(start);
					if (!text.isEmpty()) {
						position = Long.parseLong(text);
					}

					break;
				}
			}

			return position;
		} finally {
			FileUtil.close(reader);
		}
	}

	private String readLine(String filePath, long position) throws IOException {
		if (position < 0) {
			return null;
		}

		FileInputStream stream = null;
		BufferedReader reader = null;

		try {
			stream = new FileInputStream(filePath);
			skip(stream, position);

			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			return reader.readLine();
		} finally {
			FileUtil.close(stream);
			FileUtil.close(reader);
		}
	}

	private void skip(FileInputStream stream, long n) throws IOException {
		if (n <= 0) {
			return;
		}

		long real = stream.skip(n);
		skip(stream, n - real);
	}

}
