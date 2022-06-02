package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.UniformIntDistributionBuilder;

public class NetherrackReplaceBlobsFeatureConfigBuilder extends FeatureConfigBuilder {

    public NetherrackReplaceBlobsFeatureConfigBuilder() {
        super();
    }

    public NetherrackReplaceBlobsFeatureConfigBuilder radius(UniformIntDistributionBuilder processor) {
        join("radius", processor.build());
        return this;
    }

    public NetherrackReplaceBlobsFeatureConfigBuilder target(StateDataBuilder processor) {
        join("target", processor.build());
        return this;
    }

    public NetherrackReplaceBlobsFeatureConfigBuilder state(StateDataBuilder processor) {
        join("state", processor.build());
        return this;
    }
}
