package com.wilson.index;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.HashPartitioner;

public class PartitionJob {

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
		private Text keyword = new Text();
		private Text others = new Text();

		@Override
		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
			String line = value.toString();
			int end = line.indexOf('\t');
			if (end == -1) {
				return;
			}

			keyword.set(line.substring(0, end));
			others.set(line.substring(end + 1));
			output.collect(keyword, others);
		}

	}

	public static class KeyWordPartitioner extends HashPartitioner<Text, Text> {

		@Override
		public int getPartition(Text key, Text value, int numReduceTasks) {
			return (key.toString().hashCode() & Integer.MAX_VALUE) % numReduceTasks;
		}

	}

	public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text> {

		@Override
		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
			if (values.hasNext()) {
				output.collect(key, values.next());
			}
		}

	}

	public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(PartitionJob.class);
		conf.setJobName("PartitionJob");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		
		conf.setPartitionerClass(KeyWordPartitioner.class);
		conf.setNumReduceTasks(10);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
	}
}
