package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.UniformIntDistributionBuilder;

public class DeltaFeatureConfigBuilder extends FeatureConfigBuilder {

	public DeltaFeatureConfigBuilder() {
		super();
	}

	public DeltaFeatureConfigBuilder size(UniformIntDistributionBuilder processor) {
		join("size", processor.build());
		return this;
	}

	public DeltaFeatureConfigBuilder rimSize(UniformIntDistributionBuilder processor) {
		join("rim_size", processor.build());
		return this;
	}

	public DeltaFeatureConfigBuilder rim(StateDataBuilder processor) {
		join("rim", processor.build());
		return this;
	}

	public DeltaFeatureConfigBuilder contents(StateDataBuilder processor) {
		join("contents", processor.build());
		return this;
	}
}
