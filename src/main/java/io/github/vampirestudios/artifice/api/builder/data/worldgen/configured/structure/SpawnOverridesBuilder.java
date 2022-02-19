package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.structure;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import io.github.vampirestudios.artifice.api.util.Processor;

public class SpawnOverridesBuilder extends TypedJsonBuilder<JsonResource<JsonObject>> {
	public SpawnOverridesBuilder() {
		super(new JsonObject(), JsonResource::new);
	}

	public SpawnOverridesBuilder monster(Processor<MobSpawnOverrideRuleBuilder> processor) {
		with("monster", JsonObject::new, jsonObject -> processor.process(new MobSpawnOverrideRuleBuilder()).buildTo(jsonObject));
		return this;
	}

	public SpawnOverridesBuilder creature(Processor<MobSpawnOverrideRuleBuilder> processor) {
		with("creature", JsonObject::new, jsonObject -> processor.process(new MobSpawnOverrideRuleBuilder()).buildTo(jsonObject));
		return this;
	}

	public SpawnOverridesBuilder ambient(Processor<MobSpawnOverrideRuleBuilder> processor) {
		with("ambient", JsonObject::new, jsonObject -> processor.process(new MobSpawnOverrideRuleBuilder()).buildTo(jsonObject));
		return this;
	}

	public SpawnOverridesBuilder axolotls(Processor<MobSpawnOverrideRuleBuilder> processor) {
		with("axolotls", JsonObject::new, jsonObject -> processor.process(new MobSpawnOverrideRuleBuilder()).buildTo(jsonObject));
		return this;
	}

	public SpawnOverridesBuilder undergroundWaterCreature(Processor<MobSpawnOverrideRuleBuilder> processor) {
		with("underground_water_creature", JsonObject::new, jsonObject -> processor.process(new MobSpawnOverrideRuleBuilder()).buildTo(jsonObject));
		return this;
	}

	public SpawnOverridesBuilder waterCreature(Processor<MobSpawnOverrideRuleBuilder> processor) {
		with("water_creature", JsonObject::new, jsonObject -> processor.process(new MobSpawnOverrideRuleBuilder()).buildTo(jsonObject));
		return this;
	}

	public SpawnOverridesBuilder waterAmbient(Processor<MobSpawnOverrideRuleBuilder> processor) {
		with("water_ambient", JsonObject::new, jsonObject -> processor.process(new MobSpawnOverrideRuleBuilder()).buildTo(jsonObject));
		return this;
	}

	public SpawnOverridesBuilder misc(Processor<MobSpawnOverrideRuleBuilder> processor) {
		with("misc", JsonObject::new, jsonObject -> processor.process(new MobSpawnOverrideRuleBuilder()).buildTo(jsonObject));
		return this;
	}

}
