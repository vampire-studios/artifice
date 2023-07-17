package io.github.vampirestudios.artifice.api;

import net.modificationstation.stationapi.api.registry.Identifier;

public interface ArtificeAssetPackEntrypoint {
	void generateAssetPack(ArtificeResourcePack.ClientResourcePackBuilder builder);

	Identifier getClientID();
}
