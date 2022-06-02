package io.github.vampirestudios.artifice.api.builder.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import net.minecraft.resources.ResourceLocation;

/**
 * Builder for tag files ({@code namespace:tags/type/tagid.json}).
 *
 * @see <a href="https://minecraft.gamepedia.com/Tag" target="_blank">Minecraft Wiki</a>
 */
public final class TagBuilder extends TypedJsonObject {
    public TagBuilder() { super(new JsonObject()); }

	/**
	 * Set whether this tag should override or append to versions of the same tag in lower priority data packs.
	 *
	 * @param replace Whether to replace.
	 * @return this
	 */
	public TagBuilder replace(boolean replace) {
		root.addProperty("replace", replace);
		return this;
	}

    /**
     * Add a value to this tag.
     * @param id The value ID.
     * @return this
     */
    public TagBuilder value(ResourceLocation id) {
            join("values", arrayOf(id.toString()) );
        return this;
    }

    /**
     * Add multiple values to this tag.
     * @param ids The value IDs.
     * @return this
     */
    public TagBuilder values(ResourceLocation... ids) {
        JsonArray array = new JsonArray();
        for(ResourceLocation id : ids){
            array.add(id.toString());
        }
        join("values", array);
        return this;
    }

    /**
     * Include another tag into this tag's values.
     * @param tagId The tag ID.
     * @return this
     */
    public TagBuilder include(ResourceLocation tagId) {
        join("values", arrayOf("#"+tagId.toString()));
        return this;
    }
}
