package io.github.vampirestudios.artifice.api;

import net.minecraft.resources.ResourceLocation;

public interface ArtificeAssetPackEntrypoint {
	void generateClientResourcePack(ArtificeResourcePack.ClientResourcePackBuilder builder);
	ResourceLocation getClientID();
}