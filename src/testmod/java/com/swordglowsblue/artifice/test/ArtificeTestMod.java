package com.swordglowsblue.artifice.test;

import io.github.vampirestudios.artifice.api.Artifice;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.TagBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.BiomeSourceBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.ChunkGeneratorTypeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.ChunkGeneratorTypeBuilder.FlatChunkGeneratorTypeBuilder.LayersBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.DimensionBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.NoiseConfigBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.*;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.biome.BiomeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.biome.BiomeEffectsBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.ConfiguredCarverBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.ConfiguredFeatureBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.PlacedFeatureBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config.TreeFeatureConfigBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.placementmodifiers.PlacementModifier;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.FeatureSizeBuilder.TwoLayersFeatureSizeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.FoliagePlacerBuilder.BlobFoliagePlacerBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.gen.TrunkPlacerBuilder.FancyTrunkPlacerBuilder;
import io.github.vampirestudios.artifice.api.resource.StringResource;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.material.Fluids;

public class ArtificeTestMod implements ModInitializer {
    private static final FabricItemSettings itemSettings = new FabricItemSettings();

    private static final Item testItem = Registry.register(BuiltInRegistries.ITEM, id("test_item"), new Item(itemSettings));
    private static final Block testBlock = Registry.register(BuiltInRegistries.BLOCK, id("test_block"), new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
    private static final Item testBlockItem = Registry.register(BuiltInRegistries.ITEM, id("test_block"), new BlockItem(testBlock, itemSettings));

    private static final ResourceKey<DimensionType> testDimension = ResourceKey.create(Registries.DIMENSION_TYPE, id("test_dimension_type_vanilla"));
    private static final ResourceKey<DimensionType> testDimensionCustom = ResourceKey.create(Registries.DIMENSION_TYPE, id("test_dimension_type_custom"));
    private static final ResourceKey<NoiseGeneratorSettings> testDimensionSettings = ResourceKey.create(Registries.NOISE_SETTINGS, id("test_dimension"));

    private static ResourceLocation id(String name) {
        return new ResourceLocation("artifice", name);
    }

    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            entries.accept(testItem);
            entries.accept(testBlockItem);
        });
//		Registry.register(BuiltInRegistries.CHUNK_GENERATOR, ResourceKey.create(Registries.CHUNK_GENERATOR, id("test_chunk_generator")).location(), TestChunkGenerator.CODEC);

        Artifice.registerDataPack(id("optional_test"), pack -> {
            pack.setOptional();
            pack.setDisplayName("Test");

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

            RegistryUtils<Block> blockRegistryUtils = new RegistryUtils<>(BuiltInRegistries.BLOCK);
            pack.addTag("block", ArtificeTestClientMod.makeResourceLocation("test_block_tag"), new TagBuilder<Block>()
                            .replace(false)
                            .values(blockRegistryUtils.getIdByType(Blocks.GRASS_BLOCK),
                                    blockRegistryUtils.getIdByType(Blocks.STONE),
                                    blockRegistryUtils.getIdByType(Blocks.DIRT),
                                    blockRegistryUtils.getIdByType(Blocks.PODZOL),
                                    blockRegistryUtils.getIdByType(Blocks.MYCELIUM)
                            )
//					.values(Blocks.GRASS_BLOCK, Blocks.STONE, Blocks.DIRT, Blocks.PODZOL, Blocks.MYCELIUM)
            );

            RegistryUtils<Item> itemRegistryUtils = new RegistryUtils<>(BuiltInRegistries.ITEM);
            pack.addTag("item", ArtificeTestClientMod.makeResourceLocation("test_item_tag"), new TagBuilder<Item>()
                    .replace(false)
                    .values(itemRegistryUtils.getIdByType(Items.DIAMOND),
                            itemRegistryUtils.getIdByType(Items.GOLD_INGOT),
                            itemRegistryUtils.getIdByType(Items.EMERALD),
                            itemRegistryUtils.getIdByType(Items.PHANTOM_MEMBRANE),
                            itemRegistryUtils.getIdByType(Items.RAW_COPPER)
                    )
            );

            RegistryUtils<EntityType<?>> entityTypeRegistryUtils = new RegistryUtils<>(BuiltInRegistries.ENTITY_TYPE);
            pack.addTag("item", ArtificeTestClientMod.makeResourceLocation("test_entity_tag"), new TagBuilder<Item>()
                            .replace(false)
                            .values(itemRegistryUtils.getIdByType(Items.DIAMOND),
                                    itemRegistryUtils.getIdByType(Items.GOLD_INGOT),
                                    itemRegistryUtils.getIdByType(Items.EMERALD),
                                    itemRegistryUtils.getIdByType(Items.PHANTOM_MEMBRANE),
                                    itemRegistryUtils.getIdByType(Items.RAW_COPPER)
                            )
            );

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
                    .dimensionType(BuiltinDimensionTypes.OVERWORLD)
                    .flatGenerator(ChunkGeneratorTypeBuilder.flatChunks()
                            .addLayer(
                                    new LayersBuilder(Blocks.BEDROCK, 2),
                                    new LayersBuilder(Blocks.DEEPSLATE, 10),
                                    new LayersBuilder(Blocks.STONE, 2),
                                    new LayersBuilder(Blocks.GRANITE, 2)
                            ).biome(Biomes.PLAINS)
                    )
            );

            pack.addDimension(id("test_dimension2"), new DimensionBuilder()
                    .dimensionType(BuiltinDimensionTypes.OVERWORLD)
                    .flatGenerator(ChunkGeneratorTypeBuilder.flatChunks()
                            .lakes(false).features(true)
                            .addLayer(
                                    new LayersBuilder(Blocks.BEDROCK, 2),
                                    new LayersBuilder(Blocks.DEEPSLATE, 10),
                                    new LayersBuilder(Blocks.STONE, 2),
                                    new LayersBuilder(Blocks.ANDESITE, 2)
                            ).biome(Biomes.PLAINS)
                    )
            );

            pack.addDimension(id("test_dimension_custom"), new DimensionBuilder()
                    .dimensionType(BuiltinDimensionTypes.OVERWORLD)
                    .noiseGenerator(ChunkGeneratorTypeBuilder.noiseChunks()
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
                            )).noiseSettings(NoiseGeneratorSettings.OVERWORLD)
                    )
            );

            pack.addDimension(id("test_dimension_custom2"), new DimensionBuilder()
                    .dimensionType(BuiltinDimensionTypes.OVERWORLD)
                    .noiseGenerator(ChunkGeneratorTypeBuilder.noiseChunks()
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
                            )).noiseSettings("artifice:test_dimension")
                    )
            );

            pack.addConfiguredCarver(id("test_carver"), new ConfiguredCarverBuilder()
                    .probability(0.15F)
                    .type("minecraft:cave")
                    .y(HeightProviderBuilders.uniform(YOffsetBuilder.aboveBottom(8), YOffsetBuilder.absolute(180)))
                    .yScale(FloatProviderBuilders.uniform(0.1F, 0.9F))
                    .lavaLevel(YOffsetBuilder.aboveBottom(8))
                    .horizontalRadiusModifier(FloatProviderBuilders.uniform(1.7F, 2.4F))
                    .verticalRadiusModifier(FloatProviderBuilders.uniform(1.5F, 2.1F))
                    .floorLevel(FloatProviderBuilders.uniform(-1F, -0.4F))
            );

            pack.addBiome(id("test_biome"), new BiomeBuilder()
                    .hasPrecipitation(true)
                    .temperature(0.8F).downfall(0.4F)
                    .addSpawnCosts(EntityType.BEE, new BiomeBuilder.SpawnDensityBuilder(0.12, 1))
                    .addSpawnCosts(EntityType.CAT, new BiomeBuilder.SpawnDensityBuilder(0.4, 1))
                    .effects(new BiomeEffectsBuilder()
                            .waterColor(4159204)
                            .waterFogColor(329011)
                            .fogColor(12638463)
                            .skyColor(4159204)
                    ).addAirCarvers(id("test_carver").toString())
            );

            pack.addBiome(id("test_biome2"), new BiomeBuilder()
                    .hasPrecipitation(true)
                    .temperature(0.8F).downfall(0.4F)
                    .addSpawnCosts(EntityType.SPIDER, new BiomeBuilder.SpawnDensityBuilder(0.6, 1))
                    .addSpawnCosts(EntityType.CAT, new BiomeBuilder.SpawnDensityBuilder(0.4, 1))
                    .effects(new BiomeEffectsBuilder()
                            .waterColor(4159204)
                            .waterFogColor(329011)
                            .fogColor(12638463)
                            .skyColor(4159204)
                    ).addAirCarvers(id("test_carver").toString())
            );

            pack.addBiome(id("test_biome3"), new BiomeBuilder()
                    .hasPrecipitation(true)
                    .temperature(2.0F).downfall(0.4F)
                    .addSpawnCosts(EntityType.BEE, new BiomeBuilder.SpawnDensityBuilder(0.12, 1))
                    .addSpawnCosts(EntityType.CAT, new BiomeBuilder.SpawnDensityBuilder(0.4, 1))
                    .effects(new BiomeEffectsBuilder()
                            .waterColor(4159204)
                            .waterFogColor(329011)
                            .fogColor(12638463)
                            .skyColor(4159204)
                    ).addAirCarvers(id("test_carver").toString())
            );

            pack.addBiome(id("test_biome4"), new BiomeBuilder()
                    .hasPrecipitation(true)
                    .temperature(1.4F).downfall(1.0F)
                    .addSpawnCosts(EntityType.BEE, new BiomeBuilder.SpawnDensityBuilder(0.12, 1))
                    .addSpawnCosts(EntityType.CAT, new BiomeBuilder.SpawnDensityBuilder(0.4, 1))
                    .effects(new BiomeEffectsBuilder()
                            .waterColor(4159204)
                            .waterFogColor(329011)
                            .fogColor(12638463)
                            .skyColor(4159204)
                    ).addAirCarvers("minecraft:cave")
            );

            // gotta wait on noise config
            pack.addNoiseSettingsBuilder(id("test_dimension"), NoiseSettingsBuilder.create(new NoiseSettingsBuilder()
                    .defaultBlock(StateDataBuilder.name(Blocks.STONE))
                    .defaultFluid(StateDataBuilder.name(Fluids.LAVA).setProperty(BlockStateProperties.LEVEL, 0))
                    .seaLevel(65).legacyRandomSource(false).aquifersEnabled(true)
                    .disableMobGeneration(false).oreVeinsEnabled(true).oreVeinsEnabled(true)
                    .noiseConfig(NoiseConfigBuilder.noiseConfig(-64, 384, 1, 2))
                    .noiseRouter().surfaceRules(SurfaceRulesBuilder.sequence(
                            SurfaceRulesBuilder.condition(
                                    SurfaceRulesBuilder.verticalGradient(
                                            "bebrock",
                                            YOffsetBuilder.aboveBottom(0),
                                            YOffsetBuilder.aboveBottom(5)
                                    ),
                                    SurfaceRulesBuilder.block(StateDataBuilder.name(Blocks.BEDROCK))
                            ),
                            SurfaceRulesBuilder.condition(
                                    SurfaceRulesBuilder.verticalGradient(
                                            "deepslate",
                                            YOffsetBuilder.absolute(-16),
                                            YOffsetBuilder.absolute(0)
                                    ),
                                    SurfaceRulesBuilder.block(StateDataBuilder.name(Blocks.DEEPSLATE).setProperty(BlockStateProperties.AXIS, Direction.Axis.Y.getName()))
                            ),
                            SurfaceRulesBuilder.condition(
                                    SurfaceRulesBuilder.verticalGradient(
                                            "tuff",
                                            YOffsetBuilder.absolute(0),
                                            YOffsetBuilder.absolute(16)
                                    ),
                                    SurfaceRulesBuilder.block(StateDataBuilder.name(Blocks.TUFF))
                            ),
                            SurfaceRulesBuilder.condition(
                                    SurfaceRulesBuilder.aboveMainSurface(),
                                    SurfaceRulesBuilder.sequence(
                                            SurfaceRulesBuilder.condition(
                                                    SurfaceRulesBuilder.biome(id("test_biome").toString()),
                                                    SurfaceRulesBuilder.block(StateDataBuilder.name(testBlock))),
                                            SurfaceRulesBuilder.block(StateDataBuilder.name(Blocks.BEDROCK)))),
                            SurfaceRulesBuilder.condition(
                                    SurfaceRulesBuilder.aboveMainSurface(),
                                    SurfaceRulesBuilder.sequence(SurfaceRulesBuilder.condition(
                                                    SurfaceRulesBuilder.biome(id("test_biome").toString()),
                                                    SurfaceRulesBuilder.block(StateDataBuilder.name(testBlock))),
                                            SurfaceRulesBuilder.block(StateDataBuilder.name(Blocks.GRASS_BLOCK)))
                            ), SurfaceRulesBuilder.condition(
                                    SurfaceRulesBuilder.verticalGradient(
                                            "deepslate",
                                            YOffsetBuilder.absolute(-16),
                                            YOffsetBuilder.absolute(0)
                                    ),
                                    SurfaceRulesBuilder.block(StateDataBuilder.name(Blocks.DEEPSLATE).setProperty(BlockStateProperties.AXIS, Direction.Axis.Y.getName()))
                            ), SurfaceRulesBuilder.condition(
                                    SurfaceRulesBuilder.verticalGradient(
                                            "tuff",
                                            YOffsetBuilder.absolute(0),
                                            YOffsetBuilder.absolute(16)
                                    ),
                                    SurfaceRulesBuilder.block(StateDataBuilder.name(Blocks.TUFF))
                            )
                    ))
            ));
            //not even going to touch this until we get everything else working
            // Tested, it works now. Wasn't in 20w28a.
            pack.addConfiguredFeature(id("test_featureee"), new ConfiguredFeatureBuilder()
                    .featureID("minecraft:tree")
                    .featureConfig(new TreeFeatureConfigBuilder()
                            .ignoreVines(true)
                            .maxWaterDepth(5)
                            .dirtProvider(BlockStateProviderBuilder.simpleProvider(StateDataBuilder.name(Blocks.DIRT))).
                            trunkProvider(BlockStateProviderBuilder.simpleProvider(StateDataBuilder.name(Blocks.OAK_LOG)
                                    .setProperty(BlockStateProperties.AXIS, Direction.Axis.Y.getName()))
                            ).foliageProvider(BlockStateProviderBuilder.simpleProvider(StateDataBuilder.name(Blocks.SPRUCE_LEAVES)
                                    .setProperty(BlockStateProperties.PERSISTENT, false)
                                    .setProperty(BlockStateProperties.DISTANCE, 7))
                            ).foliagePlacer(BlobFoliagePlacerBuilder.create(2, 1, 2))
                            .trunkPlacer(FancyTrunkPlacerBuilder.create(12, 3, 4))
                            .minimumSize(TwoLayersFeatureSizeBuilder.create(10, 1, 9))
                            .heightmap(Heightmap.Types.OCEAN_FLOOR)
                    )
            );
            // Should be working but Minecraft coders did something wrong and the default feature is being return when it shouldn't resulting in a crash.
            pack.addPlacedFeature(id("test_placed_feature"), PlacedFeatureBuilder.create(
                    "artifice:test_featureee", PlacementModifier.biome(), PlacementModifier.inSquare()
            ));

            pack.dump("testing_data", "data", FabricLoader.getInstance().isDevelopmentEnvironment());
        });
    }

}
