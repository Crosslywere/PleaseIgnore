package com.crossly.ui;

public class InputSystem {

	private final KeyInput keyInput;
	private final MouseInput mouseInput;

	public InputSystem() {
		mouseInput = new MouseInput();
		keyInput = new KeyInput();
	}

	public void update() {
		keyInput.update();
		mouseInput.update();
	}

	public KeyInput getKeyInput() {
		return keyInput;
	}

	public MouseInput getMouseInput() {
		return mouseInput;
	}
}
