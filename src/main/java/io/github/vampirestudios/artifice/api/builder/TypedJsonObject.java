package io.github.vampirestudios.artifice.api.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

public class TypedJsonObject {
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
    public JsonObject buildCopy(){
        return merge(new JsonObject());
    }


    protected void join(JsonObject in, String key, JsonObject value) {
        in.add(key, in.has(key) ? merge(value, (JsonObject)in.get(key)) : value); }
    protected void join(String key, JsonObject value) {
        join(this.root, key, value); }

    protected void join(JsonObject in, String key, JsonArray value) {
        in.add(key, in.has(key) ?  merge((JsonArray)in.get(key), value) :  value ); }
    protected void join(String key, JsonArray value) {
        join(this.root, key, value); }


    public JsonObject merge(JsonObject target) {
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

    public static JsonArray arrayOf(TypedJsonObject... values){
        JsonArray array = new JsonArray();
        for(TypedJsonObject i : values) array.add(i.getData());
        return array;
    }

    public static JsonArray arrayOf(JsonObject... values){
        JsonArray array = new JsonArray();
        for(JsonObject i : values) array.add(i);
        return array;
    }

    public static JsonArray arrayOf(JsonElement... values){
        JsonArray array = new JsonArray();
        for(JsonElement i : values) array.add(i);
        return array;
    }

    public static JsonArray arrayOf(boolean... values) {
        JsonArray array = new JsonArray();
        for(boolean i : values) array.add(i);
        return array;
    }

    public static JsonArray arrayOf(Character... values) {
        JsonArray array = new JsonArray();
        for(Character i : values) array.add(i);
        return array;
    }

    public static JsonArray arrayOf(float... values) {
        JsonArray array = new JsonArray();
        for(Number i : values) array.add(i);
        return array;
    }

    public static JsonArray arrayOf(int... values) {
        JsonArray array = new JsonArray();
        for(Number i : values) array.add(i);
        return array;
    }

    public static JsonArray arrayOf(long... values) {
        JsonArray array = new JsonArray();
        for(Number i : values) array.add(i);
        return array;
    }

    public static JsonArray arrayOf(double... values) {
        JsonArray array = new JsonArray();
        for(Number i : values) array.add(i);
        return array;
    }
    public static JsonArray arrayOf(String... values) {
        JsonArray array = new JsonArray();
        for(String i : values) array.add(i);
        return array;
    }

    public static JsonArray arrayOf(ResourceLocation... values) {
        JsonArray array = new JsonArray();
        for(ResourceLocation i : values) array.add(i.toString());
        return array;
    }

    public TypedJsonObject add(String key, TypedJsonObject value){
        this.root.add(key,value.getData());
        return this;
    }

    public TypedJsonObject add(String key, JsonElement value){
        this.root.add(key,value);
        return this;
    }

    public TypedJsonObject add(String key, String value){
        this.root.addProperty(key,value);
        return this;
    }

    public TypedJsonObject add(String key, Number value){
        this.root.addProperty(key,value);
        return this;
    }

    public TypedJsonObject add(String key, Boolean value){
        this.root.addProperty(key,value);
        return this;
    }

    public TypedJsonObject add(String key, Character value){
        this.root.addProperty(key,value);
        return this;
    }

    public JsonElement get(String key){
        return this.root.get(key);
    }

    @Nullable
    public JsonObject getObj(String key){
        JsonObject aa = null;
        if(this.root.has(key)){
            try{
                aa = this.root.get(key).getAsJsonObject();
            } catch (IllegalStateException e){
                aa = null;
            }
        }
        return aa;
    }

    @Nullable
    public JsonArray getArray(String key){
        JsonArray aa = null;
        if(this.root.has(key)){
            try{
                aa = this.root.get(key).getAsJsonArray();
            } catch (IllegalStateException e){
                aa = null;
            }
        }
        return aa;
    }

    public InputStream toInputStream() {
        return new ByteArrayInputStream(this.root.toString().getBytes());
    }

}
