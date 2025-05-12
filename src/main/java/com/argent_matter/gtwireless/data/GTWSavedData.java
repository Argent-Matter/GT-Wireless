package com.argent_matter.gtwireless.data;

import com.argent_matter.gtwireless.GTWireless;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.http.impl.conn.Wire;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GTWSavedData extends SavedData {
    public static final String GT_WIRELESS_DATA_ID = "gt_wireless_data";

    public static Codec<BigInteger> BIG_INTEGER_CODEC = Codec.STRING.xmap(BigInteger::new, BigInteger::toString);

    private final WirelessHolder wirelessHolder;

    public GTWSavedData() {
        super();

        this.wirelessHolder = new WirelessHolder();
    }

    public GTWSavedData(WirelessHolder wirelessHolder) {
        super();

        this.wirelessHolder = wirelessHolder;
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        DataResult<Tag> wirelessHolderTag = WirelessHolder.CODEC.encodeStart(NbtOps.INSTANCE, this.wirelessHolder);

        if (wirelessHolderTag.result().isPresent()) {
            pCompoundTag.put(GT_WIRELESS_DATA_ID, wirelessHolderTag.result().get());
        } else {
            System.out.println("Failed to save wireless EU data");
        }
    }

    public static GTWSavedData load(CompoundTag pCompoundTag) {
        DataResult<Pair<WirelessHolder, Tag>> dataResult = WirelessHolder.CODEC.decode(NbtOps.INSTANCE, pCompoundTag.get(GT_WIRELESS_DATA_ID));
        Optional<Pair<WirelessHolder, Tag>> mapTagPair = dataResult
                .resultOrPartial(err -> GTWireless.LOGGER.error("Decoding error: {}", err));

        if (mapTagPair.isPresent()) {
            WirelessHolder facadeMap = mapTagPair.get().getFirst();
            return new GTWSavedData(facadeMap);
        }
        return new GTWSavedData();
    }

    private static GTWSavedData get(ServerLevel serverLevel) {
        return serverLevel.getDataStorage().computeIfAbsent(GTWSavedData::load, GTWSavedData::new, GT_WIRELESS_DATA_ID);
    }

    private static class WirelessHolder {
        public static final Codec<WirelessHolder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.unboundedMap(UUIDUtil.CODEC, BIG_INTEGER_CODEC).fieldOf("wirelessEU").forGetter(WirelessHolder::getWirelessEU),
                Codec.unboundedMap(UUIDUtil.CODEC, BIG_INTEGER_CODEC).fieldOf("wirelessComputation").forGetter(WirelessHolder::getWirelessComputation),
                Codec.unboundedMap(UUIDUtil.CODEC, UUIDUtil.CODEC).fieldOf("playerTeams").forGetter(WirelessHolder::getPlayerTeams)
        ).apply(instance, WirelessHolder::new));

        private Map<UUID, BigInteger> wirelessEUMap;
        private Map<UUID, BigInteger> wirelessComputationMap;
        private Map<UUID, UUID> playerTeams;

        public WirelessHolder() {
            this.wirelessEUMap = new HashMap<>();
            this.wirelessComputationMap = new HashMap<>();
            this.playerTeams = new HashMap<>();
        }

        public WirelessHolder(Map<UUID, BigInteger> wirelessEUMap, Map<UUID, BigInteger> wirelessComputationMap, Map<UUID, UUID> playerTeams) {
            this.wirelessEUMap = wirelessEUMap;
            this.wirelessComputationMap = wirelessComputationMap;
            this.playerTeams = playerTeams;
        }

        public Map<UUID, BigInteger> getWirelessEU() {
            return wirelessEUMap;
        }

        public Map<UUID, BigInteger> getWirelessComputation() {
            return wirelessComputationMap;
        }

        public Map<UUID, UUID> getPlayerTeams() {
            return playerTeams;
        }
    }
}
