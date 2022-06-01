package io.github.vampirestudios.artifice.api;

import io.github.vampirestudios.artifice.api.util.Processor;
import net.minecraft.resources.ResourceLocation;

public interface ArtificeServerResourcePackEntrypoint {
	void generateServerResourcePack(Processor<ArtificeResourcePack.ServerResourcePackBuilder> builder);

	ResourceLocation getServerID();
}
