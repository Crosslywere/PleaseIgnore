package com.crossly.ui;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class KeyInput {

	private final Set<Integer> keysPressed;
	private final Set<Integer> keysPast;

	public final static int
			KEY_ESCAPE  = GLFW_KEY_ESCAPE,
			KEY_F1      = GLFW_KEY_F1,
			KEY_F2      = GLFW_KEY_F2,
			KEY_F3      = GLFW_KEY_F3,
			KEY_F4      = GLFW_KEY_F4,
			KEY_F5      = GLFW_KEY_F5,
			KEY_F6      = GLFW_KEY_F6,
			KEY_F7      = GLFW_KEY_F7,
			KEY_F8      = GLFW_KEY_F8,
			KEY_F9      = GLFW_KEY_F9,
			KEY_F10     = GLFW_KEY_F10,
			KEY_F11     = GLFW_KEY_F11,
			KEY_F12     = GLFW_KEY_F12,
			KEY_GRAVE   = GLFW_KEY_GRAVE_ACCENT,
			KEY_MINUS   = GLFW_KEY_MINUS,
			KEY_EQUAL   = GLFW_KEY_EQUAL,
			KEY_BACKSPACE=GLFW_KEY_BACKSPACE,
			KEY_TAB     = GLFW_KEY_TAB,
			KEY_LSQR_BRK= GLFW_KEY_LEFT_BRACKET,
			KEY_RSQR_BRK= GLFW_KEY_RIGHT_BRACKET,
			KEY_BACKSLASH=GLFW_KEY_BACKSLASH,
			KEY_CAPS    = GLFW_KEY_CAPS_LOCK,
			KEY_SEMI_COL= GLFW_KEY_SEMICOLON,
			KEY_APOSTROPHE=GLFW_KEY_APOSTROPHE,
			KEY_ENTER   = GLFW_KEY_ENTER,
			KEY_LSHIFT  = GLFW_KEY_LEFT_SHIFT,
			KEY_COMMA   = GLFW_KEY_COMMA,
			KEY_PERIOD  = GLFW_KEY_PERIOD,
			KEY_SLASH   = GLFW_KEY_SLASH,
			KEY_RSHIFT  = GLFW_KEY_RIGHT_SHIFT,
			KEY_LCTRL   = GLFW_KEY_LEFT_CONTROL,
			KEY_LALT    = GLFW_KEY_LEFT_ALT,
			KEY_RALT    = GLFW_KEY_RIGHT_ALT,
			KEY_RCTRL   = GLFW_KEY_RIGHT_CONTROL;

	public KeyInput() {
		 keysPressed = new HashSet<>(GLFW_KEY_LAST);
		 keysPast = new HashSet<>(GLFW_KEY_LAST);
	}

	private int properKeycode(int keycode) {
		if (keycode >= (int)'a' && keycode <= (int)'z')
			keycode = Character.toUpperCase(keycode);
		return keycode;
	}

	public boolean isKeyJustPressed(int keycode) {
		keycode = properKeycode(keycode);
		return keysPressed.contains(keycode) && !keysPast.contains(keycode);
	}

	public boolean isKeyHeld(int keycode) {
		keycode = properKeycode(keycode);
		return keysPressed.contains(keycode) && keysPast.contains(keycode);
	}

	public boolean isKeyJustReleased(int keycode) {
		keycode = properKeycode(keycode);
		return !keysPressed.contains(keycode) && keysPast.contains(keycode);
	}

	public boolean isKeyReleased(int keycode) {
		keycode = properKeycode(keycode);
		return !(keysPressed.contains(keycode) || keysPast.contains(keycode));
	}

	public void update() {
		keysPast.clear();
		keysPast.addAll(keysPressed);
	}

	protected void addKey(int key) {
		keysPressed.add(key);
	}

	protected void removeKey(int key) {
		keysPressed.remove(key);
	}
}
