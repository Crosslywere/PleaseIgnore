package com.crossly.components;

import com.crossly.util.FileUtil;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

	protected int textureId = 0;

	private int width = -1, height = -1;

	protected Texture(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Texture(String filepath, boolean flipOnLoad) {
		int[] w = new int[1];
		int[] h = new int[1];
		int[] ch = new int[1];
		stbi_set_flip_vertically_on_load(flipOnLoad);
		ByteBuffer data = stbi_load(FileUtil.getRealFilepath(filepath), w, h, ch, 0);
		if (data != null) {
			width = w[0];
			height = h[0];
			textureId = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, textureId);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, ch[0] <= 3 ? GL_RGB : GL_RGBA, GL_UNSIGNED_BYTE, data);
			glGenerateMipmap(GL_TEXTURE_2D);
			stbi_image_free(data);
		} else {
			System.err.println(stbi_failure_reason());
		}
	}

	public final void bind(int index) {
		glActiveTexture(GL_TEXTURE0 + index);
		glBindTexture(GL_TEXTURE_2D, textureId);
	}

	public static void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public final void delete() {
		glDeleteTextures(textureId);
		textureId = 0;
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}
}
