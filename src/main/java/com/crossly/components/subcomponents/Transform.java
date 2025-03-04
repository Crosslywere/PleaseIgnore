package com.crossly.components.subcomponents;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
	private Transform parent = null;
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f scale;

	public Transform() {
		position = new Vector3f();
		rotation = new Vector3f();
		scale = new Vector3f(1f);
	}

	public Matrix4f getModelMatrix() {
		var matrix = new Matrix4f()
				.translate(position)
				.rotateXYZ(rotation)
				.scale(scale);
		if (parent == null) {
			return matrix;
		}
		return parent.getModelMatrix().mul(matrix);
	}

	public final Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public final Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public final Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public void setParent(Transform parent) {
		this.parent = parent;
	}
}
