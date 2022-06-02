package io.github.vampirestudios.artifice.api.builder.data.dimension;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;

public class StructureManagerBuilder extends TypedJsonBuilder<JsonObject> {
	public StructureManagerBuilder() {
		super(new JsonObject(), j -> j);
		this.root.add("structures", new JsonObject());
	}

	/**
	 * Build stronghold settings.
	 */
	public StructureManagerBuilder strongholdSettings(StrongholdSettingsBuilder strongholdSettingsBuilder) {
		with("stronghold", JsonObject::new, strongholdSettingsBuilder::buildTo);
		return this;
	}

	/**
	 * Add structure.
	 */
	public StructureManagerBuilder addStructure(String structureId, StructureConfigBuilder structureConfigBuilder) {
		with(this.root.getAsJsonObject("structures"), structureId, JsonObject::new, structureConfigBuilder::buildTo);
		return this;
	}


	public static class StrongholdSettingsBuilder extends TypedJsonBuilder<JsonObject> {

		protected StrongholdSettingsBuilder() {
			super(new JsonObject(), j -> j);
		}

		public StrongholdSettingsBuilder distance(int distance) {
			if (distance > 1023)
				throw new IllegalArgumentException("Distance can't be higher than 1023! Found " + distance);
			if (distance < 0) throw new IllegalArgumentException("Distance can't be smaller than 0! Found " + distance);
			this.root.addProperty("distance", distance);
			return this;
		}

		public StrongholdSettingsBuilder spread(int spread) {
			if (spread > 1023) throw new IllegalArgumentException("Spread can't be higher than 1023! Found " + spread);
			if (spread < 0) throw new IllegalArgumentException("Spread can't be smaller than 0! Found " + spread);
			this.root.addProperty("spread", spread);
			return this;
		}

		/**
		 * Set the number of stronghold in the dimension.
		 */
		public StrongholdSettingsBuilder count(int count) {
			if (count > 4095) throw new IllegalArgumentException("Count can't be higher than 4095! Found " + count);
			if (count < 1) throw new IllegalArgumentException("Count can't be smaller than 1! Found " + count);
			this.root.addProperty("count", count);
			return this;
		}
	}

    public static class StructureConfigBuilder extends TypedJsonObject {

        protected StructureConfigBuilder() {
            super();
        }

		public StructureConfigBuilder spacing(int spacing) {
			if (spacing > 4096) throw new IllegalArgumentException("Count can't be higher than 4096! Found " + spacing);
			if (spacing < 0) throw new IllegalArgumentException("Count can't be smaller than 0! Found " + spacing);
			this.root.addProperty("spacing", spacing);
			return this;
		}

		public StructureConfigBuilder separation(int separation) {
			if (separation > 4096)
				throw new IllegalArgumentException("Count can't be higher than 4096! Found " + separation);
			if (separation < 0)
				throw new IllegalArgumentException("Count can't be smaller than 0! Found " + separation);
			this.root.addProperty("separation", separation);
			return this;
		}

		public StructureConfigBuilder salt(int salt) {
			if (salt < 0) throw new IllegalArgumentException("Count can't be smaller than 0! Found " + salt);
			this.root.addProperty("salt", salt);
			return this;
		}
	}
}
