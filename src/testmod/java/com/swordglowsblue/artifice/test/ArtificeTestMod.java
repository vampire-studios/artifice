package com.swordglowsblue.artifice.test;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.vampirestudios.artifice.api.Artifice;
import io.github.vampirestudios.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.artifice.api.builder.data.dimension.ChunkGeneratorTypeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.BlockStateProviderBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.SurfaceRulesBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.YOffsetBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.decorator.config.CountExtraDecoratorConfigBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.decorator.config.DecoratedDecoratorConfigBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config.DecoratedFeatureConfigBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config.TreeFeatureConfigBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.FeatureSizeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.FoliagePlacerBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.TrunkPlacerBuilder;
import io.github.vampirestudios.artifice.api.resource.StringResource;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

import java.awt.*;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ArtificeTestMod implements ModInitializer, ClientModInitializer {
	private static final Item.Settings itemSettings = new Item.Settings().group(ItemGroup.MISC);
	private static final Item testItem = Registry.register(Registry.ITEM, id("test_item"), new Item(itemSettings));
	private static final Block testBlock = Registry.register(Registry.BLOCK, id("test_block"), new Block(Block.Settings.copy(Blocks.STONE)));
	private static final Item testBlockItem = Registry.register(Registry.ITEM, id("test_block"), new BlockItem(testBlock, itemSettings));
	private static final RegistryKey<DimensionType> testDimension = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, id("test_dimension_type_vanilla"));
	private static final RegistryKey<DimensionType> testDimensionCustom = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, id("test_dimension_type_custom"));
	private static final RegistryKey<DimensionOptions> testDimensionKey = RegistryKey.of(Registry.DIMENSION_KEY, id("test_dimension"));
	private static final RegistryKey<DimensionOptions> testDimensionCustomKey = RegistryKey.of(Registry.DIMENSION_KEY, id("test_dimension_custom"));

	private static Identifier id(String name) {
		return new Identifier("artifice", name);
	}

	public void onInitialize() {
		Registry.register(Registry.CHUNK_GENERATOR, RegistryKey.of(Registry.CHUNK_GENERATOR_KEY, id("test_chunk_generator")).getValue(), TestChunkGenerator.CODEC);

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

			pack.addDimensionType(testDimension.getValue(), dimensionTypeBuilder -> dimensionTypeBuilder
					.natural(false).hasRaids(false).respawnAnchorWorks(true).bedWorks(false).piglinSafe(false)
					.ambientLight(6.0F).infiniburn(BlockTags.INFINIBURN_OVERWORLD.getId())
					.ultrawarm(false).hasCeiling(false).hasSkylight(false).coordinate_scale(1.0).logicalHeight(832).height(832).minimumY(-512).effects("minecraft:the_end"));

			pack.addDimension(id("test_dimension"), dimensionBuilder -> dimensionBuilder
					.dimensionType(testDimension.getValue())
					.flatGenerator(flatChunkGeneratorTypeBuilder -> flatChunkGeneratorTypeBuilder
							.addLayer(layersBuilder -> layersBuilder.block("minecraft:bedrock").height(2))
							.addLayer(layersBuilder -> layersBuilder.block("minecraft:stone").height(2))
							.addLayer(layersBuilder -> layersBuilder.block("minecraft:granite").height(2))
							.biome("minecraft:plains")
							/*.structureManager(structureManagerBuilder ->
									structureManagerBuilder.addStructure(Registry.STRUCTURE_FEATURE.getId(StructureFeature.MINESHAFT).toString(),
											structureConfigBuilder -> structureConfigBuilder.salt(1999999).separation(1).spacing(2)))*/
					)
			);

			pack.addDimensionType(testDimensionCustom.getValue(), dimensionTypeBuilder -> dimensionTypeBuilder
					.natural(true).hasRaids(false).respawnAnchorWorks(false).bedWorks(false).piglinSafe(false)
					.ambientLight(0.0F).infiniburn(BlockTags.INFINIBURN_OVERWORLD.getId())
					.ultrawarm(false).hasCeiling(false).hasSkylight(true).coordinate_scale(1.0).logicalHeight(832).height(832).minimumY(-512));
			pack.addDimension(id("test_dimension_custom"), dimensionBuilder -> {
				dimensionBuilder.dimensionType(testDimensionCustom.getValue());
				dimensionBuilder.noiseGenerator(noiseChunkGeneratorTypeBuilder -> {
					noiseChunkGeneratorTypeBuilder.fixedBiomeSource(fixedBiomeSourceBuilder -> {
						fixedBiomeSourceBuilder.biome(id("test_biome").toString());
						fixedBiomeSourceBuilder.seed((int) new Random().nextLong());
					});
					noiseChunkGeneratorTypeBuilder.noiseSettings("minecraft:overworld");
					noiseChunkGeneratorTypeBuilder.seed((int) new Random().nextLong());
				});
			});

			pack.addBiome(id("test_biome"), biomeBuilder -> {
				biomeBuilder.precipitation(Biome.Precipitation.RAIN).surfaceBuilder("minecraft:grass");
				biomeBuilder.category(Biome.Category.PLAINS);
				biomeBuilder.depth(0.125F);
				biomeBuilder.scale(0.05F);
				biomeBuilder.temperature(0.8F);
				biomeBuilder.downfall(0.4F);
				biomeBuilder.effects(biomeEffectsBuilder -> {
					biomeEffectsBuilder.waterColor(4159204);
					biomeEffectsBuilder.waterFogColor(329011);
					biomeEffectsBuilder.fogColor(12638463);
					biomeEffectsBuilder.skyColor(4159204);
				});
				biomeBuilder.addAirCarvers(id("test_carver").toString());
				//biomeBuilder.addFeaturesbyStep(GenerationStep.Feature.VEGETAL_DECORATION, id("test_decorated_feature").toString());
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
							.lavaLevel( YOffsetBuilder.aboveBottom(8))
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
			/*pack.addNoiseSettingsBuilder(id("test_dimension"),noiseSettingsBuilder ->
					noiseSettingsBuilder.aquifersEnabled(true).defaultBlock(stateDataBuilder ->
							stateDataBuilder.name("minecraft:stone")).defaultFluid(stateDataBuilder ->
							stateDataBuilder.name("minecraft:lava")).seaLevel(65).legacyRandomSource(false).noodleCavesEnabled(false)
							.oreVeinsEnabled(true).noiseConfig(noiseConfigBuilder -> noiseConfigBuilder)
			);*/
			System.out.println(new SurfaceRulesBuilder().sequence(surfaceRulesBuilder ->
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
									"woollenpjs",
									YOffsetBuilder.belowTop(80),
									YOffsetBuilder.aboveBottom(22)
							),
							surfaceRulesBuilder1 -> surfaceRulesBuilder1.block(stateDataBuilder -> stateDataBuilder.name("minecraft:cyan_wool"))
					), surfaceRulesBuilder ->
					surfaceRulesBuilder.condition(
							surfaceRulesBuilder1 -> surfaceRulesBuilder1.aboveMainSurface(),
							surfaceRulesBuilder1 -> surfaceRulesBuilder1.sequence(surfaceRulesBuilder2 ->
									surfaceRulesBuilder2.condition(
											surfaceRulesBuilder3 -> surfaceRulesBuilder3.biome(id("test_biome").toString()),
											surfaceRulesBuilder3 -> surfaceRulesBuilder3.block(stateDataBuilder -> stateDataBuilder.name("artifice:test_block"))
									),
									surfaceRulesBuilder2 -> surfaceRulesBuilder2.block(stateDataBuilder -> stateDataBuilder.name("minecraft:grass_block"))
									)
					)

			).buildTo(new JsonObject()).toString());
/*//not even going to touch this until we get everything else working
			// Tested, it works now. Wasn't in 20w28a.
			pack.addConfiguredFeature(id("test_featureee"), configuredFeatureBuilder ->
					configuredFeatureBuilder.featureID("minecraft:tree")
							.featureConfig(treeFeatureConfigBuilder ->
									treeFeatureConfigBuilder
											.ignoreVines(true)
											.maxWaterDepth(5)
											.trunkProvider(simpleBlockStateProviderBuilder ->
															simpleBlockStateProviderBuilder.state(blockStateDataBuilder ->
																	blockStateDataBuilder.name("minecraft:oak_log")
																			.setProperty("axis", "y")),
													new BlockStateProviderBuilder.SimpleBlockStateProviderBuilder()
											).leavesProvider(simpleBlockStateProviderBuilder ->
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
			pack.addPlacedFeature(id("test_decorated_feature"), configuredFeatureBuilder -> configuredFeatureBuilder.featureID("minecraft:decorated")
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

	@Environment(EnvType.CLIENT)
	public void onInitializeClient() {
		Artifice.registerAssetPack("artifice:testmod", pack -> {
			pack.setDisplayName("Artifice Test Resources");
			pack.setDescription("Resources for the Artifice test mod");

			pack.addItemModel(id("test_item"), model -> model
					.parent(new Identifier("item/generated"))
					.texture("layer0", new Identifier("block/sand"))
					.texture("layer1", new Identifier("block/dead_bush")));
			pack.addItemModel(id("test_block"), model -> model
					.parent(id("block/test_block")));

			pack.addBlockState(id("test_block"), state -> state
					.weightedVariant("", variant -> variant
							.model(id("block/test_block"))
							.weight(3))
					.weightedVariant("", variant -> variant
							.model(new Identifier("block/coarse_dirt"))));

			pack.addBlockModel(id("test_block"), model -> model
					.parent(new Identifier("block/cube_all"))
					.texture("all", new Identifier("item/diamond_sword")));

			pack.addTranslations(id("en_us"), translations -> translations
					.entry("item.artifice.test_item", "Artifice Test Item")
					.entry("block.artifice.test_block", "Artifice Test Block"));
			pack.addLanguage("ar_tm", "Artifice", "Test Mod", false);
			pack.addTranslations(id("ar_tm"), translations -> translations
					.entry("item.artifice.test_item", "Artifice Test Item in custom lang")
					.entry("block.artifice.test_block", "Artifice Test Block in custom lang"));
			try {
				pack.dumpResources("testing_assets", "assets");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		Artifice.registerAssetPack(id("testmod2"), ArtificeResourcePack.ClientResourcePackBuilder::setOptional);
	}

	public static class TestChunkGenerator extends ChunkGenerator {
		public static final Codec<TestChunkGenerator> CODEC = RecordCodecBuilder.create((instance) ->
				instance.group(
								BiomeSource.CODEC.fieldOf("biome_source")
										.forGetter((generator) -> generator.biomeSource),
								Codec.BOOL.fieldOf("test_bool").forGetter((generator) -> generator.testBool)
						)
						.apply(instance, instance.stable(TestChunkGenerator::new))
		);

		private boolean testBool;

		public TestChunkGenerator(BiomeSource biomeSource, boolean testBool) {
			super(biomeSource, new StructuresConfig(false));
			this.testBool = testBool;
		}

		@Override
		protected Codec<? extends ChunkGenerator> getCodec() {
			return CODEC;
		}

		@Override
		public ChunkGenerator withSeed(long seed) {
			return this;
		}

		@Override
		public MultiNoiseUtil.MultiNoiseSampler getMultiNoiseSampler() {
			return (x, y, z) -> MultiNoiseUtil.createNoiseValuePoint(1.0F, 1.0F, 1.0F, 0.5F, 0.0F, 1.0F);
		}

		@Override
		public void carve(ChunkRegion chunkRegion, long seed, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver generationStep) {

		}

		@Override
		public void buildSurface(ChunkRegion region, StructureAccessor structures, Chunk chunk) {

		}

		@Override
		public void populateEntities(ChunkRegion region) {

		}

		@Override
		public int getWorldHeight() {
			return 256;
		}

		@Override
		public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, StructureAccessor structureAccessor, Chunk chunk) {
			return null;
		}

		@Override
		public int getSeaLevel() {
			return 0;
		}

		@Override
		public int getMinimumY() {
			return 0;
		}

		@Override
		public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world) {
			return 0;
		}

		@Override
		public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
			return new VerticalBlockSample(10, new BlockState[0]);
		}
	}

	public static class TestChunkGeneratorTypeBuilder extends ChunkGeneratorTypeBuilder {
		public TestChunkGeneratorTypeBuilder() {
			super();
			this.type(id("test_chunk_generator").toString());
		}

		public TestChunkGeneratorTypeBuilder testBool(boolean customBool) {
			this.root.addProperty("test_bool", customBool);
			return this;
		}
	}
}
