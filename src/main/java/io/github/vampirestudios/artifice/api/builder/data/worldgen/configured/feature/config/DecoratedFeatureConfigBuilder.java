package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.decorator.ConfiguredDecoratorBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.ConfiguredSubFeatureBuilder;

public class DecoratedFeatureConfigBuilder extends FeatureConfigBuilder {

	public DecoratedFeatureConfigBuilder() {
		super();
	}

    public DecoratedFeatureConfigBuilder feature(ConfiguredSubFeatureBuilder processor) {
        join("feature", processor.build());
        return this;
    }

	public DecoratedFeatureConfigBuilder feature(String configuredFeatureID) {
		this.root.addProperty("feature", configuredFeatureID);
		return this;
	}

    public DecoratedFeatureConfigBuilder decorator(ConfiguredDecoratorBuilder processor) {
        join("decorator", processor.build());
        return this;
    }

	public DecoratedFeatureConfigBuilder decorator(String configuredDecoratorID) {
		this.root.addProperty("decorator", configuredDecoratorID);
		return this;
	}
}
