package io.github.vampirestudios.artifice.api.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import io.github.vampirestudios.artifice.api.util.Processor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Supplier;

public abstract class TypedJsonObject {
    protected final JsonObject root;

    public TypedJsonObject() {
        this(new JsonObject());
    }
    public TypedJsonObject(JsonObject root) {
        this.root = root;
    }

    public JsonObject getData(){
        return this.root;
    }

    protected void join(JsonObject in, String key, JsonObject value) {
        in.add(key, in.has(key) ? merge(value, (JsonObject)in.get(key)) : value); }
    protected void join(String key, JsonObject value) {
        join(this.root, key, value); }

    protected void join(JsonObject in, String key, JsonArray value) {
        in.add(key, in.has(key) ?  merge((JsonArray)in.get(key), value) :  value ); }
    protected void join(String key, JsonArray value) {
        join(this.root, key, value); }


    protected JsonObject merge(JsonObject target) {
        return merge(target, this.root);
    }

    protected JsonObject merge(JsonObject target, JsonObject source) {
        for (Map.Entry<String,JsonElement> ele : source.entrySet()) {
            target.add(ele.getKey(), ele.getValue());
        }
        return target;
    }

    protected JsonArray merge(JsonArray source, JsonArray values) {
        source.addAll(values);
        return source;
    }

    protected JsonArray arrayOf(TypedJsonObject... values){
        JsonArray array = new JsonArray();
        for(TypedJsonObject entry : values) array.add(entry.getData());
        return array;
    }

    protected JsonArray arrayOf(boolean... values) {
        JsonArray array = new JsonArray();
        for(boolean i : values) array.add(i);
        return array;
    }

    protected JsonArray arrayOf(Character... values) {
        JsonArray array = new JsonArray();
        for(Character i : values) array.add(i);
        return array;
    }

    protected JsonArray arrayOf(Number... values) {
        JsonArray array = new JsonArray();
        for(Number i : values) array.add(i);
        return array;
    }

    protected JsonArray arrayOf(String... values) {
        JsonArray array = new JsonArray();
        for(String i : values) array.add(i);
        return array;
    }

    public InputStream toInputStream() {
        return new ByteArrayInputStream(this.root.toString().getBytes());
    }

}
