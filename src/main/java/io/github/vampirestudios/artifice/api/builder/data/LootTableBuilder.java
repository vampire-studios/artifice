package io.github.vampirestudios.artifice.api.builder.data;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.JsonObjectBuilder;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import net.minecraft.resources.ResourceLocation;

/**
 * Builder for loot table files ({@code namespace:loot_tables/type/lootid.json}).
 *
 * @see <a href="https://minecraft.gamepedia.com/Loot_table" target="_blank">Minecraft Wiki</a>
 */
public final class LootTableBuilder extends TypedJsonObject {
	public LootTableBuilder() {
		super(new JsonObject());
	}

	/**
	 * Set the type of this loot table.
	 *
	 * @param id The type ID.
	 * @return this
	 */
	public LootTableBuilder type(ResourceLocation id) {
		root.addProperty("type", id.toString());
		return this;
	}

	/**
	 * Add a pool to this loot table.
	 *
	 * @param settings A callback which will be passed a {@link Pool}.
	 * @return this
	 */
	public LootTableBuilder pool(Pool settings) {
		join("pools", arrayOf(settings));
		return this;
	}

	/**
	 * Builder for loot table pools.
	 *
	 * @see LootTableBuilder
	 */
	public static final class Pool extends TypedJsonObject {
		public Pool() {
			super();
		}

		/**
		 * Add an entry to this pool.
		 *
		 * @param settings A callback which will be passed an {@link Entry}.
		 * @return this {@link Pool}
		 */
		public Pool entry(Entry settings) {
			join("entries", arrayOf(settings));
			return this;
		}

		/**
		 * Add a condition to this pool. All conditions must pass for the pool to be used.
		 * The specific properties of this vary by condition, so this falls through to direct JSON building.
		 *
		 * @param id       The condition ID.
		 * @param settings A {@link TypedJsonObject}.
		 * @return this {@link Pool}
		 * @see <a href="https://minecraft.gamepedia.com/Loot_table#Conditions" target="_blank">Minecraft Wiki</a>
		 */
		public Pool condition(ResourceLocation id, TypedJsonObject settings) {
			join("conditions", arrayOf(settings.add("condition", id.toString())));
			return this;
		}

		/**
		 * Set the number of rolls to apply this pool for.
		 *
		 * @param rolls The number of rolls.
		 * @return this {@link Pool}
		 */
		public Pool rolls(int rolls) {
			root.addProperty("rolls", rolls);
			return this;
		}

		/**
		 * Set the number of rolls to apply this pool for as a range from which to randomly select a number.
		 *
		 * @param min The minimum number of rolls (inclusive).
		 * @param max The maximum number of rolls (inclusive).
		 * @return this
		 */
		public Pool rolls(int min, int max) {
			root.add("rolls", new JsonObjectBuilder().add("min", min).add("max", max).build());
			return this;
		}

		/**
		 * Set the number of bonus rolls to apply this pool for per point of luck.
		 *
		 * @param rolls The number of rolls.
		 * @return this
		 */
		public Pool bonusRolls(float rolls) {
			root.addProperty("bonus_rolls", rolls);
			return this;
		}

		/**
		 * Set the number of bonus rolls to apply this pool for per point of luck as a range from which to randomly select a number.
		 *
		 * @param min The minimum number of rolls (inclusive).
		 * @param max The maximum number of rolls (inclusive).
		 * @return this
		 */
		public Pool bonusRolls(float min, float max) {
			root.add("bonus_rolls", new JsonObjectBuilder().add("min", min).add("max", max).build());
			return this;
		}

		/**
		 * Builder for a loot table pool entry.
		 *
		 * @see Pool
		 */
		public static final class Entry extends TypedJsonObject {
			public Entry() {
				super(new JsonObject());
			}

			/**
			 * Set the type of this entry.
			 *
			 * @param id The type ID.
			 * @return this
			 */
			public Entry type(ResourceLocation id) {
				root.addProperty("type", id.toString());
				return this;
			}

			/**
			 * Set the name of this entry's value. Expected value varies by type.
			 *
			 * @param id The name of the value as an ID.
			 * @return this
			 */
			public Entry name(ResourceLocation id) {
				root.addProperty("name", id.toString());
				return this;
			}

			/**
			 * Add a child to this entry.
			 *
			 * @param settings A callback which will be passed an {@link Entry}.
			 * @return this
			 */
			public Entry child(Entry settings) {
				join("children", settings.build());
				return this;
			}

			/**
			 * For type {@code tag}, set whether to use the given tag as a list of equally weighted options or to use all tag entries.
			 *
			 * @param expand Whether to expand.
			 * @return this
			 */
			public Entry expand(boolean expand) {
				root.addProperty("expand", expand);
				return this;
			}

			/**
			 * Set the relative weight of this entry.
			 *
			 * @param weight The weight.
			 * @return this
			 */
			public Entry weight(int weight) {
				root.addProperty("weight", weight);
				return this;
			}

			/**
			 * Set the quality of this entry (modifies the weight based on the player's luck attribute).
			 *
			 * @param quality The quality.
			 * @return this
			 */
			public Entry quality(int quality) {
				root.addProperty("quality", quality);
				return this;
			}

			/**
			 * Add a function to be applied to this entry.
			 *
			 * @param id       The function ID.
			 * @param settings A callback which will be passed a {@link Function}.
			 * @return this
			 * @see <a href="https://minecraft.gamepedia.com/Loot_table#Functions" target="_blank">Minecraft Wiki</a>
			 */
			public Entry function(ResourceLocation id, Function settings) {
				join("functions", arrayOf(settings.add("function", id.toString())));
				return this;
			}

			/**
			 * Add a condition to this entry. All conditions must pass for the entry to be used.
			 * The specific properties of this vary by condition, so this falls through to direct JSON building.
			 *
			 * @param id       The condition ID.
			 * @param settings A callback which will be passed a {@link TypedJsonObject}.
			 * @return this
			 * @see <a href="https://minecraft.gamepedia.com/Loot_table#Conditions" target="_blank">Minecraft Wiki</a>
			 */
			public Entry condition(ResourceLocation id, TypedJsonObject settings) {
				join("conditions", arrayOf(settings.add("condition", id.toString())));
				return this;
			}

			/**
			 * Builder for loot table entry functions.
			 *
			 * @see Entry
			 * @see <a href="https://minecraft.gamepedia.com/Loot_table#Functions" target="_blank">Minecraft Wiki</a>
			 */
			public static final class Function extends TypedJsonObject {
				public Function(JsonObject func) {
					super(func);
				}

				/**
				 * Add a condition to this function. All conditions must pass for the function to be applied.
				 * The specific properties of this vary by condition, so this falls through to direct JSON building.
				 *
				 * @param id       The condition ID.
				 * @param settings A callback which will be passed a {@link TypedJsonObject}.
				 * @return this
				 * @see <a href="https://minecraft.gamepedia.com/Loot_table#Conditions" target="_blank">Minecraft Wiki</a>
				 */
				public Function condition(ResourceLocation id, TypedJsonObject settings) {
					this.join("conditions", arrayOf(settings.add("condition", id.toString())));
					return this;
				}
			}
		}
	}
}
