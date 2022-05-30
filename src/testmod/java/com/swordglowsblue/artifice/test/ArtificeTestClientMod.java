package com.swordglowsblue.artifice.test;

import io.github.vampirestudios.artifice.api.Artifice;
import io.github.vampirestudios.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.artifice.api.builder.assets.BlockStateBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;

public class ArtificeTestClientMod implements ClientModInitializer {
	private static ResourceLocation makeResourceLocation(String name) {
		return new ResourceLocation("artifice", name);
	}

	@Environment(EnvType.CLIENT)
	public void onInitializeClient() {
		Artifice.registerAssetPack("artifice:testmod", pack -> {
			pack.setDisplayName("Artifice Test Resources");
			pack.setDescription("Resources for the Artifice test mod");

			pack.addItemModel(makeResourceLocation("test_item"), model -> model
					.parent(new ResourceLocation("item/generated"))
					.texture("layer0", new ResourceLocation("block/sand"))
					.texture("layer1", new ResourceLocation("block/dead_bush")));
			pack.addItemModel(makeResourceLocation("test_block"), model -> model
					.parent(makeResourceLocation("block/test_block")));

			pack.addBlockState(makeResourceLocation("test_block"), state -> state
					.weightedVariant("", new BlockStateBuilder.Variant()
							.model(makeResourceLocation("block/test_block"))
							.weight(3))
					.weightedVariant("", new BlockStateBuilder.Variant()
							.model(new ResourceLocation("block/coarse_dirt"))));

			pack.addBlockModel(makeResourceLocation("test_block"), model -> model
					.parent(new ResourceLocation("block/cube_all"))
					.texture("all", new ResourceLocation("item/diamond_sword")));

			pack.addTranslations(makeResourceLocation("en_us"), translations -> translations
					.entry("item.artifice.test_item", "Artifice Test Item")
					.entry("block.artifice.test_block", "Artifice Test Block"));
			pack.addLanguage("ar_tm", "Artifice", "Test Mod", false);
			pack.addTranslations(makeResourceLocation("ar_tm"), translations -> translations
					.entry("item.artifice.test_item", "Artifice Test Item in custom lang")
					.entry("block.artifice.test_block", "Artifice Test Block in custom lang"));
			try {
				pack.dumpResources("testing_assets", "assets");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		Artifice.registerAssetPack(makeResourceLocation("testmod2"), ArtificeResourcePack.ClientResourcePackBuilder::setOptional);
	}

}
