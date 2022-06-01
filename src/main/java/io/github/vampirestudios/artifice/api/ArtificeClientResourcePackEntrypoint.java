package io.github.vampirestudios.artifice.api;

import io.github.vampirestudios.artifice.api.util.Processor;
import net.minecraft.resources.ResourceLocation;

public interface ArtificeClientResourcePackEntrypoint {
	void generateClientResourcePack(Processor<ArtificeResourcePack.ClientResourcePackBuilder> builder);

	ResourceLocation getClientID();
}
