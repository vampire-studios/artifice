package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;

public class UniformIntDistributionBuilder extends TypedJsonObject {
	public UniformIntDistributionBuilder() {
		super(new JsonObject());
	}

	public UniformIntDistributionBuilder base(int base) {
		this.root.addProperty("base", base);
		return this;
	}

	public UniformIntDistributionBuilder spread(int spread) {
		this.root.addProperty("spread", spread);
		return this;
	}
}
