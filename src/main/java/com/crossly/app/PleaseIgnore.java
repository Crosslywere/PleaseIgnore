package com.crossly.app;

import com.crossly.components.Camera;
import com.crossly.components.FrameBuffer;
import com.crossly.components.Model;
import com.crossly.components.ShaderProgram;
import com.crossly.components.subcomponents.Mesh;
import com.crossly.ui.InputSystem;
import com.crossly.ui.Timer;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.math.BigInteger;

public class PleaseIgnore extends Application {

	private Model model;
	private Mesh screenSpaceMesh;
	private ShaderProgram objectShader;
	private ShaderProgram screenSpaceShader;
	private FrameBuffer frameBuffer;
	private Camera camera;
	private final Vector2f aspectRatio = new Vector2f();

	private float accumTime = 0f;
	private boolean accumes = true;
	private int pixelDensity = 512;

	public PleaseIgnore() {
		super(800, 600, "Please Ignore", true);
	}

	@Override
	public void onCreate() {
		model = new Model("res/models/backpack.obj");
		model.getTransform().setScale(new Vector3f(.1f));
		screenSpaceMesh = new Mesh(
				new float[] {
						-1f, 1f, 0f, 0f, 1f, 0f, 0f, 0f,
						-1f,-1f, 0f, 0f, 0f, 0f, 0f, 0f,
						 1f,-1f, 0f, 1f, 0f, 0f, 0f, 0f,
						 1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
				},
				new int[] {
						0, 1, 2,
						2, 3, 0,
				});
		objectShader = new ShaderProgram("res/shaders/model.vert", "res/shaders/model.frag");
		screenSpaceShader = new ShaderProgram("res/shaders/screen_space.vert", "res/shaders/screen_space_pixelation.frag");
		frameBuffer = new FrameBuffer(getWidth(), getHeight());
		camera = new Camera(new Vector3f(0f, 0f, -1.3f));
		onResize();
		System.out.println("Controls\n========\n1 - Large pixels\n2 - Medium pixels\n3 - Small pixels\n'Space' - Pause/Play rotation");
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
		if (inputs.getKeyInput().isKeyJustReleased(' '))
			accumes = !accumes;
		if (accumes)
			accumTime += Timer.getDeltaTimeF();
		model.getTransform().setRotation(new Vector3f(0f, accumTime, 0f));

		if (inputs.getKeyInput().isKeyJustPressed('1'))
			pixelDensity = 512;
		if (inputs.getKeyInput().isKeyJustPressed('2'))
			pixelDensity = 512 + 256;
		if (inputs.getKeyInput().isKeyJustPressed('3'))
			pixelDensity = 2048;
	}

	@Override
	public void onDraw() {
		frameBuffer.bind();
		{
			FrameBuffer.clear();
			objectShader.bind();
			objectShader.setMat4("uProjView", camera.getProjectionViewMatrix(aspectRatio.x / aspectRatio.y));
			objectShader.setFloat("uLevels", 3f);
			objectShader.setFloat3("uViewPos", camera.getTransform().getPosition());
			objectShader.setFloat3("uLightPos", new Vector3f(1f, 13f, 7f));
			model.draw(objectShader);
			FrameBuffer.unbind();
		}
		FrameBuffer.clear();
		screenSpaceShader.setFloat("uPixels", aspectRatio.y * pixelDensity);
		screenSpaceShader.setFloat2("uAspectRatio", aspectRatio);
		frameBuffer.draw(screenSpaceShader, screenSpaceMesh);
	}

	public void onExit() {
		model.delete();
		screenSpaceMesh.delete();
		objectShader.delete();
		screenSpaceShader.delete();
		frameBuffer.delete();
	}

	public static void main(String[] args) {
		new PleaseIgnore().play();
	}
}
