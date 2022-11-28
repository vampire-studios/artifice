package io.github.vampirestudios.artifice.api;

import net.minecraft.resources.ResourceLocation;

public class TagResourceLocation extends ResourceLocation {
	/*public TagResourceLocation(String[] strings) {
		super(strings);
	}*/

	public TagResourceLocation(String string) {
		super(string);
	}

	public TagResourceLocation(String string, String string2) {
		super(string, string2);
	}

	public TagResourceLocation(ResourceLocation resourceLocation) {
		super(resourceLocation.getNamespace(), resourceLocation.getPath());
	}

	@Override
	public String toString() {
		return "#" + super.toString();
	}
}
