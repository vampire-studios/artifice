package io.github.vampirestudios.artifice.api.builder.data;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;

public class RuleTestBuilder extends TypedJsonObject {

    public RuleTestBuilder() {
        super(new JsonObject());
    }

    public <R extends RuleTestBuilder> R predicateType(String type) {
        this.root.addProperty("predicate_type", type);
        return (R) this;
    }

    public static class AlwaysTrueRuleTestBuilder extends RuleTestBuilder {

        public AlwaysTrueRuleTestBuilder() {
            super();
            this.predicateType("minecraft:always_true");
        }
    }

    public static class BlockRuleTestBuilder extends RuleTestBuilder {

        public BlockRuleTestBuilder() {
            super();
            this.predicateType("minecraft:block_match");
        }

        public BlockRuleTestBuilder block(String blockID) {
            this.root.addProperty("block", blockID);
            return this;
        }
    }

    public static class BlockStateRuleTestBuilder extends RuleTestBuilder {

        public BlockStateRuleTestBuilder() {
            super();
            this.predicateType("minecraft:blockstate_match");
        }

        public BlockStateRuleTestBuilder blockState(StateDataBuilder processor) {
            join("block_state", processor.build());
            return this;
        }
    }

    public static class TagMatchRuleTestBuilder extends RuleTestBuilder {

        public TagMatchRuleTestBuilder() {
            super();
            this.predicateType("minecraft:tag_match");
        }

        public TagMatchRuleTestBuilder tag(String tagID) {
            this.root.addProperty("tag", tagID);
            return this;
        }
    }

    public static class RandomBlockMatchRuleTestBuilder extends RuleTestBuilder {

        public RandomBlockMatchRuleTestBuilder() {
            super();
            this.predicateType("minecraft:random_block_match");
        }

        public RandomBlockMatchRuleTestBuilder block(String blockID) {
            this.root.addProperty("block", blockID);
            return this;
        }

        public RandomBlockMatchRuleTestBuilder probability(float probability) {
            this.root.addProperty("probability", probability);
            return this;
        }
    }

    public static class RandomBlockStateMatchRuleTestBuilder extends RuleTestBuilder {

        public RandomBlockStateMatchRuleTestBuilder() {
            super();
            this.predicateType("minecraft:random_block_match");
        }

        public RandomBlockStateMatchRuleTestBuilder blockState(StateDataBuilder processor) {
            join("block_state", processor.build());
            return this;
        }

        public RandomBlockStateMatchRuleTestBuilder probability(float probability) {
            this.root.addProperty("probability", probability);
            return this;
        }
    }
}
