package com.crossly.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

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

	public static String getShaderFileSourceFlat(String filepath) {
		Scanner scanner = new Scanner(getFileSource(filepath));
		StringBuilder builder = new StringBuilder();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.trim().startsWith("#include")) {
				line = line.trim().substring(9).trim();
				line = line.replace('"', ' ').trim();
				String includePath = "./";
				if (filepath.contains("/") || filepath.contains("\\")) {
					int index = filepath.lastIndexOf('/') >= 0 ? filepath.lastIndexOf('/') : filepath.lastIndexOf('\\');
					includePath = filepath.substring(0, index) + File.separator;
				}
				includePath += line;
				builder.append(getShaderFileSourceFlat(includePath));
			} else {
				builder.append(line).append('\n');
			}
		}
		return builder.toString();
	}
}
