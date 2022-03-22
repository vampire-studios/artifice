package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.placementmodifiers;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.resource.JsonResource;

public class PlacementModifier extends TypedJsonBuilder<JsonResource<JsonObject>> {
	public PlacementModifier() {
		super(new JsonObject(), JsonResource::new);
	}
}
