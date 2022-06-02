package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.NoiseConfigBuilder;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.Noises;

import static io.github.vampirestudios.artifice.api.builder.data.worldgen.JsonHelper.*;

public class NoiseSettingsBuilder extends TypedJsonObject {
    public NoiseSettingsBuilder() {
        super(new JsonObject());
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
    public NoiseSettingsBuilder noiseConfig(NoiseConfigBuilder noiseConfigBuilder) {
        join("noise", noiseConfigBuilder.build());
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
    public NoiseSettingsBuilder surfaceRules(SurfaceRulesBuilder surfaceRulesBuilder) {
        join("surface_rule", surfaceRulesBuilder.build());
        return this;
    }

    public NoiseSettingsBuilder noiseRouter() {
         TypedJsonObject jsonObject = new TypedJsonObject();
            DensityFunctionBuilder barrierBuilder = new DensityFunctionBuilder().type(DensityType.NOISE)
                    .noise(Noises.AQUIFER_BARRIER).xzScale(1).yScale(0.5);
            jsonObject.add("barrier", barrierBuilder.build());

            DensityFunctionBuilder fluidLevelFloodednessBuilder = new DensityFunctionBuilder().type(DensityType.NOISE)
                    .noise(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS).xzScale(1).yScale(0.67);
            jsonObject.add("fluid_level_floodedness", fluidLevelFloodednessBuilder.build());

            DensityFunctionBuilder fluidLevelSpreadBuilder = new DensityFunctionBuilder().type(DensityType.NOISE)
                    .noise(Noises.AQUIFER_FLUID_LEVEL_SPREAD).xzScale(1).yScale(0.7142857142857143);
            jsonObject.add("fluid_level_spread", fluidLevelSpreadBuilder.build());

            DensityFunctionBuilder lavaBuilder = new DensityFunctionBuilder().type(DensityType.NOISE)
                    .noise(Noises.AQUIFER_LAVA).xzScale(1).yScale(1);
            jsonObject.add("lava", lavaBuilder.build());

            DensityFunctionBuilder temperatureBuilder = new DensityFunctionBuilder().type(DensityType.SHIFTED_NOISE)
                    .noise(Noises.TEMPERATURE).xzScale(0.25).yScale(0).shiftX(NoiseRouterData.SHIFT_X).shiftY(0)
                    .shiftZ(NoiseRouterData.SHIFT_Z);
            jsonObject.add("temperature", temperatureBuilder.build());

            DensityFunctionBuilder vegetationBuilder = new DensityFunctionBuilder().type(DensityType.SHIFTED_NOISE)
                    .noise("minecraft:vegetation").xzScale(0.25).yScale(0).shiftX(NoiseRouterData.SHIFT_X).shiftY(0)
                    .shiftZ(NoiseRouterData.SHIFT_Z);
            jsonObject.add("vegetation", vegetationBuilder.build())

                    .add("continents", "minecraft:overworld/continents")
                    .add("erosion", "minecraft:overworld/erosion")
                    .add("depth", "minecraft:overworld/depth")
                    .add("ridges", /*"minecraft:overworld/ridges"*/0)

                    .add("initial_density_without_jaggedness", addInitialDensityWithoutJaggedness())
                    .add("final_density", addFinalDensity())
                    .add("vein_toggle", addVeinToggle())
                    .add("vein_ridged", addVeinRidged());

            DensityFunctionBuilder veinGapBuilder = new DensityFunctionBuilder().type(DensityType.NOISE)
                    .noise(Noises.ORE_GAP).xzScale(1).yScale(1);
            jsonObject.add("vein_gap", veinGapBuilder.build());
        join("noise_router", jsonObject.build());
        return this;
    }

    private JsonObject addInitialDensityWithoutJaggedness() {
        JsonObject initialDensityWithoutJaggedness = new JsonObject();
        JsonHelper jsonHelper = new JsonHelper(initialDensityWithoutJaggedness);
        jsonHelper.type(DensityType.MUL).noise(Noises.VEGETATION).number("argument1", 4);

        JsonObject initialDensityWithoutJaggednessArgument2 = new JsonObject();
        jsonHelper.setJsonObject(initialDensityWithoutJaggednessArgument2)
                .type(DensityType.QUARTER_NEGATIVE);

        JsonObject initialDensityWithoutJaggednessArgument2Argument = new JsonObject();
        jsonHelper.setJsonObject(initialDensityWithoutJaggednessArgument2Argument);
        jsonHelper.type(DensityType.MUL).densityFunction("argument1", NoiseRouterData.DEPTH);

        JsonObject initialDensityWithoutJaggednessArgument2ArgumentArgument2 = new JsonObject();
        jsonHelper.setJsonObject(initialDensityWithoutJaggednessArgument2ArgumentArgument2).type(DensityType.CACHE_2D)
                .densityFunction("argument", NoiseRouterData.FACTOR);
        initialDensityWithoutJaggednessArgument2Argument.add("argument2", initialDensityWithoutJaggednessArgument2ArgumentArgument2);

        initialDensityWithoutJaggednessArgument2.add("argument", initialDensityWithoutJaggednessArgument2Argument);

        initialDensityWithoutJaggedness.add("argument2", initialDensityWithoutJaggednessArgument2);

        return initialDensityWithoutJaggedness;
    }

    private JsonObject addFinalDensity() {
        JsonObject finalDensity = new JsonObject();
        JsonHelper jsonHelper = new JsonHelper(finalDensity);
        jsonHelper.type(DensityType.MIN);

        JsonObject argument1 = new JsonObject();
        jsonHelper.setJsonObject(argument1).type(DensityType.SQUEEZE);

        JsonObject argument1Argument = new JsonObject();
        jsonHelper.setJsonObject(argument1Argument).type(DensityType.MUL)
                .number("argument1", 0.64);

        JsonObject argument1ArgumentArgument2 = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2).type(DensityType.INTERPOLATED);

        JsonObject argument1ArgumentArgument2Argument = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2Argument).type(DensityType.BLEND_DENSITY);

        JsonObject argument1ArgumentArgument2ArgumentArgument = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgument).type(DensityType.SLIDE);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgument = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgument).type(DensityType.RANGE_CHOICE)
                .input(NoiseRouterData.SLOPED_CHEESE).minMaxInclusiveExclusive(-1000000, 1.5625);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenInRange = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenInRange).type(DensityType.MIN)
                .densityFunction("argument1", NoiseRouterData.SLOPED_CHEESE);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenInRangeArgument2 = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenInRangeArgument2).type(DensityType.MUL)
                .number("argument1", 5).densityFunction("argument2", NoiseRouterData.ENTRANCES);

        argument1ArgumentArgument2ArgumentArgumentArgumentWhenInRange.add("argument2", argument1ArgumentArgument2ArgumentArgumentArgumentWhenInRangeArgument2);

        argument1ArgumentArgument2ArgumentArgumentArgument.add("when_in_range", argument1ArgumentArgument2ArgumentArgumentArgumentWhenInRange);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRange = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRange).type(DensityType.MAX);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1 = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1).type(DensityType.MIN);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1 = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1).type(DensityType.MIN)
                .densityFunction("argument2", NoiseRouterData.ENTRANCES);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1 = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1).type(DensityType.ADD);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument1 = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument1)
                .type(DensityType.MUL).number("argument1", 4);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument1Argument2 = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument1Argument2)
                .type(DensityType.SQUARE);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument1Argument2Argument = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument1Argument2Argument)
                .type(DensityType.NOISE).noise(Noises.CAVE_LAYER).number("xz_scale", 1).number("y_scale", 8);

        argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument1Argument2.add("argument", argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument1Argument2Argument);

        argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument1.add("argument2", argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument1Argument2);

        argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1.add("argument1", argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument1);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2 = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2)
                .type(DensityType.ADD);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument1 = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument1)
                .type(DensityType.CLAMP).minMax(-1, 1);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument1Input = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument1Input)
                .type(DensityType.ADD).number("argument1", 0.27);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument1InputArgument2 = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument1InputArgument2)
                .type(DensityType.NOISE).noise(Noises.CAVE_CHEESE).number("xz_scale", 1).number("y_scale", 0.6666666666666666);

        argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument1Input.add("argument2", argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument1InputArgument2);

        argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument1.add("input", argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument1Input);

        argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2.add("argument1", argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument1);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument2 = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument2)
                .type(DensityType.CLAMP).minMax(0, 0.5);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument2Input = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument2Input)
                .type(DensityType.ADD).number("argument1", 1.5);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument2InputArgument2 = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument2InputArgument2)
                .type(DensityType.MUL).number("argument1", -0.64).densityFunction("argument2", NoiseRouterData.SLOPED_CHEESE);

        argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument2Input.add("argument2", argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument2InputArgument2);

        argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument2.add("input", argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument2Input);

        argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2.add("argument2", argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2Argument2);

        argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1.add("argument2", argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1Argument2);

        argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1.add("argument1", argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1Argument1);

        argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1.add("argument1", argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument1);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument2 = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument2)
                .type(DensityType.ADD).densityFunction("argument1", NoiseRouterData.SPAGHETTI_2D)
                .densityFunction("argument2", NoiseRouterData.SPAGHETTI_ROUGHNESS_FUNCTION);

        argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1.add("argument2", argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1Argument2);

        argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRange.add("argument1", argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument1);

        JsonObject argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument2 = new JsonObject();
        jsonHelper.setJsonObject(argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument2)
                .type(DensityType.RANGE_CHOICE).input(NoiseRouterData.PILLARS).minMaxInclusiveExclusive(-1000000, 0.03)
                .number("when_in_range", -1000000).densityFunction("when_out_of_range", NoiseRouterData.PILLARS);
        argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRange.add("argument2", argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRangeArgument2);

        argument1ArgumentArgument2ArgumentArgumentArgument.add("when_out_of_range", argument1ArgumentArgument2ArgumentArgumentArgumentWhenOutOfRange);

        argument1ArgumentArgument2ArgumentArgument.add("argument", argument1ArgumentArgument2ArgumentArgumentArgument);

        argument1ArgumentArgument2Argument.add("argument", argument1ArgumentArgument2ArgumentArgument);

        argument1ArgumentArgument2.add("argument", argument1ArgumentArgument2Argument);

        argument1Argument.add("argument2", argument1ArgumentArgument2);

        argument1.add("argument", argument1Argument);

        finalDensity.add("argument1", argument1);

        jsonHelper.setJsonObject(finalDensity).densityFunction("argument2", NoiseRouterData.NOODLE);

        return finalDensity;
    }

    private JsonObject addVeinToggle() {
        JsonObject veinToggle = new JsonObject();
        JsonHelper jsonHelper = new JsonHelper(veinToggle)
                .type(DensityType.INTERPOLATED);

        JsonObject veinToggleArgument = new JsonObject();
        jsonHelper.setJsonObject(veinToggleArgument).type(DensityType.RANGE_CHOICE)
                .input(NoiseRouterData.Y).minMaxInclusiveExclusive(-60, 51);
        JsonObject veinToggleArgumentWhenInRange = new JsonObject();
        jsonHelper.setJsonObject(veinToggleArgumentWhenInRange)
                .type(DensityType.NOISE).noise(Noises.ORE_VEININESS)
                .number("xz_scale", 1.5)
                .number("y_scale", 1.5);
        veinToggleArgument.add("when_in_range", veinToggleArgumentWhenInRange);

        jsonHelper.setJsonObject(veinToggleArgument)
                .number("when_out_of_range", 0);

        veinToggle.add("argument", veinToggleArgument);

        return veinToggle;
    }

    private JsonObject addVeinRidged() {
        JsonObject veinRidged = new JsonObject();
        JsonHelper jsonHelper = new JsonHelper(veinRidged)
                .type(DensityType.ADD).number("argument1", -0.07999999821186066);

        JsonObject argument2 = new JsonObject();
        jsonHelper.setJsonObject(argument2).type(DensityType.MAX);

        JsonObject argument2Argument1 = new JsonObject();
        jsonHelper.setJsonObject(argument2Argument1).type(DensityType.ABS);

        JsonObject argument2Argument1Argument = new JsonObject();
        jsonHelper.setJsonObject(argument2Argument1Argument).type(DensityType.INTERPOLATED);

        JsonObject argument2Argument1ArgumentArgument = new JsonObject();
        jsonHelper.setJsonObject(argument2Argument1ArgumentArgument).type(DensityType.RANGE_CHOICE)
                .input(NoiseRouterData.Y).minMaxInclusiveExclusive(-60, 51);

        JsonObject argument2Argument1ArgumentArgumentWhenInRange = new JsonObject();
        jsonHelper.setJsonObject(argument2Argument1ArgumentArgumentWhenInRange).type(DensityType.NOISE)
                .noise(Noises.ORE_VEIN_A).number("xz_scale", 4)
                .number("y_scale", 4);

        argument2Argument1ArgumentArgument.add("when_in_range", argument2Argument1ArgumentArgumentWhenInRange);

        jsonHelper.setJsonObject(argument2Argument1ArgumentArgument).number("when_out_of_range", 0);

        argument2Argument1Argument.add("argument", argument2Argument1ArgumentArgument);

        argument2Argument1.add("argument", argument2Argument1Argument);

        argument2.add("argument1", argument2Argument1);

        JsonObject argument2Argument2 = new JsonObject();
        jsonHelper.setJsonObject(argument2Argument2).type(DensityType.ABS);

        JsonObject argument2Argument2Argument = new JsonObject();
        jsonHelper.setJsonObject(argument2Argument2Argument).type(DensityType.INTERPOLATED);

        JsonObject argument2Argument2ArgumentArgument = new JsonObject();
        jsonHelper.setJsonObject(argument2Argument2ArgumentArgument).type(DensityType.RANGE_CHOICE)
                .input(NoiseRouterData.Y).minMaxInclusiveExclusive(-60, 51);

        JsonObject argument2Argument2ArgumentArgumentWhenInRange = new JsonObject();
        jsonHelper.setJsonObject(argument2Argument2ArgumentArgumentWhenInRange).type(DensityType.NOISE)
                .noise(Noises.ORE_VEIN_B).number("xz_scale", 4)
                .number("y_scale", 4);

        argument2Argument2ArgumentArgument.add("when_in_range", argument2Argument2ArgumentArgumentWhenInRange);

        jsonHelper.setJsonObject(argument2Argument2ArgumentArgument).number("when_out_of_range", 0);

        argument2Argument2Argument.add("argument", argument2Argument2ArgumentArgument);

        argument2Argument2.add("argument", argument2Argument2Argument);

        argument2.add("argument2", argument2Argument2);

        veinRidged.add("argument2", argument2);

        return veinRidged;
    }

}