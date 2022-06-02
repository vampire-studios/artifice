package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.placementmodifiers.PlacementModifier;

public class PlacedFeatureBuilder extends TypedJsonObject {
    public PlacedFeatureBuilder() {
        super(new JsonObject());
    }

	public PlacedFeatureBuilder feature(String id) {
		this.root.addProperty("feature", id);
		return this;
	}

    public PlacedFeatureBuilder placementModifiers(PlacementModifier... placementModifiers) {
        add("placement", arrayOf(placementModifiers));
        return this;
    }
}
