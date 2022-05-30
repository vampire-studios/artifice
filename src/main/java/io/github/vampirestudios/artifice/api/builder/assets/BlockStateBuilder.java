package io.github.vampirestudios.artifice.api.builder.assets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.JsonObjectBuilder;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import io.github.vampirestudios.artifice.api.util.Processor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

/**
 * Builder for a blockstate definition file ({@code namespace:blockstates/blockid.json}).
 * @see <a href="https://minecraft.gamepedia.com/Model#Block_states" target="_blank">Minecraft Wiki</a>
 */
@Environment(EnvType.CLIENT)
public final class BlockStateBuilder extends TypedJsonObject {
    public BlockStateBuilder() { super(new JsonObject()); }

    /**
     * Add a variant for the given state key.
     * Calling this multiple times for the same key will modify the existing value.
     * {@code variant} and {@code multipart} are incompatible; calling this will remove any existing {@code multipart} definitions.
     *
     * @param name The state key ({@code ""} for default or format: {@code "prop1=value,prop2=value"}).
     * @param settings A callback which will be passed a {@link Variant}.
     * @return this
     */
    public BlockStateBuilder variant(String name, Variant settings) {
        root.remove("multipart");
        join("variants", new TypedJsonObject().add(name,arrayOf(settings)).getData());
        return this;
    }

    /**
     * Add a variant for the given state key, with multiple weighted random options.
     * Calling this multiple times for the same key will add to the list instead of overwriting.
     * {@code variant} and {@code multipart} are incompatible; calling this will remove any existing {@code multipart} definitions.
     *
     * @param name The state key ({@code ""} for default or format: {@code "prop1=value,prop2=value"}).
     * @param settings A callback which will be passed a {@link Variant}.
     * @return this
     */
    public BlockStateBuilder weightedVariant(String name, Variant settings) {
        root.remove("multipart");
        if(getObj("variants") != null && getObj("variants").has(name)) join(getObj("variants"), name, settings.getData());
        else join("variants", new TypedJsonObject().add(name,arrayOf(settings)).getData());
        return this;
    }

    /**
     * Add a multipart case.
     * Calling this multiple times will add to the list instead of overwriting.
     * {@code variant} and {@code multipart} are incompatible; calling this will remove any existing {@code variant} definitions.
     *
     * @param settings A callback which will be passed a {@link Case}.
     * @return this
     */
    public BlockStateBuilder multipartCase(Case settings) {
        root.remove("variants");
        join("multipart", arrayOf(settings) );
        return this;
    }

    /**
     * Builder for a blockstate variant definition.
     * @see BlockStateBuilder
     */
    @Environment(EnvType.CLIENT)
    public static final class Variant extends TypedJsonObject {
        public Variant() { super(new JsonObject()); }
        private Variant(JsonObject root) { super(root); }

        /**
         * Set the model this variant should use.
         * @param id The model ID ({@code namespace:block|item/modelid}).
         * @return this
         */
        public Variant model(ResourceLocation id) {
            root.addProperty("model", id.toString());
            return this;
        }

        /**
         * Set the rotation of this variant around the X axis in increments of 90deg.
         * @param x The X axis rotation.
         * @return this
         * @throws IllegalArgumentException if {@code x} is not divisible by 90.
         */
        public Variant rotationX(int x) {
            if(x % 90 != 0) throw new IllegalArgumentException("X rotation must be in increments of 90");
            root.addProperty("x", x);
            return this;
        }

        /**
         * Set the rotation of this variant around the Y axis in increments of 90deg.
         * @param y The Y axis rotation.
         * @return this
         * @throws IllegalArgumentException if {@code y} is not divisible by 90.
         */
        public Variant rotationY(int y) {
            if(y % 90 != 0) throw new IllegalArgumentException("Y rotation must be in increments of 90");
            root.addProperty("y", y);
            return this;
        }

        /**
         * Set whether the textures of this model should not rotate with it.
         * @param uvlock Whether to lock texture rotation or not.
         * @return this
         */
        public Variant uvlock(boolean uvlock) {
            root.addProperty("uvlock", uvlock);
            return this;
        }

        /**
         * Set the relative weight of this variant.
         * This property will be ignored if the variant is not added with {@link BlockStateBuilder#weightedVariant}
         *  or {@link Case#weightedApply}.
         * @param weight The weight.
         * @return this
         */
        public Variant weight(int weight) {
            root.addProperty("weight", weight);
            return this;
        }
    }

    /**
     * Builder for a blockstate multipart case.
     * @see BlockStateBuilder
     */
    @Environment(EnvType.CLIENT)
    public static final class Case extends TypedJsonObject {
        private Case() { super(new JsonObject()); }

        /**
         * Set the condition for this case to be applied.
         * Calling this multiple times with different keys will require all of the specified properties to match.
         * @param name The state name (e.g. {@code facing}).
         * @param state The state value (e.g. {@code north}).
         * @return this
         */
        public Case when(String name, String state) {
            join("when", new TypedJsonObject().add(name, state).getData());
            JsonObject condit = this.getObj("when");
            if(condit.has("OR")){
                JsonObject or = condit.getAsJsonObject("OR");
                for (Map.Entry<String, JsonElement> a : condit.entrySet()) {
                    condit.add(a.getKey(), a.getValue());
                    or.remove(a.getKey());
                }
                condit.remove("OR");
            }
            return this;
        }

        /**
         * Set the condition for this case to be applied.
         * Calling this multiple times with different keys will require at least one of the specified properties to match.
         * @param name The state name (e.g. {@code facing}).
         * @param state The state value (e.g. {@code north}).
         * @return this
         */
        public Case whenAny(String name, String state) {
            JsonObject condit = this.getObj("when");
            if(condit != null && !condit.has("OR")){
                TypedJsonObject or = new TypedJsonObject();
                for (Map.Entry<String, JsonElement> a : condit.entrySet()) {
                    or.add(a.getKey(),a.getValue());
                    condit.remove(a.getKey());
                }
                condit.add("OR", or.getData());
            }
            join("when", new TypedJsonObject().add(name, state).getData());
            return this;
        }

        /**
         * Set the variant to be applied if the condition matches.
         * Calling this multiple times for the same key will overwrite the existing value.
         * @param settings A callback which will be passed a {@link Variant}.
         * @return this
         */
        public Case apply(Variant settings) {
            root.add("apply", settings.getData());
            return this;
        }

        /**
         * Set the variant to be applied if the condition matches, with multiple weighted random options.
         * Calling this multiple times will add to the list instead of overwriting.
         * @param settings A {@link Variant}.
         * @return this
         */
        public Case weightedApply(Variant settings) {
            join("apply", settings.getData());
            return this;
        }
    }
}
