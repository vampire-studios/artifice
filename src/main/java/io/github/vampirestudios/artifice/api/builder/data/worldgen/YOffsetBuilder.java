package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;

public class YOffsetBuilder extends TypedJsonObject {

	public YOffsetBuilder(String type, int value) {
		super();
		this.add(type, value);
	}

	public static YOffsetBuilder absolute(int offset) {
		return new YOffsetBuilder("absolute", offset);
	}

	public static YOffsetBuilder aboveBottom(int offset) {
		return new YOffsetBuilder("above_bottom", offset);
	}

	public static YOffsetBuilder belowTop(int offset) {
		return new YOffsetBuilder("below_top", offset);
	}

}
