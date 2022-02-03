package io.github.vampirestudios.artifice.api.resource;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/** A virtual resource representing a JSON file.
 *  @param <T> The specific type of {@link JsonElement} contained in this file (usually {@link com.google.gson.JsonObject JsonObject}) */
public class JsonResource<T extends JsonElement> implements ArtificeResource<T> {
    private final T root;
    /** @param root The {@link JsonElement} this resource should wrap. */
    public JsonResource(T root) { this.root = root; }

    public T getData() { return this.root; }
    public String toOutputString() { return new GsonBuilder().setPrettyPrinting().create().toJson(root); }
    public InputStream toInputStream() {
        return new ByteArrayInputStream(this.getData().toString().getBytes());
    }
}
