package com.ignore;

import java.math.BigInteger;

import static org.lwjgl.opengl.GL33.*;

public class ScreenFrameBuffer {

	private static final float[] vertices = {
		-1f, 1f, 0f, 1f,
		-1f,-1f, 0f, 0f,
		 1f,-1f, 1f, 0f,
		 1f, 1f, 1f, 1f,
	};

	private static final int[] indices = {
			0, 1, 2,
			2, 3, 0,
	};

	private static final int vao;

	static {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		int vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		int ebo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, Float.BYTES * 4, 0L);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Float.BYTES * 4, Float.BYTES * 2L);
		glEnableVertexAttribArray(1);
	}

	private final int width, height, gcd;

	private int fbo, texture, rbo;

	public ScreenFrameBuffer(int w, int h) {
		width = w;
		height = h;
		gcd = BigInteger.valueOf(width).gcd(BigInteger.valueOf(height)).intValue();
		fbo = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		// Color buffer object creation and attachment as texture
		texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);
		// Render buffer object creation and attachment
		rbo = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, rbo);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rbo);

		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
	}

	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public void destroy() {
		glDeleteRenderbuffers(rbo);
		glDeleteTextures(texture);
		glDeleteFramebuffers(fbo);
		rbo = 0;
		texture = 0;
		fbo = 0;
	}

	public void draw(Shader shader) {
		shader.bind();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture);
		shader.setInt("uTexture0", 0);
		shader.setFloat2("uAspectRatio", (float) width / gcd, (float) height / gcd);
		glBindVertexArray(vao);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0L);
		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
