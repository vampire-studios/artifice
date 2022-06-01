package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class HugeFungusFeatureConfigBuilder extends FeatureConfigBuilder {

	public HugeFungusFeatureConfigBuilder() {
		super();
	}

	public HugeFungusFeatureConfigBuilder validBaseBlock(Processor<StateDataBuilder> processor) {
		with("valid_base_block", JsonObject::new, jsonObject -> processor.process(new StateDataBuilder()).buildTo(jsonObject));
		return this;
	}

	public HugeFungusFeatureConfigBuilder stemState(Processor<StateDataBuilder> processor) {
		with("stem_state", JsonObject::new, jsonObject -> processor.process(new StateDataBuilder()).buildTo(jsonObject));
		return this;
	}

	public HugeFungusFeatureConfigBuilder hatState(Processor<StateDataBuilder> processor) {
		with("hat_state", JsonObject::new, jsonObject -> processor.process(new StateDataBuilder()).buildTo(jsonObject));
		return this;
	}

	public HugeFungusFeatureConfigBuilder decorState(Processor<StateDataBuilder> processor) {
		with("decor_state", JsonObject::new, jsonObject -> processor.process(new StateDataBuilder()).buildTo(jsonObject));
		return this;
	}

	public HugeFungusFeatureConfigBuilder planted(boolean planted) {
		this.root.addProperty("planted", planted);
		return this;
	}
}
