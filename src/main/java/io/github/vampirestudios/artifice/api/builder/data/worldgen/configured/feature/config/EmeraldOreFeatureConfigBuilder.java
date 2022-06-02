package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;

public class EmeraldOreFeatureConfigBuilder extends FeatureConfigBuilder {

    public EmeraldOreFeatureConfigBuilder() {
        super();
    }

    public EmeraldOreFeatureConfigBuilder state(StateDataBuilder processor) {
        join("state", processor.build());
        return this;
    }

    public EmeraldOreFeatureConfigBuilder target(StateDataBuilder processor) {
        join("target", processor.build());
        return this;
    }
}
