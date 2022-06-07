package io.github.vampirestudios.artifice.common;

import net.minecraft.server.packs.repository.Pack;

public interface ClientResourcePackProfileLike {
	// Supplier to avoid loading ClientResourcePackProfile on the server
	<T extends Pack> ClientOnly<Pack> toClientResourcePackProfile(Pack.PackConstructor factory);
}
