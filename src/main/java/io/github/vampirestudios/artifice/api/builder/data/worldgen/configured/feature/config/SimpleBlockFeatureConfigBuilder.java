package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonArray;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;

public class SimpleBlockFeatureConfigBuilder extends FeatureConfigBuilder {

	public SimpleBlockFeatureConfigBuilder() {
		super();
		this.root.add("place_on", new JsonArray());
		this.root.add("place_in", new JsonArray());
		this.root.add("place_under", new JsonArray());
	}

    public SimpleBlockFeatureConfigBuilder toPlace(StateDataBuilder processor) {
        join("to_place", processor.build());
        return this;
    }

    public SimpleBlockFeatureConfigBuilder addPlaceOn(StateDataBuilder processor) {
        this.root.getAsJsonArray("place_on").add(processor.build());
        return this;
    }

    public SimpleBlockFeatureConfigBuilder addPlaceIn(StateDataBuilder processor) {
        this.root.getAsJsonArray("place_in").add(processor.build());
        return this;
    }

    public SimpleBlockFeatureConfigBuilder addPlaceUnder(StateDataBuilder processor) {
        this.root.getAsJsonArray("place_under").add(processor.build());
        return this;
    }
}
