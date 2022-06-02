package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;

public class SingleStateFeatureConfigBuilder extends FeatureConfigBuilder {

	public SingleStateFeatureConfigBuilder() {
		super();
	}

    public SingleStateFeatureConfigBuilder state(StateDataBuilder processor) {
        join("state", processor.build());
        return this;
    }
}
