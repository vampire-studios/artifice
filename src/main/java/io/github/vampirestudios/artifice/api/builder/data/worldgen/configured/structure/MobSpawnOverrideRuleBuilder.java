package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.structure;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.resource.JsonResource;

public class MobSpawnOverrideRuleBuilder extends TypedJsonObject {
	protected MobSpawnOverrideRuleBuilder() {
		super(new JsonObject());
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
		for (SpawnsBuilder spawnsBuilder : spawnsBuilders) {
			jsonArrayBuilder.addObject(jsonObjectBuilder -> jsonObjectBuilder
					.add("type", spawnsBuilder.type())
					.add("weight", spawnsBuilder.weight())
					.add("minCount", spawnsBuilder.minCount())
					.add("maxCount", spawnsBuilder.maxCount()));
		}
		jsonArray("spawns", jsonArrayBuilder -> {
		});
		return this;
	}

}
