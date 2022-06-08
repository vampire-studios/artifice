package com.swordglowsblue.artifice.test;

import io.github.vampirestudios.artifice.api.ArtificeAssetPackEntrypoint;
import io.github.vampirestudios.artifice.api.ArtificeResourcePack;
import net.minecraft.resources.ResourceLocation;

import static com.swordglowsblue.artifice.test.ExampleMod.makeResourceLocation;

public class ExampleClientMod2 implements ArtificeAssetPackEntrypoint {

	@Override
	public void generateClientResourcePack(ArtificeResourcePack.ClientResourcePackBuilder pack) {
		pack.setOptional();
	}

	@Override
	public ResourceLocation getClientID() {
		return makeResourceLocation("example_client_pack_2");
	}

}
