package com.swordglowsblue.artifice.test;

import io.github.vampirestudios.artifice.api.Artifice;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.BiomeSourceBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.ChunkGeneratorTypeBuilder.FlatChunkGeneratorTypeBuilder.LayersBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.BlockStateProviderBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.SurfaceRulesBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.YOffsetBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.biome.BiomeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config.TreeFeatureConfigBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.placementmodifiers.BiomePlacementModifier;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.placementmodifiers.InSquarePlacementModifier;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.FeatureSizeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.FoliagePlacerBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.TrunkPlacerBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.structure.SpawnsBuilder;
import io.github.vampirestudios.artifice.api.resource.StringResource;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.io.IOException;

public class ArtificeTestMod implements ModInitializer {
	private static final Item.Properties itemSettings = new Item.Properties().tab(CreativeModeTab.TAB_MISC);
	private static final Item testItem = Registry.register(Registry.ITEM, id("test_item"), new Item(itemSettings));
	private static final Block testBlock = Registry.register(Registry.BLOCK, id("test_block"), new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
	private static final Item testBlockItem = Registry.register(Registry.ITEM, id("test_block"), new BlockItem(testBlock, itemSettings));
	private static final ResourceKey<DimensionType> testDimension = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, id("test_dimension_type_vanilla"));
	private static final ResourceKey<DimensionType> testDimensionCustom = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, id("test_dimension_type_custom"));
	private static final ResourceKey<NoiseGeneratorSettings> testDimensionSettings = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, id("test_dimension"));

	private static ResourceLocation id(String name) {
		return new ResourceLocation("artifice", name);
	}

	public void onInitialize() {
		Registry.register(Registry.CHUNK_GENERATOR, ResourceKey.create(Registry.CHUNK_GENERATOR_REGISTRY, id("test_chunk_generator")).location(), TestChunkGenerator.CODEC);

		Artifice.registerDataPack(id("optional_test"), pack -> {
			pack.setOptional();

			pack.add(id("recipes/test_optional.json"), new StringResource("""
					{
					  "type": "minecraft:crafting_shaped",
					  "group": "wooden_door",
					  "pattern": [
					    "##",
					    "##",
					    "# "
					  ],
					  "key": {
					    "#": {
					      "item": "minecraft:stone"
					    }
					  },
					  "result": {
					    "item": "artifice:test_item",
					    "count": 2
					  }
					}"""));
		});
		Artifice.registerDataPack(id("testmod"), pack -> {
			pack.setDisplayName("Artifice Test Data");
			pack.setDescription("Data for the Artifice test mod");

			pack.add(id("recipes/test_item.json"), new StringResource("""
					{
					  "type": "minecraft:crafting_shaped",
					  "group": "wooden_door",
					  "pattern": [
					    "##",
					    "##",
					    "##"
					  ],
					  "key": {
					    "#": {
					      "item": "minecraft:stone"
					    }
					  },
					  "result": {
					    "item": "artifice:test_item",
					    "count": 3
					  }
					}"""));

			/*pack.addDimensionType(testDimension.location(), dimensionTypeBuilder -> dimensionTypeBuilder
					.isNatural(false).hasRaids(false).respawnAnchorWorks(true).bedWorks(false).piglinSafe(false)
					.ambientLight(6.0F).infiniburn(BlockTags.INFINIBURN_OVERWORLD).ultrawarm(false).hasCeiling(false)
					.hasSkylight(false).coordinate_scale(1.0).logicalHeight(832).height(832).minimumY(-512).effects("minecraft:the_end"));

			pack.addDimensionType(testDimensionCustom.location(), dimensionTypeBuilder -> dimensionTypeBuilder
					.isNatural(true).hasRaids(false).respawnAnchorWorks(false).bedWorks(false).piglinSafe(false)
					.ambientLight(0.0F).infiniburn(BlockTags.INFINIBURN_OVERWORLD).ultrawarm(false).hasCeiling(false)
					.hasSkylight(true).coordinate_scale(1.0).logicalHeight(512).height(512).minimumY(-256)
			);*/

			pack.addDimension(id("test_dimension"), dimensionBuilder -> dimensionBuilder
					.dimensionType(/*testDimension.location()*/new ResourceLocation("overworld"))
					.flatGenerator(flatChunkGeneratorTypeBuilder -> flatChunkGeneratorTypeBuilder
							.addLayer(
									new LayersBuilder("minecraft:bedrock", 2),
									new LayersBuilder("minecraft:deepslate", 10),
									new LayersBuilder("minecraft:stone", 2),
									new LayersBuilder("minecraft:granite", 2)
							).biome("minecraft:plains")
					)
			);

			pack.addDimension(id("test_dimension2"), dimensionBuilder -> dimensionBuilder
					.dimensionType(/*testDimension.location()*/new ResourceLocation("overworld"))
					.flatGenerator(flatChunkGeneratorTypeBuilder -> flatChunkGeneratorTypeBuilder
							.lakes(false).features(true)
							.addLayer(
									new LayersBuilder("minecraft:bedrock", 2),
									new LayersBuilder("minecraft:deepslate", 10),
									new LayersBuilder("minecraft:stone", 2),
									new LayersBuilder("minecraft:andesite", 2)
							).biome("minecraft:plains")
					)
			);

			pack.addDimension(id("test_dimension_custom"), dimensionBuilder -> {
				dimensionBuilder.dimensionType(/*testDimensionCustom.location()*/new ResourceLocation("overworld"));
				dimensionBuilder.noiseGenerator(noiseChunkGeneratorTypeBuilder -> {
					noiseChunkGeneratorTypeBuilder.multiNoiseBiomeSource(multiNoiseBiomeSourceBuilder -> multiNoiseBiomeSourceBuilder.biomes(
							new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeBuilder()
								.biome(id("test_biome").toString())
								.parameters(new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeParametersBuilder()
									.temperature(0).humidity(1).continentalness(0).erosion(0)
									.weirdness(0).depth(1).offset(0)
								),
							new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeBuilder()
								.biome(id("test_biome2").toString())
								.parameters(new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeParametersBuilder()
									.temperature(0).humidity(0).continentalness(0).erosion(-0.5F)
									.weirdness(0).depth(1).offset(0)
								),
							new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeBuilder()
								.biome(id("test_biome3").toString())
								.parameters(new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeParametersBuilder()
									.temperature(0).humidity(1).continentalness(0.1F).erosion(0)
									.weirdness(0).depth(1).offset(0)
								),
							new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeBuilder()
								.biome(id("test_biome4").toString())
								.parameters(new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeParametersBuilder()
									.temperature(0).humidity(1).continentalness(0).erosion(0.2F)
									.weirdness(0).depth(1).offset(0)
								)
					));
					noiseChunkGeneratorTypeBuilder.noiseSettings("minecraft:overworld");
				});
			});

			pack.addDimension(id("test_dimension_custom2"), dimensionBuilder -> {
				dimensionBuilder.dimensionType(/*testDimensionCustom.location()*/new ResourceLocation("overworld"));
				dimensionBuilder.noiseGenerator(noiseChunkGeneratorTypeBuilder -> {
					noiseChunkGeneratorTypeBuilder.multiNoiseBiomeSource(multiNoiseBiomeSourceBuilder -> multiNoiseBiomeSourceBuilder.biomes(
							new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeBuilder()
									.biome(id("test_biome").toString())
									.parameters(new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeParametersBuilder()
											.temperature(0).humidity(1).continentalness(0).erosion(0)
											.weirdness(0).depth(1).offset(0)
									),
							new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeBuilder()
									.biome(id("test_biome2").toString())
									.parameters(new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeParametersBuilder()
											.temperature(0).humidity(0).continentalness(0).erosion(-0.5F)
											.weirdness(0).depth(1).offset(0)
									),
							new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeBuilder()
									.biome(id("test_biome3").toString())
									.parameters(new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeParametersBuilder()
											.temperature(0).humidity(1).continentalness(0.1F).erosion(0)
											.weirdness(0).depth(1).offset(0)
									),
							new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeBuilder()
									.biome(id("test_biome4").toString())
									.parameters(new BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeParametersBuilder()
											.temperature(0).humidity(1).continentalness(0).erosion(0.2F)
											.weirdness(0).depth(1).offset(0)
									)
					));
					noiseChunkGeneratorTypeBuilder.noiseSettings("artifice:test_dimension");
				});
			});

			pack.addBiome(id("test_biome"), biomeBuilder -> biomeBuilder.precipitation(Biome.Precipitation.RAIN)
					.temperature(0.8F).downfall(0.4F)
					.addSpawnCosts("minecraft:bee", new BiomeBuilder.SpawnDensityBuilder(0.12, 1))
					.addSpawnCosts("minecraft:cat", new BiomeBuilder.SpawnDensityBuilder(0.4, 1))
					.effects(biomeEffectsBuilder -> {
						biomeEffectsBuilder.waterColor(4159204);
						biomeEffectsBuilder.waterFogColor(329011);
						biomeEffectsBuilder.fogColor(12638463);
						biomeEffectsBuilder.skyColor(4159204);
					}).addAirCarvers(id("test_carver").toString())
			);

			pack.addBiome(id("test_biome2"), biomeBuilder -> biomeBuilder.precipitation(Biome.Precipitation.RAIN)
					.temperature(0.8F).downfall(0.4F)
					.addSpawnCosts("minecraft:spider", new BiomeBuilder.SpawnDensityBuilder(0.6, 1))
					.addSpawnCosts("minecraft:cat", new BiomeBuilder.SpawnDensityBuilder(0.4, 1))
					.effects(biomeEffectsBuilder -> {
						biomeEffectsBuilder.waterColor(4159204);
						biomeEffectsBuilder.waterFogColor(329011);
						biomeEffectsBuilder.fogColor(12638463);
						biomeEffectsBuilder.skyColor(4159204);
					}).addAirCarvers(id("test_carver").toString())
			);

			pack.addBiome(id("test_biome3"), biomeBuilder -> biomeBuilder.precipitation(Biome.Precipitation.RAIN)
					.temperature(2.0F).downfall(0.4F)
					.addSpawnCosts("minecraft:bee", new BiomeBuilder.SpawnDensityBuilder(0.12, 1))
					.addSpawnCosts("minecraft:cat", new BiomeBuilder.SpawnDensityBuilder(0.4, 1))
					.effects(biomeEffectsBuilder -> {
						biomeEffectsBuilder.waterColor(4159204);
						biomeEffectsBuilder.waterFogColor(329011);
						biomeEffectsBuilder.fogColor(12638463);
						biomeEffectsBuilder.skyColor(4159204);
					}).addAirCarvers(id("test_carver").toString())
			);

			pack.addBiome(id("test_biome4"), biomeBuilder -> biomeBuilder.precipitation(Biome.Precipitation.RAIN)
					.temperature(1.4F).downfall(1.0F)
					.addSpawnCosts("minecraft:bee", new BiomeBuilder.SpawnDensityBuilder(0.12, 1))
					.addSpawnCosts("minecraft:cat", new BiomeBuilder.SpawnDensityBuilder(0.4, 1))
					.effects(biomeEffectsBuilder -> {
						biomeEffectsBuilder.waterColor(4159204);
						biomeEffectsBuilder.waterFogColor(329011);
						biomeEffectsBuilder.fogColor(12638463);
						biomeEffectsBuilder.skyColor(4159204);
					}).addAirCarvers(id("test_carver").toString())
			);

			pack.addStructure(id("test_structure"), configuredStructureFeatureBuilder -> {
				configuredStructureFeatureBuilder.type("minecraft:village");
				configuredStructureFeatureBuilder.singleBiome("minecraft:plains");
				configuredStructureFeatureBuilder.adoptNoise(false);
				configuredStructureFeatureBuilder.spawnOverrides(spawnOverridesBuilder -> {});
				configuredStructureFeatureBuilder.featureConfig(featureConfigBuilder -> {
					featureConfigBuilder.jsonString("start_pool", "minecraft:village/desert/town_centers");
					featureConfigBuilder.jsonNumber("size", 6);
				});
			});

			pack.addStructure(id("test_structure2"), structureBuilder -> {
				structureBuilder.type("minecraft:village");
				structureBuilder.singleBiome("minecraft:nether_wastes");
				structureBuilder.adoptNoise(true);
				structureBuilder.spawnOverrides(spawnOverridesBuilder ->
						spawnOverridesBuilder.monster(mobSpawnOverrideRuleBuilder ->
								mobSpawnOverrideRuleBuilder.pieceBoundingBox().spawns(
										new SpawnsBuilder("minecraft:piglin", 5, 4, 4),
										new SpawnsBuilder("minecraft:piglin_brute", 5, 4, 4),
										new SpawnsBuilder("minecraft:zombified_piglin", 5, 4, 4)
								)
						)
				);
				structureBuilder.featureConfig(featureConfigBuilder -> {
					featureConfigBuilder.jsonString("start_pool", "minecraft:village/desert/town_centers");
					featureConfigBuilder.jsonNumber("size", 6);
				});
			});

			pack.addConfiguredCarver(id("test_carver"), carverBuilder ->
					carverBuilder
							.probability(0.15F)
							.type("minecraft:cave")
							.y(heightProviderBuilders -> heightProviderBuilders.uniform("y",
									uniformHeightProviderBuilder -> uniformHeightProviderBuilder
											.minInclusive(YOffsetBuilder.aboveBottom(8))
											.maxInclusive(YOffsetBuilder.absolute(180))
							))
							.yScale(heightProviderBuilders -> heightProviderBuilders.uniform("yScale",
									uniformHeightProviderBuilder -> uniformHeightProviderBuilder
											.minAndMaxInclusive(0.1F, 0.9F)
							))
							.lavaLevel(YOffsetBuilder.aboveBottom(8))
							.horizontalRadiusModifier(heightProviderBuilders -> heightProviderBuilders.uniform("horizontal_radius_modifier",
									uniformHeightProviderBuilder -> uniformHeightProviderBuilder
											.minAndMaxInclusive(1.7F, 2.4F)
							))
							.verticalRadiusModifier(heightProviderBuilders -> heightProviderBuilders.uniform("vertical_radius_modifier",
									uniformHeightProviderBuilder -> uniformHeightProviderBuilder
											.minAndMaxInclusive(1.5F, 2.1F)
							))
							.floorLevel(heightProviderBuilders -> heightProviderBuilders.uniform("floor_level",
									uniformHeightProviderBuilder -> uniformHeightProviderBuilder
											.minAndMaxInclusive(-1F, -0.4F)
							))
			);

			// gotta wait on noise config
			pack.addNoiseSettingsBuilder(id("test_dimension"),noiseSettingsBuilder ->
					noiseSettingsBuilder.defaultBlock(new StateDataBuilder().name("minecraft:stone"))
							.defaultFluid(new StateDataBuilder().name("minecraft:lava").setProperty("level", "0"))
							.seaLevel(65).legacyRandomSource(false).aquifersEnabled(true)
							.disableMobGeneration(false).aquifersEnabled(true).oreVeinsEnabled(true)
							.oreVeinsEnabled(true).noiseConfig(noiseConfigBuilder -> noiseConfigBuilder
									.minimumY(-64).height(384).sizeHorizontal(1).sizeVertical(2)
							).noiseRouter().surfaceRules(builder -> builder.sequence(surfaceRulesBuilder ->
									surfaceRulesBuilder.condition(
											surfaceRulesBuilder1 -> surfaceRulesBuilder1.verticalGradient(
													"bebrock",
													YOffsetBuilder.aboveBottom(0),
													YOffsetBuilder.aboveBottom(5)
											),
											surfaceRulesBuilder1 -> surfaceRulesBuilder1.block(stateDataBuilder -> stateDataBuilder.name("minecraft:bedrock"))
									), surfaceRulesBuilder ->
									surfaceRulesBuilder.condition(
											SurfaceRulesBuilder::aboveMainSurface,
											surfaceRulesBuilder1 -> surfaceRulesBuilder1.sequence(surfaceRulesBuilder2 ->
															surfaceRulesBuilder2.condition(
																	surfaceRulesBuilder3 -> surfaceRulesBuilder3.biome(id("test_biome").toString()),
																	surfaceRulesBuilder3 -> surfaceRulesBuilder3.block(stateDataBuilder -> stateDataBuilder.name("artifice:test_block"))
															),
													surfaceRulesBuilder2 -> surfaceRulesBuilder2.block(stateDataBuilder -> stateDataBuilder.name("minecraft:grass_block"))
											)
									), surfaceRulesBuilder ->
									surfaceRulesBuilder.condition(
											surfaceRulesBuilder1 -> surfaceRulesBuilder1.verticalGradient(
													"tuff",
													YOffsetBuilder.absolute(0),
													YOffsetBuilder.absolute(16)
											),
											surfaceRulesBuilder1 -> surfaceRulesBuilder1.block(stateDataBuilder -> stateDataBuilder.name("minecraft:tuff"))
									), surfaceRulesBuilder ->
									surfaceRulesBuilder.condition(
											surfaceRulesBuilder1 -> surfaceRulesBuilder1.verticalGradient(
													"deepslate",
													YOffsetBuilder.absolute(-16),
													YOffsetBuilder.absolute(0)
											),
											surfaceRulesBuilder1 -> surfaceRulesBuilder1.block(stateDataBuilder -> stateDataBuilder.name("minecraft:deepslate").setProperty("axis", "y"))
									)
							))
			);
			//not even going to touch this until we get everything else working
			// Tested, it works now. Wasn't in 20w28a.
			pack.addConfiguredFeature(id("test_featureee"), configuredFeatureBuilder ->
				configuredFeatureBuilder.featureID("minecraft:tree")
					.featureConfig(treeFeatureConfigBuilder ->
						treeFeatureConfigBuilder
							.ignoreVines(true)
							.maxWaterDepth(5)
							.dirtProvider(simpleBlockStateProviderBuilder ->
										simpleBlockStateProviderBuilder.state(blockStateDataBuilder ->
												blockStateDataBuilder.name("minecraft:dirt")),
								new BlockStateProviderBuilder.SimpleBlockStateProviderBuilder()
							).trunkProvider(simpleBlockStateProviderBuilder ->
											simpleBlockStateProviderBuilder.state(blockStateDataBuilder ->
													blockStateDataBuilder.name("minecraft:oak_log")
															.setProperty("axis", "y")),
									new BlockStateProviderBuilder.SimpleBlockStateProviderBuilder()
							).foliageProvider(simpleBlockStateProviderBuilder ->
									simpleBlockStateProviderBuilder.state(blockStateDataBuilder ->
											blockStateDataBuilder.name("minecraft:spruce_leaves")
													.setProperty("persistent", "false")
													.setProperty("distance", "7")
									), new BlockStateProviderBuilder.SimpleBlockStateProviderBuilder()
							).foliagePlacer(foliagePlacerBuilder ->
											foliagePlacerBuilder.height(2).offset(1).radius(2),
									new FoliagePlacerBuilder.BlobFoliagePlacerBuilder()
							).trunkPlacer(fancyTrunkPlacerBuilder ->
											fancyTrunkPlacerBuilder.baseHeight(12).heightRandA(3).heightRandB(4),
									new TrunkPlacerBuilder.FancyTrunkPlacerBuilder()
							).minimumSize(twoLayersFeatureSizeBuilder ->
											twoLayersFeatureSizeBuilder.limit(10).lowerSize(1).upperSize(9),
									new FeatureSizeBuilder.TwoLayersFeatureSizeBuilder()
							).heightmap(Heightmap.Types.OCEAN_FLOOR), new TreeFeatureConfigBuilder()
					));
			// Should be working but Minecraft coders did something wrong and the default feature is being return when it shouldn't resulting in a crash.
			pack.addPlacedFeature(id("test_placed_feature"), configuredFeatureBuilder -> configuredFeatureBuilder.feature("artifice:test_featureee")
					.placementModifiers(new BiomePlacementModifier(), new InSquarePlacementModifier())
			);

			try {
				pack.dumpResources("testing_data", "data");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
