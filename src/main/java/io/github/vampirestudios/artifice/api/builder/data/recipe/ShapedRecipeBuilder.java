package io.github.vampirestudios.artifice.api.builder.data.recipe;

import io.github.vampirestudios.artifice.api.builder.JsonObjectBuilder;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import net.minecraft.resources.ResourceLocation;

/**
 * Builder for a shaped crafting recipe ({@code namespace:recipes/id.json}).
 *
 * @see <a href="https://minecraft.gamepedia.com/Recipe#JSON_format" target="_blank">Minecraft Wiki</a>
 */
public final class ShapedRecipeBuilder extends RecipeBuilder<ShapedRecipeBuilder> {
	public ShapedRecipeBuilder() {
		super();
		type(new ResourceLocation("crafting_shaped"));
	}

	/**
	 * Set the recipe pattern for this recipe.
	 * Each character of the given strings should correspond to a key registered for an ingredient.
	 *
	 * @param rows The individual rows of the pattern.
	 * @return this
	 */
	public ShapedRecipeBuilder pattern(String... rows) {
		root.add("pattern", arrayOf(rows));
		return this;
	}

    /**
     * Add an ingredient item.
     * @param key The key in the recipe pattern corresponding to this ingredient.
     * @param id The item ID.
     * @return this
     */
    public ShapedRecipeBuilder ingredientItem(Character key, ResourceLocation id) {
        join("key", new TypedJsonObject().add(key.toString(), new TypedJsonObject().add("item", id.toString()).build()).build());
        return this;
    }

    /**
     * Add an ingredient item as any of the given tag.
     * @param key The key in the recipe pattern corresponding to this ingredient.
     * @param id The tag ID.
     * @return this
     */
    public ShapedRecipeBuilder ingredientTag(Character key, ResourceLocation id) {
        join("key", new TypedJsonObject().add(key.toString(), new TypedJsonObject().add("tag", id.toString()).build()).build());
        return this;
    }

    /**
     * Add an ingredient item as one of a list of options.
     * @param key The key in the recipe pattern corresponding to this ingredient.
     * @param settings A callback which will be passed a {@link MultiIngredientBuilder}.
     * @return this
     */
    public ShapedRecipeBuilder multiIngredient(Character key, MultiIngredientBuilder settings) {
        join("key", new TypedJsonObject().add(key.toString(), settings.build()).build());
        return this;
    }

	/**
	 * Set the item produced by this recipe.
	 *
	 * @param id    The item ID.
	 * @param count The number of result items.
	 * @return this
	 */
	public ShapedRecipeBuilder result(ResourceLocation id, int count) {
		root.add("result", new JsonObjectBuilder().add("item", id.toString()).add("count", count).build());
		return this;
	}
}
