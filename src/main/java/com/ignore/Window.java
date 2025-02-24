package com.ignore;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class Window {

	private static final int MAJOR_VERSION = 3;
	private static final int MINOR_VERSION = 3;
	private static long window;
	private static int width, height;
	private static String title;

	public static boolean createWindow(int width, int height, String title) {
		if (!glfwInit()) {
			System.err.println("glfwInit failed!");
			return false;
		}
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, MAJOR_VERSION);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, MINOR_VERSION);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		window = glfwCreateWindow(Window.width = width, Window.height = height, Window.title = title, 0, 0);
		if (window == 0) {
			glfwTerminate();
			System.err.println("glfwCreateWindow failed!");
			return false;
		}
		glfwMakeContextCurrent(window);
		GL.createCapabilities(); // Similar to gladLoadGL in C/C++
		glfwSetFramebufferSizeCallback(window, new GLFWFramebufferSizeCallback() {
			@Override
			public void invoke(long window, int x, int y) {
				Window.width = x;
				Window.height = y;
				glViewport(0, 0, x, y);
			}
		});
		return true;
	}

	public static void run(Game game) {
		game.create();
		float past = (float) glfwGetTime();
		while (!glfwWindowShouldClose(window)) {
			float now = (float) glfwGetTime();
			game.update(past - now);
			game.render();
			glfwSwapBuffers(window);
			glfwPollEvents();
			past = now;
		}
		game.exit();
		cleanup();
	}

	public static void clearFrameData() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
	}

	public static void cleanup() {
		glfwDestroyWindow(window);
		glfwTerminate();
	}
}
