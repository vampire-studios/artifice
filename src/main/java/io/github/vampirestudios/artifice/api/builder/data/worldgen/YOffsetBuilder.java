package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import java.util.Map;

public class YOffsetBuilder {

	public static Map.Entry<String, Integer> absolute(int offset) {
		return Map.entry("absolute",offset);
	}

	public static Map.Entry<String, Integer> aboveBottom(int offset) {
		return Map.entry("above_bottom",offset);
	}

	public static Map.Entry<String, Integer> belowTop(int offset) {
		return Map.entry("below_top",offset);
	}

}
