package com.ignore.app;

import com.ignore.*;

public class PleaseIgnore implements Game {

	private Shader triangleShader;
	private Texture triangleTexture;
	private Mesh triangle;
	private Shader frameBufferShader;
	private FrameBuffer frameBuffer;

	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	public void create() {
		triangleShader = new Shader("res/shaders/vertex.glsl", "res/shaders/fragment.glsl");
		float[] vertices = {
				  0f,  .5f, .5f, 1f,
				 .5f, -.5f,  1f, 0f,
				-.5f, -.5f,  0f, 0f,
		};
		int[] indices = {
				0, 1, 2,
		};
		triangleTexture = new Texture("res/images/wall.jpg", false);
		triangle = new Mesh(vertices, indices, 2, 2);
		frameBufferShader = new Shader("res/shaders/screen_space.vert", "res/shaders/screen_space_pixelation.frag");
		frameBuffer = new FrameBuffer(WIDTH, HEIGHT);
	}

	public void update(float dt) {
	}

	public void render() {
		frameBuffer.bind();
			Window.clearFrameData();
			triangleShader.bind();
			triangleTexture.bind(0);
			triangle.draw();
			triangleTexture.unbind();
			triangleShader.unbind();
		frameBuffer.unbind();
		Window.clearFrameData();
		frameBufferShader.bind();
		frameBufferShader.setFloat("uPixels", 600f);
		frameBuffer.draw(frameBufferShader);
	}

	public void exit() {
		triangleShader.destroy();
		triangleTexture.destroy();
		triangle.destroy();
		frameBufferShader.destroy();
		frameBuffer.destroy();
	}

	public static void main(String[] args) {
		if (Window.createWindow(WIDTH, HEIGHT, "Please Ignore")) {
			Window.run(new PleaseIgnore());
		}
	}
}
