package com.swordglowsblue.artifice.test;

import net.minecraft.util.Mth;

public class CustomColor {
	private static final CustomColor INTERNAL = new CustomColor();

	private float x;
	private float y;
	private float z;
	private float a;
	private boolean hsvMode = false;
	
	public CustomColor() {
		this(0F, 0F, 0F, 1F);
	}

	public CustomColor(float r, float g, float b) {
		this(r, g, b, 1F);
	}

	public CustomColor(int r, int g, int b) {
		set(r, g, b, 255);
	}

	public CustomColor(float r, float g, float b, float a) {
		set(r, g, b, a);
	}
	
	public CustomColor(int r, int g, int b, int a) {
		set(r, g, b, a);
	}
	
	public CustomColor(int value) {
		set(value);
	}

	public CustomColor(boolean useHSV) {
		this();
		hsvMode = useHSV;
	}

	public int getAsInt() {
		if (this.hsvMode) {
			return INTERNAL.set(this).switchToRGB().getAsInt();
		}
		int cr = (int) Mth.clamp(x * 255, 0, 255);
		int cg = (int) Mth.clamp(y * 255, 0, 255);
		int cb = (int) Mth.clamp(z * 255, 0, 255);
		int ca = (int) Mth.clamp(a * 255, 0, 255);
		return (ca << 24) | (cb << 16) | (cg << 8) | cr;
	}
	
	public CustomColor set(int r, int g, int b, int a) {
		this.x = r / 255F;
		this.y = g / 255F;
		this.z = b / 255F;
		this.a = a / 255F;
		return this;
	}

	public CustomColor set(float r, float g, float b) {
		this.x = r;
		this.y = g;
		this.z = b;
		return this;
	}

	public CustomColor set(float r, float g, float b, float a) {
		this.x = r;
		this.y = g;
		this.z = b;
		this.a = a;
		return this;
	}
	
	public CustomColor set(CustomColor color) {
		this.x = color.x;
		this.y = color.y;
		this.z = color.z;
		this.a = color.a;
		this.hsvMode = color.hsvMode;
		return this;
	}
	
	public CustomColor set(int value) {
		x = (value & 255) / 255F;
		y = ((value >> 8) & 255) / 255F;
		z = ((value >> 16) & 255) / 255F;
		a = ((value >> 24) & 255) / 255F;
		return this;
	}

	public CustomColor alphaBlend(CustomColor color) {
		return mixWith(color,color.a);
	}

	public CustomColor mixWith(CustomColor color, float blend) {
		return mixWith(color,blend, true,false);
	}

	public CustomColor mixWith(CustomColor color, float blend, boolean parentAlpha, boolean alphaAffectsBlendStrength) {
		if(alphaAffectsBlendStrength) blend = color.a * blend;
		if (hsvMode) {
			if (Math.abs(this.x - color.x) > 0.5F) {
				float x1 = this.x;
				float x2 = color.x;
				if (x2 > x1) {
					x1 += 1;
				}
				else {
					x2 += 1;
				}
				this.x = wrap(Mth.lerp(blend, x1, x2), 1);
			}
			else {
				this.x = Mth.lerp(blend, this.x, color.x) % 1.0F;
			}
		}
		else {
			this.x = Mth.lerp(blend, this.x, color.x);
		}
		this.y = Mth.lerp(blend, this.y, color.y);
		this.z = Mth.lerp(blend, this.z, color.z);
		if(!parentAlpha) this.a = Mth.lerp(blend, this.a, color.a);

		return this;
	}

	public static float wrap(float x, float y) {
		return x - Mth.floor(x / y) * y;
	}

	public static int wrap(int x, int y) {
		return x < 0 ? (x - Mth.floor(x / y) + y) % y : x % y;
	}

	public CustomColor copyMaxAlpha(CustomColor color) {
		this.a = Math.max(this.a, color.a);
		return this;
	}

	public float getRed() {
		return x;
	}

	public CustomColor setRed(float r) {
		this.x = r;
		return this;
	}

	public float getGreen() {
		return y;
	}

	public CustomColor setGreen(float g) {
		this.y = g;
		return this;
	}

	public float getBlue() {
		return z;
	}

	public CustomColor setBlue(float b) {
		this.z = b;
		return this;
	}

	public float getAlpha() {
		return a;
	}

	public CustomColor setAlpha(float a) {
		this.a = a;
		return this;
	}

	public CustomColor switchToRGB() {
		if (hsvMode) {
			float newRed = 0;
			float newGreen = 0;
			float newBlue = 0;

			if (y == 0.0F) {
				newRed = newGreen = newBlue = z;
			}
			else {
				float segment = (x - (float) Math.floor(x)) * 6.0F;
				float invert = segment - (float) Math.floor(segment);
				float v1 = z * (1.0F - y);
				float v2 = z * (1.0F - y * invert);
				float v3 = z * (1.0F - y * (1.0F - invert));
				switch ((int) segment) {
					case 0 -> {
						newRed = z;
						newGreen = v3;
						newBlue = v1;
					}
					case 1 -> {
						newRed = v2;
						newGreen = z;
						newBlue = v1;
					}
					case 2 -> {
						newRed = v1;
						newGreen = z;
						newBlue = v3;
					}
					case 3 -> {
						newRed = v1;
						newGreen = v2;
						newBlue = z;
					}
					case 4 -> {
						newRed = v3;
						newGreen = v1;
						newBlue = z;
					}
					case 5 -> {
						newRed = z;
						newGreen = v1;
						newBlue = v2;
					}
				}
			}

			x = newRed;
			y = newGreen;
			z = newBlue;
		}
		hsvMode = false;
		return this;
	}

	public CustomColor switchToHSV() {
		if (!hsvMode) {
			float br = Math.max(x, y);
			if (z > br) {
				br = z;
			}

			float minCol = Math.min(x, y);
			if (z < minCol) {
				minCol = z;
			}

			float sat = br != 0 ? (br - minCol) / br : 0;

			float hue;
			if (sat == 0.0F) {
				hue = 0.0F;
			} else {
				float var9 = (br - x) / (br - minCol);
				float var10 = (br - y) / (br - minCol);
				float var11 = (br - z) / (br - minCol);
				if (x == br) {
					hue = var11 - var10;
				} else if (y == br) {
					hue = 2.0F + var9 - var11;
				} else {
					hue = 4.0F + var10 - var9;
				}

				hue /= 6.0F;
				if (hue < 0.0F) {
					++hue;
				}
			}

			x = hue;
			y = sat;
			z = br;
		}
		hsvMode = true;
		return this;
	}

	public float getHue() {
		return x;
	}

	public CustomColor setHue(float hue) {
		this.x = wrap(hue, 1);
		return this;
	}

	public float getSaturation() {
		return y;
	}

	public CustomColor setSaturation(float sat) {
		this.y = Mth.clamp(sat, 0F, 1F);
		return this;
	}

	public float getBrightness() {
		return z;
	}

	public CustomColor setBrightness(float br) {
		this.z = Mth.clamp(br, 0F, 1F);
		return this;
	}


	public CustomColor forceRGB() {
		hsvMode = false;
		return this;
	}

	public CustomColor forceHSV() {
		hsvMode = true;
		return this;
	}

	@Override
	public CustomColor clone() {
		return new CustomColor(x, y, z, a);
	}
}