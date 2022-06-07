package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import io.github.vampirestudios.artifice.api.builder.data.worldgen.BlockStateProviderBuilder;

public class BlockPileFeatureConfigBuilder extends FeatureConfigBuilder {

	public BlockPileFeatureConfigBuilder() {
		super();
	}

	public <P extends BlockStateProviderBuilder> BlockPileFeatureConfigBuilder stateProvider(P processor) {
		join("state_provider", processor.build());
		return this;
	}
}
