package io.github.vampirestudios.artifice.api.virtualpack;

import io.github.vampirestudios.artifice.api.ArtificeResourcePack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.packs.repository.Pack;

import static io.github.vampirestudios.artifice.impl.ArtificeResourcePackImpl.ARTIFICE_RESOURCE_PACK_SOURCE;

/**
 * A wrapper around {@link Pack} exposing optionality/visibility.
 *
 * @see ArtificeResourcePack.ClientResourcePackBuilder#setOptional
 * @see ArtificeResourcePack.ClientResourcePackBuilder#setVisible
 */
@Environment(EnvType.CLIENT)
public class ArtificeResourcePackContainer extends Pack {
	private final boolean optional;
	private final boolean visible;

	public ArtificeResourcePackContainer(boolean optional, boolean visible, Pack wrapping) {
		super(
				wrapping.getId(), !optional, wrapping::open,
				wrapping.getTitle(), wrapping.getDescription(),
				wrapping.getCompatibility(), wrapping.getDefaultPosition(),
				wrapping.isFixedPosition(), ARTIFICE_RESOURCE_PACK_SOURCE
		);

		this.optional = optional;
		this.visible = visible;
	}

	/**
	 * @return Whether this pack is optional.
	 */
	public boolean isOptional() {
		return this.optional;
	}

	/**
	 * @return Whether this pack is visible.
	 */
	public boolean isVisible() {
		return this.visible;
	}
}
