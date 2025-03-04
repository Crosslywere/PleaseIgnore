package com.crossly.ui;

import com.crossly.app.Application;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class Window {

	private final long window;

	public Window(Application app, InputSystem inputs) {
		if (!glfwInit())
			throw new RuntimeException("glfwInit failed!");
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		if (!app.isResizable())
			glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		window = glfwCreateWindow(app.getWidth(), app.getHeight(), app.getTitle(), 0L, 0L);
		if (window == 0)
			throw new RuntimeException("glfwCreateWindow failed!");
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		GL.createCapabilities();
		if (app.isResizable()) {
			glfwSetFramebufferSizeCallback(window, new GLFWFramebufferSizeCallback() {
				@Override
				public void invoke(long window, int width, int height) {
					app.setWidth(width);
					app.setHeight(height);
					app.onResize();
					glViewport(0, 0, width, height);
				}
			});
		}
		initKeyInput(inputs.getKeyInput());
		initMouseInput(inputs.getMouseInput());
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
	}

	public boolean isOpen() {
		return !glfwWindowShouldClose(window);
	}

	public void endFrame() {
		glfwSwapBuffers(window);
		glfwPollEvents();
	}

	public void cleanUp() {
		glfwDestroyWindow(window);
		glfwTerminate();
	}

	public void initKeyInput(KeyInput keyInput) {
		glfwSetKeyCallback(window, new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action >= GLFW_PRESS) {
					keyInput.addKey(key);
				} else {
					keyInput.removeKey(key);
				}
			}
		});
	}

	public void initMouseInput(MouseInput mouseInput) {
		glfwSetCursorPosCallback(window, new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				mouseInput.setMousePos(xpos, ypos);
			}
		});
		glfwSetScrollCallback(window, new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				mouseInput.setScrollAmount(xoffset, yoffset);
			}
		});
		glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				if (action == GLFW_PRESS) {
					mouseInput.addMouseButton(button);
				} else {
					mouseInput.removeMouseButton(button);
				}
			}
		});
	}
}
