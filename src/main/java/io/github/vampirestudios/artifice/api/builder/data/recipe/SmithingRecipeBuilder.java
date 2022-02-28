package io.github.vampirestudios.artifice.api.builder.data.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.vampirestudios.artifice.api.builder.JsonObjectBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;
import net.minecraft.resources.ResourceLocation;

/**
 * Builder for a smithing recipe ({@code namespace:recipes/id.json}).
 * @see <a href="https://minecraft.gamepedia.com/Recipe#JSON_format" target="_blank">Minecraft Wiki</a>
 */
public class SmithingRecipeBuilder extends RecipeBuilder<SmithingRecipeBuilder> {
    public SmithingRecipeBuilder() {
        super();
        type(new ResourceLocation("smithing"));
    }

    /**
     * Set the item being smithed
     * @param id The item ID
     * @return this
     * */
    public SmithingRecipeBuilder baseItem(ResourceLocation id) {
        root.add("base", item(id));
        return this;
    }

    /**
     * Set the item being smithed to be any one of the given tag
     * @param id The tag ID
     * @return this
     * */
    public SmithingRecipeBuilder baseTag(ResourceLocation id) {
        root.add("base", tag(id));
        return this;
    }

    /**
     * Set the item being smithed as one of a list of options.
     * @param settings A callback which will be passed a {@link MultiIngredientBuilder}.
     * @return this
     */
    public SmithingRecipeBuilder multiBase(Processor<MultiIngredientBuilder> settings) {
        root.add("base", settings.process(new MultiIngredientBuilder()).build());
        return this;
    }

    /**
     * Set the item to be added on during the smithing
     * @param id The item ID
     * @return this
     * */
    public SmithingRecipeBuilder additionItem(ResourceLocation id) {
        root.add("addition", item(id));
        return this;
    }

    /**
     * Set the item to be added on to be any one of the given tag
     * @param id The ta ID
     * @return this
     * */
    public SmithingRecipeBuilder additionTag(ResourceLocation id) {
        root.add("addition", tag(id));
        return this;
    }

    /**
     * Set the item being added on as one of a list of options.
     * @param settings A callback which will be passed a {@link MultiIngredientBuilder}.
     * @return this
     */
    public SmithingRecipeBuilder multiAddition(Processor<MultiIngredientBuilder> settings) {
        root.add("addition", settings.process(new MultiIngredientBuilder()).build());
        return this;
    }

    /**
     * Set the result of the smithing.
     * Item NBT will be preserved.
     * @param id The ID of the resulting item
     * @return this
     * */
    public SmithingRecipeBuilder result(ResourceLocation id) {
        root.add("result", new JsonPrimitive(id.toString()));
        return this;
    }

    private JsonObject item(ResourceLocation id) {
        return new JsonObjectBuilder().add("item", id.toString()).getData();
    }

    private JsonObject tag(ResourceLocation id) {
        return new JsonObjectBuilder().add("tag", id.toString()).getData();
    }
}
