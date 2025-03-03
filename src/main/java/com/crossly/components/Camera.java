package com.crossly.components;

import com.crossly.components.subcomponents.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera extends Transform {

	private float fov;

	public Camera() {
		super();
		fov = 45f;
	}

	public Camera(Vector3f position) {
		this();
		super.setPosition(position);
	}

	public Matrix4f getProjectionViewMatrix(float aspect) {
		return new Matrix4f().perspective(fov, aspect, 0.3f, 100f)
				.mul(getModelMatrix());
	}

	public float getFov() {
		return fov;
	}

	public void setFov(float fov) {
		this.fov = fov;
	}
}
