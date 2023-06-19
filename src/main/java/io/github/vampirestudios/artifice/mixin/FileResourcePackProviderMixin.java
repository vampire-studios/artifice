package io.github.vampirestudios.artifice.mixin;

import io.github.vampirestudios.artifice.common.ArtificeRegistry;
import io.github.vampirestudios.artifice.common.ClientResourcePackProfileLike;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@Mixin(FolderRepositorySource.class)
public class FileResourcePackProviderMixin {
	@Shadow @Final private PackType packType;
	private static final PackSource RUNTIME = PackSource.create(getSourceTextSupplier(), true);
	private static final Logger ARRP_LOGGER = LogManager.getLogger("Artifice/FileResourcePackProviderMixin");

	private static UnaryOperator<Component> getSourceTextSupplier() {
		Component text = Component.translatable("pack.source.runtime");
		return name -> Component.translatable("pack.nameAndSource", name, text).withStyle(ChatFormatting.GRAY);
	}

	@Inject(method = "loadPacks", at = @At("HEAD"))
	public void register(
		Consumer<Pack> adder,
		CallbackInfo ci
	) {
		ARRP_LOGGER.info("Artifice register - before user");

		for (ClientResourcePackProfileLike asset : ArtificeRegistry.ASSETS) {
			Pack pack = asset.toClientResourcePackProfile().get();
			adder.accept(Pack.create(
					pack.getId(),
					pack.getTitle(),
					pack.isRequired(),
					(name) -> pack.open(),
					new Pack.Info(pack.getDescription(), SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES), pack.getRequestedFeatures()),
					this.packType,
					Pack.Position.TOP,
					pack.isFixedPosition(),
					RUNTIME
			));
		}
	}
}