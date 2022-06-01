package io.github.vampirestudios.artifice.api.builder.assets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.builder.JsonObjectBuilder;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

/**
 * Builder for a blockstate definition file ({@code namespace:blockstates/blockid.json}).
 *
 * @see <a href="https://minecraft.gamepedia.com/Model#Block_states" target="_blank">Minecraft Wiki</a>
 */
@Environment(EnvType.CLIENT)
public final class BlockStateBuilder extends TypedJsonBuilder<JsonResource<JsonObject>> {
	public BlockStateBuilder() {
		super(new JsonObject(), JsonResource::new);
	}

	/**
	 * Add a variant for the given state key.
	 * Calling this multiple times for the same key will modify the existing value.
	 * {@code variant} and {@code multipart} are incompatible; calling this will remove any existing {@code multipart} definitions.
	 *
	 * @param name     The state key ({@code ""} for default or format: {@code "prop1=value,prop2=value"}).
	 * @param settings A callback which will be passed a {@link Variant}.
	 * @return this
	 */
	public BlockStateBuilder variant(String name, Variant settings) {
		root.remove("multipart");
		with("variants", JsonObject::new, variants -> with(variants, name, JsonObject::new, settings::buildTo));
		return this;
	}

	/**
	 * Add a variant for the given state key, with multiple weighted random options.
	 * Calling this multiple times for the same key will add to the list instead of overwriting.
	 * {@code variant} and {@code multipart} are incompatible; calling this will remove any existing {@code multipart} definitions.
	 *
	 * @param name     The state key ({@code ""} for default or format: {@code "prop1=value,prop2=value"}).
	 * @param settings A callback which will be passed a {@link Variant}.
	 * @return this
	 */
	public BlockStateBuilder weightedVariant(String name, Variant settings) {
		root.remove("multipart");
		with("variants", JsonObject::new, variants -> with(variants, name, JsonArray::new, options ->
				options.add(settings.build())));
		return this;
	}

	/**
	 * Add a multipart case.
	 * Calling this multiple times will add to the list instead of overwriting.
	 * {@code variant} and {@code multipart} are incompatible; calling this will remove any existing {@code variant} definitions.
	 *
	 * @param settings A callback which will be passed a {@link Case}.
	 * @return this
	 */
	public BlockStateBuilder multipartCase(Case settings) {
		root.remove("variants");
		with("multipart", JsonArray::new, cases -> cases.add(settings.build()));
		return this;
	}

	/**
	 * Builder for a blockstate variant definition.
	 *
	 * @see BlockStateBuilder
	 */
	@Environment(EnvType.CLIENT)
	public static final class Variant extends TypedJsonBuilder<JsonObject> {
		public Variant() {
			super(new JsonObject(), j -> j);
		}

		private Variant(JsonObject root) {
			super(root, j -> j);
		}

		/**
		 * Set the model this variant should use.
		 *
		 * @param id The model ID ({@code namespace:block|item/modelid}).
		 * @return this
		 */
		public Variant model(ResourceLocation id) {
			root.addProperty("model", id.toString());
			return this;
		}

		/**
		 * Set the rotation of this variant around the X axis in increments of 90deg.
		 *
		 * @param x The X axis rotation.
		 * @return this
		 * @throws IllegalArgumentException if {@code x} is not divisible by 90.
		 */
		public Variant rotationX(int x) {
			if (x % 90 != 0) throw new IllegalArgumentException("X rotation must be in increments of 90");
			root.addProperty("x", x);
			return this;
		}

		/**
		 * Set the rotation of this variant around the Y axis in increments of 90deg.
		 *
		 * @param y The Y axis rotation.
		 * @return this
		 * @throws IllegalArgumentException if {@code y} is not divisible by 90.
		 */
		public Variant rotationY(int y) {
			if (y % 90 != 0) throw new IllegalArgumentException("Y rotation must be in increments of 90");
			root.addProperty("y", y);
			return this;
		}

		/**
		 * Set whether the textures of this model should not rotate with it.
		 *
		 * @param uvlock Whether to lock texture rotation or not.
		 * @return this
		 */
		public Variant uvlock(boolean uvlock) {
			root.addProperty("uvlock", uvlock);
			return this;
		}

		/**
		 * Set the relative weight of this variant.
		 * This property will be ignored if the variant is not added with {@link BlockStateBuilder#weightedVariant}
		 * or {@link Case#weightedApply}.
		 *
		 * @param weight The weight.
		 * @return this
		 */
		public Variant weight(int weight) {
			root.addProperty("weight", weight);
			return this;
		}
	}

	/**
	 * Builder for a blockstate multipart case.
	 *
	 * @see BlockStateBuilder
	 */
	@Environment(EnvType.CLIENT)
	public static final class Case extends TypedJsonBuilder<JsonObject> {
		private Case() {
			super(new JsonObject(), j -> j);
		}

		/**
		 * Set the condition for this case to be applied.
		 * Calling this multiple times with different keys will require all of the specified properties to match.
		 *
		 * @param name  The state name (e.g. {@code facing}).
		 * @param state The state value (e.g. {@code north}).
		 * @return this
		 */
		public Case when(String name, String state) {
			with("when", JsonObject::new, when -> {
				when.remove("OR");
				when.addProperty(name, state);
			});
			return this;
		}

		/**
		 * Set the condition for this case to be applied.
		 * Calling this multiple times with different keys will require at least one of the specified properties to match.
		 *
		 * @param name  The state name (e.g. {@code facing}).
		 * @param state The state value (e.g. {@code north}).
		 * @return this
		 */
		public Case whenAny(String name, String state) {
			with("when", JsonObject::new, when -> with(when, "OR", JsonArray::new, cases -> {
				when.entrySet().forEach(e -> {
					if (!e.getKey().equals("OR")) when.remove(e.getKey());
				});
				cases.add(new JsonObjectBuilder().add(name, state).build());
			}));
			return this;
		}

		/**
		 * Set the variant to be applied if the condition matches.
		 * Calling this multiple times for the same key will overwrite the existing value.
		 *
		 * @param settings A callback which will be passed a {@link Variant}.
		 * @return this
		 */
		public Case apply(Variant settings) {
			root.add("apply", settings.build());
			return this;
		}

		/**
		 * Set the variant to be applied if the condition matches, with multiple weighted random options.
		 * Calling this multiple times will add to the list instead of overwriting.
		 *
		 * @param settings A callback which will be passed a {@link Variant}.
		 * @return this
		 */
		public Case weightedApply(Variant settings) {
			with("apply", JsonArray::new, options -> options.add(settings.build()));
			return this;
		}
	}
}
