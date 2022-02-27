package io.github.vampirestudios.artifice.mixin;

import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TransferableSelectionList.PackEntry.class)
public interface MixinResourcePackEntry {
    @Accessor("pack")
    PackSelectionModel.Entry getResourcePackInfo();
}
