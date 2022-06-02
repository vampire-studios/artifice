package io.github.vampirestudios.artifice.api;

import net.minecraft.resources.ResourceLocation;

public interface ArtificeAssetPackEntrypoint {
	void generateAssetPack(ArtificeResourcePack.ClientResourcePackBuilder builder);

	ResourceLocation getClientID();
}
