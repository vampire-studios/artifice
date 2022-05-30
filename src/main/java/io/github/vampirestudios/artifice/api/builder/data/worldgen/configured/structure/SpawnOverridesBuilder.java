package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.structure;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import io.github.vampirestudios.artifice.api.util.Processor;

public class SpawnOverridesBuilder extends TypedJsonObject {
	public SpawnOverridesBuilder() {
		super(new JsonObject());
	}

	public SpawnOverridesBuilder monster(MobSpawnOverrideRuleBuilder processor) {
		join("monster",processor.getData());
		return this;
	}

	public SpawnOverridesBuilder creature(MobSpawnOverrideRuleBuilder processor) {
		join("creature",processor.getData());
		return this;
	}

	public SpawnOverridesBuilder ambient(MobSpawnOverrideRuleBuilder processor) {
		join("ambient",processor.getData());
		return this;
	}

	public SpawnOverridesBuilder axolotls(MobSpawnOverrideRuleBuilder processor) {
		join("axolotls",processor.getData());
		return this;
	}

	public SpawnOverridesBuilder undergroundWaterCreature(MobSpawnOverrideRuleBuilder processor) {
		join("underground_water_creature",processor.getData());
		return this;
	}

	public SpawnOverridesBuilder waterCreature(MobSpawnOverrideRuleBuilder processor) {
		join("water_creature",processor.getData());
		return this;
	}

	public SpawnOverridesBuilder waterAmbient(MobSpawnOverrideRuleBuilder processor) {
		join("water_ambient",processor.getData());
		return this;
	}

	public SpawnOverridesBuilder misc(MobSpawnOverrideRuleBuilder processor) {
		join("misc",processor.getData());
		return this;
	}

}
