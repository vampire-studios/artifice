package io.github.vampirestudios.artifice.api.builder.data.worldgen.structure;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.resource.JsonResource;

public class SpawnOverridesBuilder extends TypedJsonBuilder<JsonResource<JsonObject>> {
	public SpawnOverridesBuilder() {
		super(new JsonObject(), JsonResource::new);
	}

	public SpawnOverridesBuilder monster(MobSpawnOverrideRuleBuilder processor) {
		with("monster", JsonObject::new, processor::buildTo);
		return this;
	}

	public SpawnOverridesBuilder creature(MobSpawnOverrideRuleBuilder processor) {
		with("creature", JsonObject::new, processor::buildTo);
		return this;
	}

	public SpawnOverridesBuilder ambient(MobSpawnOverrideRuleBuilder processor) {
		with("ambient", JsonObject::new, processor::buildTo);
		return this;
	}

	public SpawnOverridesBuilder axolotls(MobSpawnOverrideRuleBuilder processor) {
		with("axolotls", JsonObject::new, processor::buildTo);
		return this;
	}

	public SpawnOverridesBuilder undergroundWaterCreature(MobSpawnOverrideRuleBuilder processor) {
		with("underground_water_creature", JsonObject::new, processor::buildTo);
		return this;
	}

	public SpawnOverridesBuilder waterCreature(MobSpawnOverrideRuleBuilder processor) {
		with("water_creature", JsonObject::new, processor::buildTo);
		return this;
	}

	public SpawnOverridesBuilder waterAmbient(MobSpawnOverrideRuleBuilder processor) {
		with("water_ambient", JsonObject::new, processor::buildTo);
		return this;
	}

	public SpawnOverridesBuilder misc(MobSpawnOverrideRuleBuilder processor) {
		with("misc", JsonObject::new, processor::buildTo);
		return this;
	}

}
