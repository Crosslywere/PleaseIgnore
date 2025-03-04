package com.crossly.components.subcomponents;

import static org.lwjgl.opengl.GL33.*;

public class RenderBuffer {

	private int renderBufferId;

	public RenderBuffer(int w, int h) {
		renderBufferId = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, renderBufferId);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, w, h);
	}

	public int getId() {
		return renderBufferId;
	}

	public void delete() {
		glDeleteRenderbuffers(renderBufferId);
		renderBufferId = 0;
	}
}
