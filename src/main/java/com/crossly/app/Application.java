package com.crossly.app;

import com.crossly.ui.InputSystem;
import com.crossly.ui.Timer;
import com.crossly.ui.Window;

public abstract class Application {

	private int width, height;
	private final String title;
	private boolean resizable = false;

	public Application(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
	}

	public Application(int width, int height, String title, boolean resizable) {
		this(width, height, title);
		this.resizable = resizable;
	}

	public final int getWidth() {
		return width;
	}

	public final void setWidth(int width) {
		this.width = width;
	}

	public final int getHeight() {
		return height;
	}

	public final void setHeight(int height) {
		this.height = height;
	}

	public final String getTitle() {
		return title;
	}

	public final boolean isResizable() {
		return resizable;
	}

	public abstract void onCreate();

	public abstract void onUpdate(InputSystem inputs);

	public abstract void onDraw();

	public void onExit() {
	}

	public void onResize() {
	}

	public final void play() {
		InputSystem inputs = new InputSystem();
		Window window = new Window(this, inputs);
		onCreate();
		while (window.isOpen()) {
			onUpdate(inputs);
			onDraw();
			inputs.update();
			Timer.update();
			window.endFrame();
		}
		onExit();
		window.cleanUp();
	}
}
