package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class EmeraldOreFeatureConfigBuilder extends FeatureConfigBuilder {

	public EmeraldOreFeatureConfigBuilder() {
		super();
	}

	public EmeraldOreFeatureConfigBuilder state(Processor<StateDataBuilder> processor) {
		with("state", JsonObject::new, jsonObject -> processor.process(new StateDataBuilder()).buildTo(jsonObject));
		return this;
	}

	public EmeraldOreFeatureConfigBuilder target(Processor<StateDataBuilder> processor) {
		with("target", JsonObject::new, jsonObject -> processor.process(new StateDataBuilder()).buildTo(jsonObject));
		return this;
	}
}
