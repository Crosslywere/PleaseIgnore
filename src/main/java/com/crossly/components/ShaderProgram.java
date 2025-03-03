package com.crossly.components;

import com.crossly.components.subcomponents.Shader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL33.*;

public class ShaderProgram {

	private int programId;
	private final Map<String, Integer> uniformCache = new HashMap<>();

	public ShaderProgram(Shader vertShader, Shader fragShader) {
		programId = glCreateProgram();
		glAttachShader(programId, vertShader.getId());
		glAttachShader(programId, fragShader.getId());
		glLinkProgram(programId);
		validateProgram();
	}

	public ShaderProgram(String vertFilepath, String fragFilepath) {
		this(new Shader(vertFilepath, Shader.VERT_SHADER), new Shader(fragFilepath, Shader.FRAG_SHADER));
	}

	public void bind() {
		glUseProgram(programId);
	}

	public static void unbind() {
		glUseProgram(0);
	}

	public void delete() {
		glDeleteProgram(programId);
		programId = 0;
	}

	public void setInt(String name, int value) {
		bind();
		glUniform1i(getUniformLocation(name), value);
	}

	public void setFloat(String name, float value) {
		bind();
		glUniform1f(getUniformLocation(name), value);
	}

	public void setFloat2(String name, float x, float y) {
		bind();
		glUniform2f(getUniformLocation(name), x, y);
	}

	public void setFloat2(String name, Vector2f v2f) {
		setFloat2(name, v2f.x, v2f.y);
	}

	public void setFloat3(String name, float x, float y, float z) {
		bind();
		glUniform3f(getUniformLocation(name), x, y, z);
	}

	public void setFloat3(String name, Vector3f v3f) {
		setFloat3(name, v3f.x, v3f.y, v3f.z);
	}

	public void setMat4(String name, Matrix4f mat4) {
		float[] data = new float[16];
		mat4.get(data);
		bind();
		glUniformMatrix4fv(getUniformLocation(name), false, data);
	}

	private int getUniformLocation(String name) {
		if (uniformCache.containsKey(name))
			return uniformCache.get(name);
		int location = glGetUniformLocation(programId, name);
		uniformCache.put(name, location);
		return location;
	}

	private void validateProgram() {
		int[] success = new int[1];
		glGetProgramiv(programId, GL_LINK_STATUS, success);
		if (success[0] == 0) {
			throw new RuntimeException(glGetProgramInfoLog(programId));
		}
	}
}
