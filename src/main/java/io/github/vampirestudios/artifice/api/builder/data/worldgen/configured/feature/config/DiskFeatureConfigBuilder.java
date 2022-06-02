package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonArray;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.UniformIntDistributionBuilder;

public class DiskFeatureConfigBuilder extends FeatureConfigBuilder {

    public DiskFeatureConfigBuilder() {
        super();
        this.root.add("targets", new JsonArray());
    }

    public DiskFeatureConfigBuilder state(StateDataBuilder processor) {
        join("state", processor.build());
        return this;
    }

    public DiskFeatureConfigBuilder radius(int radius) {
        this.root.addProperty("radius", radius);
        return this;
    }

    public DiskFeatureConfigBuilder radius(UniformIntDistributionBuilder processor) {
        join("radius", processor.build());
        return this;
    }

    public DiskFeatureConfigBuilder halfHeight(int halfHeight) {
        if (halfHeight > 4) throw new IllegalArgumentException("halfHeight can't be higher than 4! Found " + halfHeight);
        if (halfHeight < 0) throw new IllegalArgumentException("halfHeight can't be smaller than 0! Found " + halfHeight);
        this.root.addProperty("half_height", halfHeight);
        return this;
    }

    public DiskFeatureConfigBuilder addTarget(StateDataBuilder processor) {
        this.root.getAsJsonArray("targets").add(processor.build());
        return this;
    }
}
