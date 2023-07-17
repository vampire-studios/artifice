package io.github.vampirestudios.artifice.api;

import io.github.vampirestudios.artifice.api.builder.assets.*;
import io.github.vampirestudios.artifice.api.builder.data.AdvancementBuilder;
import io.github.vampirestudios.artifice.api.builder.data.LootTableBuilder;
import io.github.vampirestudios.artifice.api.builder.data.TagBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.DimensionBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.DimensionTypeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.recipe.*;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.NoiseSettingsBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.biome.BiomeBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.ConfiguredCarverBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.ConfiguredFeatureBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.PlacedFeatureBuilder;
import io.github.vampirestudios.artifice.api.builder.data.worldgen.structure.StructureBuilder;
import io.github.vampirestudios.artifice.api.resource.ArtificeResource;
import io.github.vampirestudios.artifice.api.util.CallableFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceType;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;

/**
 * A resource pack containing Artifice-based resources. May be used
 * as a virtual resource pack with {@link Artifice#registerAssets} or {@link Artifice#registerData}.
 */
@SuppressWarnings({"DeprecatedIsStillUsed"})
public interface ArtificeResourcePack {
	/**
	 * Add a resource at the given path.
	 *
	 * @param id       The resource path.
	 * @param resource The resource to add.
	 */
	void add(Identifier id, ArtificeResource<?> resource);

	/**
	 * Set this pack's display name. Defaults to the pack's ID if not set.
	 *
	 * @param name The desired name.
	 */
	void setDisplayName(String name);

	/**
	 * Set this pack's description.
	 *
	 * @param desc The desired description.
	 */
	void setDescription(String desc);

	/**
	 * Dumps the pack files
	 *
	 * @param filePath The path to dump to
	 */
	@ApiStatus.Internal
	default void dumpResources(String filePath, String type) throws IOException {
		try {
			this.dumpResources(filePath, type, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	default void dump(String filePath, String type) {
		this.dump(filePath, type, true);
	}

	/**
	 * Dumps the pack files
	 *
	 * @param filePath The path to dump to
	 */
	@ApiStatus.Internal
	void dumpResources(String filePath, String type, boolean enableDump) throws IOException;

	void dump(String filePath, String type, boolean enableDump);

	/**
	 * Add an item model for the given item ID.
	 *
	 * @param id An item ID, which will be converted into the correct path.
	 * @param f  A callback which will be passed a {@link ModelBuilder} to create the item model.
	 */
	void addItemModel(Identifier id, ModelBuilder f);

	/**
	 * Add a block model for the given block ID.
	 *
	 * @param id A block ID, which will be converted into the correct path.
	 * @param f  A callback which will be passed a {@link ModelBuilder} to create the block model.
	 */
	void addBlockModel(Identifier id, ModelBuilder f);

	/**
	 * Add a blockstate definition for the given block ID.
	 *
	 * @param id A block ID, which will be converted into the correct path.
	 * @param f  A callback which will be passed a {@link BlockStateBuilder} to create the blockstate definition.
	 */
	void addBlockState(Identifier id, BlockStateBuilder f);

	/**
	 * Add a translation file for the given language.
	 *
	 * @param id The namespace and language code of the desired language.
	 * @param f  A callback which will be passed a {@link TranslationBuilder} to create the language file.
	 */
	void addTranslations(Identifier id, TranslationBuilder f);

//	/**
//	 * Add a particle definition for the given particle ID.
//	 *
//	 * @param id A particle ID, which will be converted into the correct path.
//	 * @param f  A callback which will be passed a {@link ParticleBuilder} to create the particle definition.
//	 */
//	void addParticle(Identifier id, ParticleBuilder f);
//
//	/**
//	 * Add a texture animation for the given item ID.
//	 *
//	 * @param id An item ID, which will be converted into the correct path.
//	 * @param f  A callback which will be passed an {@link AnimationBuilder} to create the texture animation.
//	 */
//	void addItemAnimation(Identifier id, AnimationBuilder f);
//
//	/**
//	 * Add a texture animation for the given block ID.
//	 *
//	 * @param id A block ID, which will be converted into the correct path.
//	 * @param f  A callback which will be passed an {@link AnimationBuilder} to create the texture animation.
//	 */
//	void addBlockAnimation(Identifier id, AnimationBuilder f);


	byte[] addRootResource(String path, byte[] data);

	byte[] addResource(ResourceType type, Identifier path, byte[] data);

	byte[] addAsset(Identifier path, byte[] data);

	byte[] addData(Identifier path, byte[] data);
}
