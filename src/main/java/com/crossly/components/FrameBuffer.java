package com.crossly.components;

import com.crossly.components.subcomponents.Mesh;
import com.crossly.components.subcomponents.RenderBuffer;
import com.crossly.components.subcomponents.SubTexture;

import static org.lwjgl.opengl.GL33.*;

public class FrameBuffer {

	private final int frameBufferId;
	private final SubTexture renderTarget;
	private final RenderBuffer renderBuffer;

	public FrameBuffer(int w, int h) {
		frameBufferId = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId);
		renderTarget = new SubTexture(w, h);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, renderTarget.getId(), 0);
		renderBuffer = new RenderBuffer(w, h);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, renderBuffer.getId());
		Texture.unbind();
		unbind();
	}

	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId);
	}

	public static void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public void delete() {
		glDeleteFramebuffers(frameBufferId);
		renderTarget.delete();
		renderBuffer.delete();
	}

	public void draw(ShaderProgram shader, Mesh mesh) {
		shader.bind();
		renderTarget.bind(0);
		shader.setInt("uRenderTexture", 0);
		mesh.draw();
		Texture.unbind();
	}

	public static void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
	}

	public static void clearColor(float r, float g, float b) {
		glClearColor(r, g, b, 1f);
	}
}
