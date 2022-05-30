package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.BlockStateProviderBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class BlockPileFeatureConfigBuilder extends FeatureConfigBuilder {

    public BlockPileFeatureConfigBuilder() {
        super();
    }

    public <P extends BlockStateProviderBuilder> BlockPileFeatureConfigBuilder stateProvider(P processor) {
        join("state_provider", processor.getData());
        return this;
    }
}
