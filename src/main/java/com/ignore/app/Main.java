package com.ignore.app;

import com.ignore.ScreenFrameBuffer;
import com.ignore.Shader;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class Main {
	public void run() {
		if (!glfwInit())
			throw new RuntimeException("Failed to initialize glfw!");

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		long window = glfwCreateWindow(800, 600, "Test Application", 0L, 0L);
		if (window == 0)
			throw new RuntimeException("Failed to create glfw window!");

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		GL.createCapabilities();
		float[] vertices = {
				  0f, .5f, .5f, 1f,
				 .5f,-.5f,  1f, 0f,
				-.5f,-.5f,  0f, 0f,
		};
		int[] indices = {
				0, 1, 2,
		};
		int vao = glGenVertexArrays();
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
		Shader frameBufferShader = new Shader("res/shaders/screen_space.vert", "res/shaders/screen_space_pixelation.frag");
		Shader shader = new Shader("res/shaders/vertex.glsl", "res/shaders/fragment.glsl");
		ScreenFrameBuffer frameBuffer = new ScreenFrameBuffer(800, 600);
		while (!glfwWindowShouldClose(window)) {
			// start frameBuffer
			frameBuffer.bind();
			glClear(GL_COLOR_BUFFER_BIT);
			shader.bind();
			glBindVertexArray(vao);
			glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_INT, 0L);
			glBindVertexArray(0);
			shader.unbind();
			// end frameBuffer
			frameBuffer.unbind();
			// draw frameBuffer texture
			glClear(GL_COLOR_BUFFER_BIT);
			frameBufferShader.bind();
			frameBufferShader.setFloat("uPixels", 128f);
			frameBuffer.draw(frameBufferShader);
			frameBufferShader.unbind();
			glfwSwapBuffers(window);
			glfwPollEvents();
		}
		frameBuffer.destroy();
		shader.destroy();
		glfwDestroyWindow(window);
		glfwTerminate();
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
