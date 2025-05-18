package com.argent_matter.gtwireless.content.hatches;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

import com.argent_matter.gtwireless.data.GTWSavedData;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.UUID;

public class WirelessEnergyHatchPartMachine extends TieredIOPartMachine implements IMachineLife {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER;

    static {
        MANAGED_FIELD_HOLDER = new ManagedFieldHolder(WirelessEnergyHatchPartMachine.class, TieredIOPartMachine.MANAGED_FIELD_HOLDER);
    }

    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    public final NotifiableEnergyContainer energyContainer;

    @Persisted
    public UUID ownerUUID = null;

    private TickableSubscription wirelessHatchHandler = null;

    @Getter
    protected int amperage;

    public WirelessEnergyHatchPartMachine(IMachineBlockEntity holder, int tier, IO io, int amperage, Object... args) {
        super(holder, tier, io);

        this.amperage = amperage;
        this.energyContainer = this.createEnergyContainer(args);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        subscribeServerTick(wirelessHatchHandler, new WirelessHatchHandler(this));
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (this.wirelessHatchHandler != null) {
            this.wirelessHatchHandler.unsubscribe();
            this.wirelessHatchHandler = null;
        }
    }

    @Override
    public void onMachinePlaced(LivingEntity player, ItemStack stack) {
        if (player != null) {
            this.ownerUUID = player.getUUID();
        }
    }

    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }

    protected @NotNull NotifiableEnergyContainer createEnergyContainer(Object... args) {
        NotifiableEnergyContainer container;
        if (this.io == IO.OUT) {
            container = NotifiableEnergyContainer.emitterContainer(this, getHatchEnergyCapacity(this.tier, this.amperage), GTValues.V[this.tier], (long) this.amperage); // Same
                                                                                                                                                                         // Capacity
                                                                                                                                                                         // as
                                                                                                                                                                         // a
                                                                                                                                                                         // normal
                                                                                                                                                                         // energy
                                                                                                                                                                         // hatch
            container.setSideOutputCondition((s) -> false); // No Connection
            container.setCapabilityValidator((s) -> s == null || s == this.getFrontFacing());
        } else {
            container = NotifiableEnergyContainer.receiverContainer(this, getHatchEnergyCapacity(this.tier, this.amperage), GTValues.V[this.tier], (long) this.amperage); // Same
                                                                                                                                                                          // Capacity
                                                                                                                                                                          // as
                                                                                                                                                                          // a
                                                                                                                                                                          // normal
                                                                                                                                                                          // energy
                                                                                                                                                                          // hatch
            container.setSideInputCondition((s) -> false); // No Connection
            container.setCapabilityValidator((s) -> s == null || s == this.getFrontFacing());
        }

        return container;
    }

    public static long getHatchEnergyCapacity(int tier, int amperage) {
        return GTValues.V[tier] * 64L * (long) amperage;
    }

    public int tintColor(int index) {
        return index == 2 ? GTValues.VC[this.getTier()] : super.tintColor(index);
    }

    private static class WirelessHatchHandler implements Runnable {

        WirelessEnergyHatchPartMachine hatch;
        ServerLevel level;

        public WirelessHatchHandler(WirelessEnergyHatchPartMachine hatch) {
            this.hatch = hatch;

            if (hatch.getLevel() instanceof ServerLevel serverlevel)
                this.level = serverlevel;
            else this.level = null;
        }

        @Override
        public void run() {
            GTWSavedData savedData;

            if (hatch.ownerUUID != null)
                if (level != null) {
                    savedData = GTWSavedData.get(level);

                    if (this.hatch.io == IO.OUT) {
                        savedData.getWirelessHolder().pushWirelessEU(hatch.ownerUUID, BigInteger.valueOf(this.hatch.energyContainer.getEnergyStored()));
                        this.hatch.energyContainer.setEnergyStored(0L);
                    } else {
                        BigInteger energy = savedData.getWirelessHolder().pullWirelessEU(hatch.ownerUUID, BigInteger.valueOf(this.hatch.energyContainer.getEnergyCapacity() - this.hatch.energyContainer.getEnergyStored()));

                        if (energy != null) {
                            this.hatch.energyContainer.setEnergyStored(this.hatch.energyContainer.getEnergyStored() + energy.longValue());
                        }
                    }

                    savedData.setDirty();
                }
        }
    }
}
