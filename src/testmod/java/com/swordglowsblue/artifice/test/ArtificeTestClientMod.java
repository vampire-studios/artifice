package com.swordglowsblue.artifice.test;

import io.github.vampirestudios.artifice.api.ArtificeAssetPackEntrypoint;
import io.github.vampirestudios.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.artifice.api.builder.assets.BlockStateBuilder;
import io.github.vampirestudios.artifice.api.builder.assets.ModelBuilder;
import io.github.vampirestudios.artifice.api.builder.assets.TranslationBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;

public class ArtificeTestClientMod implements ArtificeAssetPackEntrypoint {
	public static ResourceLocation makeResourceLocation(String name) {
		return new ResourceLocation("artifice", name);
	}

	@Override
	public void generateAssetPack(ArtificeResourcePack.ClientResourcePackBuilder pack) {
		pack.setDisplayName(Component.literal("Artifice Test Resources"));
		pack.setDescription(Component.literal("Resources for the Artifice test mod"));

		pack.addItemModel(makeResourceLocation("test_item"), new ModelBuilder()
				.parent(new ResourceLocation("item/generated"))
				.texture("layer0", new ResourceLocation("block/sand"))
				.texture("layer1", new ResourceLocation("block/dead_bush")));
		pack.addItemModel(makeResourceLocation("test_block"), new ModelBuilder()
				.parent(makeResourceLocation("block/test_block")));

		pack.addBlockState(makeResourceLocation("test_block"), new BlockStateBuilder()
				.weightedVariant("", new BlockStateBuilder.Variant()
						.model(makeResourceLocation("block/test_block"))
						.weight(3))
				.weightedVariant("", new BlockStateBuilder.Variant()
						.model(new ResourceLocation("block/coarse_dirt"))));

		pack.addBlockModel(makeResourceLocation("test_block"), new ModelBuilder()
				.parent(new ResourceLocation("block/cube_all"))
				.texture("all", new ResourceLocation("item/diamond_sword")));

		pack.addTranslations(makeResourceLocation("en_us"), new TranslationBuilder()
				.entry("item.artifice.test_item", "Artifice Test Item")
				.entry("block.artifice.test_block", "Artifice Test Block"));
		pack.addLanguage("ar_tm", "Artifice", "Test Mod", false);
		pack.addTranslations(makeResourceLocation("ar_tm"), new TranslationBuilder()
				.entry("item.artifice.test_item", "Artifice Test Item in custom lang")
				.entry("block.artifice.test_block", "Artifice Test Block in custom lang"));

		BufferTexture texture = new BufferTexture(16, 16);
		texture.setPixel(0, 0, new CustomColor(255, 255, 255));
		texture.setPixel(15, 0, new CustomColor(255, 255, 255));
		texture.setPixel(0, 15, new CustomColor(255, 255, 255));
		texture.setPixel(15, 15, new CustomColor(255, 255, 255));
		pack.addTexture(makeResourceLocation("testing"), texture.makeImage());

		try {
			pack.dumpResources("testing_assets", "assets");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ResourceLocation getClientID() {
		return makeResourceLocation("test_resource_pack");
	}

}