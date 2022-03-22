package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.placementmodifiers.PlacementModifier;
import io.github.vampirestudios.artifice.api.resource.JsonResource;

public class PlacedFeatureBuilder extends TypedJsonBuilder<JsonResource<JsonObject>> {
    public PlacedFeatureBuilder() {
        super(new JsonObject(), JsonResource::new);
    }

    public PlacedFeatureBuilder feature(String id) {
        this.root.addProperty("feature", id);
        return this;
    }

    public PlacedFeatureBuilder placementModifiers(PlacementModifier... placementModifiers) {
        jsonArray("placement", jsonArrayBuilder -> {
            for (PlacementModifier placementModifier : placementModifiers) {
                jsonArrayBuilder.add(placementModifier.buildTo(new JsonObject()).getData());
            }
        });
        return this;
    }
}
