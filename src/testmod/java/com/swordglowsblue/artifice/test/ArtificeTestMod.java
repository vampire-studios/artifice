package com.swordglowsblue.artifice.test;

import io.github.vampirestudios.artifice.api.Artifice;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.ChunkGeneratorTypeBuilder.FlatChunkGeneratorTypeBuilder.LayersBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.SurfaceRulesBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.YOffsetBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.biome.BiomeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.structure.SpawnsBuilder;
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

import java.io.IOException;
import java.util.Random;

public class ArtificeTestMod implements ModInitializer {
	private static final Item.Properties itemSettings = new Item.Properties().tab(CreativeModeTab.TAB_MISC);
	private static final Item testItem = Registry.register(Registry.ITEM, id("test_item"), new Item(itemSettings));
	private static final Block testBlock = Registry.register(Registry.BLOCK, id("test_block"), new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
	private static final Item testBlockItem = Registry.register(Registry.ITEM, id("test_block"), new BlockItem(testBlock, itemSettings));
//	private static final ResourceKey<DimensionType> testDimension = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, id("test_dimension_type_vanilla"));
//	private static final ResourceKey<DimensionType> testDimensionCustom = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, id("test_dimension_type_custom"));

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

//			pack.addDimensionType(testDimension.location(), dimensionTypeBuilder -> dimensionTypeBuilder
//					.isNatural(false).hasRaids(false).respawnAnchorWorks(true).bedWorks(false).piglinSafe(false)
//					.ambientLight(6.0F).infiniburn(BlockTags.INFINIBURN_OVERWORLD)
//					.ultrawarm(false).hasCeiling(false).hasSkylight(false).coordinate_scale(1.0).logicalHeight(832).height(832).minimumY(-512).effects("minecraft:the_end"));
//
//			pack.addDimensionType(testDimensionCustom.location(), dimensionTypeBuilder -> dimensionTypeBuilder
//					.isNatural(true).hasRaids(false).respawnAnchorWorks(false).bedWorks(false).piglinSafe(false)
//					.ambientLight(0.0F).infiniburn(BlockTags.INFINIBURN_OVERWORLD).ultrawarm(false)
//					.hasCeiling(false).hasSkylight(true).coordinate_scale(1.0).logicalHeight(512).height(512)
//					.minimumY(-256)
//			);

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
					noiseChunkGeneratorTypeBuilder.fixedBiomeSource(fixedBiomeSourceBuilder -> {
						fixedBiomeSourceBuilder.biome(id("test_biome").toString());
						fixedBiomeSourceBuilder.seed((int) new Random().nextLong());
					});
					noiseChunkGeneratorTypeBuilder.noiseSettings("minecraft:overworld");
					noiseChunkGeneratorTypeBuilder.seed((int) new Random().nextLong());
				});
			});

			pack.addDimension(id("test_dimension_custom2"), dimensionBuilder -> {
				dimensionBuilder.dimensionType(/*testDimensionCustom.location()*/new ResourceLocation("overworld"));
				dimensionBuilder.noiseGenerator(noiseChunkGeneratorTypeBuilder -> {
					noiseChunkGeneratorTypeBuilder.fixedBiomeSource(fixedBiomeSourceBuilder -> {
						fixedBiomeSourceBuilder.biome(id("test_biome").toString());
						fixedBiomeSourceBuilder.seed((int) new Random().nextLong());
					});
					noiseChunkGeneratorTypeBuilder.noiseSettings("artifice:test_dimension");
					noiseChunkGeneratorTypeBuilder.seed((int) new Random().nextLong());
				});
			});

			pack.addBiome(id("test_biome"), biomeBuilder -> biomeBuilder.precipitation(Biome.Precipitation.RAIN)
					.category(Biome.BiomeCategory.PLAINS)
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

			pack.addBiome(id("test_biome2"), biomeBuilder -> biomeBuilder
					.precipitation(Biome.Precipitation.RAIN)
					.category(Biome.BiomeCategory.DESERT)
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

			pack.addBiome(id("test_biome3"), biomeBuilder -> biomeBuilder
					.precipitation(Biome.Precipitation.RAIN)
					.category(Biome.BiomeCategory.PLAINS)
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

			pack.addBiome(id("test_biome4"), biomeBuilder -> biomeBuilder
					.precipitation(Biome.Precipitation.RAIN)
					.category(Biome.BiomeCategory.PLAINS)
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

			pack.addConfiguredStructureFeature(id("test_structure"), configuredStructureFeatureBuilder -> {
				configuredStructureFeatureBuilder.type("minecraft:village");
				configuredStructureFeatureBuilder.singleBiome("minecraft:plains");
				configuredStructureFeatureBuilder.adoptNoise(false);
				configuredStructureFeatureBuilder.spawnOverrides(spawnOverridesBuilder -> {});
				configuredStructureFeatureBuilder.featureConfig(featureConfigBuilder -> {
					featureConfigBuilder.jsonString("start_pool", "minecraft:village/desert/town_centers");
					featureConfigBuilder.jsonNumber("size", 6);
				});
			});

			pack.addConfiguredStructureFeature(id("test_structure2"), configuredStructureFeatureBuilder -> {
				configuredStructureFeatureBuilder.type("minecraft:village");
				configuredStructureFeatureBuilder.singleBiome("minecraft:nether_wastes");
				configuredStructureFeatureBuilder.adoptNoise(true);
				configuredStructureFeatureBuilder.spawnOverrides(spawnOverridesBuilder ->
						spawnOverridesBuilder.monster(mobSpawnOverrideRuleBuilder ->
								mobSpawnOverrideRuleBuilder.pieceBoundingBox().spawns(
										new SpawnsBuilder("minecraft:piglin", 5, 4, 4),
										new SpawnsBuilder("minecraft:piglin_brute", 5, 4, 4),
										new SpawnsBuilder("minecraft:zombified_piglin", 5, 4, 4)
								)
						)
				);
				configuredStructureFeatureBuilder.featureConfig(featureConfigBuilder -> {
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
									.sampling(1, 1, 80, 160)
									.bottomSlide(0.1171875, 3, 0)
									.topSlide(-0.078125, 2, 8)
									.terrainShaper(
											noiseConfigBuilder.spline(
													"continents",
													noiseConfigBuilder.point(-1.1, 0, 0.044),
													noiseConfigBuilder.point(-1.02, 0, -0.2222),
													noiseConfigBuilder.point(-0.51, 0, -0.2222),
													noiseConfigBuilder.point(-0.44, 0, -0.12),
													noiseConfigBuilder.point(-0.18, 0, -0.12),
													noiseConfigBuilder.point(-0.16, 0, noiseConfigBuilder.spline(
															"erosion",
															noiseConfigBuilder.point(-0.85, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.38940096, -0.08880186),
																	noiseConfigBuilder.point(1, 0.38940096, 0.69000006)
															)),
															noiseConfigBuilder.point(-0.7, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.37788022, -0.115760356),
																	noiseConfigBuilder.point(1, 0.37788022, 0.6400001)
															)),
															noiseConfigBuilder.point(-0.4, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0, -0.2222),
																	noiseConfigBuilder.point(-0.75, 0.37788022, -0.2222),
																	noiseConfigBuilder.point(-0.65, 0, 0),
																	noiseConfigBuilder.point(0.5954547, 0, 2.9802322e-8),
																	noiseConfigBuilder.point(0.6054547, 0.2534563, 2.9802322e-8),
																	noiseConfigBuilder.point(1, 0.2534563, 0.100000024)
															)),
															noiseConfigBuilder.point(-0.35, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.3),
																	noiseConfigBuilder.point(-0.4, 0, 0.05),
																	noiseConfigBuilder.point(0, 0, 0.05),
																	noiseConfigBuilder.point(0.4, 0, 0.05),
																	noiseConfigBuilder.point(1, 0.007000001, 0.060000002)
															)),
															noiseConfigBuilder.point(-0.1, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.15),
																	noiseConfigBuilder.point(-0.4, 0, 0),
																	noiseConfigBuilder.point(0, 0, 0),
																	noiseConfigBuilder.point(0.4, 0.1, 0.05),
																	noiseConfigBuilder.point(1, 0.007000001, 0.060000002)
															)),
															noiseConfigBuilder.point(0.2, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.15),
																	noiseConfigBuilder.point(-0.4, 0, 0),
																	noiseConfigBuilder.point(0, 0, 0),
																	noiseConfigBuilder.point(0.4, 0, 0),
																	noiseConfigBuilder.point(1, 0, 0)
															)),
															noiseConfigBuilder.point(0.7, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.02),
																	noiseConfigBuilder.point(-0.4, 0, -0.03),
																	noiseConfigBuilder.point(0, 0, -0.03),
																	noiseConfigBuilder.point(0.4, 0.06, 0.05),
																	noiseConfigBuilder.point(1, 0, 0)
															))
													)),
													noiseConfigBuilder.point(-0.15, 0, noiseConfigBuilder.spline(
															"erosion",
															noiseConfigBuilder.point(-0.85, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.38940096, -0.08880186),
																	noiseConfigBuilder.point(1, 0.38940096, 0.69000006)
															)),
															noiseConfigBuilder.point(-0.7, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.37788022, -0.115760356),
																	noiseConfigBuilder.point(1, 0.37788022, 0.6400001)
															)),
															noiseConfigBuilder.point(-0.4, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0, -0.115760356),
																	noiseConfigBuilder.point(-0.75, 0, -0.2222),
																	noiseConfigBuilder.point(-0.65, 0, 0),
																	noiseConfigBuilder.point(0.5954547, 0, 2.9802322e-8),
																	noiseConfigBuilder.point(0.6054547, 0.2534563, 2.9802322e-8),
																	noiseConfigBuilder.point(1, 0.2534563, 0.100000024)
															)),
															noiseConfigBuilder.point(-0.35, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.3),
																	noiseConfigBuilder.point(-0.4, 0, 0.05),
																	noiseConfigBuilder.point(0, 0, 0.05),
																	noiseConfigBuilder.point(0.4, 0, 0.05),
																	noiseConfigBuilder.point(1, 0.007000001, 0.060000002)
															)),
															noiseConfigBuilder.point(-0.1, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.15),
																	noiseConfigBuilder.point(-0.4, 0, 0),
																	noiseConfigBuilder.point(0, 0, 0),
																	noiseConfigBuilder.point(0.4, 0.1, 0.05),
																	noiseConfigBuilder.point(1, 0.007000001, 0.060000002)
															)),
															noiseConfigBuilder.point(0.2, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.15),
																	noiseConfigBuilder.point(-0.4, 0, 0),
																	noiseConfigBuilder.point(0, 0, 0),
																	noiseConfigBuilder.point(0.4, 0, 0),
																	noiseConfigBuilder.point(1, 0, 0)
															)),
															noiseConfigBuilder.point(0.7, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.02),
																	noiseConfigBuilder.point(-0.4, 0, -0.03),
																	noiseConfigBuilder.point(0, 0, -0.03),
																	noiseConfigBuilder.point(0.4, 0.06, 0),
																	noiseConfigBuilder.point(1, 0, 0)
															))
													)),
													noiseConfigBuilder.point(-0.1, 0, noiseConfigBuilder.spline(
															"erosion",
															noiseConfigBuilder.point(-0.85, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.38940096, -0.08880186),
																	noiseConfigBuilder.point(1, 0.38940096, 0.69000006)
															)),
															noiseConfigBuilder.point(-0.7, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.37788022, -0.115760356),
																	noiseConfigBuilder.point(1, 0.37788022, 0.6400001)
															)),
															noiseConfigBuilder.point(-0.4, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0, -0.2222),
																	noiseConfigBuilder.point(-0.75, 0, -0.2222),
																	noiseConfigBuilder.point(-0.65, 0, 0),
																	noiseConfigBuilder.point(0.5954547, 0, -0.115760356),
																	noiseConfigBuilder.point(0.6054547, 0.2534563, -0.115760356),
																	noiseConfigBuilder.point(1, 0.2534563, 0.100000024)
															)),
															noiseConfigBuilder.point(-0.35, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.25),
																	noiseConfigBuilder.point(-0.4, 0, 0.05),
																	noiseConfigBuilder.point(0, 0, 0.05),
																	noiseConfigBuilder.point(0.4, 0, 0.05),
																	noiseConfigBuilder.point(1, 0.007000001, 0.060000002)
															)),
															noiseConfigBuilder.point(-0.1, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.1),
																	noiseConfigBuilder.point(-0.4, 0.01, 0.001),
																	noiseConfigBuilder.point(0, 0.01, 0.003),
																	noiseConfigBuilder.point(0.4, 0.094000004, 0.05),
																	noiseConfigBuilder.point(1, 0.007000001, 0.060000002)
															)),
															noiseConfigBuilder.point(0.2, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.1),
																	noiseConfigBuilder.point(-0.4, 0, 0.01),
																	noiseConfigBuilder.point(0, 0, 0.01),
																	noiseConfigBuilder.point(0.4, 0.04, 0.03),
																	noiseConfigBuilder.point(1, 0.049, 0.1)
															)),
															noiseConfigBuilder.point(0.7, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0, -0.02),
																	noiseConfigBuilder.point(-0.4, 0, -0.03),
																	noiseConfigBuilder.point(0, 0, -0.03),
																	noiseConfigBuilder.point(0.4, 0.12, 0.03),
																	noiseConfigBuilder.point(1, 0.049, 0.1)
															))
													)),
													noiseConfigBuilder.point(0.25, 0, noiseConfigBuilder.spline(
															"erosion",
															noiseConfigBuilder.point(-0.85, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0, 0.20235021),
																	noiseConfigBuilder.point(0, 0.5138249, 0.7161751),
																	noiseConfigBuilder.point(1, 0.5138249, 1.23)
															)),
															noiseConfigBuilder.point(-0.7, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0, 0.2),
																	noiseConfigBuilder.point(0, 0.43317974, 0.44682026),
																	noiseConfigBuilder.point(1, 0.43317974, 0.88)
															)),
															noiseConfigBuilder.point(-0.4, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0, 0.2),
																	noiseConfigBuilder.point(0, 0.3917051, 0.30829495),
																	noiseConfigBuilder.point(1, 0.3917051, 0.70000005)
															)),
															noiseConfigBuilder.point(-0.35, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.25),
																	noiseConfigBuilder.point(-0.4, 0, 0.35),
																	noiseConfigBuilder.point(0, 0, 0.35),
																	noiseConfigBuilder.point(0.4, 0, 0.35),
																	noiseConfigBuilder.point(1, 0.049000014, 0.42000002)
															)),
															noiseConfigBuilder.point(-0.1, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.1),
																	noiseConfigBuilder.point(-0.4, 0.07, 0.0069999998),
																	noiseConfigBuilder.point(0, 0.07, 0.021),
																	noiseConfigBuilder.point(0.4, 0.658, 0.35),
																	noiseConfigBuilder.point(1, 0.049000014, 0.42000002)
															)),
															noiseConfigBuilder.point(0.2, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.1),
																	noiseConfigBuilder.point(-0.4, 0, 0.01),
																	noiseConfigBuilder.point(0, 0, 0.01),
																	noiseConfigBuilder.point(0.4, 0.04, 0.03),
																	noiseConfigBuilder.point(1, 0.049, 0.1)
															)),
															noiseConfigBuilder.point(0.4, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.1),
																	noiseConfigBuilder.point(-0.4, 0, 0.01),
																	noiseConfigBuilder.point(0, 0, 0.01),
																	noiseConfigBuilder.point(0.4, 0.04, 0.03),
																	noiseConfigBuilder.point(1, 0.049, 0.1)
															)),
															noiseConfigBuilder.point(0.45, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0, -0.1),
																	noiseConfigBuilder.point(-0.4, 0, noiseConfigBuilder.spline(
																			"ridges",
																			noiseConfigBuilder.point(-1, 0.5, -0.1),
																			noiseConfigBuilder.point(-0.4, 0, 0.01),
																			noiseConfigBuilder.point(0, 0, 0.01),
																			noiseConfigBuilder.point(0.4, 0.04, 0.03),
																			noiseConfigBuilder.point(1, 0.049, 0.1)
																	)),
																	noiseConfigBuilder.point(0, 0, 0.17)
															)),
															noiseConfigBuilder.point(0.55, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0, -0.1),
																	noiseConfigBuilder.point(-0.4, 0, noiseConfigBuilder.spline(
																			"ridges",
																			noiseConfigBuilder.point(-1, 0.5, -0.1),
																			noiseConfigBuilder.point(-0.4, 0, 0.01),
																			noiseConfigBuilder.point(0, 0, 0.01),
																			noiseConfigBuilder.point(0.4, 0.04, 0.03),
																			noiseConfigBuilder.point(1, 0.049, 0.1)
																	)),
																	noiseConfigBuilder.point(0, 0, 0.17)
															)),
															noiseConfigBuilder.point(0.58, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.1),
																	noiseConfigBuilder.point(-0.4, 0, 0.01),
																	noiseConfigBuilder.point(0, 0, 0.01),
																	noiseConfigBuilder.point(0.4, 0.04, 0.03),
																	noiseConfigBuilder.point(1, 0.049, 0.1)
															)),
															noiseConfigBuilder.point(0.7, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.02),
																	noiseConfigBuilder.point(-0.4, 0, -0.03),
																	noiseConfigBuilder.point(0, 0, -0.03),
																	noiseConfigBuilder.point(0.4, 0.12, 0.03),
																	noiseConfigBuilder.point(1, 0.049, 0.1)
															))
													)),
													noiseConfigBuilder.point(1.0, 0, noiseConfigBuilder.spline(
															"erosion",
															noiseConfigBuilder.point(-0.85, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0, 0.34792626),
																	noiseConfigBuilder.point(0, 0.5760369, 0.9239631),
																	noiseConfigBuilder.point(1, 0.5760369, 1.5)
															)),
															noiseConfigBuilder.point(-0.7, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0, 0.2),
																	noiseConfigBuilder.point(0, 0.4608295, 0.5391705),
																	noiseConfigBuilder.point(1, 0.4608295, 1)
															)),
															noiseConfigBuilder.point(-0.4, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0, 0.2),
																	noiseConfigBuilder.point(0, 0.4608295, 0.5391705),
																	noiseConfigBuilder.point(1, 0.4608295, 1)
															)),
															noiseConfigBuilder.point(-0.35, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.2),
																	noiseConfigBuilder.point(-0.4, 0, 0.5),
																	noiseConfigBuilder.point(0, 0, 0.5),
																	noiseConfigBuilder.point(0.4, 0, 0.5),
																	noiseConfigBuilder.point(1, 0.070000015, 0.6)
															)),
															noiseConfigBuilder.point(-0.1, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.05),
																	noiseConfigBuilder.point(-0.4, 0.099999994, 0.01),
																	noiseConfigBuilder.point(0, 0.099999994, 0.03),
																	noiseConfigBuilder.point(0.4, 0.94, 0.5),
																	noiseConfigBuilder.point(1, 0.070000015, 0.6)
															)),
															noiseConfigBuilder.point(0.2, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.05),
																	noiseConfigBuilder.point(-0.4, 0, 0.01),
																	noiseConfigBuilder.point(0, 0, 0.01),
																	noiseConfigBuilder.point(0.4, 0.04, 0.03),
																	noiseConfigBuilder.point(1, 0.049, 0.1)
															)),
															noiseConfigBuilder.point(0.4, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.05),
																	noiseConfigBuilder.point(-0.4, 0, 0.01),
																	noiseConfigBuilder.point(0, 0, 0.01),
																	noiseConfigBuilder.point(0.4, 0.04, 0.03),
																	noiseConfigBuilder.point(1, 0.049, 0.1)
															)),
															noiseConfigBuilder.point(0.45, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0, -0.05),
																	noiseConfigBuilder.point(-0.4, 0, noiseConfigBuilder.spline(
																			"ridges",
																			noiseConfigBuilder.point(-1, 0.5, -0.05),
																			noiseConfigBuilder.point(-0.4, 0, 0.01),
																			noiseConfigBuilder.point(0, 0, 0.01),
																			noiseConfigBuilder.point(0.4, 0.04, 0.03),
																			noiseConfigBuilder.point(1, 0.049, 0.1)
																	)),
																	noiseConfigBuilder.point(0, 0, 0.17)
															)),
															noiseConfigBuilder.point(0.55, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0, -0.05),
																	noiseConfigBuilder.point(-0.4, 0, noiseConfigBuilder.spline(
																			"ridges",
																			noiseConfigBuilder.point(-1, 0.5, -0.05),
																			noiseConfigBuilder.point(-0.4, 0, 0.01),
																			noiseConfigBuilder.point(0, 0, 0.01),
																			noiseConfigBuilder.point(0.4, 0.04, 0.03),
																			noiseConfigBuilder.point(1, 0.049, 0.1)
																	)),
																	noiseConfigBuilder.point(0, 0, 0.17)
															)),
															noiseConfigBuilder.point(0.58, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.05),
																	noiseConfigBuilder.point(-0.4, 0, 0.01),
																	noiseConfigBuilder.point(0, 0, 0.01),
																	noiseConfigBuilder.point(0.4, 0.04, 0.03),
																	noiseConfigBuilder.point(1, 0.049, 0.1)
															)),
															noiseConfigBuilder.point(0.7, 0, noiseConfigBuilder.spline(
																	"ridges",
																	noiseConfigBuilder.point(-1, 0.5, -0.02),
																	noiseConfigBuilder.point(-0.4, 0, -0.03),
																	noiseConfigBuilder.point(0, 0, -0.03),
																	noiseConfigBuilder.point(0.4, 0.04, 0.03),
																	noiseConfigBuilder.point(1, 0.049, 0.1)
															))
													))
											), 0, 0
									)
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
											surfaceRulesBuilder1 -> surfaceRulesBuilder1.verticalGradient(
													"deepslate",
													YOffsetBuilder.absolute(-16),
													YOffsetBuilder.absolute(0)
											),
											surfaceRulesBuilder1 -> surfaceRulesBuilder1.block(stateDataBuilder -> stateDataBuilder.name("minecraft:deepslate").setProperty("axis", "y"))
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
													"woollenpjs",
													YOffsetBuilder.belowTop(80),
													YOffsetBuilder.aboveBottom(22)
											),
											surfaceRulesBuilder1 -> surfaceRulesBuilder1.block(stateDataBuilder -> stateDataBuilder.name("minecraft:cyan_wool"))
									)
							))
			);
			//not even going to touch this until we get everything else working
			// Tested, it works now. Wasn't in 20w28a.
/*			pack.addConfiguredFeature(id("test_featureee"), configuredFeatureBuilder ->
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
											).heightmap(Heightmap.Type.OCEAN_FLOOR), new TreeFeatureConfigBuilder()
							));
			// Should be working but Minecraft coders did something wrong and the default feature is being return when it shouldn't resulting in a crash.
			pack.addPlacedFeature(id("test_placed_feature"), configuredFeatureBuilder -> configuredFeatureBuilder.featureID("artifice:test_featureee")
					.featureConfig(decoratedFeatureConfigBuilder -> decoratedFeatureConfigBuilder.feature(configuredSubFeatureBuilder ->
							configuredSubFeatureBuilder.featureID("minecraft:decorated").featureConfig(decoratedFeatureConfigBuilder1 ->
									decoratedFeatureConfigBuilder1.feature(id("test_featureee").toString())
											.decorator(configuredDecoratorBuilder ->
													configuredDecoratorBuilder.name("minecraft:decorated")
															.config(decoratedDecoratorConfigBuilder ->
																			decoratedDecoratorConfigBuilder.innerDecorator(configuredDecoratorBuilder1 ->
																					configuredDecoratorBuilder1.defaultConfig().name("minecraft:heightmap")
																			).outerDecorator(configuredDecoratorBuilder1 ->
																					configuredDecoratorBuilder1.defaultConfig().name("minecraft:square")
																			),
																	new DecoratedDecoratorConfigBuilder()
															)
											), new DecoratedFeatureConfigBuilder())).decorator(configuredDecoratorBuilder ->
							configuredDecoratorBuilder.name("minecraft:count_extra")
									.config(countExtraDecoratorConfigBuilder ->
											countExtraDecoratorConfigBuilder.count(10).extraChance(0.2F).extraCount(2), new CountExtraDecoratorConfigBuilder())), new DecoratedFeatureConfigBuilder()));*/
			try {
				pack.dumpResources("testing_data", "data");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
