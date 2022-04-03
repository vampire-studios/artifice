package io.github.vampirestudios.artifice.api.builder.data.worldgen.structure;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config.FeatureConfigBuilder;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import io.github.vampirestudios.artifice.api.util.Processor;

public class StructureBuilder extends TypedJsonBuilder<JsonResource<JsonObject>> {
	public StructureBuilder() {
		super(new JsonObject(), JsonResource::new);
	}

	public StructureBuilder type(String type) {
		this.root.addProperty("type", type);
		return this;
	}

	public StructureBuilder singleBiome(String biome) {
		this.root.addProperty("biomes", biome);
		return this;
	}

	public StructureBuilder biomeTag(String tag) {
		this.root.addProperty("biomes", tag);
		return this;
	}

	public StructureBuilder multipleBiomes(String... biomes) {
		JsonArray biomesArray = new JsonArray();
		for (String s : biomes) {
			biomesArray.add(s);
		}
		this.root.add("biomes", biomesArray);
		return this;
	}

	/*
	* Weather or not it should add extra terrain below the structure.
	* */
	public StructureBuilder adoptNoise(boolean adoptNoise) {
		this.root.addProperty("adoptNoise", adoptNoise);
		return this;
	}

	public StructureBuilder spawnOverrides(Processor<SpawnOverridesBuilder> processor) {
		with("spawn_overrides", JsonObject::new, jsonObject -> processor.process(new SpawnOverridesBuilder()).buildTo(jsonObject));
		return this;
	}

	public StructureBuilder featureConfig(Processor<FeatureConfigBuilder> processor) {
		with("config", JsonObject::new, jsonObject -> processor.process(new FeatureConfigBuilder()).buildTo(jsonObject));
		return this;
	}
}
