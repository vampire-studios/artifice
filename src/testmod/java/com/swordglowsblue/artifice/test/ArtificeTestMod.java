package com.swordglowsblue.artifice.test;

import io.github.vampirestudios.artifice.api.Artifice;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.BiomeSourceBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.ChunkGeneratorTypeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.ChunkGeneratorTypeBuilder.FlatChunkGeneratorTypeBuilder.LayersBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.NoiseConfigBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.FloatProviderBuilders;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.HeightProviderBuilders;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.SurfaceRulesBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.YOffsetBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.biome.BiomeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.biome.BiomeEffectsBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config.FeatureConfigBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.structure.MobSpawnOverrideRuleBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.structure.SpawnOverridesBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.BlockStateProviderBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config.TreeFeatureConfigBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.placementmodifiers.BiomePlacementModifier;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.placementmodifiers.InSquarePlacementModifier;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.structure.SpawnsBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.FeatureSizeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.FoliagePlacerBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.TrunkPlacerBuilder;
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
import java.util.Random;

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
					.flatGenerator( ChunkGeneratorTypeBuilder.FlatChunks()
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
					.flatGenerator(ChunkGeneratorTypeBuilder.FlatChunks()
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
				dimensionBuilder.noiseGenerator(ChunkGeneratorTypeBuilder.NoiseChunks()
						.multiNoiseBiomeSource(BiomeSourceBuilder.multiNoise().biomes(
								BiomeSourceBuilder.noiseBiome(
								id("test_biome").toString(),
								BiomeSourceBuilder.noiseBiomeParameters()
									.temperature(0).humidity(1).continentalness(0).erosion(0)
									.weirdness(0).depth(1).offset(0)
								),
								BiomeSourceBuilder.noiseBiome(
								id("test_biome2").toString(),
								BiomeSourceBuilder.noiseBiomeParameters()
									.temperature(0).humidity(0).continentalness(0).erosion(-0.5F)
									.weirdness(0).depth(1).offset(0)
								),
								BiomeSourceBuilder.noiseBiome(
								id("test_biome3").toString(),
								BiomeSourceBuilder.noiseBiomeParameters()
									.temperature(0).humidity(1).continentalness(0.1F).erosion(0)
									.weirdness(0).depth(1).offset(0)
								),
								BiomeSourceBuilder.noiseBiome(
								id("test_biome4").toString(),
								BiomeSourceBuilder.noiseBiomeParameters()
									.temperature(0).humidity(1).continentalness(0).erosion(0.2F)
									.weirdness(0).depth(1).offset(0)
								)
					))
					.noiseSettings("minecraft:overworld")
					.seed((int) new Random().nextLong())
				);
			});

			pack.addDimension(id("test_dimension_custom2"), dimensionBuilder -> {
				dimensionBuilder.dimensionType(/*testDimensionCustom.location()*/new ResourceLocation("overworld"));
				dimensionBuilder.noiseGenerator(ChunkGeneratorTypeBuilder.NoiseChunks()
					.multiNoiseBiomeSource(BiomeSourceBuilder.multiNoise().biomes(
							BiomeSourceBuilder.noiseBiome(
									id("test_biome").toString(),
									BiomeSourceBuilder.noiseBiomeParameters()
											.temperature(0).humidity(1).continentalness(0).erosion(0)
											.weirdness(0).depth(1).offset(0)
							),
							BiomeSourceBuilder.noiseBiome(
									id("test_biome2").toString(),
									BiomeSourceBuilder.noiseBiomeParameters()
											.temperature(0).humidity(0).continentalness(0).erosion(-0.5F)
											.weirdness(0).depth(1).offset(0)
							),
							BiomeSourceBuilder.noiseBiome(
									id("test_biome3").toString(),
									BiomeSourceBuilder.noiseBiomeParameters()
											.temperature(0).humidity(1).continentalness(0.1F).erosion(0)
											.weirdness(0).depth(1).offset(0)
							),
							BiomeSourceBuilder.noiseBiome(
									id("test_biome4").toString(),
									BiomeSourceBuilder.noiseBiomeParameters()
											.temperature(0).humidity(1).continentalness(0).erosion(0.2F)
											.weirdness(0).depth(1).offset(0)
									)
					))
					.noiseSettings("artifice:test_dimension")
					.seed((int) new Random().nextLong())
				);
			});

			pack.addBiome(id("test_biome"), biomeBuilder -> biomeBuilder.precipitation(Biome.Precipitation.RAIN)
					.category(Biome.BiomeCategory.PLAINS)
					.temperature(0.8F).downfall(0.4F)
					.addSpawnCosts("minecraft:bee", new BiomeBuilder.SpawnDensityBuilder(0.12, 1))
					.addSpawnCosts("minecraft:cat", new BiomeBuilder.SpawnDensityBuilder(0.4, 1))
					.effects(new BiomeEffectsBuilder()
						.waterColor(4159204)
						.waterFogColor(329011)
						.fogColor(12638463)
						.skyColor(4159204)
					).addAirCarvers(id("test_carver").toString())
			);

			pack.addBiome(id("test_biome2"), biomeBuilder -> biomeBuilder
					.precipitation(Biome.Precipitation.RAIN)
					.category(Biome.BiomeCategory.DESERT)
					.temperature(0.8F).downfall(0.4F)
					.addSpawnCosts("minecraft:spider", new BiomeBuilder.SpawnDensityBuilder(0.6, 1))
					.addSpawnCosts("minecraft:cat", new BiomeBuilder.SpawnDensityBuilder(0.4, 1))
					.effects(new BiomeEffectsBuilder()
						.waterColor(4159204)
						.waterFogColor(329011)
						.fogColor(12638463)
						.skyColor(4159204)
					).addAirCarvers(id("test_carver").toString())
			);

			pack.addBiome(id("test_biome3"), biomeBuilder -> biomeBuilder
					.precipitation(Biome.Precipitation.RAIN)
					.category(Biome.BiomeCategory.PLAINS)
					.temperature(2.0F).downfall(0.4F)
					.addSpawnCosts("minecraft:bee", new BiomeBuilder.SpawnDensityBuilder(0.12, 1))
					.addSpawnCosts("minecraft:cat", new BiomeBuilder.SpawnDensityBuilder(0.4, 1))
					.effects(new BiomeEffectsBuilder()
						.waterColor(4159204)
						.waterFogColor(329011)
						.fogColor(12638463)
						.skyColor(4159204)
					).addAirCarvers(id("test_carver").toString())
			);

			pack.addBiome(id("test_biome4"), biomeBuilder -> biomeBuilder
					.precipitation(Biome.Precipitation.RAIN)
					.category(Biome.BiomeCategory.PLAINS)
					.temperature(1.4F).downfall(1.0F)
					.addSpawnCosts("minecraft:bee", new BiomeBuilder.SpawnDensityBuilder(0.12, 1))
					.addSpawnCosts("minecraft:cat", new BiomeBuilder.SpawnDensityBuilder(0.4, 1))
					.effects(new BiomeEffectsBuilder()
						.waterColor(4159204)
						.waterFogColor(329011)
						.fogColor(12638463)
						.skyColor(4159204)
					).addAirCarvers("minecraft:cave")
			);

			pack.addConfiguredStructureFeature(id("test_structure"), configuredStructureFeatureBuilder -> {
				configuredStructureFeatureBuilder.type("minecraft:village");
				configuredStructureFeatureBuilder.singleBiome("minecraft:plains");
				configuredStructureFeatureBuilder.adoptNoise(false);
				configuredStructureFeatureBuilder.spawnOverrides(new SpawnOverridesBuilder());
				configuredStructureFeatureBuilder.featureConfig((FeatureConfigBuilder)new FeatureConfigBuilder()
					.add("start_pool", "minecraft:village/desert/town_centers")
					.add("size", 6)
				);
			});

			pack.addConfiguredStructureFeature(id("test_structure2"), configuredStructureFeatureBuilder -> {
				configuredStructureFeatureBuilder.type("minecraft:village");
				configuredStructureFeatureBuilder.singleBiome("minecraft:nether_wastes");
				configuredStructureFeatureBuilder.adoptNoise(true);
				configuredStructureFeatureBuilder.spawnOverrides(new SpawnOverridesBuilder()
						.monster(MobSpawnOverrideRuleBuilder.spawns("piece",
										new SpawnsBuilder("minecraft:piglin", 5, 4, 4),
										new SpawnsBuilder("minecraft:piglin_brute", 5, 4, 4),
										new SpawnsBuilder("minecraft:zombified_piglin", 5, 4, 4)
								)
						)
				);
				configuredStructureFeatureBuilder.featureConfig((FeatureConfigBuilder)new FeatureConfigBuilder()
					.add("start_pool", "minecraft:village/desert/town_centers")
					.add("size", 6)
				);
			});

			pack.addConfiguredCarver(id("test_carver"), carverBuilder ->
					carverBuilder
							.probability(0.15F)
							.type("minecraft:cave")
							.y(HeightProviderBuilders.uniform(YOffsetBuilder.aboveBottom(8), YOffsetBuilder.absolute(180)))
							.yScale(FloatProviderBuilders.uniform(0.1F, 0.9F))
							.lavaLevel(YOffsetBuilder.aboveBottom(8))
							.horizontalRadiusModifier(FloatProviderBuilders.uniform(1.7F, 2.4F))
							.verticalRadiusModifier(FloatProviderBuilders.uniform(1.5F, 2.1F))
							.floorLevel(FloatProviderBuilders.uniform(-1F, -0.4F))
			);

			// gotta wait on noise config
			pack.addNoiseSettingsBuilder(id("test_dimension"),noiseSettingsBuilder ->
					noiseSettingsBuilder.defaultBlock(StateDataBuilder.name("minecraft:stone"))
							.defaultFluid(StateDataBuilder.name("minecraft:lava").setProperty("level", "0"))
							.seaLevel(65).legacyRandomSource(false).aquifersEnabled(true)
							.disableMobGeneration(false).aquifersEnabled(true).oreVeinsEnabled(true)
							.oreVeinsEnabled(true).noiseConfig(new NoiseConfigBuilder()
									.minimumY(-64).height(384).sizeHorizontal(1).sizeVertical(2)
									.sampling(1, 1, 80, 160)
									.bottomSlide(0.1171875, 3, 0)
									.topSlide(-0.078125, 2, 8)
									.terrainShaper(
											NoiseConfigBuilder.spline(
													"continents",
													NoiseConfigBuilder.point(-1.1, 0, 0.044),
													NoiseConfigBuilder.point(-1.02, 0, -0.2222),
													NoiseConfigBuilder.point(-0.51, 0, -0.2222),
													NoiseConfigBuilder.point(-0.44, 0, -0.12),
													NoiseConfigBuilder.point(-0.18, 0, -0.12),
													NoiseConfigBuilder.point(-0.16, 0, NoiseConfigBuilder.spline(
															"erosion",
															NoiseConfigBuilder.point(-0.85, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.38940096, -0.08880186),
																	NoiseConfigBuilder.point(1, 0.38940096, 0.69000006)
															)),
															NoiseConfigBuilder.point(-0.7, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.37788022, -0.115760356),
																	NoiseConfigBuilder.point(1, 0.37788022, 0.6400001)
															)),
															NoiseConfigBuilder.point(-0.4, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0, -0.2222),
																	NoiseConfigBuilder.point(-0.75, 0.37788022, -0.2222),
																	NoiseConfigBuilder.point(-0.65, 0, 0),
																	NoiseConfigBuilder.point(0.5954547, 0, 2.9802322e-8),
																	NoiseConfigBuilder.point(0.6054547, 0.2534563, 2.9802322e-8),
																	NoiseConfigBuilder.point(1, 0.2534563, 0.100000024)
															)),
															NoiseConfigBuilder.point(-0.35, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.3),
																	NoiseConfigBuilder.point(-0.4, 0, 0.05),
																	NoiseConfigBuilder.point(0, 0, 0.05),
																	NoiseConfigBuilder.point(0.4, 0, 0.05),
																	NoiseConfigBuilder.point(1, 0.007000001, 0.060000002)
															)),
															NoiseConfigBuilder.point(-0.1, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.15),
																	NoiseConfigBuilder.point(-0.4, 0, 0),
																	NoiseConfigBuilder.point(0, 0, 0),
																	NoiseConfigBuilder.point(0.4, 0.1, 0.05),
																	NoiseConfigBuilder.point(1, 0.007000001, 0.060000002)
															)),
															NoiseConfigBuilder.point(0.2, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.15),
																	NoiseConfigBuilder.point(-0.4, 0, 0),
																	NoiseConfigBuilder.point(0, 0, 0),
																	NoiseConfigBuilder.point(0.4, 0, 0),
																	NoiseConfigBuilder.point(1, 0, 0)
															)),
															NoiseConfigBuilder.point(0.7, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.02),
																	NoiseConfigBuilder.point(-0.4, 0, -0.03),
																	NoiseConfigBuilder.point(0, 0, -0.03),
																	NoiseConfigBuilder.point(0.4, 0.06, 0.05),
																	NoiseConfigBuilder.point(1, 0, 0)
															))
													)),
													NoiseConfigBuilder.point(-0.15, 0, NoiseConfigBuilder.spline(
															"erosion",
															NoiseConfigBuilder.point(-0.85, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.38940096, -0.08880186),
																	NoiseConfigBuilder.point(1, 0.38940096, 0.69000006)
															)),
															NoiseConfigBuilder.point(-0.7, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.37788022, -0.115760356),
																	NoiseConfigBuilder.point(1, 0.37788022, 0.6400001)
															)),
															NoiseConfigBuilder.point(-0.4, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0, -0.115760356),
																	NoiseConfigBuilder.point(-0.75, 0, -0.2222),
																	NoiseConfigBuilder.point(-0.65, 0, 0),
																	NoiseConfigBuilder.point(0.5954547, 0, 2.9802322e-8),
																	NoiseConfigBuilder.point(0.6054547, 0.2534563, 2.9802322e-8),
																	NoiseConfigBuilder.point(1, 0.2534563, 0.100000024)
															)),
															NoiseConfigBuilder.point(-0.35, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.3),
																	NoiseConfigBuilder.point(-0.4, 0, 0.05),
																	NoiseConfigBuilder.point(0, 0, 0.05),
																	NoiseConfigBuilder.point(0.4, 0, 0.05),
																	NoiseConfigBuilder.point(1, 0.007000001, 0.060000002)
															)),
															NoiseConfigBuilder.point(-0.1, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.15),
																	NoiseConfigBuilder.point(-0.4, 0, 0),
																	NoiseConfigBuilder.point(0, 0, 0),
																	NoiseConfigBuilder.point(0.4, 0.1, 0.05),
																	NoiseConfigBuilder.point(1, 0.007000001, 0.060000002)
															)),
															NoiseConfigBuilder.point(0.2, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.15),
																	NoiseConfigBuilder.point(-0.4, 0, 0),
																	NoiseConfigBuilder.point(0, 0, 0),
																	NoiseConfigBuilder.point(0.4, 0, 0),
																	NoiseConfigBuilder.point(1, 0, 0)
															)),
															NoiseConfigBuilder.point(0.7, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.02),
																	NoiseConfigBuilder.point(-0.4, 0, -0.03),
																	NoiseConfigBuilder.point(0, 0, -0.03),
																	NoiseConfigBuilder.point(0.4, 0.06, 0),
																	NoiseConfigBuilder.point(1, 0, 0)
															))
													)),
													NoiseConfigBuilder.point(-0.1, 0, NoiseConfigBuilder.spline(
															"erosion",
															NoiseConfigBuilder.point(-0.85, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.38940096, -0.08880186),
																	NoiseConfigBuilder.point(1, 0.38940096, 0.69000006)
															)),
															NoiseConfigBuilder.point(-0.7, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.37788022, -0.115760356),
																	NoiseConfigBuilder.point(1, 0.37788022, 0.6400001)
															)),
															NoiseConfigBuilder.point(-0.4, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0, -0.2222),
																	NoiseConfigBuilder.point(-0.75, 0, -0.2222),
																	NoiseConfigBuilder.point(-0.65, 0, 0),
																	NoiseConfigBuilder.point(0.5954547, 0, -0.115760356),
																	NoiseConfigBuilder.point(0.6054547, 0.2534563, -0.115760356),
																	NoiseConfigBuilder.point(1, 0.2534563, 0.100000024)
															)),
															NoiseConfigBuilder.point(-0.35, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.25),
																	NoiseConfigBuilder.point(-0.4, 0, 0.05),
																	NoiseConfigBuilder.point(0, 0, 0.05),
																	NoiseConfigBuilder.point(0.4, 0, 0.05),
																	NoiseConfigBuilder.point(1, 0.007000001, 0.060000002)
															)),
															NoiseConfigBuilder.point(-0.1, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.1),
																	NoiseConfigBuilder.point(-0.4, 0.01, 0.001),
																	NoiseConfigBuilder.point(0, 0.01, 0.003),
																	NoiseConfigBuilder.point(0.4, 0.094000004, 0.05),
																	NoiseConfigBuilder.point(1, 0.007000001, 0.060000002)
															)),
															NoiseConfigBuilder.point(0.2, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.1),
																	NoiseConfigBuilder.point(-0.4, 0, 0.01),
																	NoiseConfigBuilder.point(0, 0, 0.01),
																	NoiseConfigBuilder.point(0.4, 0.04, 0.03),
																	NoiseConfigBuilder.point(1, 0.049, 0.1)
															)),
															NoiseConfigBuilder.point(0.7, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0, -0.02),
																	NoiseConfigBuilder.point(-0.4, 0, -0.03),
																	NoiseConfigBuilder.point(0, 0, -0.03),
																	NoiseConfigBuilder.point(0.4, 0.12, 0.03),
																	NoiseConfigBuilder.point(1, 0.049, 0.1)
															))
													)),
													NoiseConfigBuilder.point(0.25, 0, NoiseConfigBuilder.spline(
															"erosion",
															NoiseConfigBuilder.point(-0.85, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0, 0.20235021),
																	NoiseConfigBuilder.point(0, 0.5138249, 0.7161751),
																	NoiseConfigBuilder.point(1, 0.5138249, 1.23)
															)),
															NoiseConfigBuilder.point(-0.7, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0, 0.2),
																	NoiseConfigBuilder.point(0, 0.43317974, 0.44682026),
																	NoiseConfigBuilder.point(1, 0.43317974, 0.88)
															)),
															NoiseConfigBuilder.point(-0.4, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0, 0.2),
																	NoiseConfigBuilder.point(0, 0.3917051, 0.30829495),
																	NoiseConfigBuilder.point(1, 0.3917051, 0.70000005)
															)),
															NoiseConfigBuilder.point(-0.35, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.25),
																	NoiseConfigBuilder.point(-0.4, 0, 0.35),
																	NoiseConfigBuilder.point(0, 0, 0.35),
																	NoiseConfigBuilder.point(0.4, 0, 0.35),
																	NoiseConfigBuilder.point(1, 0.049000014, 0.42000002)
															)),
															NoiseConfigBuilder.point(-0.1, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.1),
																	NoiseConfigBuilder.point(-0.4, 0.07, 0.0069999998),
																	NoiseConfigBuilder.point(0, 0.07, 0.021),
																	NoiseConfigBuilder.point(0.4, 0.658, 0.35),
																	NoiseConfigBuilder.point(1, 0.049000014, 0.42000002)
															)),
															NoiseConfigBuilder.point(0.2, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.1),
																	NoiseConfigBuilder.point(-0.4, 0, 0.01),
																	NoiseConfigBuilder.point(0, 0, 0.01),
																	NoiseConfigBuilder.point(0.4, 0.04, 0.03),
																	NoiseConfigBuilder.point(1, 0.049, 0.1)
															)),
															NoiseConfigBuilder.point(0.4, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.1),
																	NoiseConfigBuilder.point(-0.4, 0, 0.01),
																	NoiseConfigBuilder.point(0, 0, 0.01),
																	NoiseConfigBuilder.point(0.4, 0.04, 0.03),
																	NoiseConfigBuilder.point(1, 0.049, 0.1)
															)),
															NoiseConfigBuilder.point(0.45, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0, -0.1),
																	NoiseConfigBuilder.point(-0.4, 0, NoiseConfigBuilder.spline(
																			"ridges",
																			NoiseConfigBuilder.point(-1, 0.5, -0.1),
																			NoiseConfigBuilder.point(-0.4, 0, 0.01),
																			NoiseConfigBuilder.point(0, 0, 0.01),
																			NoiseConfigBuilder.point(0.4, 0.04, 0.03),
																			NoiseConfigBuilder.point(1, 0.049, 0.1)
																	)),
																	NoiseConfigBuilder.point(0, 0, 0.17)
															)),
															NoiseConfigBuilder.point(0.55, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0, -0.1),
																	NoiseConfigBuilder.point(-0.4, 0, NoiseConfigBuilder.spline(
																			"ridges",
																			NoiseConfigBuilder.point(-1, 0.5, -0.1),
																			NoiseConfigBuilder.point(-0.4, 0, 0.01),
																			NoiseConfigBuilder.point(0, 0, 0.01),
																			NoiseConfigBuilder.point(0.4, 0.04, 0.03),
																			NoiseConfigBuilder.point(1, 0.049, 0.1)
																	)),
																	NoiseConfigBuilder.point(0, 0, 0.17)
															)),
															NoiseConfigBuilder.point(0.58, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.1),
																	NoiseConfigBuilder.point(-0.4, 0, 0.01),
																	NoiseConfigBuilder.point(0, 0, 0.01),
																	NoiseConfigBuilder.point(0.4, 0.04, 0.03),
																	NoiseConfigBuilder.point(1, 0.049, 0.1)
															)),
															NoiseConfigBuilder.point(0.7, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.02),
																	NoiseConfigBuilder.point(-0.4, 0, -0.03),
																	NoiseConfigBuilder.point(0, 0, -0.03),
																	NoiseConfigBuilder.point(0.4, 0.12, 0.03),
																	NoiseConfigBuilder.point(1, 0.049, 0.1)
															))
													)),
													NoiseConfigBuilder.point(1.0, 0, NoiseConfigBuilder.spline(
															"erosion",
															NoiseConfigBuilder.point(-0.85, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0, 0.34792626),
																	NoiseConfigBuilder.point(0, 0.5760369, 0.9239631),
																	NoiseConfigBuilder.point(1, 0.5760369, 1.5)
															)),
															NoiseConfigBuilder.point(-0.7, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0, 0.2),
																	NoiseConfigBuilder.point(0, 0.4608295, 0.5391705),
																	NoiseConfigBuilder.point(1, 0.4608295, 1)
															)),
															NoiseConfigBuilder.point(-0.4, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0, 0.2),
																	NoiseConfigBuilder.point(0, 0.4608295, 0.5391705),
																	NoiseConfigBuilder.point(1, 0.4608295, 1)
															)),
															NoiseConfigBuilder.point(-0.35, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.2),
																	NoiseConfigBuilder.point(-0.4, 0, 0.5),
																	NoiseConfigBuilder.point(0, 0, 0.5),
																	NoiseConfigBuilder.point(0.4, 0, 0.5),
																	NoiseConfigBuilder.point(1, 0.070000015, 0.6)
															)),
															NoiseConfigBuilder.point(-0.1, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.05),
																	NoiseConfigBuilder.point(-0.4, 0.099999994, 0.01),
																	NoiseConfigBuilder.point(0, 0.099999994, 0.03),
																	NoiseConfigBuilder.point(0.4, 0.94, 0.5),
																	NoiseConfigBuilder.point(1, 0.070000015, 0.6)
															)),
															NoiseConfigBuilder.point(0.2, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.05),
																	NoiseConfigBuilder.point(-0.4, 0, 0.01),
																	NoiseConfigBuilder.point(0, 0, 0.01),
																	NoiseConfigBuilder.point(0.4, 0.04, 0.03),
																	NoiseConfigBuilder.point(1, 0.049, 0.1)
															)),
															NoiseConfigBuilder.point(0.4, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.05),
																	NoiseConfigBuilder.point(-0.4, 0, 0.01),
																	NoiseConfigBuilder.point(0, 0, 0.01),
																	NoiseConfigBuilder.point(0.4, 0.04, 0.03),
																	NoiseConfigBuilder.point(1, 0.049, 0.1)
															)),
															NoiseConfigBuilder.point(0.45, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0, -0.05),
																	NoiseConfigBuilder.point(-0.4, 0, NoiseConfigBuilder.spline(
																			"ridges",
																			NoiseConfigBuilder.point(-1, 0.5, -0.05),
																			NoiseConfigBuilder.point(-0.4, 0, 0.01),
																			NoiseConfigBuilder.point(0, 0, 0.01),
																			NoiseConfigBuilder.point(0.4, 0.04, 0.03),
																			NoiseConfigBuilder.point(1, 0.049, 0.1)
																	)),
																	NoiseConfigBuilder.point(0, 0, 0.17)
															)),
															NoiseConfigBuilder.point(0.55, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0, -0.05),
																	NoiseConfigBuilder.point(-0.4, 0, NoiseConfigBuilder.spline(
																			"ridges",
																			NoiseConfigBuilder.point(-1, 0.5, -0.05),
																			NoiseConfigBuilder.point(-0.4, 0, 0.01),
																			NoiseConfigBuilder.point(0, 0, 0.01),
																			NoiseConfigBuilder.point(0.4, 0.04, 0.03),
																			NoiseConfigBuilder.point(1, 0.049, 0.1)
																	)),
																	NoiseConfigBuilder.point(0, 0, 0.17)
															)),
															NoiseConfigBuilder.point(0.58, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.05),
																	NoiseConfigBuilder.point(-0.4, 0, 0.01),
																	NoiseConfigBuilder.point(0, 0, 0.01),
																	NoiseConfigBuilder.point(0.4, 0.04, 0.03),
																	NoiseConfigBuilder.point(1, 0.049, 0.1)
															)),
															NoiseConfigBuilder.point(0.7, 0, NoiseConfigBuilder.spline(
																	"ridges",
																	NoiseConfigBuilder.point(-1, 0.5, -0.02),
																	NoiseConfigBuilder.point(-0.4, 0, -0.03),
																	NoiseConfigBuilder.point(0, 0, -0.03),
																	NoiseConfigBuilder.point(0.4, 0.04, 0.03),
																	NoiseConfigBuilder.point(1, 0.049, 0.1)
															))
													))
											), 0, 0
									)
							).noiseRouter().surfaceRules(SurfaceRulesBuilder.sequence(
									SurfaceRulesBuilder.condition(
										SurfaceRulesBuilder.verticalGradient(
											"bebrock",
											YOffsetBuilder.aboveBottom(0),
											YOffsetBuilder.aboveBottom(5)
										),
										SurfaceRulesBuilder.block(StateDataBuilder.name("minecraft:bedrock"))
									),
									SurfaceRulesBuilder.condition(
										SurfaceRulesBuilder.verticalGradient(
											"deepslate",
											YOffsetBuilder.absolute(-16),
											YOffsetBuilder.absolute(0)
										),
										SurfaceRulesBuilder.block(StateDataBuilder.name("minecraft:deepslate").setProperty("axis", "y"))
									),
									SurfaceRulesBuilder.condition(
										SurfaceRulesBuilder.verticalGradient(
											"tuff",
											YOffsetBuilder.absolute(0),
											YOffsetBuilder.absolute(16)
										),
										SurfaceRulesBuilder.block(StateDataBuilder.name("minecraft:tuff"))
									),
									SurfaceRulesBuilder.condition(
										SurfaceRulesBuilder.aboveMainSurface(),
										SurfaceRulesBuilder.sequence(
											SurfaceRulesBuilder.condition(
												SurfaceRulesBuilder.biome(id("test_biome").toString()),
												SurfaceRulesBuilder.block(StateDataBuilder.name("artifice:test_block"))),
											SurfaceRulesBuilder.block(StateDataBuilder.name("minecraft:bedrock")))),
											SurfaceRulesBuilder.condition(
												SurfaceRulesBuilder.aboveMainSurface(),
												SurfaceRulesBuilder.sequence(SurfaceRulesBuilder.condition(
															SurfaceRulesBuilder.biome(id("test_biome").toString()),
															SurfaceRulesBuilder.block(StateDataBuilder.name("artifice:test_block"))),
													SurfaceRulesBuilder.block(StateDataBuilder.name("minecraft:grass_block")))
											), SurfaceRulesBuilder.condition(
												SurfaceRulesBuilder.verticalGradient(
													"deepslate",
													YOffsetBuilder.absolute(-16),
													YOffsetBuilder.absolute(0)
												),
												SurfaceRulesBuilder.block(StateDataBuilder.name("minecraft:deepslate").setProperty("axis", "y"))
												), SurfaceRulesBuilder.condition(
														SurfaceRulesBuilder.verticalGradient(
													"tuff",
														YOffsetBuilder.absolute(0),
														YOffsetBuilder.absolute(16)
														),
													SurfaceRulesBuilder.block(StateDataBuilder.name("minecraft:tuff"))
												)
							))
			);
			//not even going to touch this until we get everything else working
			// Tested, it works now. Wasn't in 20w28a.
			pack.addConfiguredFeature(id("test_featureee"), configuredFeatureBuilder ->
				configuredFeatureBuilder.featureID("minecraft:tree")
					.featureConfig(new TreeFeatureConfigBuilder()
							.ignoreVines(true)
							.maxWaterDepth(5)
							.dirtProvider(BlockStateProviderBuilder.simpleProvider(StateDataBuilder.name("minecraft:dirt"))).
							trunkProvider(BlockStateProviderBuilder.simpleProvider(StateDataBuilder.name("minecraft:oak_log")
								.setProperty("axis", "y"))
							).foliageProvider(BlockStateProviderBuilder.simpleProvider(StateDataBuilder.name("minecraft:spruce_leaves")
								.setProperty("persistent", "false")
								.setProperty("distance", "7"))
							).foliagePlacer(new FoliagePlacerBuilder.BlobFoliagePlacerBuilder().height(2).offset(1).radius(2))
							.trunkPlacer(new TrunkPlacerBuilder.FancyTrunkPlacerBuilder().baseHeight(12).heightRandA(3)
								.heightRandB(4)).minimumSize(new FeatureSizeBuilder.TwoLayersFeatureSizeBuilder()
									.limit(10).lowerSize(1).upperSize(9))
							.heightmap(Heightmap.Types.OCEAN_FLOOR)
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
