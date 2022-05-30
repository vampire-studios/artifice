package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.RuleTestBuilder;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class OreFeatureConfigBuilder extends FeatureConfigBuilder {

    public OreFeatureConfigBuilder() {
        super();
    }

    public <R extends RuleTestBuilder> OreFeatureConfigBuilder targetRule(R processor) {
        join("target", processor.getData());
        return this;
    }

    public OreFeatureConfigBuilder state(StateDataBuilder processor) {
        join("state", processor.getData());
        return this;
    }

    public OreFeatureConfigBuilder size(int size) {
        if (size > 64) throw new IllegalArgumentException("size can't be higher than 64! Found " + size);
        if (size < 0) throw new IllegalArgumentException("size can't be smaller than 0! Found " + size);
        this.root.addProperty("size", size);
        return this;
    }
}
