package com.crossly.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtil {
	public static String getRealFilepath(String filepath) {
		var url = FileUtil.class.getClassLoader().getResource(filepath);
		if (url != null) {
			filepath = url.getPath().substring(1);
		}
		return filepath;
	}

	public static String getFileSource(String filepath) {
		filepath = getRealFilepath(filepath);
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(filepath));
			String line;
			while ((line = buffer.readLine()) != null) {
				builder.append(line).append('\n');
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		return builder.toString();
	}
}
