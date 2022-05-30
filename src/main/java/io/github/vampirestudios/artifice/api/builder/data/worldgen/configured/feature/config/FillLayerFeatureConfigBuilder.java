package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class FillLayerFeatureConfigBuilder extends FeatureConfigBuilder {
    public FillLayerFeatureConfigBuilder() {
        super();
    }

    public FillLayerFeatureConfigBuilder state(StateDataBuilder processor) {
        join("state", processor.getData());
        return this;
    }

    public FillLayerFeatureConfigBuilder height(int height) {
        if (height > 255) throw new IllegalArgumentException("height can't be higher than 255! Found " + height);
        if (height < 0) throw new IllegalArgumentException("height can't be smaller than 0! Found " + height);
        this.root.addProperty("height", height);
        return this;
    }
}
