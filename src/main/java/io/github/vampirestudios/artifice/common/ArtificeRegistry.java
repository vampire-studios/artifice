package io.github.vampirestudios.artifice.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ArtificeRegistry {
    /**
     * The {@link Registry} for client-side resource packs.
     */
    @Environment(EnvType.CLIENT)
    public static final Registry<ClientResourcePackProfileLike> ASSETS = FabricRegistryBuilder.createSimple(
        ClientResourcePackProfileLike.class, new Identifier("artifice", "common_assets")
    ).buildAndRegister();

    /**
     * The {@link Registry} for server-side resource packs.
     */
    public static final Registry<ServerResourcePackProfileLike> DATA = FabricRegistryBuilder.createSimple(
            ServerResourcePackProfileLike.class, new Identifier("artifice", "common_data")
    ).buildAndRegister();
}
