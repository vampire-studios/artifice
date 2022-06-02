package io.github.vampirestudios.artifice.api.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.util.Processor;

public class JsonObjectBuilder extends TypedJsonObject {
    public JsonObjectBuilder() { super(new JsonObject()); }
    public JsonObjectBuilder(JsonObject root) { super(root); }

	public JsonObjectBuilder(JsonObject root) {
		super(root, j -> j);
	}

	public JsonObjectBuilder add(String name, JsonElement value) {
		root.add(name, value);
		return this;
	}

	public JsonObjectBuilder add(String name, String value) {
		root.addProperty(name, value);
		return this;
	}

	public JsonObjectBuilder add(String name, boolean value) {
		root.addProperty(name, value);
		return this;
	}

	public JsonObjectBuilder add(String name, Number value) {
		root.addProperty(name, value);
		return this;
	}

	public JsonObjectBuilder add(String name, Character value) {
		root.addProperty(name, value);
		return this;
	}

    public JsonObjectBuilder addObject(String name, JsonObjectBuilder settings) {
        root.add(name, settings.build());
		return this;
	}

    public JsonObjectBuilder addArray(String name, jsonArrayBuilder settings) {
        root.add(name, settings.build());
		return this;
	}
}
