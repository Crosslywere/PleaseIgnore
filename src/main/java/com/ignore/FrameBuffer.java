package com.ignore;

import java.math.BigInteger;

import static org.lwjgl.opengl.GL33.*;

public class FrameBuffer {

	private static final Mesh screenMesh;

	static {
		float[] vertices = {
				-1f, 1f, 0f, 1f,
				-1f,-1f, 0f, 0f,
				1f,-1f, 1f, 0f,
				1f, 1f, 1f, 1f,
		};
		int[] indices = {
				0, 1, 2,
				2, 3, 0,
		};
		screenMesh = new Mesh(vertices, indices, 2, 2);
	}

	private final int width, height, gcd;

	private int fbo, texture, rbo;

	public FrameBuffer(int w, int h) {
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
		screenMesh.draw();
//		glBindVertexArray(vao);
//		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0L);
//		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
