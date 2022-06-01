package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import net.minecraft.resources.ResourceLocation;

public class FeatureConfigBuilder extends TypedJsonBuilder<JsonObject> {
	public FeatureConfigBuilder() {
		super(new JsonObject(), j -> j);
	}

	public FeatureConfigBuilder startPool(ResourceLocation startPool) {
		return startPool(startPool.toString());
	}

	public FeatureConfigBuilder startPool(String startPool) {
		jsonString("start_pool", startPool);
		return this;
	}

	public FeatureConfigBuilder size(Number size) {
		jsonNumber("size", size);
		return this;
	}
}
