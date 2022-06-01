package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config.FeatureConfigBuilder;
import io.github.vampirestudios.artifice.api.resource.JsonResource;

public class ConfiguredFeatureBuilder extends TypedJsonBuilder<JsonResource<JsonObject>> {
	public ConfiguredFeatureBuilder() {
		super(new JsonObject(), JsonResource::new);
	}

	public ConfiguredFeatureBuilder featureID(String id) {
		this.root.addProperty("type", id);
		return this;
	}

	public <C extends FeatureConfigBuilder> ConfiguredFeatureBuilder featureConfig(C instance) {
		with("config", JsonObject::new, instance::buildTo);
		return this;
	}

	public ConfiguredFeatureBuilder defaultConfig() {
		return this.featureConfig(new FeatureConfigBuilder());
	}
}
