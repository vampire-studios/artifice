package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.NoiseConfigBuilder;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import io.github.vampirestudios.artifice.api.util.Processor;

public class NoiseSettingsBuilder extends TypedJsonBuilder<JsonResource<JsonObject>> {
    public NoiseSettingsBuilder() {
        super(new JsonObject(), JsonResource::new);
    }

    /**
     * Set the sea level.
     */
    public NoiseSettingsBuilder seaLevel(int seaLevel) {
        this.root.addProperty("sea_level", seaLevel);
        return this;
    }

    /**
     * Build noise config.
     */
    public NoiseSettingsBuilder noiseConfig(Processor<NoiseConfigBuilder> noiseConfigBuilder) {
        with("noise", JsonObject::new, jsonObject -> noiseConfigBuilder.process(new NoiseConfigBuilder()).buildTo(jsonObject));
        return this;
    }

    public NoiseSettingsBuilder disableMobGeneration(boolean disableMobGeneration) {
        this.root.addProperty("disable_mob_generation", disableMobGeneration);
        return this;
    }

    public NoiseSettingsBuilder aquifersEnabled(boolean aquifersEnabled) {
        this.root.addProperty("aquifers_enabled", aquifersEnabled);
        return this;
    }

    public NoiseSettingsBuilder oreVeinsEnabled(boolean oreVeinsEnabled) {
        this.root.addProperty("ore_veins_enabled", oreVeinsEnabled);
        return this;
    }

    public NoiseSettingsBuilder legacyRandomSource(boolean legacyRandomSource) {
        this.root.addProperty("legacy_random_source", legacyRandomSource);
        return this;
    }

    /**
     * Set a block state.
     */
    public NoiseSettingsBuilder setBlockState(String id, StateDataBuilder stateDataBuilder) {
        this.root.add(id, stateDataBuilder.build());
        return this;
    }

    /**
     * Build default block.
     */
    public NoiseSettingsBuilder defaultBlock(StateDataBuilder stateDataBuilder) {
        return this.setBlockState("default_block", stateDataBuilder);
    }

    /**
     * Build default fluid.
     */
    public NoiseSettingsBuilder defaultFluid(StateDataBuilder stateDataBuilder) {
        return this.setBlockState("default_fluid", stateDataBuilder);
    }

    /**
     * Build surface rules.
     */
    public NoiseSettingsBuilder surfaceRules(Processor<SurfaceRulesBuilder> surfaceRulesBuilder) {
        with("surface_rule", JsonObject::new, jsonObject -> surfaceRulesBuilder.process(new SurfaceRulesBuilder()).buildTo(jsonObject));
        return this;
    }
}
