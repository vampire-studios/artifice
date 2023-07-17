package io.github.vampirestudios.artifice.api;

import io.github.vampirestudios.artifice.api.ArtificeResourcePack.ClientResourcePackBuilder;
import io.github.vampirestudios.artifice.api.ArtificeResourcePack.ServerResourcePackBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;
import io.github.vampirestudios.artifice.common.ArtificeRegistry;
import io.github.vampirestudios.artifice.impl.ArtificeImpl;
import io.github.vampirestudios.artifice.impl.DynamicResourcePackFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registry methods for Artifice's virtual resource pack support.
 */
public final class Artifice {
	public static final Logger LOGGER = LoggerFactory.getLogger(Artifice.class);

	private Artifice() {
	}

	/**
	 * Register a new client-side resource pack, creating resources with the given callback.
	 *
	 * @param id       The pack ID.
	 * @param register A callback which will be passed a {@link ClientResourcePackBuilder}.
	 */
	@Environment(EnvType.CLIENT)
	public static void registerAssetPack(Identifier id, Processor<ClientResourcePackBuilder> register) {
		ArtificeImpl.registerSafely(ArtificeRegistry.ASSETS, id, new DynamicResourcePackFactory<>(ResourceType.CLIENT_RESOURCES, id, register));
	}

	/**
	 * Register a new server-side resource pack, creating resources with the given callback.
	 *
	 * @param id       The pack ID.
	 * @param register A callback which will be passed a {@link ServerResourcePackBuilder}.
	 */
	public static void registerDataPack(Identifier id, Processor<ServerResourcePackBuilder> register) {
		ArtificeImpl.registerSafely(ArtificeRegistry.DATA, id, new DynamicResourcePackFactory<>(ResourceType.SERVER_DATA, id, register));
	}

	/**
	 * Register a new client-side resource pack.
	 *
	 * @param id   The pack ID.
	 * @param pack The pack to register.
	 * @return The registered pack.
	 */
	@Environment(EnvType.CLIENT)
	public static ArtificeResourcePack registerAssets(String id, ArtificeResourcePack pack) {
		return registerAssets(Identifier.of(id), pack);
	}

	/**
	 * Register a new server-side resource pack.
	 *
	 * @param id   The pack ID.
	 * @param pack The pack to register.
	 * @return The registered pack.
	 */
	public static ArtificeResourcePack registerData(String id, ArtificeResourcePack pack) {
		return registerData(Identifier.of(id), pack);
	}

	/**
	 * Register a new client-side resource pack.
	 *
	 * @param id   The pack ID.
	 * @param pack The pack to register.
	 * @return The registered pack.
	 */
	@Environment(EnvType.CLIENT)
	public static ArtificeResourcePack registerAssets(Identifier id, ArtificeResourcePack pack) {
		if (pack.getType() != ResourceType.CLIENT_RESOURCES)
			throw new IllegalArgumentException("Cannot register a server-side pack as assets");
		return ArtificeImpl.registerSafely(ArtificeRegistry.ASSETS, id, pack);
	}

	/**
	 * Register a new server-side resource pack.
	 *
	 * @param id   The pack ID.
	 * @param pack The pack to register.
	 * @return The registered pack.
	 */
	public static ArtificeResourcePack registerData(Identifier id, ArtificeResourcePack pack) {
		if (pack.getType() != ResourceType.SERVER_DATA)
			throw new IllegalArgumentException("Cannot register a client-side pack as data");
		return ArtificeImpl.registerSafely(ArtificeRegistry.DATA, id, pack);
	}
}
