package io.github.vampirestudios.artifice.impl;

import com.google.gson.JsonObject;
import io.github.vampirestudios.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.artifice.api.builder.JsonObjectBuilder;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.builder.assets.*;
import io.github.vampirestudios.artifice.api.resource.ArtificeResource;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceType;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class MutableResourcePack implements ArtificeResourcePack {
    protected final JsonResource<JsonObject> metadata;
    protected final Map<List<String>, Supplier<byte[]>> root = new ConcurrentHashMap<>();
    protected final Map<Identifier, Supplier<byte[]>> assets = new ConcurrentHashMap<>();
    protected final Map<Identifier, Supplier<byte[]>> data = new ConcurrentHashMap<>();

    public MutableResourcePack() {
        JsonObject packMeta = new JsonObjectBuilder()
                .add("pack_format", 13)
                .add("description", "In-memory resource pack created with Artifice")
                .build();

        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.add("pack", packMeta);

        this.metadata = new JsonResource<>(builder.build());
    }

    protected Map<Identifier, Supplier<byte[]>> getSys(ResourceType side) {
        return side == ResourceType.CLIENT_RESOURCES ? this.assets : this.data;
    }

    private Identifier adaptId(Identifier id, String dir) {
        return id.modID.id(StationAPI.MODID + "/" + dir + id.id);
    }

    private byte[] toBytes(TypedJsonObject jsonObject) {
        try {
            return jsonObject.toInputStream().readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Identifier id, ArtificeResource<?> resource) {

    }

    @Override
    public void setDisplayName(String name) {

    }

    @Override
    public void setDescription(String desc) {

    }

    @Override
    public void dumpResources(String filePath, String type, boolean enableDump) throws IOException {

    }

    @Override
    public void dump(String filePath, String type, boolean enableDump) {

    }

    @Override
    public void addItemModel(Identifier id, ModelBuilder f) {
        this.addAsset(this.adaptId(id, "models/item/"), this.toBytes(f));
    }

    @Override
    public void addBlockModel(Identifier id, ModelBuilder f) {
        this.addAsset(this.adaptId(id, "models/block/"), this.toBytes(f));
    }

    @Override
    public void addBlockState(Identifier id, BlockStateBuilder f) {
        this.addAsset(this.adaptId(id, "blockstates/"), this.toBytes(f));
    }

    @Override
    public void addTranslations(Identifier id, TranslationBuilder f) {
        this.addAsset(this.adaptId(id, "lang/"), this.toBytes(f));
    }

//    @Override
//    public void addParticle(Identifier id, ParticleBuilder f) {
//
//    }
//
//    @Override
//    public void addItemAnimation(Identifier id, AnimationBuilder f) {
//
//    }
//
//    @Override
//    public void addBlockAnimation(Identifier id, AnimationBuilder f) {
//
//    }

    @Override
    public byte[] addRootResource(String path, byte[] data) {
        this.root.put(List.of(path.split("/")), () -> data);

        return data;
    }

    @Override
    public byte[] addResource(ResourceType type, Identifier path, byte[] data) {
        this.getSys(type).put(path, () -> data);

        return data;
    }

    @Override
    public byte[] addAsset(Identifier path, byte[] data) {
        return this.addResource(ResourceType.CLIENT_RESOURCES, path, data);
    }

    @Override
    public byte[] addData(Identifier path, byte[] data) {
        return this.addResource(ResourceType.SERVER_DATA, path, data);
    }
}
