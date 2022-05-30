package io.github.vampirestudios.artifice.api.builder.data.worldgen.configured.feature.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.util.Processor;

public class EndSpikeFeatureConfigBuilder extends FeatureConfigBuilder {

    public EndSpikeFeatureConfigBuilder() {
        super();
        this.root.add("spikes", new JsonArray());
    }

    public EndSpikeFeatureConfigBuilder crystalInvulnerable(boolean crystalInvulnerable) {
        this.root.addProperty("crystal_invulnerable", crystalInvulnerable);
        return this;
    }

    public EndSpikeFeatureConfigBuilder crystalBeamTarget(int x, int y, int z) {
        this.root.add("crystal_beam_target", new JsonArray());
        this.root.getAsJsonArray("crystal_beam_target").add(x);
        this.root.getAsJsonArray("crystal_beam_target").add(y);
        this.root.getAsJsonArray("crystal_beam_target").add(z);
        return this;
    }

    public EndSpikeFeatureConfigBuilder addSpike(SpikeBuilder processor) {
        this.root.getAsJsonArray("spikes").add(processor.getData());
        return this;
    }

    public static class SpikeBuilder extends TypedJsonObject {
        public SpikeBuilder() {
            super();
        }

        public SpikeBuilder centerX(int centerX) {
            this.root.addProperty("centerX", centerX);
            return this;
        }

        public SpikeBuilder centerZ(int centerZ) {
            this.root.addProperty("centerZ", centerZ);
            return this;
        }

        public SpikeBuilder radius(int radius) {
            this.root.addProperty("radius", radius);
            return this;
        }

        public SpikeBuilder height(int height) {
            this.root.addProperty("height", height);
            return this;
        }

        public SpikeBuilder guarded(boolean guarded) {
            this.root.addProperty("guarded", guarded);
            return this;
        }
    }
}
