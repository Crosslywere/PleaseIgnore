package com.crossly.components.subcomponents;

import org.lwjgl.opengl.GL15;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL33.*;

public class Mesh {

	private int vertexArrayId, elementCount;
	private final boolean useIndices;
	private final ArrayList<Integer> buffers = new ArrayList<>();

	public Mesh(float[] positions, float[] texCoords, float[] normals) {
		useIndices = false;
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

	// Construct as a tightly packed set of data
	public Mesh(float[] positions, float[] texCoords, float[] normals, int[] indices) {
		useIndices = true;
		vertexArrayId = glGenVertexArrays();
		glBindVertexArray(vertexArrayId);
		elementCount = indices.length;
		int pos_ao = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, pos_ao);
		glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0L);
		glEnableVertexAttribArray(0);
		buffers.add(pos_ao);
		if (texCoords != null) {
			int tex_ao = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, tex_ao);
			glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0L);
			glEnableVertexAttribArray(1);
			buffers.add(tex_ao);
		}
		if (normals != null) {
			int norm_ao = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, norm_ao);
			glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
			glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0L);
			glEnableVertexAttribArray(2);
			buffers.add(norm_ao);
		}
		int elem_eo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elem_eo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		buffers.add(elem_eo);
		unbind();
	}

	// Construct as an interlaced set of data
	public Mesh(float[] vertices, int[] indices) {
		useIndices = true;
		vertexArrayId = glGenVertexArrays();
		glBindVertexArray(vertexArrayId);
		elementCount = indices.length;
		int vertexBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		int elementBuffer = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBuffer);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES * 8, 0L);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Float.BYTES * 8, Float.BYTES * 3L);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Float.BYTES * 8, Float.BYTES * 5L);
		glEnableVertexAttribArray(2);
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
		if (!useIndices) {
			glDrawArrays(GL_TRIANGLES, 0, elementCount);
		} else {
			glDrawElements(GL_TRIANGLES, elementCount, GL_UNSIGNED_INT, 0L);
		}
	}

	public void delete() {
		buffers.forEach(GL15::glDeleteBuffers);
		glDeleteVertexArrays(vertexArrayId);
		vertexArrayId = 0;
		elementCount = 0;
	}
}
