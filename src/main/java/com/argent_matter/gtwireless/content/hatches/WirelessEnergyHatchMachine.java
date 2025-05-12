package com.argent_matter.gtwireless.content.hatches;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Generated;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class WirelessEnergyHatchMachine extends TieredIOPartMachine {
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER;
    @Persisted
    public final NotifiableEnergyContainer energyContainer;
    protected @Nullable ISubscription energyListener;
    protected int amperage;

    public WirelessEnergyHatchMachine(IMachineBlockEntity holder, int tier, IO io, int amperage, Object... args) {
        super(holder, tier, io);
        this.amperage = amperage;
        this.energyContainer = this.createEnergyContainer(args);
    }

    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        NotifiableEnergyContainer container;
        if (this.io == IO.OUT) {
            container = NotifiableEnergyContainer.emitterContainer(this, GTValues.V[this.tier] * 64L * (long) this.amperage, GTValues.V[this.tier], (long) this.amperage); // Same Capacity as a normal energy hatch
            container.setSideOutputCondition((s) -> false); // No Connection
            container.setCapabilityValidator((s) -> false); // No Connection
        } else {
            container = NotifiableEnergyContainer.receiverContainer(this, GTValues.V[this.tier] * 16L * (long) this.amperage, GTValues.V[this.tier], (long) this.amperage); // Same Capacity as a normal energy hatch
            container.setSideInputCondition((s) -> false); // No Connection
            container.setCapabilityValidator((s) -> false); // No Connection
        }

        return container;
    }

    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }

    public void onLoad() {
        super.onLoad();

    }

    public void onUnload() {
        super.onUnload();
        if (this.energyListener != null) {
            this.energyListener.unsubscribe();
            this.energyListener = null;
        }
    }

    public int tintColor(int index) {
        return index == 2 ? GTValues.VC[this.getTier()] : super.tintColor(index);
    }

    public static long getHatchEnergyCapacity(int tier, int amperage) {
        return GTValues.V[tier] * 64L * (long) amperage;
    }

    @Generated
    public int getAmperage() {
        return this.amperage;
    }

    static {
        MANAGED_FIELD_HOLDER = new ManagedFieldHolder(com.gregtechceu.gtceu.common.machine.multiblock.part.EnergyHatchPartMachine.class, TieredIOPartMachine.MANAGED_FIELD_HOLDER);
    }
}
