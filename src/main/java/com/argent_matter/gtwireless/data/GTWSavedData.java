package com.argent_matter.gtwireless.data;

import com.argent_matter.gtwireless.GTWireless;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.http.impl.conn.Wire;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public WirelessHolder getWirelessHolder() {
        return this.wirelessHolder;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag pCompoundTag) {
        DataResult<Tag> wirelessHolderDataResult = WirelessHolder.CODEC.encodeStart(NbtOps.INSTANCE, this.wirelessHolder);
        Optional<Tag> wirelessHolderOptional = wirelessHolderDataResult.resultOrPartial
                (err -> GTWireless.LOGGER.error("Encoding error: {}", err));

        if (wirelessHolderOptional.isPresent()) {
            pCompoundTag.put(GT_WIRELESS_DATA_ID, wirelessHolderDataResult.result().get());
        } else {
            System.out.println("Failed to save GT Wireless data");
        }

        System.out.println("Saving GT Wireless data");
        return pCompoundTag;
    }

    public static GTWSavedData load(CompoundTag pCompoundTag) {
        System.out.println("Loading GT Wireless data");
        DataResult<Pair<WirelessHolder, Tag>> wirelessHolderDataResult = WirelessHolder.CODEC.decode(NbtOps.INSTANCE, pCompoundTag.get(GT_WIRELESS_DATA_ID));
        Optional<Pair<WirelessHolder, Tag>> wirelessHolderOptional = wirelessHolderDataResult
                .resultOrPartial(err -> GTWireless.LOGGER.error("Decoding error: {}", err));

        if (wirelessHolderOptional.isPresent()) {
            WirelessHolder wirelessHolder = wirelessHolderOptional.get().getFirst();
            return new GTWSavedData(wirelessHolder);
        }
        return new GTWSavedData();
    }

    public static GTWSavedData get(ServerLevel serverLevel) {
        return serverLevel.getDataStorage().computeIfAbsent(GTWSavedData::load, GTWSavedData::new, GT_WIRELESS_DATA_ID);
    }

    public static class WirelessHolder {
        public static final Codec<WirelessHolder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.unboundedMap(Codec.STRING, BIG_INTEGER_CODEC).fieldOf("wirelessEU").forGetter(WirelessHolder::wirelessEUToStr),
                Codec.unboundedMap(Codec.STRING, BIG_INTEGER_CODEC).fieldOf("wirelessComputation").forGetter(WirelessHolder::wirelessCompToStr),
                Codec.unboundedMap(Codec.STRING, UUIDUtil.CODEC).fieldOf("playerTeams").forGetter(WirelessHolder::playerTeamsToStr)
        ).apply(instance, WirelessHolder::wirelessHolderFromStr));

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

        public static WirelessHolder wirelessHolderFromStr(Map<String, BigInteger> strWirelessEUMap, Map<String, BigInteger> strWirelessComputationMap, Map<String, UUID> strPlayerTeams) {
            Map<UUID, BigInteger> eu = new HashMap<>();
            Map<UUID, BigInteger> comp = new HashMap<>();
            Map<UUID, UUID> teams = new HashMap<>();

            strWirelessEUMap.forEach((str, bigI) -> eu.put(UUID.fromString(str), bigI));
            strWirelessComputationMap.forEach((str, bigI) -> comp.put(UUID.fromString(str), bigI));
            strPlayerTeams.forEach((str, uuid) -> teams.put(UUID.fromString(str), uuid));

            return new WirelessHolder(eu, comp, teams);
        }

        public Map<String, BigInteger> wirelessEUToStr() {
            Map<String, BigInteger> toStr = new HashMap<>();

            this.wirelessEUMap.forEach((uuid, bigI) -> toStr.put(uuid.toString(), bigI));
            return toStr;
        }

        public Map<String, BigInteger> wirelessCompToStr() {
            Map<String, BigInteger> toStr = new HashMap<>();

            this.wirelessComputationMap.forEach((uuid, bigI) -> toStr.put(uuid.toString(), bigI));
            return toStr;
        }

        public Map<String, UUID> playerTeamsToStr() {
            Map<String, UUID> toStr = new HashMap<>();

            this.playerTeams.forEach((uuid, uuid2) -> toStr.put(uuid.toString(), uuid2));
            return toStr;
        }

        // ---------- //

        public void pushWirelessEU(UUID player, BigInteger value) {
            UUID team = getTeam(player);
            this.wirelessEUMap.put(team, this.wirelessEUMap.computeIfAbsent(team, k -> BigInteger.ZERO).add(value));
        }

        public BigInteger pullWirelessEU(UUID player, BigInteger value) {
            UUID team = getTeam(player);
            BigInteger totalPulled = BigInteger.ZERO;

            if (getEU(team).compareTo(value) <= 0) { // wireless EU <= required
                totalPulled = totalPulled.add(getEU(team));
                setEU(team, BigInteger.ZERO);
                return totalPulled;
            } else { // wireless EU > required
                totalPulled = totalPulled.add(value);
                setEU(team, getEU(team).subtract(totalPulled));
                return totalPulled;
            }
        }

        // ---------- //

        public void pushWirelessComputation(UUID uuid, BigInteger value) {
            this.wirelessComputationMap.put(uuid, this.wirelessComputationMap.computeIfAbsent(uuid, k -> BigInteger.ZERO).add(value));
        }

        // ---------- //

        public Pair<Boolean, Component> putTeam(UUID player1, UUID player2) {
            if (this.playerTeams.get(player2) != player2) {
                return new Pair<>(false, Component.translatable("gtwireless.commands.wireless_team.already_in_a_team"));
            } else {
                this.playerTeams.put(player1, player2);
                return new Pair<>(true, Component.translatable("gtwireless.commands.wireless_team.success"));
            }
        }

        public void putPlayer(UUID player) {
            this.playerTeams.put(player, player);
            this.wirelessEUMap.put(player, BigInteger.ZERO);
            this.wirelessComputationMap.put(player, BigInteger.ZERO);
        }

        // ---------- //

        public Map<UUID, BigInteger> getWirelessEU() {
            return wirelessEUMap;
        }

        public Map<UUID, BigInteger> getWirelessComputation() {
            return wirelessComputationMap;
        }

        public Map<UUID, UUID> getPlayerTeams() {
            return playerTeams;
        }

        // ---------- //

        public BigInteger getEU(UUID team) {
            return this.wirelessEUMap.get(team);
        }

        public void setEU(UUID team, BigInteger EU) {
            this.wirelessEUMap.put(team, EU);
        }

        public BigInteger getComputation(UUID team) {
            return this.wirelessComputationMap.get(team);
        }

        public UUID getTeam(UUID player) {
            return this.playerTeams.get(player);
        }

        // ---------- //

        public Component statusOf(UUID player) { // TODO: Change this whenever Computation is properly implemented (no stocking)
            return Component.literal("EU: ").withStyle(ChatFormatting.RED)
                    .append(
                            Component.literal(getEU(getTeam(player)).toString()).withStyle(ChatFormatting.WHITE)
                    );
        }

        // ---------- //

    }
}
