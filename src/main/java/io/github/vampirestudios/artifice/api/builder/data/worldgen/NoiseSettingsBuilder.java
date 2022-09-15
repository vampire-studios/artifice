package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.NoiseConfigBuilder;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.Noises;

import static io.github.vampirestudios.artifice.api.builder.data.worldgen.JsonHelper.DensityType;

public class NoiseSettingsBuilder extends TypedJsonObject {
	private final Gson GSON = new GsonBuilder().setLenient().setPrettyPrinting().create();

	public NoiseSettingsBuilder() {
		super(new JsonObject());
	}

	public static NoiseSettingsBuilder create(NoiseSettingsBuilder noiseSettingsBuilder) {
		return noiseSettingsBuilder;
	}

	/**
	 * Set the sea level.
	 */
	public NoiseSettingsBuilder seaLevel(int seaLevel) {
		NoiseSettingsBuilder builder = new NoiseSettingsBuilder();
		builder.add("sea_level", seaLevel);
		return builder;
	}

	/**
	 * Build noise config.
	 */
	public NoiseSettingsBuilder noiseConfig(NoiseConfigBuilder noiseConfigBuilder) {
		NoiseSettingsBuilder builder = new NoiseSettingsBuilder();
		builder.add("noise", noiseConfigBuilder.build());
		return builder;
	}

	public NoiseSettingsBuilder disableMobGeneration(boolean disableMobGeneration) {
		NoiseSettingsBuilder builder = new NoiseSettingsBuilder();
		builder.add("disable_mob_generation", disableMobGeneration);
		return builder;
	}

	public NoiseSettingsBuilder aquifersEnabled(boolean aquifersEnabled) {
		NoiseSettingsBuilder builder = new NoiseSettingsBuilder();
		builder.add("aquifers_enabled", aquifersEnabled);
		return builder;
	}

	public NoiseSettingsBuilder oreVeinsEnabled(boolean oreVeinsEnabled) {
		NoiseSettingsBuilder builder = new NoiseSettingsBuilder();
		builder.add("ore_veins_enabled", oreVeinsEnabled);
		return builder;
	}

	public NoiseSettingsBuilder legacyRandomSource(boolean legacyRandomSource) {
		NoiseSettingsBuilder builder = new NoiseSettingsBuilder();
		builder.add("legacy_random_source", legacyRandomSource);
		return builder;
	}

	/**
	 * Set a block state.
	 */
	public NoiseSettingsBuilder setBlockState(String id, StateDataBuilder stateDataBuilder) {
		NoiseSettingsBuilder builder = new NoiseSettingsBuilder();
		builder.add(id, stateDataBuilder.build());
		return builder;
	}

	/**
	 * Build default block.
	 */
	public NoiseSettingsBuilder defaultBlock(StateDataBuilder stateDataBuilder) {
		return setBlockState("default_block", stateDataBuilder);
	}

	/**
	 * Build default fluid.
	 */
	public NoiseSettingsBuilder defaultFluid(StateDataBuilder stateDataBuilder) {
		return setBlockState("default_fluid", stateDataBuilder);
	}

	/**
	 * Build surface rules.
	 */
	public NoiseSettingsBuilder surfaceRules(SurfaceRulesBuilder surfaceRulesBuilder) {
		NoiseSettingsBuilder builder = new NoiseSettingsBuilder();
		builder.join("surface_rule", surfaceRulesBuilder.build());
		return builder;
	}

	public NoiseSettingsBuilder noiseRouter() {
		NoiseSettingsBuilder builder = new NoiseSettingsBuilder();
		TypedJsonObject jsonObject = new TypedJsonObject();
		DensityFunctionBuilder barrierBuilder = DensityFunctionBuilder.noise(Noises.AQUIFER_BARRIER, 1, 0.5);
		jsonObject.add("barrier", barrierBuilder.build());

		DensityFunctionBuilder fluidLevelFloodednessBuilder = DensityFunctionBuilder.noise(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS, 1, 0.67);
		jsonObject.add("fluid_level_floodedness", fluidLevelFloodednessBuilder.build());

		DensityFunctionBuilder fluidLevelSpreadBuilder = DensityFunctionBuilder.noise(Noises.AQUIFER_FLUID_LEVEL_SPREAD, 1, 0.7142857142857143);
		jsonObject.add("fluid_level_spread", fluidLevelSpreadBuilder.build());

		DensityFunctionBuilder lavaBuilder = DensityFunctionBuilder.noise(Noises.AQUIFER_LAVA, 1, 1);
		jsonObject.add("lava", lavaBuilder.build());

		DensityFunctionBuilder temperatureBuilder = DensityFunctionBuilder.shiftedNoise(Noises.TEMPERATURE, 0.25, 0,
				DensityFunctionBuilder.shiftX(NoiseRouterData.SHIFT_X),
				DensityFunctionBuilder.shiftY(0),
				DensityFunctionBuilder.shiftZ(NoiseRouterData.SHIFT_Z)
		);
		jsonObject.add("temperature", temperatureBuilder.build());

		DensityFunctionBuilder vegetationBuilder = DensityFunctionBuilder.shiftedNoise(Noises.VEGETATION, 0.25, 0,
				DensityFunctionBuilder.shiftX(NoiseRouterData.SHIFT_X),
				DensityFunctionBuilder.shiftY(0),
				DensityFunctionBuilder.shiftZ(NoiseRouterData.SHIFT_Z)
		);
		jsonObject.add("vegetation", vegetationBuilder.build())
				.add("continents", "minecraft:overworld/continents")
				.add("erosion", "minecraft:overworld/erosion")
				.add("depth", "minecraft:overworld/depth")
				.add("ridges", "minecraft:overworld/ridges")
				.add("initial_density_without_jaggedness", builder.addInitialDensityWithoutJaggedness())
				.add("final_density", builder.addFinalDensity())
				.add("vein_toggle", builder.addVeinToggle())
				.add("vein_ridged", builder.addVeinRidged());

		DensityFunctionBuilder veinGapBuilder = DensityFunctionBuilder.noise(Noises.ORE_GAP, 1, 1);
		jsonObject.add("vein_gap", veinGapBuilder.build());
		builder.join("noise_router", jsonObject.build());
		return builder;
	}

	private DensityFunctionBuilder addInitialDensityWithoutJaggedness() {
		return DensityFunctionBuilder.add(0.1171875,
				DensityFunctionBuilder.mul(DensityFunctionBuilder.yClampedGradient(-64, -40, 0, 1),
						DensityFunctionBuilder.add(-0.1171875, DensityFunctionBuilder.add(-0.1171875,
										DensityFunctionBuilder.mul(DensityFunctionBuilder.yClampedGradient(240, 256, 1, 0),
												DensityFunctionBuilder.add(0.078125, DensityFunctionBuilder.clamp(DensityFunctionBuilder.add(-0.703125,
														DensityFunctionBuilder.mul(4, DensityFunctionBuilder.quarterNegative(
																DensityFunctionBuilder.mul(NoiseRouterData.DEPTH, DensityFunctionBuilder.cache2d(NoiseRouterData.FACTOR))
														))
												), -64, 64))
										)
								)
						)));
	}

	private JsonObject addFinalDensity() {
		/*DensityFunctionBuilder.min(DensityFunctionBuilder.square(
						DensityFunctionBuilder.mul(
								0.64,
								DensityFunctionBuilder.interpolated(
										DensityFunctionBuilder.blendDensity(
												DensityFunctionBuilder.add(
														0.1171875,
														DensityFunctionBuilder.mul(
																DensityFunctionBuilder.yClampedGradient(-64, -40, 0, 1),
																DensityFunctionBuilder.add(
																		-0.1171875,
																		DensityFunctionBuilder.add(
																				-0.078125,
																				DensityFunctionBuilder.mul(
																						DensityFunctionBuilder.yClampedGradient(240, 256, 1, 0),
																						DensityFunctionBuilder.add(
																								0.078125,
																								DensityFunctionBuilder.rangeChoice(
																										DensityFunction.
																								)
																						)
																				)
																		)
																)
														)
												)
										)
								)
						)
				)
		);*/
		String finalDensity = """
				{
				      "type": "minecraft:min",
				      "argument1": {
				        "type": "minecraft:squeeze",
				        "argument": {
				          "type": "minecraft:mul",
				          "argument1": 0.64,
				          "argument2": {
				            "type": "minecraft:interpolated",
				            "argument": {
				              "type": "minecraft:blend_density",
				              "argument": {
				                "type": "minecraft:add",
				                "argument1": 0.1171875,
				                "argument2": {
				                  "type": "minecraft:mul",
				                  "argument1": {
				                    "type": "minecraft:y_clamped_gradient",
				                    "from_y": -64,
				                    "to_y": -40,
				                    "from_value": 0,
				                    "to_value": 1
				                  },
				                  "argument2": {
				                    "type": "minecraft:add",
				                    "argument1": -0.1171875,
				                    "argument2": {
				                      "type": "minecraft:add",
				                      "argument1": -0.078125,
				                      "argument2": {
				                        "type": "minecraft:mul",
				                        "argument1": {
				                          "type": "minecraft:y_clamped_gradient",
				                          "from_y": 240,
				                          "to_y": 256,
				                          "from_value": 1,
				                          "to_value": 0
				                        },
				                        "argument2": {
				                          "type": "minecraft:add",
				                          "argument1": 0.078125,
				                          "argument2": {
				                            "type": "minecraft:range_choice",
				                            "input": "minecraft:overworld/sloped_cheese",
				                            "min_inclusive": -1000000,
				                            "max_exclusive": 1.5625,
				                            "when_in_range": {
				                              "type": "minecraft:min",
				                              "argument1": "minecraft:overworld/sloped_cheese",
				                              "argument2": {
				                                "type": "minecraft:mul",
				                                "argument1": 5,
				                                "argument2": "minecraft:overworld/caves/entrances"
				                              }
				                            },
				                            "when_out_of_range": {
				                              "type": "minecraft:max",
				                              "argument1": {
				                                "type": "minecraft:min",
				                                "argument1": {
				                                  "type": "minecraft:min",
				                                  "argument1": {
				                                    "type": "minecraft:add",
				                                    "argument1": {
				                                      "type": "minecraft:mul",
				                                      "argument1": 4,
				                                      "argument2": {
				                                        "type": "minecraft:square",
				                                        "argument": {
				                                          "type": "minecraft:noise",
				                                          "noise": "minecraft:cave_layer",
				                                          "xz_scale": 1,
				                                          "y_scale": 8
				                                        }
				                                      }
				                                    },
				                                    "argument2": {
				                                      "type": "minecraft:add",
				                                      "argument1": {
				                                        "type": "minecraft:clamp",
				                                        "input": {
				                                          "type": "minecraft:add",
				                                          "argument1": 0.27,
				                                          "argument2": {
				                                            "type": "minecraft:noise",
				                                            "noise": "minecraft:cave_cheese",
				                                            "xz_scale": 1,
				                                            "y_scale": 0.6666666666666666
				                                          }
				                                        },
				                                        "min": -1,
				                                        "max": 1
				                                      },
				                                      "argument2": {
				                                        "type": "minecraft:clamp",
				                                        "input": {
				                                          "type": "minecraft:add",
				                                          "argument1": 1.5,
				                                          "argument2": {
				                                            "type": "minecraft:mul",
				                                            "argument1": -0.64,
				                                            "argument2": "minecraft:overworld/sloped_cheese"
				                                          }
				                                        },
				                                        "min": 0,
				                                        "max": 0.5
				                                      }
				                                    }
				                                  },
				                                  "argument2": "minecraft:overworld/caves/entrances"
				                                },
				                                "argument2": {
				                                  "type": "minecraft:add",
				                                  "argument1": "minecraft:overworld/caves/spaghetti_2d",
				                                  "argument2": "minecraft:overworld/caves/spaghetti_roughness_function"
				                                }
				                              },
				                              "argument2": {
				                                "type": "minecraft:range_choice",
				                                "input": "minecraft:overworld/caves/pillars",
				                                "min_inclusive": -1000000,
				                                "max_exclusive": 0.03,
				                                "when_in_range": -1000000,
				                                "when_out_of_range": "minecraft:overworld/caves/pillars"
				                              }
				                            }
				                          }
				                        }
				                      }
				                    }
				                  }
				                }
				              }
				            }
				          }
				        }
				      },
				      "argument2": "minecraft:overworld/caves/noodle"
				    }""";
		return GSON.fromJson(finalDensity, JsonObject.class);
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