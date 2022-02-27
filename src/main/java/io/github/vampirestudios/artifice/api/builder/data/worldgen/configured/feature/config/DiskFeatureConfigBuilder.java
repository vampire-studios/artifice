package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.UniformIntDistributionBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class DiskFeatureConfigBuilder extends FeatureConfigBuilder {

    public DiskFeatureConfigBuilder() {
        super();
        this.root.add("targets", new JsonArray());
    }

    public DiskFeatureConfigBuilder state(StateDataBuilder processor) {
        with("state", JsonObject::new, processor::merge);
        return this;
    }

    public DiskFeatureConfigBuilder radius(int radius) {
        this.root.addProperty("radius", radius);
        return this;
    }

    public DiskFeatureConfigBuilder radius(Processor<UniformIntDistributionBuilder> processor) {
        with("radius", JsonObject::new, jsonObject -> processor.process(new UniformIntDistributionBuilder()).buildTo(jsonObject));
        return this;
    }

    public DiskFeatureConfigBuilder halfHeight(int halfHeight) {
        if (halfHeight > 4) throw new IllegalArgumentException("halfHeight can't be higher than 4! Found " + halfHeight);
        if (halfHeight < 0) throw new IllegalArgumentException("halfHeight can't be smaller than 0! Found " + halfHeight);
        this.root.addProperty("half_height", halfHeight);
        return this;
    }

    public DiskFeatureConfigBuilder addTarget(StateDataBuilder processor) {
        this.root.getAsJsonArray("targets").add(processor.getData());
        return this;
    }
}
