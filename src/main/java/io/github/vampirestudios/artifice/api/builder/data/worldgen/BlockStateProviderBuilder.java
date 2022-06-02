package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;

public class BlockStateProviderBuilder extends TypedJsonObject {

    public BlockStateProviderBuilder() {
        super(new JsonObject());
    }

    public <P extends BlockStateProviderBuilder> P type(String type) {
        this.root.addProperty("type", type);
        return (P)this;
    }
    public static SimpleBlockStateProviderBuilder simpleProvider(StateDataBuilder processor){
        return new SimpleBlockStateProviderBuilder().state(processor);
    }

	public static class SimpleBlockStateProviderBuilder extends BlockStateProviderBuilder {
		public SimpleBlockStateProviderBuilder() {
			super();
			this.type("minecraft:simple_state_provider");
		}

        public SimpleBlockStateProviderBuilder state(StateDataBuilder processor) {
            join("state", processor.build());
            return this;
        }
    }

	public static class WeightedBlockStateProviderBuilder extends BlockStateProviderBuilder {
		public WeightedBlockStateProviderBuilder() {
			super();
			this.type("minecraft:weighted_state_provider");
			this.root.add("entries", new JsonArray());
		}

        public WeightedBlockStateProviderBuilder addEntry(int weight, StateDataBuilder processor) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("weight", weight);
            jsonObject.add("data", processor.build());
            this.root.getAsJsonArray("entries").add(jsonObject);
            return this;
        }
    }

	public static class PlainFlowerBlockStateProviderBuilder extends BlockStateProviderBuilder {
		public PlainFlowerBlockStateProviderBuilder() {
			super();
			this.type("minecraft:plain_flower_provider");
		}
	}

	public static class ForestFlowerBlockStateProviderBuilder extends BlockStateProviderBuilder {
		public ForestFlowerBlockStateProviderBuilder() {
			super();
			this.type("minecraft:forest_flower_provider");
		}
	}

	public static class PillarBlockStateProviderBuilder extends BlockStateProviderBuilder {
		public PillarBlockStateProviderBuilder() {
			super();
			this.type("minecraft:pillar_state_provider");
		}

        public PillarBlockStateProviderBuilder state(StateDataBuilder processor) {
            join("state", processor.build());
            return this;
        }
    }
}
