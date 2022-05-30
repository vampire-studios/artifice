package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.UniformIntDistributionBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class NetherrackReplaceBlobsFeatureConfigBuilder extends FeatureConfigBuilder {

    public NetherrackReplaceBlobsFeatureConfigBuilder() {
        super();
    }

    public NetherrackReplaceBlobsFeatureConfigBuilder radius(UniformIntDistributionBuilder processor) {
        join("radius", processor.getData());
        return this;
    }

    public NetherrackReplaceBlobsFeatureConfigBuilder target(StateDataBuilder processor) {
        join("target", processor.getData());
        return this;
    }

    public NetherrackReplaceBlobsFeatureConfigBuilder state(StateDataBuilder processor) {
        join("state", processor.getData());
        return this;
    }
}
