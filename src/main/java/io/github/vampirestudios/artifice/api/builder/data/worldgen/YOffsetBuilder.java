package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;

public class YOffsetBuilder extends TypedJsonBuilder<JsonObject> {

	public YOffsetBuilder() {
		super(new JsonObject(), j->j);
	}

	public YOffsetBuilder absolute(int offset) {
		this.root.addProperty("absolute", offset);
		return this;
	}

	public YOffsetBuilder aboveBottom(int offset) {
		this.root.addProperty("above_bottom", offset);
		return this;
	}

	public YOffsetBuilder belowTop(int offset) {
		this.root.addProperty("below_top", offset);
		return this;
	}

}
