package io.github.vampirestudios.artifice.api.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.function.Supplier;

public abstract class TypedJsonObject {
    protected final JsonObject root;

    public TypedJsonObject() {
        this(new JsonObject());
    }
    public TypedJsonObject(JsonObject root) {
        this.root = root;
    }

    protected  <J extends JsonElement> void with(JsonObject in, String key, J merge) {
        in.add(key, in.has(key) ? (J)in.get(key) : ctor.get()); }


    public InputStream toInputStream() {
        return new ByteArrayInputStream(this.root.toString().getBytes());
    }

}
