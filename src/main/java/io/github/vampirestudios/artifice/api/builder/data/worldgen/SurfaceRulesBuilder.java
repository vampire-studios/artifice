package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

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

    public SurfaceRulesBuilder condition(Processor<SurfaceRulesBuilder> surfaceRulesBuilder) {
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

}
