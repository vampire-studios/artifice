package io.github.vampirestudios.artifice.api;

import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
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
import io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.structure.ConfiguredStructureFeatureBuilder;
import io.github.vampirestudios.artifice.api.resource.ArtificeResource;
import io.github.vampirestudios.artifice.api.util.Processor;
import io.github.vampirestudios.artifice.api.virtualpack.ArtificeResourcePackContainer;
import io.github.vampirestudios.artifice.common.ClientOnly;
import io.github.vampirestudios.artifice.common.ClientResourcePackProfileLike;
import io.github.vampirestudios.artifice.common.ServerResourcePackProfileLike;
import io.github.vampirestudios.artifice.impl.ArtificeResourcePackImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.ServerPacksSource;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * A resource pack containing Artifice-based resources. May be used
 * as a virtual resource pack with {@link Artifice#registerAssets} or {@link Artifice#registerData}.
 */
@SuppressWarnings( {"DeprecatedIsStillUsed"})
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
     * Dump all resources from this pack to the given folder path.
     *
     * @param folderPath The path generated resources should go under (relative to Minecraft's installation folder)
     * @throws IOException              if there is an error creating the necessary directories.
     * @throws IllegalArgumentException if the given path points to a file that is not a directory.
     */
    @Deprecated
    void dumpResources(String folderPath) throws IOException;

    /**
     * The pack will be placed on top of all other packs in order to overwrite them, it will not be optional or visible.
     */
    boolean isShouldOverwrite();

    /**
     * Create a client-side {@link Pack} for this pack.
     *
     * @param factory The factory function passed to {@link ServerPacksSource#loadPacks(Consumer, Pack.PackConstructor)}.
     * @return The created container.
     */
    @Override
    @Environment(EnvType.CLIENT)
    default <T extends Pack> ClientOnly<Pack> toClientResourcePackProfile(Pack.PackConstructor factory) {
        return new ClientOnly<>(getAssetsContainer(factory));
    }

    /**
     * Create a server-side {@link Pack} for this pack.
     *
     * @param factory The factory function passed to {@link ServerPacksSource#loadPacks}.
     * @return The created container.
     */
    @Override
    default <T extends Pack> Pack toServerResourcePackProfile(Pack.PackConstructor factory) {
        return getDataContainer(factory);
    }

    /**
     * @param factory The factory function passed to {@link ServerPacksSource#loadPacks(Consumer, Pack.PackConstructor)}.
     * @return The created container.
     * @deprecated use {@link ArtificeResourcePack#toClientResourcePackProfile(Pack.PackConstructor)}
     * Create a client-side {@link Pack} for this pack.
     */
    @Environment(EnvType.CLIENT)
    @Deprecated
    ArtificeResourcePackContainer getAssetsContainer(Pack.PackConstructor factory);

    /**
     * @param factory The factory function passed to {@link ServerPacksSource#loadPacks}.
     * @return The created container.
     * @deprecated use {@link ArtificeResourcePack#toServerResourcePackProfile(Pack.PackConstructor)}
     * Create a server-side {@link Pack} for this pack.
     */
    @Deprecated
    Pack getDataContainer(Pack.PackConstructor factory);

    /**
     * @deprecated  use {@link }
     * Create a new client-side {@link ArtificeResourcePack} and register resources using the given callback.
     *
     * @param register A callback which will be passed a {@link ClientResourcePackBuilder}.
     * @return The created pack.
     */
    @Deprecated
    @Environment(EnvType.CLIENT)
    static ArtificeResourcePack ofAssets(Processor<ClientResourcePackBuilder> register) {
        return new ArtificeResourcePackImpl(PackType.CLIENT_RESOURCES, null, register);
    }

    /**
     * Create a new server-side {@link ArtificeResourcePack} and register resources using the given callback.
     *
     * @param register A callback which will be passed a {@link ServerResourcePackBuilder}.
     * @return The created pack.
     */
    @Deprecated
    static ArtificeResourcePack ofData(Processor<ServerResourcePackBuilder> register) {
        return new ArtificeResourcePackImpl(PackType.SERVER_DATA, null, register);
    }

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
        void dumpResources(String filePath, String type) throws IOException;

        /**
         * Mark this pack as optional (can be disabled in the resource packs menu). Will automatically mark it as visible.
         */
        void setOptional();
        
        void shouldOverwrite();

        <T extends TypedJsonObject> void add(String path, ResourceLocation id, T f);
        <T extends TypedJsonObject> void add(String path, ResourceLocation id, String ext, T f);
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
        @Deprecated
        default void addItemModel(ResourceLocation id, ModelBuilder f) {
            this.addModel("item", id, f);
        }

        /**
         * Add a block model for the given block ID.
         *
         * @param id A block ID, which will be converted into the correct path.
         * @param f  A callback which will be passed a {@link ModelBuilder} to create the block model.
         */
        @Deprecated
        default void addBlockModel(ResourceLocation id, ModelBuilder f) {
            this.addModel("block", id, f);
        }

        default void addModel(String modelType, ResourceLocation id, ModelBuilder f) {
            this.add("models/" + modelType + "/", id, f);
        }

        /**
         * Add a blockstate definition for the given block ID.
         *
         * @param id A block ID, which will be converted into the correct path.
         * @param f  A callback which will be passed a {@link BlockStateBuilder} to create the blockstate definition.
         */
        default void addBlockState(ResourceLocation id, BlockStateBuilder f) {
            this.add("blockstates/", id, f);
        }

        /**
         * Add a translation file for the given language.
         *
         * @param id The namespace and language code of the desired language.
         * @param f  A callback which will be passed a {@link TranslationBuilder} to create the language file.
         */
        default void addTranslations(ResourceLocation id, TranslationBuilder f) {
            this.add("lang/", id, f);
        }

        /**
         * Add a particle definition for the given particle ID.
         *
         * @param id A particle ID, which will be converted into the correct path.
         * @param f  A callback which will be passed a {@link ParticleBuilder} to create the particle definition.
         */
        default void addParticle(ResourceLocation id, ParticleBuilder f) {
            this.add("particles/", id, f);
        }

        /**
         * Add a texture animation for the given item ID.
         *
         * @param id An item ID, which will be converted into the correct path.
         * @param f  A callback which will be passed an {@link AnimationBuilder} to create the texture animation.
         */
        default void addItemAnimation(ResourceLocation id, AnimationBuilder f) {
            this.addAnimation("item", id, f);
        }

        /**
         * Add a texture animation for the given block ID.
         *
         * @param id A block ID, which will be converted into the correct path.
         * @param f  A callback which will be passed an {@link AnimationBuilder} to create the texture animation.
         */
        default void addBlockAnimation(ResourceLocation id, AnimationBuilder f) {
            this.addAnimation("block", id, f);
        }

        default void addAnimation(String type, ResourceLocation id, AnimationBuilder f) {
            this.add(type + "/", id, f);
        }

        /**
         * Add a custom language. Translations must be added separately with {@link ClientResourcePackBuilder#addTranslations}.
         *
         * @param def A {@link LanguageInfo} for the desired language.
         */
        void addLanguage(LanguageInfo def);

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
         * Mark this pack as visible (will be shown in the resource packs menu).
         */
        void setVisible();
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
        default void addAdvancement(ResourceLocation id, AdvancementBuilder f) {
            this.add("advancements/", id, f);
        }

        /**
         * Add a Dimension Type with the given ID.
         *
         * @param id The ID of the dimension type, which will be converted into the correct path.
         * @param f  A callback which will be passed an {@link DimensionTypeBuilder} to create the dimension type.
         */
        default void addDimensionType(ResourceLocation id, DimensionTypeBuilder f) {
            this.add("dimension_type/", id, f);
        }

        /**
         * Add a Dimension with the given ID.
         *
         * @param id The ID of the dimension, which will be converted into the correct path.
         * @param f  A callback which will be passed an {@link DimensionBuilder} to create the dimension.
         */
        default void addDimension(ResourceLocation id, DimensionBuilder f) {
            this.add("dimension/", id, f);
        }

        /**
         * Add a Biome with the given ID.
         *
         * @param id The ID of the biome, which will be converted into the correct path.
         * @param f  A callback which will be passed an {@link BiomeBuilder} to create the biome.
         */
        default void addBiome(ResourceLocation id, BiomeBuilder f) {
            this.add("worldgen/biome/", id, f);
        }

        /**
         * Add a Carver with the given ID.
         *
         * @param id The ID of the carver, which will be converted into the correct path.
         * @param f  A callback which will be passed an {@link ConfiguredCarverBuilder} to create the carver.
         */
        default void addConfiguredCarver(ResourceLocation id, ConfiguredCarverBuilder f) {
            this.add("worldgen/configured_carver/", id, f);
        }

        /**
         * Add a Carver with the given ID.
         *
         * @param id The ID of the carver, which will be converted into the correct path.
         * @param f  A callback which will be passed an {@link ConfiguredStructureFeatureBuilder} to create the carver.
         */
        default void addConfiguredStructureFeature(ResourceLocation id, ConfiguredStructureFeatureBuilder f) {
            this.add("worldgen/configured_structure_feature/", id, f);
        }

        /**
         * Add a Feature with the given ID.
         *
         * @param id The ID of the feature, which will be converted into the correct path.
         * @param f  A callback which will be passed an {@link ConfiguredFeatureBuilder} to create the feature.
         */
        default void addConfiguredFeature(ResourceLocation id, ConfiguredFeatureBuilder f) {
            this.add("worldgen/configured_feature/", id, f);
        }

        /**
         * Add a Feature with the given ID.
         *
         * @param id The ID of the feature, which will be converted into the correct path.
         * @param f  A callback which will be passed an {@link PlacedFeatureBuilder} to create the feature.
         */
        default void addPlacedFeature(ResourceLocation id, PlacedFeatureBuilder f) {
            this.add("worldgen/placed_feature/", id, f);
        }

        /**
         * Add a NoiseSettingsBuilder with the given ID.
         *
         * @param id The ID of the noise settings builder, which will be converted into the correct path.
         * @param f  A callback which will be passed an {@link NoiseSettingsBuilder}
         *           to create the noise settings .
         */
        default void addNoiseSettingsBuilder(ResourceLocation id, NoiseSettingsBuilder f) {
            this.add("worldgen/noise_settings/", id, f);
        }

        /**
         * Add a loot table with the given ID.
         *
         * @param id The ID of the loot table, which will be converted into the correct path.
         * @param f  A callback which will be passed a {@link LootTableBuilder} to create the loot table.
         */
        default void addLootTable(String type, ResourceLocation id, LootTableBuilder f) {
            this.add("loot_tables/" + type + "/", id, f);
        }

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
        default void addTag(String tagType, ResourceLocation id, TagBuilder f) {
            this.add("tags/" + tagType + "/", id, f);
        }

        /**
         * Add a recipe with the given ID.
         *
         * @param id The ID of the recipe, which will be converted into the correct path.
         * @param f  A callback which will be passed a {@link GenericRecipeBuilder} to create the recipe.
         */
        @Deprecated
        default void addGenericRecipe(ResourceLocation id, GenericRecipeBuilder f) {
            this.addRecipe("generic", id, f);
        }

        /**
         * Add a shaped crafting recipe with the given ID.
         *
         * @param id The ID of the recipe, which will be converted into the correct path.
         * @param f  A callback which will be passed a {@link ShapedRecipeBuilder} to create the recipe.
         */
        @Deprecated
        default void addShapedRecipe(ResourceLocation id, ShapedRecipeBuilder f) {
            this.addRecipe("shaped", id, f);
        }

        /**
         * Add a shapeless crafting recipe with the given ID.
         *
         * @param id The ID of the recipe, which will be converted into the correct path.
         * @param f  A callback which will be passed a {@link ShapelessRecipeBuilder} to create the recipe.
         */
        @Deprecated
        default void addShapelessRecipe(ResourceLocation id, ShapelessRecipeBuilder f) {
            this.addRecipe("shapeless", id, f);
        }

        /**
         * Add a stonecutter recipe with the given ID.
         *
         * @param id The ID of the recipe, which will be converted into the correct path.
         * @param f  A callback which will be passed a {@link StonecuttingRecipeBuilder} to create the recipe.
         */
        @Deprecated
        default void addStonecuttingRecipe(ResourceLocation id, StonecuttingRecipeBuilder f) {
            this.addRecipe("stonecutting", id, f);
        }

        /**
         * Add a smelting recipe with the given ID.
         *
         * @param id The ID of the recipe, which will be converted into the correct path.
         * @param f  A callback which will be passed a {@link CookingRecipeBuilder} to create the recipe.
         */
        @Deprecated
        default void addSmeltingRecipe(ResourceLocation id, CookingRecipeBuilder f) {
            this.addRecipe("smelting", id, f);
        }

        /**
         * Add a blast furnace recipe with the given ID.
         *
         * @param id The ID of the recipe, which will be converted into the correct path.
         * @param f  A callback which will be passed a {@link CookingRecipeBuilder} to create the recipe.
         */
        @Deprecated
        default void addBlastingRecipe(ResourceLocation id, CookingRecipeBuilder f) {
            this.addRecipe("blasting", id, f);
        }

        /**
         * Add a smoker recipe with the given ID.
         *
         * @param id The ID of the recipe, which will be converted into the correct path.
         * @param f  A callback which will be passed a {@link CookingRecipeBuilder} to create the recipe.
         */
        @Deprecated
        default void addSmokingRecipe(ResourceLocation id, CookingRecipeBuilder f) {
            this.addRecipe("smoking", id, f);
        }

        /**
         * Add a campfire recipe with the given ID.
         *
         * @param id The ID of the recipe, which will be converted into the correct path.
         * @param f  A callback which will be passed a {@link CookingRecipeBuilder} to create the recipe.
         */
        @Deprecated
        default void addCampfireRecipe(ResourceLocation id, CookingRecipeBuilder f) {
            this.addRecipe("campfire", id, f);
        }

        /**
         * Add a smithing table recipe with the given ID.
         *
         * @param id The ID of the recipe, which will be converted into the correct path.
         * @param f  A callback which will be passed a {@link SmithingRecipeBuilder} to create the recipe.
         */
        @Deprecated
        default void addSmithingRecipe(ResourceLocation id, SmithingRecipeBuilder f) {
            this.addRecipe("smithing", id, f);
        }

        /**
         * Add a recipe with the given ID.
         *
         * @param id The ID of the recipe, which will be converted into the correct path.
         * @param f  A callback which will be passed a {@link CookingRecipeBuilder} to create the recipe.
         */
        default void addRecipe(String recipeType, ResourceLocation id, RecipeBuilder<?> f) {
            if (f.rootFolder != null) {
                this.add("recipes/" + f.rootFolder + "/", id, f.type(new ResourceLocation(recipeType)));
            } else {
                this.add("recipes/" + recipeType + "/", id, f.type(new ResourceLocation(recipeType)));
            }
        }
    }
}
