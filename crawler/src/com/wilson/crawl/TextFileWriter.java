package com.wilson.crawl;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class TextFileWriter {
	// private final String fileName;
	private final BufferedWriter writer;

	private final static Map<String, TextFileWriter> map = new HashMap<>();

	public synchronized static TextFileWriter getFileWriter(String fileName) throws IOException {
		TextFileWriter writer = map.get(fileName);

		if (writer == null) {
			writer = new TextFileWriter(fileName);
			map.put(fileName, writer);
		}

		return writer;
	}

	public static void close(String fileName) {
		TextFileWriter writer = map.get(fileName);
		if (writer != null) {
			writer.close();
		}
	}

	private TextFileWriter(String fileName) throws IOException {
		// this.fileName = fileName;
		this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8"));
	}

	public synchronized void appendLine(String content) throws IOException {
		writer.append(content);
		writer.newLine();
	}

	private void close() {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
