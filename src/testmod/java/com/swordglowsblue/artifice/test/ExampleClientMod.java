package com.swordglowsblue.artifice.test;

import io.github.vampirestudios.artifice.api.Artifice;
import io.github.vampirestudios.artifice.api.ArtificeAssetPackEntrypoint;
import io.github.vampirestudios.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.artifice.api.builder.assets.BlockStateBuilder;
import io.github.vampirestudios.artifice.api.builder.assets.ModelBuilder;
import io.github.vampirestudios.artifice.api.builder.assets.TranslationBuilder;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;

public class ExampleClientMod implements ArtificeAssetPackEntrypoint {
	private static ResourceLocation makeResourceLocation(String name) {
		return new ResourceLocation("artifice", name);
	}

	@Override
	public void generateClientResourcePack(ArtificeResourcePack.ClientResourcePackBuilder pack) {
		pack.setDisplayName("Artifice Test Resources");
		pack.setDescription("Resources for the Artifice test mod");

		pack.addItemModel(makeResourceLocation("test_item"), new ModelBuilder()
				.parent(new ResourceLocation("item/generated"))
				.texture("layer0", new ResourceLocation("block/sand"))
				.texture("layer1", new ResourceLocation("block/dead_bush"))
		);
		pack.addItemModel(makeResourceLocation("test_block"), new ModelBuilder()
				.parent(makeResourceLocation("block/test_block"))
		);

		pack.addBlockState(makeResourceLocation("test_block"), new BlockStateBuilder()
				.weightedVariant("", new BlockStateBuilder.Variant()
						.model(makeResourceLocation("block/test_block"))
						.weight(3))
				.weightedVariant("", new BlockStateBuilder.Variant()
						.model(new ResourceLocation("block/coarse_dirt")))
		);

		pack.addBlockModel(makeResourceLocation("test_block"), new ModelBuilder()
				.parent(new ResourceLocation("block/cube_all"))
				.texture("all", new ResourceLocation("item/diamond_sword"))
		);

		pack.addTranslations(makeResourceLocation("en_us"), new TranslationBuilder()
				.entry("item.artifice.test_item", "Artifice Test Item")
				.entry("block.artifice.test_block", "Artifice Test Block"));
		pack.addLanguage("ar_tm", "Artifice", "Test Mod", false);
		pack.addTranslations(makeResourceLocation("ar_tm"), new TranslationBuilder()
				.entry("item.artifice.test_item", "Artifice Test Item in custom lang")
				.entry("block.artifice.test_block", "Artifice Test Block in custom lang"));
		try {
			pack.dumpResources("testing_assets", "assets");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Artifice.registerAssetPack(makeResourceLocation("testmod2"), ArtificeResourcePack.ClientResourcePackBuilder::setOptional);
	}

	@Override
	public ResourceLocation getClientID() {
		return ExampleMod.makeResourceLocation("example_client_pack");
	}

}
