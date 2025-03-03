package com.crossly.ui;

import org.joml.Vector2d;
import org.joml.Vector2f;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LAST;

public class MouseInput {

	public static final int
			LEFT_MOUSE      = 0,
			RIGHT_MOUSE     = 1,
			MIDDLE_MOUSE    = 2,
			BACK_MOUSE      = 3,
			FRONT_MOUSE     = 4;

	private final Set<Integer> mouseButtons;
	private Vector2d scrollAmount;
	private final Vector2d mousePos;

	public MouseInput() {
		mouseButtons = new HashSet<>(GLFW_MOUSE_BUTTON_LAST);
		scrollAmount = new Vector2d();
		mousePos = new Vector2d();
	}

	protected void setMousePos(double x, double y) {
		mousePos.x = x;
		mousePos.y = y;
	}

	public Vector2f getMousePos() {
		return new Vector2f((float) mousePos.x, (float) mousePos.y);
	}

	protected void setScrollAmount(double xoffset, double yoffset) {
		scrollAmount.x = xoffset;
		scrollAmount.y = yoffset;
	}

	public int getScroll() {
		return (int) scrollAmount.y;
	}

	public int getScrollX() {
		return (int) scrollAmount.x;
	}

	public void update() {
		scrollAmount = new Vector2d();
	}

	protected void addMouseButton(int button) {
		mouseButtons.add(button);
	}

	protected void removeMouseButton(int button) {
		mouseButtons.remove(button);
	}

	public boolean isButtonPressed(int button) {
		return mouseButtons.contains(button);
	}

	public boolean isButtonReleased(int button) {
		return !isButtonPressed(button);
	}
}
