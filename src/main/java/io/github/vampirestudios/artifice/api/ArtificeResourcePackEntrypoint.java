package io.github.vampirestudios.artifice.api;

import io.github.vampirestudios.artifice.api.util.Processor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public interface ArtificeResourcePackEntrypoint {
	void generateResourcePack(Processor<ArtificeResourcePack.ResourcePackBuilder> resourcePackBuilderProcessor);

	ResourceLocation getID();

	PackType getType();
}
