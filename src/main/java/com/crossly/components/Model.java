package com.crossly.components;

import com.crossly.components.subcomponents.Mesh;
import com.crossly.components.subcomponents.Transform;
import com.crossly.util.FileUtil;
import org.lwjgl.assimp.*;

import java.util.ArrayList;

public class Model {

	private final Transform transform;
	private final ArrayList<Mesh> meshes = new ArrayList<>();

	public Model(String filepath) {
		transform = new Transform();
		try (AIScene scene = Assimp.aiImportFile(FileUtil.getRealFilepath(filepath), Assimp.aiProcess_Triangulate)) {
			if (scene == null) {
				throw new RuntimeException("Model loading failed! " + Assimp.aiGetErrorString());
			}
			AINode root;
			if ((scene.mFlags() & Assimp.AI_SCENE_FLAGS_INCOMPLETE) != 0 || (root = scene.mRootNode()) == null) {
				throw new RuntimeException("Model loading failed! " + Assimp.aiGetErrorString());
			}
			processNode(root, scene);
		}
	}

	private void processNode(AINode node, AIScene scene) {
		for (int i = 0; i < node.mNumMeshes(); i++) {
			long mesh = scene.mMeshes().get(node.mMeshes().get(i));
			meshes.add(processMesh(AIMesh.create(mesh)));
		}
		for (int i = 0; i < node.mNumChildren(); i++) {
			AINode next = AINode.create(node.mChildren().get(i));
			processNode(next, scene);
		}
	}

	private Mesh processMesh(AIMesh mesh) {
		// Interlaced position, texture-coordinate, and normal data
		float[] vertices = new float[mesh.mNumVertices() * 8];
		AIVector3D.Buffer normalBuffer = mesh.mNormals();
		AIVector3D.Buffer texCoordBuffer = mesh.mTextureCoords(0);
		for (int i = 0; i < mesh.mNumVertices(); i++) {
			vertices[i * 8] = mesh.mVertices().get(i).x();
			vertices[1 + i * 8] = mesh.mVertices().get(i).y();
			vertices[2 + i * 8] = mesh.mVertices().get(i).z();
			if (texCoordBuffer != null) {
				vertices[3 + i * 8] = texCoordBuffer.get(i).x();
				vertices[4 + i * 8] = texCoordBuffer.get(i).y();
			}
			if (normalBuffer != null) {
				vertices[5 + i * 8] = normalBuffer.get(i).x();
				vertices[6 + i * 8] = normalBuffer.get(i).y();
				vertices[7 + i * 8] = normalBuffer.get(i).z();
			}
		}
		ArrayList<Integer> indicesList = new ArrayList<>();
		for (int i = 0; i < mesh.mNumFaces(); i++) {
			AIFace face = mesh.mFaces().get(i);
			for (int j = 0; j < face.mNumIndices(); j++) {
				indicesList.add(face.mIndices().get(j));
			}
		}
		int[] indices = new int[indicesList.size()];
		for (int i = 0; i < indicesList.size(); i++) {
			indices[i] = indicesList.get(i);
		}
		return new Mesh(vertices, indices);
	}

	public void draw(ShaderProgram shader) {
		shader.setMat4("uModel", transform.getModelMatrix());
		meshes.forEach(Mesh::draw);
	}

	public void delete() {
		meshes.forEach(Mesh::delete);
	}

	public Transform getTransform() {
		return transform;
	}
}
