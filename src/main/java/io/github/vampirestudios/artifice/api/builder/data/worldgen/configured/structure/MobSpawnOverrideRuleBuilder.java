package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.structure;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.resource.JsonResource;

public class MobSpawnOverrideRuleBuilder extends TypedJsonObject {
	protected MobSpawnOverrideRuleBuilder() {
		super(new JsonObject());
	}

	public static MobSpawnOverrideRuleBuilder spawns(String boundingBox, SpawnsBuilder... spawnsBuilders) {
		MobSpawnOverrideRuleBuilder result = new MobSpawnOverrideRuleBuilder();
		result.root.addProperty("bounding_box", boundingBox);
		JsonArray aaa = new JsonArray();
		for (SpawnsBuilder spawnsBuilder : spawnsBuilders) {
			aaa.add(new TypedJsonObject().add("type", spawnsBuilder.type())
					.add("weight", spawnsBuilder.weight())
					.add("minCount", spawnsBuilder.minCount())
					.add("maxCount", spawnsBuilder.maxCount()).getData());
		}
		result.join("spawns", aaa);
		return result;
	}

}
