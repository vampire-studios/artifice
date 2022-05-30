package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class SingleStateFeatureConfigBuilder extends FeatureConfigBuilder {

    public SingleStateFeatureConfigBuilder() {
        super();
    }

    public SingleStateFeatureConfigBuilder state(StateDataBuilder processor) {
        join("state", processor.getData());
        return this;
    }
}
