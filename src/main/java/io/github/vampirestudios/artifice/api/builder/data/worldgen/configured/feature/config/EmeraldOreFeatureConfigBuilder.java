package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class EmeraldOreFeatureConfigBuilder extends FeatureConfigBuilder {

    public EmeraldOreFeatureConfigBuilder() {
        super();
    }

    public EmeraldOreFeatureConfigBuilder state(StateDataBuilder processor) {
        join("state", processor.getData());
        return this;
    }

    public EmeraldOreFeatureConfigBuilder target(StateDataBuilder processor) {
        join("target", processor.getData());
        return this;
    }
}
