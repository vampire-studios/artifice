package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;

public class HugeFungusFeatureConfigBuilder extends FeatureConfigBuilder {

	public HugeFungusFeatureConfigBuilder() {
		super();
	}

	public HugeFungusFeatureConfigBuilder validBaseBlock(StateDataBuilder processor) {
		join("valid_base_block", processor.build());
		return this;
	}

	public HugeFungusFeatureConfigBuilder stemState(StateDataBuilder processor) {
		join("stem_state", processor.build());
		return this;
	}

	public HugeFungusFeatureConfigBuilder hatState(StateDataBuilder processor) {
		join("hat_state", processor.build());
		return this;
	}

	public HugeFungusFeatureConfigBuilder decorState(StateDataBuilder processor) {
		join("decor_state", processor.build());
		return this;
	}

	public HugeFungusFeatureConfigBuilder planted(boolean planted) {
		this.root.addProperty("planted", planted);
		return this;
	}
}
