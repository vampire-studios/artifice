package io.github.vampirestudios.artifice.api.builder.data.worldgen;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.builder.data.StateDataBuilder;
import io.github.vampirestudios.artifice.api.builder.data.dimension.NoiseConfigBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

import java.util.Map;
import java.util.function.Function;

public class SurfaceRulesBuilder extends TypedJsonBuilder<JsonObject> {

    public SurfaceRulesBuilder() {
        super(new JsonObject(), j->j);
    }

    //maybe this should be less verbose considering surface rules are literally just a ton of unique entries nested in sequences and conditionals
    public SurfaceRulesBuilder sequence(Processor<SurfaceRulesBuilder>... ruleBuilders) {
        jsonArray("sequence", jsonArrayBuilder -> {
            for (Processor<SurfaceRulesBuilder> rule : ruleBuilders) {
                jsonArrayBuilder.add(rule.process(new SurfaceRulesBuilder()).build());
            }
        });
        root.addProperty("type","minecraft:sequence");
        return this;
    }

    public SurfaceRulesBuilder condition(Processor<SurfaceRulesBuilder> ifTrue, Processor<SurfaceRulesBuilder> theRun) {
        this.with("if_true", JsonObject::new, jsonObject -> ifTrue.process(new SurfaceRulesBuilder()).buildTo(jsonObject));
        this.with("then_run", JsonObject::new, jsonObject -> theRun.process(new SurfaceRulesBuilder()).buildTo(jsonObject));
        root.addProperty("type","minecraft:condition");
        return this;
    }

    public SurfaceRulesBuilder bandlands() {
        root.addProperty("type","minecraft:bandlands");
        return this;
    }

    public SurfaceRulesBuilder block(Processor<StateDataBuilder> blockStateBuilderProcessor) {
        with("result_state", JsonObject::new, jsonObject -> blockStateBuilderProcessor.process(new StateDataBuilder()).buildTo(jsonObject));
        root.addProperty("type","minecraft:block");
        return this;
    }

    public SurfaceRulesBuilder verticalGradient(String randomName,Map.Entry<String, Integer> trueAtAndBelow, Map.Entry<String, Integer> falseAtAndAbove) {
        root.addProperty("random_name",randomName);
        this.with("true_at_and_below", JsonObject::new, jsonObject -> jsonObject.addProperty(trueAtAndBelow.getKey(),trueAtAndBelow.getValue()));
        this.with("false_at_and_above", JsonObject::new, jsonObject -> jsonObject.addProperty(falseAtAndAbove.getKey(),falseAtAndAbove.getValue()));
        root.addProperty("type","minecraft:vertical_gradient");
        return this;
    }

    public SurfaceRulesBuilder not(Processor<SurfaceRulesBuilder> rule) {
        this.with("invert", JsonObject::new, jsonObject -> rule.process(new SurfaceRulesBuilder()).buildTo(jsonObject));
        root.addProperty("type","minecraft:not");
        return this;
    }

    public SurfaceRulesBuilder biome(String... biomes) {
        jsonArray("biome_is", jsonArrayBuilder -> {
            for (String biome : biomes) {
                jsonArrayBuilder.add(biome);
            }
        });
        root.addProperty("type","minecraft:biome");
        return this;
    }

    public SurfaceRulesBuilder yAbove(Map.Entry<String, Integer> yPos, int surfaceDepthMultiplier, boolean addStoneDepth) {
        this.with("anchor", JsonObject::new, jsonObject -> jsonObject.addProperty(yPos.getKey(),yPos.getValue()));
        root.addProperty("surface_depth_multiplier",surfaceDepthMultiplier);
        root.addProperty("add_stone_depth",addStoneDepth);
        root.addProperty("type","minecraft:y_above");
        return this;
    }

    public SurfaceRulesBuilder water(int offset, int surfaceDepthMultiplier, boolean addStoneDepth) {
        root.addProperty("offset",offset);
        root.addProperty("surface_depth_multiplier",surfaceDepthMultiplier);
        root.addProperty("add_stone_depth",addStoneDepth);
        root.addProperty("type","minecraft:water");
        return this;
    }

    public SurfaceRulesBuilder noiseThreshold(String noiseId, double min, double max) {
        root.addProperty("noise",noiseId);
        root.addProperty("min_threshold",min);
        root.addProperty("max_threshold",max);
        root.addProperty("type","minecraft:noise_threshold");
        return this;
    }

    public SurfaceRulesBuilder stoneDepth(String surfaceType, int offset, boolean addSurfaceDepth, boolean addSurfaceSecondaryDepth) {
        root.addProperty("surface_type",surfaceType);
        root.addProperty("offset",offset);
        root.addProperty("add_Surface_depth",addSurfaceDepth);
        root.addProperty("add_surface_secondary_depth",addSurfaceSecondaryDepth);
        root.addProperty("type","minecraft:stone_depth");
        return this;
    }

    public SurfaceRulesBuilder hole() {
        root.addProperty("type","minecraft:hole");
        return this;
    }

    public SurfaceRulesBuilder steep() {
        root.addProperty("type","minecraft:steep");
        return this;
    }

    public SurfaceRulesBuilder temperature() {
        root.addProperty("type","minecraft:temperature");
        return this;
    }

    public SurfaceRulesBuilder aboveMainSurface() {
        root.addProperty("type","minecraft:above_preliminary_surface");
        return this;
    }

}
