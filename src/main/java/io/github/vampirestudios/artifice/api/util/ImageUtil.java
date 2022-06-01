package io.github.vampirestudios.artifice.api.util;

public class ImageUtil {
	public static int recolor(int input, int val) {
		int a = input >> 24 & 0xFF, r = input >> 16 & 0xFF, g = input >> 8 & 0xFF, b = input & 0xFF;
		input = a << 24;
		if (a != 0) {
			input |= (int) (r + ((1 - r / 255D) * ((val >> 16) & 0xFF))) << 16;
			input |= (int) (g + ((1 - g / 255D) * ((val >> 8) & 0xFF))) << 8;
			input |= (int) (b + ((1 - b / 255D) * (val & 0xFF)));
		}
		return input;
	}

	public static int layer(int base, int layer) {
		int al = getAlpha(layer);
		int r = (getRed(base) * (255 - al) + getRed(layer) * al) / 255;
		int g = (getGreen(base) * (255 - al) + getGreen(layer) * al) / 255;
		int b = (getBlue(base) * (255 - al) + getBlue(layer) * al) / 255;
		int a = getAlpha(base) | al;
		return combine(r, g, b, a);
	}

	public static int tint(int col, int tint) {
		int r = (getRed(col) + getRed(tint)) / 2;
		int g = (getGreen(col) + getGreen(tint)) / 2;
		int b = (getBlue(col) + getBlue(tint)) / 2;
		int a = getAlpha(col);
		return combine(r, g, b, a);
	}

	private static int getRed(int col) {
		return (col >> 16) & 0xFF;
	}

	private static int getGreen(int col) {
		return (col >> 8) & 0xFF;
	}

	private static int getBlue(int col) {
		return (col) & 0xFF;
	}

	private static int getAlpha(int col) {
		return (col >> 24) & 0xff;
	}

	private static int combine(int r, int g, int b, int a) {
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

}