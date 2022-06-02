package io.github.vampirestudios.artifice.api.builder.data.worldgen.structure;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;

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
		MobSpawnOverrideRuleBuilder result = new MobSpawnOverrideRuleBuilder();
		JsonArray aaa = new JsonArray();
		for (SpawnsBuilder spawnsBuilder : spawnsBuilders) {
			aaa.add(new TypedJsonObject().add("type", spawnsBuilder.type())
					.add("weight", spawnsBuilder.weight())
					.add("minCount", spawnsBuilder.minCount())
					.add("maxCount", spawnsBuilder.maxCount()).build());
		}
		result.join("spawns", aaa);
		return result;
	}

}
