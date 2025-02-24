package com.ignore;

public interface Game {
	// Setup game objects
	void create();
	// Perform game updates for objects
	void update(float dt);
	// Render game objects
	void render();
	// Clean up
	void exit();
}
