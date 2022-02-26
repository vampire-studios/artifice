package io.github.vampirestudios.artifice.api.builder.data;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;

public class StateDataBuilder extends TypedJsonBuilder<JsonObject> {

    private final JsonObject jsonObject = new JsonObject();

    public StateDataBuilder() {
        super(new JsonObject(), j->j);
    }

    /**
     * Set the id of the block.
     */
    public StateDataBuilder name(String id) {
        this.root.addProperty("Name", id);
        return this;
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
