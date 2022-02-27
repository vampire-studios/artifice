package io.github.vampirestudios.artifice.api.builder.data;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;

public class StateDataBuilder extends TypedJsonObject {

    private final JsonObject jsonObject = new JsonObject();

    public StateDataBuilder() {
        super(new JsonObject());
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
