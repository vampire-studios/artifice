package io.github.vampirestudios.artifice.api.builder.data;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class StateDataBuilder extends TypedJsonObject {

    private final JsonObject jsonObject = new JsonObject();

    public StateDataBuilder() {
        super(new JsonObject());
    }

    /**
     * Set the id of the block.
     */
    public static StateDataBuilder name(Block block) {
        return name(Registry.BLOCK.getKey(block).toString());
    }

    /**
     * Set the id of the block.
     */
    public static StateDataBuilder name(ResourceLocation id) {
        return name(id.toString());
    }

    /**
     * Set the id of the block.
     */
    public static StateDataBuilder name(String id) {
        StateDataBuilder builder = new StateDataBuilder();
        builder.root.addProperty("Name", id);
        return builder;
    }

    /**
     * Set a property to a state.
     */
    public StateDataBuilder setProperty(String property, String state) {
        this.jsonObject.addProperty(property, state);
        this.root.add("Properties", this.jsonObject);
        return this;
    }
}
