package com.argent_matter.gtwireless.content.hatches;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableComputationContainer;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import com.argent_matter.gtwireless.data.VolatileData;
import lombok.Generated;
import lombok.Getter;

import java.util.UUID;

public class CloudComputationHatchMachine extends MultiblockPartMachine implements IMachineLife {

    private final boolean transmitter;
    protected NotifiableComputationContainer computationContainer;

    @Getter
    @Persisted
    private UUID ownerUUID = null;

    @Override
    public void onMachinePlaced(LivingEntity player, ItemStack stack) {
        if (player != null) {
            this.ownerUUID = player.getUUID();
            VolatileData.INSTANCE.checkOrAddComputationHatch(this);
        }
    }

    @Override
    public void onLoad() {
        VolatileData.INSTANCE.checkOrAddComputationHatch(this);
    }

    @Override
    public void onUnload() {
        VolatileData.INSTANCE.removeComputationHatch(this);
    }

    public void onMachineRemoved() {
        Level level = this.getLevel();
        if (level != null && !level.isClientSide) {
            VolatileData.INSTANCE.removeComputationHatch(this);
        }
    }

    public CloudComputationHatchMachine(IMachineBlockEntity holder, boolean transmitter) {
        super(holder);
        this.transmitter = transmitter;
        this.computationContainer = this.createComputationContainer(transmitter);
    }

    protected NotifiableComputationContainer createComputationContainer(Object... args) {
        IO InOut = IO.IN;
        if (args.length > 1) {
            Object isInput = args[args.length - 2];
            if (isInput instanceof IO) {
                IO newInOut = (IO) isInput;
                InOut = newInOut;
            }
        }

        if (args.length > 0) {
            Object var6 = args[args.length - 1];
            if (var6 instanceof Boolean) {
                Boolean transmitter = (Boolean) var6;
                return new NotifiableComputationContainer(this, InOut, transmitter);
            }
        }

        throw new IllegalArgumentException();
    }

    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }

    public boolean canShared() {
        return false;
    }

    @Generated
    public boolean isTransmitter() {
        return this.transmitter;
    }
}
