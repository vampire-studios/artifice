package io.github.vampirestudios.artifice.api.builder.data.worldgen.structure;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.resource.JsonResource;

public class MobSpawnOverrideRuleBuilder extends TypedJsonBuilder<JsonResource<JsonObject>> {
	protected MobSpawnOverrideRuleBuilder() {
		super(new JsonObject(), JsonResource::new);
	}

	public MobSpawnOverrideRuleBuilder pieceBoundingBox() {
		this.root.addProperty("bounding_box", "piece");
		return this;
	}

	public MobSpawnOverrideRuleBuilder fullBoundingBox() {
		this.root.addProperty("bounding_box", "full");
		return this;
	}

	public MobSpawnOverrideRuleBuilder spawns(SpawnsBuilder... spawnsBuilders) {
		jsonArray("spawns", jsonArrayBuilder -> {
			for (SpawnsBuilder spawnsBuilder : spawnsBuilders) {
				jsonArrayBuilder.addObject(jsonObjectBuilder -> jsonObjectBuilder
					.add("type", spawnsBuilder.type())
					.add("weight", spawnsBuilder.weight())
					.add("minCount", spawnsBuilder.minCount())
					.add("maxCount", spawnsBuilder.maxCount()));
			}
		});
		return this;
	}

}
