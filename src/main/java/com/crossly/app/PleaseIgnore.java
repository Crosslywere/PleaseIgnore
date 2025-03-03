package com.crossly.app;

import com.crossly.components.*;
import com.crossly.ui.InputSystem;
import com.crossly.ui.Timer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.math.BigInteger;

public class PleaseIgnore extends Application {

	private Mesh triangle;
	private Mesh screenSpaceMesh;
	private ShaderProgram triangleShader;
	private ShaderProgram screenSpaceShader;
	private Texture triangleTexture;
	private FrameBuffer frameBuffer;
	private Camera camera;
	private final Vector2f aspectRatio = new Vector2f();

	public PleaseIgnore() {
		super(800, 600, "Please Ignore");
	}

	@Override
	public void onCreate() {
		triangle = new Mesh(
				new float[]{
						  0f, .5f, 0f,
						-.5f,-.5f, 0f,
						 .5f,-.5f, 0f,
				},
				new float[]{
						.5f,1f,
						0f, 0f,
						1f, 0f,
				}, null);
		screenSpaceMesh = new Mesh(
				new float[] {
						-1f, 1f, 0f,
						-1f,-1f, 0f,
						 1f,-1f, 0f,
						 1f,-1f, 0f,
						 1f, 1f, 0f,
						-1f, 1f, 0f,
				},
				new float[] {
						0f, 1f,
						0f, 0f,
						1f, 0f,
						1f, 0f,
						1f, 1f,
						0f, 1f,
				}, null);
		triangleShader = new ShaderProgram("res/shaders/vertex.glsl", "res/shaders/fragment.glsl");
		screenSpaceShader = new ShaderProgram("res/shaders/screen_space.vert", "res/shaders/screen_space_pixelation.frag");
		triangleTexture = new Texture("res/images/wall.jpg", false);
		frameBuffer = new FrameBuffer(getWidth(), getHeight());
		camera = new Camera(new Vector3f(0f, 0f, -1.3f));
		onResize();
	}

	@Override
	public void onResize() {
		int gcd = BigInteger.valueOf(getWidth()).gcd(BigInteger.valueOf(getHeight())).intValue();
		aspectRatio.x = (float) getWidth() / gcd;
		aspectRatio.y = (float) getHeight() / gcd;
		frameBuffer.delete();
		frameBuffer = new FrameBuffer(getWidth(), getHeight());
	}

	@Override
	public void onUpdate(InputSystem inputs) {
		camera.setRotation(new Vector3f(0f, 0f, Timer.getTimeF() * .5f));
	}

	@Override
	public void onDraw() {
		frameBuffer.bind();
		{
			FrameBuffer.clear();
			triangleShader.bind();
			triangleShader.setMat4("uProjView", camera.getProjectionViewMatrix(aspectRatio.x / aspectRatio.y));
			triangleShader.setMat4("uModel", new Matrix4f());
			triangleShader.setFloat("uLevels", 3f);
			triangleTexture.bind(0);
			triangle.draw();
			FrameBuffer.unbind();
		}
		FrameBuffer.clear();
		screenSpaceShader.setFloat("uPixels", 600f);
		screenSpaceShader.setFloat2("uAspectRatio", aspectRatio.x, aspectRatio.y);
		frameBuffer.draw(screenSpaceShader, screenSpaceMesh);
	}

	public void onExit() {
		triangle.delete();
		screenSpaceMesh.delete();
		triangleShader.delete();
		screenSpaceShader.delete();
		triangleTexture.delete();
		frameBuffer.delete();
	}

	public static void main(String[] args) {
		new PleaseIgnore().play();
	}
}
