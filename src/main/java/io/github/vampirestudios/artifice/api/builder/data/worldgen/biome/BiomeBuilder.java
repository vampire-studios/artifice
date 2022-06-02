package io.github.vampirestudios.artifice.api.builder.data.worldgen.biome;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;

public class BiomeBuilder extends TypedJsonObject {
    public BiomeBuilder() {
        super(new JsonObject());
        this.add("carvers", new JsonObject());
        this.add("starts", new JsonArray());
        this.add("features", new JsonArray());
        for (GenerationStep.Decoration step : GenerationStep.Decoration.values()) {
            this.root.getAsJsonArray("features").add(new JsonArray());
        }
        this.root.add("spawn_costs", new JsonObject());
        this.root.add("spawners", new JsonObject());
        for (MobCategory spawnGroup : MobCategory.values()) {
            this.root.getAsJsonObject("spawners").add(spawnGroup.getName(), new JsonArray());
        }
    }

    public BiomeBuilder temperature(float temperature) {
        this.root.addProperty("temperature", temperature);
        return this;
    }

    public BiomeBuilder downfall(float downfall) {
        this.root.addProperty("downfall", downfall);
        return this;
    }

    public BiomeBuilder parent(String parent) {
        this.root.addProperty("parent", parent);
        return this;
    }

    public BiomeBuilder precipitation(Biome.Precipitation precipitation) {
        this.root.addProperty("precipitation", precipitation.getName());
        return this;
    }

    public BiomeBuilder category(Biome.BiomeCategory category) {
        this.root.addProperty("category", category.getName());
        return this;
    }

    public BiomeBuilder effects(BiomeEffectsBuilder biomeEffectsBuilder) {
        join("effects", biomeEffectsBuilder.build());
        return this;
    }

    public BiomeBuilder addSpawnCosts(String entityID, SpawnDensityBuilder spawnDensityBuilderProcessor) {
        TypedJsonObject spawnDensityBuilder = new TypedJsonObject();
        spawnDensityBuilder.add("energy_budget", spawnDensityBuilderProcessor.energyBudget)
                .add("charge", spawnDensityBuilderProcessor.charge);
        join(entityID, spawnDensityBuilder.build());
        return this;
    }

    public BiomeBuilder addSpawnEntry(MobCategory spawnGroup, BiomeSpawnEntryBuilder biomeSpawnEntryBuilderProcessor) {
        this.root.getAsJsonObject("spawners").get(spawnGroup.getName())
                .getAsJsonArray()
                .add(biomeSpawnEntryBuilderProcessor.build());
        return this;
    }

    private BiomeBuilder addCarver(GenerationStep.Carving carver, String[] configuredCaverIDs) {
        for (String configuredCaverID : configuredCaverIDs)
            this.root.getAsJsonObject("carvers").getAsJsonArray(carver.getName()).add(configuredCaverID);
        return this;
    }

    public BiomeBuilder addAirCarvers(String... configuredCarverIds) {
        this.root.getAsJsonObject("carvers").add(GenerationStep.Carving.AIR.getName(), new JsonArray());
        this.addCarver(GenerationStep.Carving.AIR, configuredCarverIds);
        return this;
    }

    public BiomeBuilder addLiquidCarvers(String... configuredCarverIds) {
        this.root.getAsJsonObject("carvers").add(GenerationStep.Carving.LIQUID.getName(), new JsonArray());
        this.addCarver(GenerationStep.Carving.LIQUID, configuredCarverIds);
        return this;
    }

    public BiomeBuilder addFeaturesByStep(GenerationStep.Decoration step, String... featureIDs) {
        for (String featureID : featureIDs)
            this.root.getAsJsonArray("features").get(step.ordinal()).getAsJsonArray().add(featureID);
        return this;
    }

    public BiomeBuilder addStructure(String structureID) {
        this.root.getAsJsonArray("starts").add(structureID);
        return this;
    }

    public record SpawnDensityBuilder(double energyBudget, double charge){}
}
