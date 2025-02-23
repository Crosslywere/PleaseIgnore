package com.ignore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL33.*;

public class Shader {
	private int program;

	private boolean linked = false;

	private final Map<String, Integer> uniformCache = new HashMap<>();

	public Shader() {
		program = glCreateProgram();
	}

	public Shader(String vertexShader, String fragmentShader) {
		this();
		attachVertexShader(getSource(vertexShader));
		attachFragmentShader(getSource(fragmentShader));
		link();
	}

	public void attachVertexShader(String source) {
		int shader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(shader, source);
		glCompileShader(shader);
		validateShaderCompile(shader);
		glAttachShader(program, shader);
		glDeleteShader(shader);
	}

	public void attachFragmentShader(String source) {
		int shader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(shader, source);
		glCompileShader(shader);
		validateShaderCompile(shader);
		glAttachShader(program, shader);
		glDeleteShader(shader);
	}

	public void bind() {
		link();
		glUseProgram(program);
	}

	public void unbind() {
		glUseProgram(0);
	}

	public void destroy() {
		glDeleteProgram(program);
		program = 0;
	}

	public void link() {
		if (!linked) {
			glLinkProgram(program);
			linked = true;
		}
	}

	private String getSource(String filepath) {
		try {
			BufferedReader readBuffer = new BufferedReader(new FileReader(filepath));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = readBuffer.readLine()) != null) {
				builder.append(line).append('\n');
			}
			readBuffer.close();
			return builder.toString();
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		return "/* Exception was thrown */";
	}

	private void validateShaderCompile(int shader) {
		int[] success = new int[1];
		glGetShaderiv(shader, GL_COMPILE_STATUS, success);
		if (success[0] == 0) {
			String infoLog = glGetShaderInfoLog(shader);
			System.err.println(infoLog);
		}
	}

	private int getUniformLocation(String name) {
		if (uniformCache.containsKey(name)) {
			return uniformCache.get(name);
		}
		int loc = glGetUniformLocation(program, name);
		uniformCache.put(name, loc);
		return loc;
	}

	public void setInt(String name, int value) {
		glUniform1i(getUniformLocation(name), value);
	}

	public void setFloat(String name, float value) {
		glUniform1f(getUniformLocation(name), value);
	}

	public void setFloat2(String name, float vx, float vy) {
		glUniform2f(getUniformLocation(name), vx, vy);
	}
}
