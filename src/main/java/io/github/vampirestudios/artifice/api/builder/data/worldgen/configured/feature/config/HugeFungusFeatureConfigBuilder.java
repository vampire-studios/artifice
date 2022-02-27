package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class HugeFungusFeatureConfigBuilder extends FeatureConfigBuilder {

    public HugeFungusFeatureConfigBuilder() {
        super();
    }

    public HugeFungusFeatureConfigBuilder validBaseBlock(StateDataBuilder processor) {
        with("valid_base_block", JsonObject::new, processor::merge);
        return this;
    }

    public HugeFungusFeatureConfigBuilder stemState(StateDataBuilder processor) {
        with("stem_state", JsonObject::new, processor::merge);
        return this;
    }

    public HugeFungusFeatureConfigBuilder hatState(StateDataBuilder processor) {
        with("hat_state", JsonObject::new, processor::merge);
        return this;
    }

    public HugeFungusFeatureConfigBuilder decorState(StateDataBuilder processor) {
        with("decor_state", JsonObject::new, processor::merge);
        return this;
    }

    public HugeFungusFeatureConfigBuilder planted(boolean planted) {
        this.root.addProperty("planted", planted);
        return this;
    }
}
