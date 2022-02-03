package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.UniformIntDistributionBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class NetherrackReplaceBlobsFeatureConfigBuilder extends FeatureConfigBuilder {

    public NetherrackReplaceBlobsFeatureConfigBuilder() {
        super();
    }

    public NetherrackReplaceBlobsFeatureConfigBuilder radius(Processor<UniformIntDistributionBuilder> processor) {
        with("radius", JsonObject::new, jsonObject -> processor.process(new UniformIntDistributionBuilder()).buildTo(jsonObject));
        return this;
    }

    public NetherrackReplaceBlobsFeatureConfigBuilder target(Processor<StateDataBuilder> processor) {
        with("target", JsonObject::new, jsonObject -> processor.process(new StateDataBuilder()).buildTo(jsonObject));
        return this;
    }

    public NetherrackReplaceBlobsFeatureConfigBuilder state(Processor<StateDataBuilder> processor) {
        with("state", JsonObject::new, jsonObject -> processor.process(new StateDataBuilder()).buildTo(jsonObject));
        return this;
    }
}
