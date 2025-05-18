package com.argent_matter.gtwireless.data;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;

import net.minecraft.server.level.ServerLevel;

import com.argent_matter.gtwireless.content.hatches.CloudComputationHatchMachine;
import com.argent_matter.gtwireless.content.hatches.CloudDataHatchMachine;

import java.util.*;

public class VolatileData {

    public static final VolatileData INSTANCE = new VolatileData();

    public static VolatileData getInstance() {
        return INSTANCE;
    }

    public HashMap<UUID, ArrayList<CloudDataHatchMachine>> DATA_HATCHES;
    public HashMap<UUID, ArrayList<CloudComputationHatchMachine>> COMPUTATION_HATCHES;

    private VolatileData() {
        DATA_HATCHES = new HashMap<>();
        COMPUTATION_HATCHES = new HashMap<>();
    }

    public List<CloudDataHatchMachine> getDataHatches(UUID playerUUID) {
        System.out.println("Getting data hatches for player: " + playerUUID);
        return DATA_HATCHES.getOrDefault(playerUUID, new ArrayList<>());
    }

    public List<CloudComputationHatchMachine> getComputationHatches(UUID playerUUID) {
        return COMPUTATION_HATCHES.getOrDefault(playerUUID, new ArrayList<>());
    }

    public void checkOrAddDataHatch(CloudDataHatchMachine hatch) {
        if (hatch.getLevel() == null || hatch.getLevel().isClientSide || hatch.getOwnerUUID() == null || hatch.isInput()) {
            return;
        }
        ServerLevel level = (ServerLevel) hatch.getLevel();
        UUID uuid = GTWSavedData.get(level).getWirelessHolder().getTeam(hatch.getOwnerUUID());
        System.out.println("Adding data hatch to: " + uuid);
        if (hatch.isFormed())
            for (IMultiPart part : hatch.getControllers().first().getParts())
                System.out.println("Part: " + part.getClass().getName());
        DATA_HATCHES.computeIfAbsent(uuid, k -> new ArrayList<>()).add(hatch);
    }

    public void checkOrAddComputationHatch(CloudComputationHatchMachine hatch) {
        if (hatch.getLevel() == null || hatch.getLevel().isClientSide) {
            return;
        }
        System.out.println("Adding Computation Hatch");
        ServerLevel level = (ServerLevel) hatch.getLevel();
        UUID uuid = GTWSavedData.get(level).getWirelessHolder().getTeam(hatch.getOwnerUUID());

        COMPUTATION_HATCHES.computeIfAbsent(uuid, k -> new ArrayList<>()).add(hatch);
    }

    public void removeDataHatch(CloudDataHatchMachine hatch) {
        if (hatch.getLevel() == null || hatch.getLevel().isClientSide) {
            return;
        }
        ServerLevel level = (ServerLevel) hatch.getLevel();
        UUID uuid = GTWSavedData.get(level).getWirelessHolder().getTeam(hatch.getOwnerUUID());

        List<CloudDataHatchMachine> hatches = DATA_HATCHES.get(uuid);
        if (hatches != null) {
            hatches.remove(hatch);
        }
    }

    public void removeComputationHatch(CloudComputationHatchMachine hatch) {
        if (hatch.getLevel() == null || hatch.getLevel().isClientSide) {
            return;
        }
        ServerLevel level = (ServerLevel) hatch.getLevel();
        UUID uuid = GTWSavedData.get(level).getWirelessHolder().getTeam(hatch.getOwnerUUID());

        List<CloudComputationHatchMachine> hatches = COMPUTATION_HATCHES.get(uuid);
        if (hatches != null) {
            hatches.remove(hatch);
        }
    }
}
