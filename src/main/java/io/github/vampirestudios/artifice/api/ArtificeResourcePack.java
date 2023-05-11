package io.github.vampirestudios.artifice.api;

import com.mojang.blaze3d.platform.NativeImage;
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
import io.github.vampirestudios.artifice.api.virtualpack.ArtificeResourcePackContainer;
import io.github.vampirestudios.artifice.common.ClientOnly;
import io.github.vampirestudios.artifice.common.ClientResourcePackProfileLike;
import io.github.vampirestudios.artifice.common.ServerResourcePackProfileLike;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
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
public interface ArtificeResourcePack extends PackResources, ServerResourcePackProfileLike, ClientResourcePackProfileLike {
	/**
	 * @return The {@link PackType} this pack contains.
	 */
	PackType getType();

	/**
	 * @return Whether this pack is set as optional
	 */
	boolean isOptional();

	/**
	 * @return Whether this pack is set as visible in the resource packs menu (only relevant for client-side packs)
	 */
	boolean isVisible();

	/**
	 * The pack will be placed on top of all other packs in order to overwrite them, it will not be optional or visible.
	 */
	boolean isShouldOverwrite();

	/**
	 * Create a client-side {@link Pack} for this pack.
	 *
	 * @return The created container.
	 */
	@Override
	@Environment(EnvType.CLIENT)
	default ClientOnly<Pack> toClientResourcePackProfile() {
		return new ClientOnly<>(getAssetsContainer());
	}

	/**
	 * Create a server-side {@link Pack} for this pack.
	 *
	 * @return The created container.
	 */
	@Override
	default Pack toServerResourcePackProfile() {
		return getDataContainer();
	}

	/**
	 * @return The created container.
	 * @deprecated use {@link ArtificeResourcePack#toClientResourcePackProfile()}
	 * Create a client-side {@link Pack} for this pack.
	 */
	@Environment(EnvType.CLIENT)
	@Deprecated
	ArtificeResourcePackContainer getAssetsContainer();

	/**
	 * @return The created container.
	 * @deprecated use {@link ArtificeResourcePack#toServerResourcePackProfile()}
	 * Create a server-side {@link Pack} for this pack.
	 */
	@Deprecated
	Pack getDataContainer();

	/**
	 * Passed to resource construction callbacks to register resources.
	 */
	interface ResourcePackBuilder {
		/**
		 * Add a resource at the given path.
		 *
		 * @param id       The resource path.
		 * @param resource The resource to add.
		 */
		void add(ResourceLocation id, ArtificeResource<?> resource);

		/**
		 * Set this pack's display name. Defaults to the pack's ID if not set.
		 *
		 * @param name The desired name.
		 */
		@Deprecated
		default void setDisplayName(String name) {
			this.setDisplayName(Component.literal(name));
		}

		/**
		 * Set this pack's display name. Defaults to the pack's ID if not set.
		 *
		 * @param title The desired name.
		 */
		void setDisplayName(Component title);

		/**
		 * Set this pack's description.
		 *
		 * @param desc The desired description.
		 */
		@Deprecated
		default void setDescription(String desc) {
			this.setDescription(Component.literal(desc));
		}

		/**
		 * Set this pack's description.
		 *
		 * @param desc The desired description.
		 */
		void setDescription(Component desc);

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
		 * Mark this pack as optional (can be disabled in the resource packs menu). Will automatically mark it as visible.
		 */
		void setOptional();

		void shouldOverwrite();
	}

	/**
	 * Passed to resource construction callbacks to register client-side resources.
	 */
	@Environment(EnvType.CLIENT)
	interface ClientResourcePackBuilder extends ResourcePackBuilder {
		/**
		 * Add an item model for the given item ID.
		 *
		 * @param id An item ID, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link ModelBuilder} to create the item model.
		 */
		void addItemModel(ResourceLocation id, ModelBuilder f);

		/**
		 * Add a block model for the given block ID.
		 *
		 * @param id A block ID, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link ModelBuilder} to create the block model.
		 */
		void addBlockModel(ResourceLocation id, ModelBuilder f);

		/**
		 * Add a blockstate definition for the given block ID.
		 *
		 * @param id A block ID, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link BlockStateBuilder} to create the blockstate definition.
		 */
		void addBlockState(ResourceLocation id, BlockStateBuilder f);

		/**
		 * Add a translation file for the given language.
		 *
		 * @param id The namespace and language code of the desired language.
		 * @param f  A callback which will be passed a {@link TranslationBuilder} to create the language file.
		 */
		void addTranslations(ResourceLocation id, TranslationBuilder f);

		/**
		 * Add a particle definition for the given particle ID.
		 *
		 * @param id A particle ID, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link ParticleBuilder} to create the particle definition.
		 */
		void addParticle(ResourceLocation id, ParticleBuilder f);

		/**
		 * Add a texture animation for the given item ID.
		 *
		 * @param id An item ID, which will be converted into the correct path.
		 * @param f  A callback which will be passed an {@link AnimationBuilder} to create the texture animation.
		 */
		void addItemAnimation(ResourceLocation id, AnimationBuilder f);

		/**
		 * Add a texture animation for the given block ID.
		 *
		 * @param id A block ID, which will be converted into the correct path.
		 * @param f  A callback which will be passed an {@link AnimationBuilder} to create the texture animation.
		 */
		void addBlockAnimation(ResourceLocation id, AnimationBuilder f);

		/**
		 * Add a custom language. Translations must be added separately with {@link ClientResourcePackBuilder#addTranslations}.
		 *
		 * @param def A {@link LanguageInfo} for the desired language.
		 */
		void addLanguage(ExpandedLanguageInfo def);

		/**
		 * Add a custom language. Translations must be added separately with {@link ClientResourcePackBuilder#addTranslations}.
		 *
		 * @param code   The language code for the custom language (i.e. {@code en_us}). Must be all lowercase alphanum / underscores.
		 * @param region The name of the language region (i.e. United States).
		 * @param name   The name of the language (i.e. English).
		 * @param rtl    Whether the language is written right-to-left instead of left-to-right.
		 */
		void addLanguage(String code, String region, String name, boolean rtl);

		/**
		 * Adds a texture png
		 * <p>
		 * ".png" is automatically appended to the path
		 */
		void addTexture(ResourceLocation id, NativeImage image);

		/**
		 * Reads, clones, and recolors the texture at the given path, and puts the newly created image in the given id.
		 *
		 * <b>if your resource pack is registered at a higher priority than where you expect the texture to be in, mc will
		 * be unable to find the asset you are looking for</b>
		 *
		 * @param id     the place to put the new texture
		 * @param target the input stream of the original texture
		 * @param pixel  the pixel recolorer
		 */
		void addRecoloredImage(ResourceLocation id, InputStream target, IntUnaryOperator pixel);

		/**
		 * Mark this pack as visible (will be shown in the resource packs menu).
		 */
		void setVisible();

		/**
		 * adds an async resource, this is evaluated off-thread, this does not hold all resource retrieval unlike
		 *
		 * @see #async(Consumer)
		 */
		Future<byte[]> addAsyncResource(PackType type, ResourceLocation identifier, CallableFunction<ResourceLocation, byte[]> data);

		void addLazyResource(PackType type, ResourceLocation path, BiFunction<ArtificeResourcePack, ResourceLocation, byte[]> func);

		/**
		 * adds an async resource, this is evaluated off-thread, this does not hold all resource retrieval unlike
		 *
		 * @see #async(Consumer)
		 */
		Future<byte[]> addAsyncRootResource(String path, CallableFunction<String, byte[]> data);

		void addLazyRootResource(String path, BiFunction<ArtificeResourcePack, String, byte[]> data);

		byte[] addRootResource(String path, byte[] data);

		byte[] addResource(PackType type, ResourceLocation path, byte[] data);

		byte[] addAsset(ResourceLocation path, byte[] data);

		byte[] addData(ResourceLocation path, byte[] data);
	}

	/**
	 * Passed to resource construction callbacks to register server-side resources.
	 */
	interface ServerResourcePackBuilder extends ResourcePackBuilder {
		/**
		 * Add an advancement with the given ID.
		 *
		 * @param id The ID of the advancement, which will be converted into the correct path.
		 * @param f  A callback which will be passed an {@link AdvancementBuilder} to create the advancement.
		 */
		void addAdvancement(ResourceLocation id, AdvancementBuilder f);

		/**
		 * Add a Dimension Type with the given ID.
		 *
		 * @param id The ID of the dimension type, which will be converted into the correct path.
		 * @param f  A callback which will be passed an {@link DimensionTypeBuilder} to create the dimension type.
		 */
		void addDimensionType(ResourceLocation id, DimensionTypeBuilder f);

		/**
		 * Add a Dimension with the given ID.
		 *
		 * @param id The ID of the dimension, which will be converted into the correct path.
		 * @param f  A callback which will be passed an {@link DimensionBuilder} to create the dimension.
		 */
		void addDimension(ResourceLocation id, DimensionBuilder f);

		/**
		 * Add a Biome with the given ID.
		 *
		 * @param id The ID of the biome, which will be converted into the correct path.
		 * @param f  A callback which will be passed an {@link BiomeBuilder} to create the biome.
		 */
		void addBiome(ResourceLocation id, BiomeBuilder f);

		/**
		 * Add a Carver with the given ID.
		 *
		 * @param id The ID of the carver, which will be converted into the correct path.
		 * @param f  A callback which will be passed an {@link ConfiguredCarverBuilder} to create the carver.
		 */
		void addConfiguredCarver(ResourceLocation id, ConfiguredCarverBuilder f);

		/**
		 * Add a Carver with the given ID.
		 *
		 * @param id The ID of the carver, which will be converted into the correct path.
		 * @param f  A callback which will be passed an {@link StructureBuilder} to create the carver.
		 */
		void addStructure(ResourceLocation id, StructureBuilder f);

		/**
		 * Add a Feature with the given ID.
		 *
		 * @param id The ID of the feature, which will be converted into the correct path.
		 * @param f  A callback which will be passed an {@link ConfiguredFeatureBuilder} to create the feature.
		 */
		void addConfiguredFeature(ResourceLocation id, ConfiguredFeatureBuilder f);

		/**
		 * Add a Feature with the given ID.
		 *
		 * @param id The ID of the feature, which will be converted into the correct path.
		 * @param f  A callback which will be passed an {@link PlacedFeatureBuilder} to create the feature.
		 */
		void addPlacedFeature(ResourceLocation id, PlacedFeatureBuilder f);

		/**
		 * Add a NoiseSettingsBuilder with the given ID.
		 *
		 * @param id The ID of the noise settings builder, which will be converted into the correct path.
		 * @param f  A callback which will be passed an {@link NoiseSettingsBuilder}
		 *           to create the noise settings .
		 */
		void addNoiseSettingsBuilder(ResourceLocation id, NoiseSettingsBuilder f);

		/**
		 * Add a loot table with the given ID.
		 *
		 * @param id The ID of the loot table, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link LootTableBuilder} to create the loot table.
		 */
		void addLootTable(ResourceLocation id, LootTableBuilder f);

		/**
		 * Add an item tag with the given ID.
		 *
		 * @param id The ID of the tag, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link TagBuilder} to create the tag.
		 */
		@Deprecated
		default void addItemTag(ResourceLocation id, TagBuilder f) {
			addTag("item", id, f);
		}

		/**
		 * Add a block tag with the given ID.
		 *
		 * @param id The ID of the tag, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link TagBuilder} to create the tag.
		 */
		@Deprecated
		default void addBlockTag(ResourceLocation id, TagBuilder f) {
			addTag("block", id, f);
		}

		/**
		 * Add an entity type tag with the given ID.
		 *
		 * @param id The ID of the tag, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link TagBuilder} to create the tag.
		 */
		@Deprecated
		default void addEntityTypeTag(ResourceLocation id, TagBuilder f) {
			addTag("entity_type", id, f);
		}

		/**
		 * Add a fluid tag with the given ID.
		 *
		 * @param id The ID of the tag, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link TagBuilder} to create the tag.
		 */
		@Deprecated
		default void addFluidTag(ResourceLocation id, TagBuilder f) {
			addTag("fluid", id, f);
		}

		/**
		 * Add a function tag with the given ID.
		 *
		 * @param id The ID of the tag, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link TagBuilder} to create the tag.
		 */
		@Deprecated
		default void addFunctionTag(ResourceLocation id, TagBuilder f) {
			addTag("function", id, f);
		}

		/**
		 * Add a tag with the given ID.
		 *
		 * @param tagType The type of tag you want
		 * @param id      The ID of the tag, which will be converted into the correct path.
		 * @param f       A callback which will be passed a {@link TagBuilder} to create the tag.
		 */
		void addTag(String tagType, ResourceLocation id, TagBuilder f);

		/**
		 * Add a recipe with the given ID.
		 *
		 * @param id The ID of the recipe, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link GenericRecipeBuilder} to create the recipe.
		 */
		void addGenericRecipe(ResourceLocation id, GenericRecipeBuilder f);

		/**
		 * Add a shaped crafting recipe with the given ID.
		 *
		 * @param id The ID of the recipe, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link ShapedRecipeBuilder} to create the recipe.
		 */
		void addShapedRecipe(ResourceLocation id, ShapedRecipeBuilder f);

		/**
		 * Add a shapeless crafting recipe with the given ID.
		 *
		 * @param id The ID of the recipe, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link ShapelessRecipeBuilder} to create the recipe.
		 */
		void addShapelessRecipe(ResourceLocation id, ShapelessRecipeBuilder f);

		/**
		 * Add a stonecutter recipe with the given ID.
		 *
		 * @param id The ID of the recipe, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link StonecuttingRecipeBuilder} to create the recipe.
		 */
		void addStonecuttingRecipe(ResourceLocation id, StonecuttingRecipeBuilder f);

		/**
		 * Add a smelting recipe with the given ID.
		 *
		 * @param id The ID of the recipe, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link CookingRecipeBuilder} to create the recipe.
		 */
		void addSmeltingRecipe(ResourceLocation id, CookingRecipeBuilder f);

		/**
		 * Add a blast furnace recipe with the given ID.
		 *
		 * @param id The ID of the recipe, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link CookingRecipeBuilder} to create the recipe.
		 */
		void addBlastingRecipe(ResourceLocation id, CookingRecipeBuilder f);

		/**
		 * Add a smoker recipe with the given ID.
		 *
		 * @param id The ID of the recipe, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link CookingRecipeBuilder} to create the recipe.
		 */
		void addSmokingRecipe(ResourceLocation id, CookingRecipeBuilder f);

		/**
		 * Add a campfire recipe with the given ID.
		 *
		 * @param id The ID of the recipe, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link CookingRecipeBuilder} to create the recipe.
		 */
		void addCampfireRecipe(ResourceLocation id, CookingRecipeBuilder f);

		/**
		 * Add a smithing table recipe with the given ID.
		 *
		 * @param id The ID of the recipe, which will be converted into the correct path.
		 * @param f  A callback which will be passed a {@link CookingRecipeBuilder} to create the recipe.
		 */
		void addSmithingRecipe(ResourceLocation id, SmithingRecipeBuilder f);
	}

	/**
	 * invokes the action on the RRP executor, RRPs are thread-safe you can create expensive assets here, all resources
	 * are blocked until all async tasks are completed invokes the action on the RRP executor, RRPs are thread-safe you
	 * can create expensive assets here, all resources are blocked until all async tasks are completed
	 * <p>
	 * calling an this function from itself will result in a infinite loop
	 *
	 * @see ClientResourcePackBuilder#addAsyncResource(PackType, ResourceLocation, CallableFunction)
	 */
	Future<?> async(Consumer<ArtificeResourcePack> action);
}
