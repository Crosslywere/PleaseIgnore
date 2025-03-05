package com.crossly.components.subcomponents;

import com.crossly.util.FileUtil;

import static org.lwjgl.opengl.GL33.*;

public class Shader {

	private int shaderId;

	public static final int
			VERT_SHADER = GL_VERTEX_SHADER,
			FRAG_SHADER = GL_FRAGMENT_SHADER;

	public Shader(String filepath, int type) {
		shaderId = glCreateShader(type);
		glShaderSource(shaderId, FileUtil.getShaderFileSourceFlat(filepath));
		glCompileShader(shaderId);
		validateShader();
	}

	public int getId() {
		return shaderId;
	}

	public void delete() {
		glDeleteShader(shaderId);
		shaderId = 0;
	}

	private void validateShader() {
		int[] success = new int[1];
		glGetShaderiv(shaderId, GL_COMPILE_STATUS, success);
		if (success[0] == 0) {
			throw new RuntimeException(glGetShaderInfoLog(shaderId));
		}
	}
}
