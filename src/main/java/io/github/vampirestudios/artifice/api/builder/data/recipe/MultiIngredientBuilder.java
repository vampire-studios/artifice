package io.github.vampirestudios.artifice.api.builder.data.recipe;

import com.google.gson.JsonArray;
import io.github.vampirestudios.artifice.api.builder.JsonObjectBuilder;
import net.minecraft.resources.ResourceLocation;

/**
 * Bulder for a recipe ingredient option list.
 *
 * @see CookingRecipeBuilder
 * @see ShapedRecipeBuilder
 * @see ShapelessRecipeBuilder
 * @see StonecuttingRecipeBuilder
 */
public final class MultiIngredientBuilder {
	private final JsonArray ingredients = new JsonArray();

	public MultiIngredientBuilder() {
	}

	/**
	 * Add an item as an option.
	 *
	 * @param id The item ID.
	 * @return this
	 */
	public MultiIngredientBuilder item(ResourceLocation id) {
		ingredients.add(new JsonObjectBuilder().add("item", id.toString()).build());
		return this;
	}

	/**
	 * Add all items from the given tag as options.
	 *
	 * @param id The tag ID.
	 * @return this
	 */
	public MultiIngredientBuilder tag(ResourceLocation id) {
		ingredients.add(new JsonObjectBuilder().add("tag", id.toString()).build());
		return this;
	}

	public JsonArray build() {
		return ingredients;
	}
}
