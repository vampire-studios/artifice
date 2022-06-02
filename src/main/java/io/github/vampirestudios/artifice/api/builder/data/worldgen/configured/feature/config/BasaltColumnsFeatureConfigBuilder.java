package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import io.github.vampirestudios.artifice.api.builder.data.worldgen.UniformIntDistributionBuilder;

public class BasaltColumnsFeatureConfigBuilder extends FeatureConfigBuilder {

    public BasaltColumnsFeatureConfigBuilder() {
        super();
    }

    public BasaltColumnsFeatureConfigBuilder reach(UniformIntDistributionBuilder processor) {
        join("reach", processor.build());
        return this;
    }

    public BasaltColumnsFeatureConfigBuilder height(UniformIntDistributionBuilder processor) {
        join("height", processor.build());
        return this;
    }
}
