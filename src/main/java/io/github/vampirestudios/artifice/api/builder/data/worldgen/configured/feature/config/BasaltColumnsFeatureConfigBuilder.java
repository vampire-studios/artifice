package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.UniformIntDistributionBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class BasaltColumnsFeatureConfigBuilder extends FeatureConfigBuilder {

    public BasaltColumnsFeatureConfigBuilder() {
        super();
    }

    public BasaltColumnsFeatureConfigBuilder reach(UniformIntDistributionBuilder processor) {
        join("reach", processor.getData());
        return this;
    }

    public BasaltColumnsFeatureConfigBuilder height(UniformIntDistributionBuilder processor) {
        join("height", processor.getData());
        return this;
    }
}
