package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.NoiseConfigBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

import java.util.function.Function;

public class SurfaceRulesBuilder extends TypedJsonBuilder<JsonObject> {

    public SurfaceRulesBuilder() {
        super(new JsonObject(), j->j);
    }

    //maybe this should be less verbose considering surface rules are literally just a ton of unique entries nested in these
    public SurfaceRulesBuilder sequence(Processor<SurfaceRulesBuilder>... surfaceRulesBuilder) {
        jsonArray("sequence", jsonArrayBuilder -> {
            for (Processor<SurfaceRulesBuilder> rule : surfaceRulesBuilder) {
                jsonArrayBuilder.add(rule.process(new SurfaceRulesBuilder()).build());
            }
        });
        root.addProperty("type","minecraft:sequence");
        return this;
    }

    public SurfaceRulesBuilder condition(Processor<ConditionalRuleBuilder> conditionalRuleBuilder) {
        root.addProperty("type","minecraft:condition");
        return this;
    }

    public SurfaceRulesBuilder verticalGradient(Processor<SurfaceRulesBuilder> surfaceRulesBuilder) {
        root.addProperty("type","minecraft:vertical_gradient");
        return this;
    }

    public SurfaceRulesBuilder resultState(Processor<SurfaceRulesBuilder> surfaceRulesBuilder) {
        root.addProperty("type","minecraft:result_state");
        return this;
    }

    public class ConditionalRuleBuilder extends TypedJsonBuilder<JsonObject>{

        protected ConditionalRuleBuilder() {
            super(new JsonObject(), j->j);
        }

        public SurfaceRulesBuilder.ConditionalRuleBuilder ifTrue(Processor<SurfaceRulesBuilder> surfaceRulesBuilder) {
            this.with("if_true", JsonObject::new, jsonObject -> surfaceRulesBuilder.process(new SurfaceRulesBuilder()).buildTo(jsonObject));
            return this;
        }

        public SurfaceRulesBuilder.ConditionalRuleBuilder thenRun(Processor<SurfaceRulesBuilder> surfaceRulesBuilder) {
            this.with("then_run", JsonObject::new, jsonObject -> surfaceRulesBuilder.process(new SurfaceRulesBuilder()).buildTo(jsonObject));
            return this;
        }
    }

}
