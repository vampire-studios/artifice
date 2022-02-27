package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class SimpleBlockFeatureConfigBuilder extends FeatureConfigBuilder {

    public SimpleBlockFeatureConfigBuilder() {
        super();
        this.root.add("place_on", new JsonArray());
        this.root.add("place_in", new JsonArray());
        this.root.add("place_under", new JsonArray());
    }

    public SimpleBlockFeatureConfigBuilder toPlace(StateDataBuilder processor) {
        with("to_place", JsonObject::new, processor::merge);
        return this;
    }

    public SimpleBlockFeatureConfigBuilder addPlaceOn(StateDataBuilder processor) {
        this.root.getAsJsonArray("place_on").add(processor.getData());
        return this;
    }

    public SimpleBlockFeatureConfigBuilder addPlaceIn(StateDataBuilder processor) {
        this.root.getAsJsonArray("place_in").add(processor.getData());
        return this;
    }

    public SimpleBlockFeatureConfigBuilder addPlaceUnder(StateDataBuilder processor) {
        this.root.getAsJsonArray("place_under").add(processor.getData());
        return this;
    }
}
