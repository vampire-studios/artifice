package com.swordglowsblue.artifice.test;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TestChunkGenerator extends ChunkGenerator {
	public static final Codec<TestChunkGenerator> CODEC = RecordCodecBuilder.create(
			instance -> commonCodec(instance)
					.and(RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter(debugChunkGenerator -> debugChunkGenerator.biomeRegistry))
					.and(Codec.BOOL.fieldOf("test_bool").forGetter((generator) -> generator.testBool))
					.apply(instance, instance.stable(TestChunkGenerator::new))
	);

	private final boolean testBool;
	private final Registry<Biome> biomeRegistry;

	public TestChunkGenerator(Registry<StructureSet> registry, Registry<Biome> registry2, boolean testBool) {
		super(registry, Optional.empty(), new FixedBiomeSource(registry2.getOrCreateHolderOrThrow(Biomes.PLAINS)));
		this.testBool = testBool;
		this.biomeRegistry = registry2;
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public void applyCarvers(WorldGenRegion level, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureFeatureManager, ChunkAccess chunk, GenerationStep.Carving step) {

	}

	@Override
	public void buildSurface(WorldGenRegion region, StructureManager structureManager, RandomState randomState, ChunkAccess chunk) {

	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion level) {

	}

	@Override
	public int getGenDepth() {
		return 384;
	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState, StructureManager structureFeatureManager, ChunkAccess chunk) {
		List<BlockState> list = List.of(Blocks.STONE.defaultBlockState(), Blocks.DIRT.defaultBlockState(), Blocks.BEDROCK.defaultBlockState());
		BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
		Heightmap heightmap = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
		Heightmap heightmap2 = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);

		for(int i = 0; i < Math.min(chunk.getHeight(), list.size()); ++i) {
			BlockState blockState = list.get(i);
			if (blockState != null) {
				int j = chunk.getMinBuildHeight() + i;

				for(int k = 0; k < 16; ++k) {
					for(int l = 0; l < 16; ++l) {
						chunk.setBlockState(mutableBlockPos.set(k, j, l), blockState, false);
						heightmap.update(k, j, l, blockState);
						heightmap2.update(k, j, l, blockState);
					}
				}
			}
		}

		return CompletableFuture.completedFuture(chunk);
	}

	@Override
	public int getSeaLevel() {
		return 63;
	}

	@Override
	public int getMinY() {
		return -64;
	}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState randomState) {
		List<BlockState> list = List.of(Blocks.STONE.defaultBlockState(), Blocks.DIRT.defaultBlockState(), Blocks.BEDROCK.defaultBlockState());

		for(int i = Math.min(list.size(), level.getMaxBuildHeight()) - 1; i >= 0; --i) {
			BlockState blockState = list.get(i);
			if (blockState != null && type.isOpaque().test(blockState)) {
				return level.getMinBuildHeight() + i + 1;
			}
		}

		return level.getMinBuildHeight();
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) {
		return new NoiseColumn(
				level.getMinBuildHeight(),
				new BlockState[]{Blocks.STONE.defaultBlockState(), Blocks.DIRT.defaultBlockState(), Blocks.BEDROCK.defaultBlockState()}
		);
	}

	@Override
	public void addDebugScreenInfo(List<String> list, RandomState randomState, BlockPos blockPos) {

	}
}