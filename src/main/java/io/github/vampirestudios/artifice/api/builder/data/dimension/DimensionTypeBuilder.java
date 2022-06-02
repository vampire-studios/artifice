package io.github.vampirestudios.artifice.api.builder.data.dimension;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.TagResourceLocation;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class DimensionTypeBuilder extends TypedJsonObject {
    public DimensionTypeBuilder() {
        super(new JsonObject());
    }

	/**
	 * Defines if the dimension is ultrawarm like the nether.
	 * When true, makes water not placable and ice will melt.
	 * <p>
	 * Overworld -> false
	 * Nether -> true
	 * End -> false
	 */
	public DimensionTypeBuilder ultrawarm(boolean ultrawarm) {
		root.addProperty("ultrawarm", ultrawarm);
		return this;
	}

	/**
	 * Overworld -> true
	 * Nether -> false
	 * End -> false
	 */
	public DimensionTypeBuilder isNatural(boolean natural) {
		root.addProperty("natural", natural);
		return this;
	}

	/**
	 * Defines how many blocks is one block when travelling between dimensions.
	 * <p>
	 * Overworld -> 1.0D
	 * Nether -> 8.0D (1 block in Overworld is 8 blocks in the Nether)
	 * End -> 1.0D
	 */
	public DimensionTypeBuilder coordinate_scale(double coordinate_scale) {
		if (coordinate_scale < 0.00001)
			throw new IllegalArgumentException("Coordinate scale can't be higher than 0.00001D! Found " + coordinate_scale);
		if (coordinate_scale > 30000000)
			throw new IllegalArgumentException("Coordinate scale can't be higher than 30000000D! Found " + coordinate_scale);
		root.addProperty("coordinate_scale", coordinate_scale);
		return this;
	}

	/**
	 * Overworld -> 0.0
	 * Nether -> 0.1
	 * End -> 0.0
	 */
	public DimensionTypeBuilder ambientLight(float ambientLight) {
		root.addProperty("ambient_light", ambientLight);
		return this;
	}

	/**
	 * Overworld -> true
	 * Nether -> false
	 * End -> false
	 */
	public DimensionTypeBuilder hasSkylight(boolean hasSkylight) {
		root.addProperty("has_skylight", hasSkylight);
		return this;
	}

	/**
	 * Overworld -> false
	 * Nether -> true
	 * End -> false
	 */
	public DimensionTypeBuilder hasCeiling(boolean hasCeiling) {
		root.addProperty("has_ceiling", hasCeiling);
		return this;
	}

	/**
	 * Defines if the dimension can have a dragon fight or not.
	 * <p>
	 * Overworld -> false
	 * Nether -> false
	 * End -> true
	 *
	 * @return this
	 */
	public DimensionTypeBuilder hasEnderDragonFight(boolean hasEnderDragonFight) {
		root.addProperty("has_ender_dragon_fight", hasEnderDragonFight);
		return this;
	}

	/**
	 * A block tag of which the blocks will not stop burning in the dimension.
	 * <p>
	 * Overworld -> #minecraft:infiniburn_overworld
	 * Nether -> #minecraft:infiniburn_nether
	 * End -> #minecraft:infiniburn_end
	 *
	 * @param infiniburn The block tag id.
	 * @return this
	 */
	public DimensionTypeBuilder infiniburn(ResourceLocation infiniburn) {
		root.addProperty("infiniburn", infiniburn.toString());
		return this;
	}

	/**
	 * @param infiniburn The block tag.
	 * @return this
	 */
	public DimensionTypeBuilder infiniburn(TagKey<Block> infiniburn) {
		return infiniburn(new TagResourceLocation(infiniburn.location()));
	}

    /**
     * A list of blocks of which will not stop burning in the dimension.
     *
     * @param infiniburns The block tag id.
     * @return this
     */
    public DimensionTypeBuilder infiniburn(ResourceLocation... infiniburns) {
        this.add("infiniburn", arrayOf(infiniburns));
        return this;
    }

	/**
	 * Defines the bottom position of the dimension
	 * <p>
	 * Overworld -> -64
	 * Nether -> 0
	 * End -> 0
	 */
	public DimensionTypeBuilder minimumY(int minimumY) {
		root.addProperty("min_y", minimumY);
		return this;
	}

	/**
	 * Defines the full height of the dimension.
	 * <p>
	 * Overworld -> 384
	 * Nether -> 128
	 * End -> 256
	 */
	public DimensionTypeBuilder height(int height) {
		if (height < 16) throw new IllegalArgumentException("Height can't be lower than 16! Found " + height);
		if (height > 2048) throw new IllegalArgumentException("Height can't be higher than 2048! Found " + height);
		root.addProperty("height", height);
		return this;
	}

	/**
	 * Overworld -> 256
	 * Nether -> 128
	 * End -> 256
	 */
	public DimensionTypeBuilder logicalHeight(int logicalHeight) {
		if (logicalHeight < 0)
			throw new IllegalArgumentException("Height can't be lower than 0! Found " + logicalHeight);
		if (logicalHeight > 2048)
			throw new IllegalArgumentException("Height can't be higher than 2048! Found " + logicalHeight);
		root.addProperty("logical_height", logicalHeight);
		return this;
	}

	/**
	 * This sets the time to a specific time like in the nether and end.
	 * Do not use this if you want a day/night cycle.
	 * <p>
	 * Nether -> 18000
	 * End -> 6000
	 *
	 * @param fixedTime Time of the days in ticks
	 * @return this
	 */
	public DimensionTypeBuilder fixedTime(long fixedTime) {
		root.addProperty("fixed_time", fixedTime);
		return this;
	}

	/**
	 * Defines if raids can start in this dimension or not.
	 * <p>
	 * Overworld -> true
	 * Nether -> false
	 * End -> true
	 */
	public DimensionTypeBuilder hasRaids(boolean hasRaids) {
		root.addProperty("has_raids", hasRaids);
		return this;
	}

	/**
	 * Defines if Respawn Anchors should work or if they should explode when used.
	 * <p>
	 * Overworld -> false
	 * Nether -> true
	 * End -> false
	 */
	public DimensionTypeBuilder respawnAnchorWorks(boolean respawnAnchorWork) {
		root.addProperty("respawn_anchor_works", respawnAnchorWork);
		return this;
	}

	/**
	 * Defines if beds work like in the overworld or like in the nether/end.
	 * <p>
	 * Overworld -> true
	 * Nether -> false
	 * End -> false
	 */
	public DimensionTypeBuilder bedWorks(boolean bedWorks) {
		root.addProperty("bed_works", bedWorks);
		return this;
	}

	/**
	 * Defines if piglins will convert to zombified or not.
	 * <p>
	 * Overworld -> false
	 * Nether -> true
	 * End -> false
	 */
	public DimensionTypeBuilder piglinSafe(boolean piglinSafe) {
		root.addProperty("piglin_safe", piglinSafe);
		return this;
	}

	/**
	 * Effects determine the sky effect of the dimension.
	 * <p>
	 * Overworld -> minecraft:overworld
	 * Nether -> minecraft:the_nether
	 * End -> minecraft:the_end
	 *
	 * @param effects thing
	 * @return this
	 */
	public DimensionTypeBuilder effects(String effects) {
		root.addProperty("effects", effects);
		return this;
	}
}
