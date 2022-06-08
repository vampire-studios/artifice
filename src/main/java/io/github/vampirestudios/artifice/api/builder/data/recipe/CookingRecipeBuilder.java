package io.github.vampirestudios.artifice.api.builder.data.recipe;

import io.github.vampirestudios.artifice.api.builder.JsonObjectBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

/**
 * Builder for cooking recipes ({@code namespace:recipes/id.json}).
 * Used for all types of cooking (smelting, blasting, smoking, campfire_cooking).
 * @see <a href="https://minecraft.gamepedia.com/Recipe#JSON_format" target="_blank">Minecraft Wiki</a>
 */
public final class CookingRecipeBuilder extends RecipeBuilder<CookingRecipeBuilder> {
    /**
     * Set the item being cooked.
     * @param id The item ID.
     * @return this
     */
    public CookingRecipeBuilder ingredientItem(ResourceLocation id) {
        root.add("ingredient", new JsonObjectBuilder().add("item", id.toString()).build());
        return this;
    }

    /**
     * Set the item being cooked.
     * @param id The item ID.
     * @return this
     */
    public CookingRecipeBuilder ingredientItem(ItemLike id) {
        root.add("ingredient", new JsonObjectBuilder().add("item", Registry.ITEM.getKey(id.asItem()).toString()).build());
        return this;
    }

    /**
     * Set the item being cooked as any of the given tag.
     * @param id The tag ID.
     * @return this
     */
    public CookingRecipeBuilder ingredientTag(ResourceLocation id) {
        root.add("ingredient", new JsonObjectBuilder().add("tag", id.toString()).build());
        return this;
    }

    /**
     * Set the item being cooked.
     * @param id The item ID.
     * @return this
     */
    public CookingRecipeBuilder ingredientItem(TagKey<Item> id) {
        root.add("ingredient", new JsonObjectBuilder().add("item", id.location().toString()).build());
        return this;
    }

    /**
     * Set the item being cooked as one of a list of options.
     * @param settings A callback which will be passed a {@link MultiIngredientBuilder}.
     * @return this
     */
    public CookingRecipeBuilder multiIngredient(MultiIngredientBuilder settings) {
        root.add("ingredient", settings.build());
        return this;
    }

    /**
     * Set the item produced by this recipe.
     * @param id The item ID.
     * @return this
     */
    public CookingRecipeBuilder result(ResourceLocation id) {
        root.addProperty("result", id.toString());
        return this;
    }

    /**
     * Set the item produced by this recipe.
     * @param id The item ID.
     * @return this
     */
    public CookingRecipeBuilder result(ItemLike id) {
        root.addProperty("result", Registry.ITEM.getKey(id.asItem()).toString());
        return this;
    }

    /**
     * Set the amount of experience given by this recipe.
     * @param exp The amount of experience.
     * @return this
     */
    public CookingRecipeBuilder experience(double exp) {
        root.addProperty("experience", exp);
        return this;
    }

    /**
     * Set how long this recipe should take to complete in ticks.
     * @param time The number of ticks.
     * @return this
     */
    public CookingRecipeBuilder cookingTime(int time) {
        root.addProperty("cookingtime", time);
        return this;
    }
}
