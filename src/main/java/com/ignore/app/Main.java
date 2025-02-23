package com.ignore.app;

import com.ignore.FrameBuffer;
import com.ignore.Mesh;
import com.ignore.Shader;
import com.ignore.Texture;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL33.glClear;

public class Main {
	public void run() {
		if (!glfwInit())
			throw new RuntimeException("Failed to initialize glfw!");

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		long window = glfwCreateWindow(800, 600, "PleaseIgnore", 0L, 0L);
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
		Mesh mesh = new Mesh(vertices, indices, 2, 2);
		Shader frameBufferShader = new Shader("res/shaders/screen_space.vert", "res/shaders/screen_space_pixelation.frag");
		Shader shader = new Shader("res/shaders/vertex.glsl", "res/shaders/fragment.glsl");
		FrameBuffer frameBuffer = new FrameBuffer(800, 600);
		Texture texture = new Texture("res/images/wall.jpg", false);
		while (!glfwWindowShouldClose(window)) {
			// start frameBuffer
			frameBuffer.bind();
			glClear(GL_COLOR_BUFFER_BIT);
			shader.bind();
			shader.setInt("uTexture", 0);
			texture.bind(0);
			mesh.draw();
			texture.unbind();
			shader.unbind();
			// end frameBuffer
			frameBuffer.unbind();
			// draw frameBuffer texture
			glClear(GL_COLOR_BUFFER_BIT);
			frameBufferShader.bind();
			frameBufferShader.setFloat("uPixels", 512f);
			frameBuffer.draw(frameBufferShader);
			frameBufferShader.unbind();
			glfwSwapBuffers(window);
			glfwPollEvents();
		}
		texture.destroy();
		mesh.destroy();
		frameBuffer.destroy();
		shader.destroy();
		glfwDestroyWindow(window);
		glfwTerminate();
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
