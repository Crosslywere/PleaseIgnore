package com.crossly.ui;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Timer {

	private static double now = 0.0;
	private static double deltaTime = 0.0;

	public static void update() {
		double past = now;
		now = glfwGetTime();
		deltaTime = now - past;
	}

	public static double getTime() {
		return now;
	}

	public static float getTimeF() {
		return (float) now;
	}

	public static double getDeltaTime() {
		return deltaTime;
	}

	public static float getDeltaTimeF() {
		return (float) deltaTime;
	}

}
