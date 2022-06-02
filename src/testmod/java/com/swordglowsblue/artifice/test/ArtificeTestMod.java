package com.swordglowsblue.artifice.test;

import io.github.vampirestudios.artifice.api.Artifice;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.BiomeSourceBuilder.MultiNoiseBiomeSourceBuilder.BiomeParametersBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.ChunkGeneratorTypeBuilder.FlatChunkGeneratorTypeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.ChunkGeneratorTypeBuilder.FlatChunkGeneratorTypeBuilder.LayersBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.ChunkGeneratorTypeBuilder.NoiseChunkGeneratorTypeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.DimensionBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.*;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.HeightProviderBuilders.UniformHeightProviderBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.biome.BiomeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.ConfiguredCarverBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.ConfiguredFeatureBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.PlacedFeatureBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config.FeatureConfigBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config.TreeFeatureConfigBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.placementmodifiers.BiomePlacementModifier;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.placementmodifiers.InSquarePlacementModifier;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.FeatureSizeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.FoliagePlacerBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.TrunkPlacerBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.structure.MobSpawnOverrideRuleBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.structure.SpawnOverridesBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.structure.SpawnsBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.structure.StructureBuilder;
import io.github.vampirestudios.artifice.api.resource.StringResource;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
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
import java.util.Map;

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

			pack.addDimension(id("test_dimension"), new DimensionBuilder()
					.dimensionType(/*testDimension.location()*/new ResourceLocation("overworld"))
					.flatGenerator(new FlatChunkGeneratorTypeBuilder()
							.addLayer(
									new LayersBuilder("minecraft:bedrock", 2),
									new LayersBuilder("minecraft:deepslate", 10),
									new LayersBuilder("minecraft:stone", 2),
									new LayersBuilder("minecraft:granite", 2)
							).biome("minecraft:plains")
					)
			);

			pack.addDimension(id("test_dimension2"), new DimensionBuilder()
					.dimensionType(/*testDimension.location()*/new ResourceLocation("overworld"))
					.flatGenerator(new FlatChunkGeneratorTypeBuilder().lakes(false).features(true)
							.addLayer(
									new LayersBuilder("minecraft:bedrock", 2),
									new LayersBuilder("minecraft:deepslate", 10),
									new LayersBuilder("minecraft:stone", 2),
									new LayersBuilder("minecraft:andesite", 2)
							).biome("minecraft:plains")
					)
			);

			pack.addDimension(id("test_dimension_custom"), new DimensionBuilder()
					.dimensionType(/*testDimensionCustom.location()*/new ResourceLocation("overworld"))
					.noiseGenerator(new NoiseChunkGeneratorTypeBuilder().multiNoiseBiomeSource(
							new MultiNoiseBiomeSourceBuilder().biomes(
									new MultiNoiseBiomeSourceBuilder.BiomeBuilder()
											.biome(id("test_biome").toString())
											.parameters(new BiomeParametersBuilder()
													.temperature(0).humidity(1).continentalness(0).erosion(0)
													.weirdness(0).depth(1).offset(0)
											),
									new MultiNoiseBiomeSourceBuilder.BiomeBuilder()
											.biome(id("test_biome2").toString())
											.parameters(new BiomeParametersBuilder()
													.temperature(0).humidity(0).continentalness(0).erosion(-0.5F)
													.weirdness(0).depth(1).offset(0)
											),
									new MultiNoiseBiomeSourceBuilder.BiomeBuilder()
											.biome(id("test_biome3").toString())
											.parameters(new BiomeParametersBuilder()
													.temperature(0).humidity(1).continentalness(0.1F).erosion(0)
													.weirdness(0).depth(1).offset(0)
											),
									new MultiNoiseBiomeSourceBuilder.BiomeBuilder()
											.biome(id("test_biome4").toString())
											.parameters(new BiomeParametersBuilder()
													.temperature(0).humidity(1).continentalness(0).erosion(0.2F)
													.weirdness(0).depth(1).offset(0)
											)
							)).noiseSettings("minecraft:overworld")
					)
			);

			pack.addDimension(id("test_dimension_custom2"), new DimensionBuilder()
					.dimensionType(/*testDimensionCustom.location()*/new ResourceLocation("overworld"))
					.noiseGenerator(new NoiseChunkGeneratorTypeBuilder().multiNoiseBiomeSource(
							new MultiNoiseBiomeSourceBuilder().biomes(
									new MultiNoiseBiomeSourceBuilder.BiomeBuilder()
											.biome(id("test_biome").toString())
											.parameters(new BiomeParametersBuilder()
													.temperature(0).humidity(1).continentalness(0).erosion(0)
													.weirdness(0).depth(1).offset(0)
											),
									new MultiNoiseBiomeSourceBuilder.BiomeBuilder()
											.biome(id("test_biome2").toString())
											.parameters(new BiomeParametersBuilder()
													.temperature(0).humidity(0).continentalness(0).erosion(-0.5F)
													.weirdness(0).depth(1).offset(0)
											),
									new MultiNoiseBiomeSourceBuilder.BiomeBuilder()
											.biome(id("test_biome3").toString())
											.parameters(new BiomeParametersBuilder()
													.temperature(0).humidity(1).continentalness(0.1F).erosion(0)
													.weirdness(0).depth(1).offset(0)
											),
									new MultiNoiseBiomeSourceBuilder.BiomeBuilder()
											.biome(id("test_biome4").toString())
											.parameters(new BiomeParametersBuilder()
													.temperature(0).humidity(1).continentalness(0).erosion(0.2F)
													.weirdness(0).depth(1).offset(0)
											)
							)).noiseSettings("artifice:test_dimension")
					)
			);

			pack.addBiome(id("test_biome"), new BiomeBuilder()
					.precipitation(Biome.Precipitation.RAIN)
					.temperature(0.8F).downfall(0.4F)
					.addSpawnCosts(EntityType.BEE, new BiomeBuilder.SpawnDensityBuilder(0.12, 1))
					.addSpawnCosts(EntityType.CAT, new BiomeBuilder.SpawnDensityBuilder(0.4, 1))
					.effects(biomeEffectsBuilder -> {
						biomeEffectsBuilder.waterColor(4159204);
						biomeEffectsBuilder.waterFogColor(329011);
						biomeEffectsBuilder.fogColor(12638463);
						biomeEffectsBuilder.skyColor(4159204);
					}).addAirCarvers(id("test_carver").toString())
			);

			pack.addBiome(id("test_biome2"), new BiomeBuilder()
					.precipitation(Biome.Precipitation.RAIN)
					.temperature(0.8F).downfall(0.4F)
					.addSpawnCosts(EntityType.SPIDER, new BiomeBuilder.SpawnDensityBuilder(0.6, 1))
					.addSpawnCosts(EntityType.CAT, new BiomeBuilder.SpawnDensityBuilder(0.4, 1))
					.effects(biomeEffectsBuilder -> {
						biomeEffectsBuilder.waterColor(4159204);
						biomeEffectsBuilder.waterFogColor(329011);
						biomeEffectsBuilder.fogColor(12638463);
						biomeEffectsBuilder.skyColor(4159204);
					}).addAirCarvers(id("test_carver").toString())
			);

			pack.addBiome(id("test_biome3"), new BiomeBuilder()
					.precipitation(Biome.Precipitation.RAIN)
					.temperature(2.0F).downfall(0.4F)
					.addSpawnCosts(EntityType.BEE, new BiomeBuilder.SpawnDensityBuilder(0.12, 1))
					.addSpawnCosts(EntityType.CAT, new BiomeBuilder.SpawnDensityBuilder(0.4, 1))
					.effects(biomeEffectsBuilder -> {
						biomeEffectsBuilder.waterColor(4159204);
						biomeEffectsBuilder.waterFogColor(329011);
						biomeEffectsBuilder.fogColor(12638463);
						biomeEffectsBuilder.skyColor(4159204);
					}).addAirCarvers(id("test_carver").toString())
			);

			pack.addBiome(id("test_biome4"), new BiomeBuilder()
					.precipitation(Biome.Precipitation.RAIN)
					.temperature(1.4F).downfall(1.0F)
					.addSpawnCosts(EntityType.BEE, new BiomeBuilder.SpawnDensityBuilder(0.12, 1))
					.addSpawnCosts(EntityType.CAT, new BiomeBuilder.SpawnDensityBuilder(0.4, 1))
					.effects(biomeEffectsBuilder -> {
						biomeEffectsBuilder.waterColor(4159204);
						biomeEffectsBuilder.waterFogColor(329011);
						biomeEffectsBuilder.fogColor(12638463);
						biomeEffectsBuilder.skyColor(4159204);
					}).addAirCarvers(id("test_carver").toString())
			);

			pack.addStructure(id("test_structure"), new StructureBuilder()
					.type("minecraft:village")
					.singleBiome("minecraft:plains")
					.adoptNoise(false)
					.spawnOverrides(new SpawnOverridesBuilder())
					.featureConfig(new FeatureConfigBuilder()
							.startPool("minecraft:village/desert/town_centers").size(6)
					)
			);

			pack.addStructure(id("test_structure2"), new StructureBuilder()
					.type("minecraft:village")
					.singleBiome("minecraft:nether_wastes")
					.adoptNoise(true)
					.spawnOverrides(new SpawnOverridesBuilder()
							.monster(new MobSpawnOverrideRuleBuilder()
									.pieceBoundingBox().spawns(
											new SpawnsBuilder("minecraft:piglin", 5, 4, 4),
											new SpawnsBuilder("minecraft:piglin_brute", 5, 4, 4),
											new SpawnsBuilder("minecraft:zombified_piglin", 5, 4, 4)
									)
							)
					)
					.featureConfig(new FeatureConfigBuilder()
							.startPool("minecraft:village/desert/town_centers")
							.size(6)
					)
			);

			pack.addConfiguredCarver(id("test_carver"), new ConfiguredCarverBuilder()
					.probability(0.15F)
					.type("minecraft:cave")
					.y(new HeightProviderBuilders().uniform("y",
							UniformHeightProviderBuilder.minAndMaxInclusive(
									YOffsetBuilder.aboveBottom(8),
									YOffsetBuilder.absolute(180)
							)
					))
					.yScale(new HeightProviderBuilders().uniform("yScale",
							UniformHeightProviderBuilder.minAndMaxInclusive(
									0.1F, 0.9F
							)
					))
					.lavaLevel(YOffsetBuilder.aboveBottom(8))
					.horizontalRadiusModifier(new HeightProviderBuilders().uniform("horizontal_radius_modifier",
							UniformHeightProviderBuilder.minAndMaxInclusive(
									1.7F, 2.4F
							)
					))
					.verticalRadiusModifier(new HeightProviderBuilders().uniform("vertical_radius_modifier",
							UniformHeightProviderBuilder.minAndMaxInclusive(
									1.5F, 2.1F
							)
					))
					.floorLevel(new HeightProviderBuilders().uniform("floor_level",
							UniformHeightProviderBuilder.minAndMaxInclusive(
									-1F, -0.4F
							)
					))
			);

			pack.addNoiseSettingsBuilder(id("test_dimension"), new NoiseSettingsBuilder()
					.defaultBlock(new StateDataBuilder().name(Blocks.STONE))
					.defaultFluid(new StateDataBuilder().name("minecraft:lava").setProperty(Map.entry("level", "0")))
					.seaLevel(65).legacyRandomSource(false).aquifersEnabled(true)
					.disableMobGeneration(false).aquifersEnabled(true).oreVeinsEnabled(true)
					.oreVeinsEnabled(true).noiseConfig(-64, 384, 1, 2)
					.noiseRouter().surfaceRules(new SurfaceRulesBuilder().sequence(
							new SurfaceRulesBuilder().condition(
									new SurfaceRulesBuilder().verticalGradient(
											"bebrock",
											YOffsetBuilder.aboveBottom(0),
											YOffsetBuilder.aboveBottom(5)
									),
									new SurfaceRulesBuilder().block(new StateDataBuilder().name(Blocks.BEDROCK))
							), new SurfaceRulesBuilder().condition(
									SurfaceRulesBuilder.aboveMainSurface(),
									new SurfaceRulesBuilder().sequence(new SurfaceRulesBuilder().condition(
													new SurfaceRulesBuilder().biome(id("test_biome")),
													new SurfaceRulesBuilder().block(new StateDataBuilder().name(testBlock))
											),
											new SurfaceRulesBuilder().block(new StateDataBuilder().name(Blocks.GRASS_BLOCK))
									)
							), new SurfaceRulesBuilder().condition(
									new SurfaceRulesBuilder().verticalGradient(
											"tuff",
											YOffsetBuilder.absolute(0),
											YOffsetBuilder.absolute(16)
									),
									new SurfaceRulesBuilder().block(new StateDataBuilder().name(Blocks.TUFF))
							), new SurfaceRulesBuilder().condition(
									new SurfaceRulesBuilder().verticalGradient("deepslate", YOffsetBuilder.absolute(-16),
											YOffsetBuilder.absolute(0)
									),
									new SurfaceRulesBuilder().block(new StateDataBuilder().name(Blocks.DEEPSLATE).setProperty(Map.entry("axis", "y")))
							)
					))
			);
			//not even going to touch this until we get everything else working
			// Tested, it works now. Wasn't in 20w28a.
			pack.addConfiguredFeature(id("test_featureee"), new ConfiguredFeatureBuilder()
					.featureID("minecraft:tree").featureConfig(new TreeFeatureConfigBuilder()
							.ignoreVines(true)
							.maxWaterDepth(5)
							.dirtProvider(new BlockStateProviderBuilder.SimpleBlockStateProviderBuilder()
									.state(new StateDataBuilder().name(Blocks.DIRT))
							).trunkProvider(new BlockStateProviderBuilder.SimpleBlockStateProviderBuilder()
									.state(new StateDataBuilder().name(Blocks.OAK_LOG).setProperty(Map.entry("axis", "y")))
							).foliageProvider(new BlockStateProviderBuilder.SimpleBlockStateProviderBuilder()
									.state(new StateDataBuilder().name(Blocks.SPRUCE_LEAVES).setProperty(Map.entry("persistent", "false"), Map.entry("distance", "7")))
							).foliagePlacer(new FoliagePlacerBuilder.BlobFoliagePlacerBuilder().height(2).offset(1).radius(2))
							.trunkPlacer(new TrunkPlacerBuilder.FancyTrunkPlacerBuilder().baseHeight(12)
									.heightRandA(3).heightRandB(4)
							).minimumSize(new FeatureSizeBuilder.TwoLayersFeatureSizeBuilder().limit(10).lowerSize(1).upperSize(9))
							.heightmap(Heightmap.Types.OCEAN_FLOOR)
					));
			// Should be working but Minecraft coders did something wrong and the default feature is being return when it shouldn't resulting in a crash.
			pack.addPlacedFeature(id("test_placed_feature"), new PlacedFeatureBuilder()
					.feature("artifice:test_featureee")
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
