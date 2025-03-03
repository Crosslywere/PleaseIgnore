package com.crossly.components;

import static org.lwjgl.opengl.GL33.*;

public class Mesh {

	private int vertexArrayId, elementCount;

	public Mesh(float[] positions, float[] texCoords, float[] normals) {
		vertexArrayId = glGenVertexArrays();
		glBindVertexArray(vertexArrayId);
		elementCount = positions.length / 3;
		int pos_ao = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, pos_ao);
		glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0L);
		glEnableVertexAttribArray(0);
		if (texCoords != null) {
			int tex_ao = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, tex_ao);
			glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0L);
			glEnableVertexAttribArray(1);
		}
		if (normals != null) {
			int norm_ao = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, norm_ao);
			glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
			glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0L);
			glEnableVertexAttribArray(2);
		}
		unbind();
	}

	public void bind() {
		glBindVertexArray(vertexArrayId);
	}

	public static void unbind() {
		glBindVertexArray(0);
	}

	public void draw() {
		bind();
		glDrawArrays(GL_TRIANGLES, 0, elementCount);
	}

	public void delete() {
		glDeleteVertexArrays(vertexArrayId);
		vertexArrayId = 0;
		elementCount = 0;
	}
}
