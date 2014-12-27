package com.wilson.common;

import java.util.ArrayList;
import java.util.List;

public class IndexInfo {
	private final static String DELIMITER = ";";
	private final static String POS_DELIMITER = ",";

	private String url;
	private int rank;
	private List<Integer> positions;

	public static List<IndexInfo> parse(String line) {
		List<IndexInfo> result = new ArrayList<IndexInfo>();

		if (line == null || line.isEmpty()) {
			return result;
		}

		String[] list = line.split(DELIMITER);

		for (int i = 0; i + 2 < list.length; i += 3) {
			IndexInfo info = new IndexInfo();

			// url
			info.url = list[i];

			// rank
			info.rank = Integer.parseInt(list[i + 1]);

			// positions
			String posText = list[i + 2];
			String[] posList = posText.split(POS_DELIMITER);
			for (String pos : posList) {
				if (!pos.isEmpty()) {
					info.addPosition(Integer.parseInt(pos));
				}
			}

			result.add(info);
		}

		return result;
	}

	public IndexInfo() {
		positions = new ArrayList<Integer>();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		sb.append(DELIMITER);
		sb.append(rank);
		sb.append(DELIMITER);

		for (int i = 0; i < positions.size(); i++) {
			if (i > 0) {
				sb.append(POS_DELIMITER);
			}

			sb.append(positions.get(i));
		}

		return sb.toString();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public void addPosition(int position) {
		positions.add(position);
	}

	public List<Integer> getPositions() {
		return positions;
	}
}
