package com.swordglowsblue.artifice.test;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.resources.metadata.animation.AnimationFrame;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;

import java.util.Arrays;
import java.util.List;

public class BufferTexture {
	int width;
	int height;
	int[] buffer;
	AnimationMetadataSection animation;
	
	public BufferTexture(int width, int height) {
		this.width = width;
		this.height = height;
		buffer = new int[width * height];
		this.animation = AnimationMetadataSection.EMPTY;
	}

	public BufferTexture(NativeImage image, AnimationMetadataSection animation) {
		this.width = image.getWidth();
		this.height = image.getHeight();
		buffer = new int[width * height];
		for (int i = 0; i < buffer.length; i++) {
			int x = i % width;
			int y = i / width;
			buffer[i] = image.getPixelRGBA(x, y);
		}
		this.animation = animation;
	}

	private BufferTexture(BufferTexture texture) {
		this.width = texture.width;
		this.height = texture.height;
		buffer = Arrays.copyOf(texture.buffer, texture.buffer.length);
		List<AnimationFrame> copyani = Lists.<AnimationFrame>newArrayList();
		texture.animation.forEachFrame((index, time) -> copyani.add(new AnimationFrame(index, time)));
		FrameSize s = texture.animation.calculateFrameSize(-1,-1);
		this.animation = new AnimationMetadataSection(copyani,
				s.width(),
				s.height(),
				texture.animation.getDefaultFrameTime(),
				texture.animation.isInterpolatedFrames());
	}

	public void setPixel(int x, int y, CustomColor color) {
		buffer[y * width + x] = color.getAsInt();
	}

	public int getPixel(int x, int y) {
		return buffer[y * width + x];
	}

	public NativeImage makeImage() {
		NativeImage img = new NativeImage(width, height, false);
		for (int i = 0; i < buffer.length; i++) {
			int x = i % width;
			int y = i / width;
			img.setPixelRGBA(x, y, buffer[y * width + x]);
		}
		return img;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public void changeSize(int width, int height) {
		this.width = width;
		this.height = height;
		this.buffer = new int[width * height];
	}

	public void upscale(int scale) {
		changeSize(width * scale, height * scale);
	}

	public void downscale(int scale) {
		changeSize(width / scale, height / scale);
	}

	public AnimationMetadataSection getAnimation() {
		return animation;
	}

	public BufferTexture cloneTexture() {
		return new BufferTexture(this);
	}
}