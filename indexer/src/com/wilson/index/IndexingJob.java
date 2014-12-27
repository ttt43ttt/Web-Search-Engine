package com.wilson.index;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.HashPartitioner;
import org.wltea.analyzer.core.Lexeme;

import com.wilson.segment.TextSegmenter;

public class IndexingJob {

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
		private Text word = new Text();
		private Text info = new Text();

		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
			String line = value.toString();

			// parse line
			String[] fields = parseLine(line);
			if (fields.length < 6) {
				return;
			}

			String url = fields[0];
			String title = fields[1];
			// String author = fields[2];
			// String time = fields[3];
			// String ip = fields[4];
			String content = fields[5];

			if (url == null || title == null || content == null) {
				return;
			}

			// segment words
			List<Lexeme> lexemes = TextSegmenter.getLexemes(content);
			for (Lexeme lexeme : lexemes) {
				// key
				String kw = lexeme.getLexemeText();

				if (lexeme.getLexemeType() == Lexeme.TYPE_CNWORD && kw.length() > 1) {
					word.set(kw);

					// value
					StringBuilder sb = new StringBuilder();
					sb.append(url);
					sb.append(';');
					sb.append(1);
					sb.append(';');
					sb.append(lexeme.getBeginPosition());

					info.set(sb.toString());

					// collect
					output.collect(word, info);
					// System.out.println("map: " + word.toString() + " " + info.toString());
				}
			}
		}

		private String[] parseLine(String line) {
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

			return fields;
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
		private Text info = new Text();

		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
			HashMap<String, Integer> ranks = new HashMap<>();
			HashMap<String, StringBuilder> positions = new HashMap<>();

			while (values.hasNext()) {
				String value = values.next().toString();
				String[] list = value.split(";");

				for (int i = 0; i + 2 < list.length; i += 3) {
					String url = list[i];

					Integer rank = ranks.get(url);
					if (rank == null) {
						rank = 0;
					}

					rank += Integer.parseInt(list[i + 1]);
					ranks.put(url, rank);

					StringBuilder positionBuilder = positions.get(url);
					if (positionBuilder == null) {
						positionBuilder = new StringBuilder();
						positions.put(url, positionBuilder);
					}

					if (positionBuilder.length() > 0) {
						positionBuilder.append(",");
					}
					positionBuilder.append(list[i + 2]);
				}
			}

			// sort by rank
			List<Entry<String, Integer>> sortedRanks = new ArrayList<>(ranks.entrySet());

			Collections.sort(sortedRanks, new Comparator<Entry<String, Integer>>() {

				@Override
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					return o2.getValue() - o1.getValue();
				}
			});

			StringBuilder sb = new StringBuilder();

			for (Entry<String, Integer> entry : sortedRanks) {
				String url = entry.getKey();
				sb.append(url);
				sb.append(';');
				sb.append(entry.getValue());
				sb.append(';');
				sb.append(positions.get(url).toString());
				sb.append(';');
			}

			info.set(sb.toString());
			output.collect(key, info);
			// System.out.println("reduce: " + key.toString() + " " + info.toString());
		}
	}

	public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(IndexingJob.class);
		conf.setJobName("indexing");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);

		conf.setPartitionerClass(HashPartitioner.class);
		conf.setNumReduceTasks(3);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
	}
}
