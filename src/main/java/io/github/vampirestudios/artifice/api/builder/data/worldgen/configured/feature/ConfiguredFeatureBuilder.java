package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config.FeatureConfigBuilder;

public class ConfiguredFeatureBuilder extends TypedJsonObject {
	public ConfiguredFeatureBuilder() {
		super(new JsonObject());
	}

	public ConfiguredFeatureBuilder featureID(String id) {
		this.root.addProperty("type", id);
		return this;
	}

	public <C extends FeatureConfigBuilder> ConfiguredFeatureBuilder featureConfig(C processor) {
		join("config", processor.build());
		return this;
	}

	public ConfiguredFeatureBuilder defaultConfig() {
		return this.featureConfig(new FeatureConfigBuilder());
	}
}
