package com.ignore;

import static org.lwjgl.opengl.GL33.*;

public class Mesh {

	private int vao, vbo, ebo;

	private final int count;

	public Mesh(float[] vertices, int[] indices, int ...offsetPointers) {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		ebo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		int stride = 0;
		for (int offset : offsetPointers) {
			stride += offset;
		}
		stride *= Float.BYTES;
		for (int i = 0, offset = 0; i < offsetPointers.length; i++) {
			glVertexAttribPointer(i, offsetPointers[i], GL_FLOAT, false, stride, (long) Float.BYTES * offset);
			glEnableVertexAttribArray(i);
			offset += offsetPointers[i];
		}
		count = indices.length;
		glBindVertexArray(0);
	}

	public void bind() {
		glBindVertexArray(vao);
	}

	public void unbind() {
		glBindVertexArray(0);
	}

	public void destroy() {
		glDeleteBuffers(ebo);
		glDeleteBuffers(vbo);
		glDeleteVertexArrays(vao);
		ebo = 0;
		vbo = 0;
		vao = 0;
	}

	public void draw() {
		bind();
		glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0L);
		unbind();
	}

	public void draw(int primCount) {
		bind();
		glDrawElementsInstanced(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0L, primCount);
		unbind();
	}
}
