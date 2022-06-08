package io.github.vampirestudios.artifice.api;

import net.minecraft.resources.ResourceLocation;

public interface ArtificeDataPackEntrypoint {
	void generateServerResourcePack(ArtificeResourcePack.ServerResourcePackBuilder builder);
	ResourceLocation getServerID();
}