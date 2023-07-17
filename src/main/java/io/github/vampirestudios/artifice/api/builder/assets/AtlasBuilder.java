package io.github.vampirestudios.artifice.api.builder.assets;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.Locale;

/**
 * Builder for a atlas file ({@code namespace:atlases/"atlas name".json}).
 * @see <a href="https://minecraft.gamepedia.com/Resource_pack#Animation" target="_blank">Minecraft Wiki</a>
 */
@Environment(EnvType.CLIENT)
public final class AtlasBuilder extends TypedJsonObject {
    public AtlasBuilder() {
        super(new JsonObject());
    }

    /**
     * Set whether this animation should interpolate between frames with a frametime &gt; 1 between them.
     * @param sources Whether to interpolate (default: false).
     * @return this
     */
    public AtlasBuilder sources(Source... sources) {
        root.add("sources", arrayOf(sources));
        return this;
    }

    /**
     * Builder for the `frames` property of a texture animation file.
     * @see AtlasBuilder
     */
    @Environment(EnvType.CLIENT)
    public static final class Source extends TypedJsonObject {
        private Source() {}

        public static Source singleFile(Identifier resource) {
            return new Source()
                    .type(SourceType.SINGLE_FILE)
                    .resource(resource);
        }

        public static Source directory(String source, String prefix) {
            return new Source()
                    .type(SourceType.DIRECTORY)
                    .source(source)
                    .prefix(prefix);
        }

//        public static Source filter(Identifier resourceLocationPattern) {
//            return new Source()
//                    .type(SourceType.FILTER)
//                    .source(source)
//                    .prefix(prefix);
//        }

        public static Source unsticher(String source, String prefix) {
            return new Source()
                    .type(SourceType.UNSTITCHER)
                    .source(source)
                    .prefix(prefix);
        }

        /**
         * Sets the source type for this specific source.
         * @param sourceType The type this source is
         * @return this
         */
        public Source type(SourceType sourceType) {
            root.addProperty("type", sourceType.toString().toLowerCase(Locale.ROOT));
            return this;
        }

        /**
         * Sets the resource for this specific source if source type is set to SINGLE_FILE
         * @param resource the path for the resource
         * @return this
         */
        public Source resource(Identifier resource) {
            root.addProperty("resource", resource.toString());
            return this;
        }

        /**
         * Sets the resource for this specific source if source type is set to SINGLE_FILE
         * @param resource the path for the resource
         * @return this
         */
        public Source sprite(Identifier resource) {
            root.addProperty("resource", resource.toString());
            return this;
        }

        /**
         * Sets the source for this specific source if source type is set to DIRECTORY
         * @param source the path for the resource
         * @return this
         */
        public Source source(String source) {
            root.addProperty("source", source);
            return this;
        }

        /**
         * Sets the prefix for this specific source if source type is set to DIRECTORY
         * @param prefix the path for the resource
         * @return this
         */
        public Source prefix(String prefix) {
            root.addProperty("prefix", prefix);
            return this;
        }

        public enum SourceType {
            SINGLE_FILE,
            DIRECTORY,
            FILTER,
            UNSTITCHER
        }
    }
}
