package com.crossly.components;

import com.crossly.components.subcomponents.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

	private Transform transform;
	private float fov;

	public Camera() {
		transform = new Transform();
		fov = 45f;
	}

	public Camera(Vector3f position) {
		this();
		transform.setPosition(position);
	}

	public Matrix4f getProjectionViewMatrix(float aspect) {
		return new Matrix4f().perspective(fov, aspect, 0.3f, 100f)
				.mul(transform.getModelMatrix());
	}

	public Transform getTransform() {
		return transform;
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}

	public float getFov() {
		return fov;
	}

	public void setFov(float fov) {
		this.fov = fov;
	}
}
