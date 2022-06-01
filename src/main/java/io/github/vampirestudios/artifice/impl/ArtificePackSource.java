package io.github.vampirestudios.artifice.impl;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackSource;
import org.jetbrains.annotations.NotNull;

public class ArtificePackSource implements PackSource {
	private final String modId;

	public ArtificePackSource(String modId) {
		this.modId = modId;
	}

	@Override
	public Component decorate(@NotNull Component packName) {
		return Component.translatable("pack.nameAndSource", packName, Component.translatable("pack.source.builtinArtifice", modId)).withStyle(ChatFormatting.GRAY);
	}

}