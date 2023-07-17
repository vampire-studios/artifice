package io.github.vampirestudios.artifice.impl;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.resource.InputSupplier;
import net.modificationstation.stationapi.api.resource.ResourcePack;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadataReader;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;

public class RuntimeResourcePackImpl extends MutableResourcePack implements ResourcePack {
    @Override
    public @Nullable InputSupplier<InputStream> openRoot(String... segments) {
        Supplier<byte[]> supplier = this.root.get(Arrays.asList(segments));

        if (supplier == null) {
            return null;
        }

        return () -> new ByteArrayInputStream(supplier.get());
    }

    @Override
    public @Nullable InputSupplier<InputStream> open(ResourceType type, Identifier id) {
        Supplier<byte[]> supplier = this.getSys(type).get(id);
        return supplier == null ? null : () -> new ByteArrayInputStream(supplier.get());
    }

    @Override
    public void findResources(ResourceType type, ModID namespace, String prefix, ResultConsumer consumer) {
        for (Identifier identifier : this.getSys(type).keySet()) {
            // deleted section: detecting "No resource found for..."
            if (identifier.modID.equals(namespace) && identifier.id.startsWith(prefix)) {
                consumer.accept(identifier, open(type, identifier));
            }
        }
    }

    @Override
    public Set<ModID> getNamespaces(ResourceType type) {
        Set<ModID> namespaces = new HashSet<>();

        for (Identifier identifier : this.getSys(type).keySet()) {
            namespaces.add(identifier.modID);
        }

        return namespaces;
    }

    @Override
    public <T> @Nullable T parseMetadata(ResourceMetadataReader<T> reader) throws IOException {
        return metadata.getData().has(reader.getKey())
                ? reader.fromJson(metadata.getData().getAsJsonObject(reader.getKey()))
                : null;
    }

    @Override
    public String getName() {
        return "Artifice Generated ResourcePack";
    }

    @Override
    public boolean isAlwaysStable() {
        return true;
    }

    @Override
    public void close() {

    }
}
