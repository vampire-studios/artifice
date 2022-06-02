package io.github.vampirestudios.artifice.api.builder.data.worldgen.biome;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;

public class BiomeEffectsBuilder extends TypedJsonObject {

    public BiomeEffectsBuilder() {
        super(new JsonObject());
    }

    /**
     * @param fog_color RGB value.
     * @return BiomeBuilder
     */
    public BiomeEffectsBuilder fogColor(int fog_color) {
        this.root.addProperty("fog_color", fog_color);
        return this;
    }

    /**
     * @param water_color RGB value.
     * @return BiomeBuilder
     */
    public BiomeEffectsBuilder waterColor(int water_color) {
        this.root.addProperty("water_color", water_color);
        return this;
    }

    /**
     * @param sky_color RGB value.
     * @return BiomeBuilder
     */
    public BiomeEffectsBuilder skyColor(int sky_color) {
        this.root.addProperty("sky_color", sky_color);
        return this;
    }

    /**
     * @param foliage_color RGB value.
     * @return BiomeBuilder
     */
    public BiomeEffectsBuilder foliageColor(int foliage_color) {
        this.root.addProperty("foliage_color", foliage_color);
        return this;
    }

    /**
     * @param grass_color RGB value.
     * @return BiomeBuilder
     */
    public BiomeEffectsBuilder grassColor(int grass_color) {
        this.root.addProperty("grass_color", grass_color);
        return this;
    }

    /**
     * @param grass_color_modifier RGB value.
     * @return BiomeBuilder
     */
    public BiomeEffectsBuilder grassColorModifier(int grass_color_modifier) {
        this.root.addProperty("grass_color_modifier", grass_color_modifier);
        return this;
    }

    /**
     * @param water_fog_color RGB value.
     * @return BiomeBuilder
     */
    public BiomeEffectsBuilder waterFogColor(int water_fog_color) {
        this.root.addProperty("water_fog_color", water_fog_color);
        return this;
    }

    public BiomeEffectsBuilder ambientSound(String soundID) {
        this.root.addProperty("ambient_sound", soundID);
        return this;
    }

    public BiomeEffectsBuilder moodSound(BiomeMoodSoundBuilder biomeMoodSoundBuilder) {
        join("mood_sound", biomeMoodSoundBuilder.build());
        return this;
    }

    public BiomeEffectsBuilder additionsSound(BiomeAdditionsSoundBuilder biomeAdditionsSoundBuilder) {
        join("additions_sound", biomeAdditionsSoundBuilder.build());
        return this;
    }

    public BiomeEffectsBuilder music(BiomeMusicSoundBuilder biomeMusicSoundBuilder) {
        join("music", biomeMusicSoundBuilder.build());
        return this;
    }

    public BiomeEffectsBuilder particle(BiomeParticleConfigBuilder biomeParticleConfigBuilder) {
        join("particle", biomeParticleConfigBuilder.build());
        return this;
    }

    public static class BiomeMoodSoundBuilder extends TypedJsonObject {

        public BiomeMoodSoundBuilder() {
            super(new JsonObject());
        }

        public BiomeMoodSoundBuilder tickDelay(int tick_delay) {
            this.root.addProperty("tick_delay", tick_delay);
            return this;
        }

        public BiomeMoodSoundBuilder blockSearchExtent(int block_search_extent) {
            this.root.addProperty("block_search_extent", block_search_extent);
            return this;
        }

        public BiomeMoodSoundBuilder offset(double offset) {
            this.root.addProperty("offset", offset);
            return this;
        }

        public BiomeMoodSoundBuilder soundID(String soundID) {
            this.root.addProperty("sound", soundID);
            return this;
        }
    }

    public static class BiomeMusicSoundBuilder extends TypedJsonObject {

        public BiomeMusicSoundBuilder() {
            super(new JsonObject());
        }

        public BiomeMusicSoundBuilder minDelay(int min_delay) {
            this.root.addProperty("min_delay", min_delay);
            return this;
        }

        public BiomeMusicSoundBuilder maxDelay(int max_delay) {
            this.root.addProperty("max_delay", max_delay);
            return this;
        }

        public BiomeMusicSoundBuilder replaceCurrentMusic(boolean replace_current_music) {
            this.root.addProperty("replace_current_music", replace_current_music);
            return this;
        }

        public BiomeMusicSoundBuilder soundID(String soundID) {
            this.root.addProperty("sound", soundID);
            return this;
        }
    }

    public static class BiomeAdditionsSoundBuilder extends TypedJsonObject {

        public BiomeAdditionsSoundBuilder() {
            super(new JsonObject());
        }

        public BiomeAdditionsSoundBuilder tickChance(double tick_chance) {
            this.root.addProperty("tick_chance", tick_chance);
            return this;
        }

        public BiomeAdditionsSoundBuilder soundID(String soundID) {
            this.root.addProperty("sound", soundID);
            return this;
        }
    }

    public static class BiomeParticleConfigBuilder extends TypedJsonObject {

        public BiomeParticleConfigBuilder() {
            super(new JsonObject());
        }

        public BiomeParticleConfigBuilder probability(float probability) {
            this.root.addProperty("probability", probability);
            return this;
        }

        public BiomeParticleConfigBuilder particleID(String id) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", id);
            this.root.add("options", jsonObject);
            return this;
        }

    }
}
