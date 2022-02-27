package io.github.vampirestudios.artifice.common;

import net.minecraft.server.packs.repository.Pack;

public interface ServerResourcePackProfileLike {
    <T extends Pack> Pack toServerResourcePackProfile(Pack.PackConstructor factory);
}
