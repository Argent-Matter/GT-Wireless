package com.argent_matter.gtwireless.content.hatches;

import com.argent_matter.gtwireless.data.GTWSavedData;
import com.argent_matter.gtwireless.data.VolatileData;
import com.gregtechceu.gtceu.api.capability.IDataAccessHatch;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IDataInfoProvider;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.common.item.PortableScannerBehavior;
import com.gregtechceu.gtceu.common.item.PortableScannerBehavior.DisplayMode;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.research.DataBankMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DataAccessHatchMachine;
import com.gregtechceu.gtceu.common.recipe.condition.ResearchCondition;
import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;
import com.gregtechceu.gtceu.utils.ResearchManager;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.*;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Getter;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CloudDataHatchMachine extends TieredPartMachine implements IMachineLife, IDataAccessHatch, IDataInfoProvider {
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER;

    private final Set<GTRecipe> recipes;
    public int getRecipeCount() {
        return this.recipes.size();
    }

    private final IO io;
    public boolean isInput() {
        return this.io == IO.IN;
    }

    @Getter
    @Persisted
    private UUID ownerUUID = null;

    @Override
    public void onMachinePlaced(LivingEntity player, ItemStack stack) {
        if (player != null) {
            this.ownerUUID = player.getUUID();
            VolatileData.INSTANCE.checkOrAddDataHatch(this);
        }
    }

    @Override
    public void onLoad() {
        VolatileData.INSTANCE.checkOrAddDataHatch(this);
    }

    @Override
    public void onUnload() {
        VolatileData.INSTANCE.removeDataHatch(this);
    }

    public void onMachineRemoved() {
        Level level = this.getLevel();
        if (level != null && !level.isClientSide) {
            VolatileData.INSTANCE.removeDataHatch(this);
        }
    }

    public CloudDataHatchMachine(IMachineBlockEntity holder, int tier, IO io) {
        super(holder, tier);
        this.io = io;
        this.recipes = new ObjectOpenHashSet();
    }

    private boolean isDataBank() {
        return (CloudDataHatchMachine.this.isFormed() && CloudDataHatchMachine.this.getControllers().first() instanceof DataBankMachine);
    }

    private @Nullable DataBankMachine getDataBank() {
        if (!isDataBank()) return null;

        return (DataBankMachine) this.getControllers().first();
    }

    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }

    private void rebuildData(boolean isDataBank) {
        if (this.getLevel() != null && !this.getLevel().isClientSide) {

            if (this.io == IO.OUT) {
                this.recipes.clear();

                DataBankMachine dataBank = getDataBank();
                List<IMultiPart> parts;

                try {
                    parts = dataBank.getParts();
                } catch (NullPointerException e) {
                    return;
                }

                List<DataAccessHatchMachine> hatches = parts.stream()
                        .filter(part -> part instanceof DataAccessHatchMachine)
                        .map(part -> (DataAccessHatchMachine) part)
                        .toList();


                for (DataAccessHatchMachine hatch : hatches) {
                    for (int i = 0; i < hatch.importItems.getSlots(); i++) {
                        ItemStack stack = hatch.importItems.getStackInSlot(i);
                        Pair<GTRecipeType, String> researchData = ResearchManager.readResearchId(stack);
                        boolean isValid = ResearchManager.isStackDataItem(stack, isDataBank);
                        if (researchData != null && isValid) {
                            Collection<GTRecipe> collection = ((GTRecipeType) researchData.getFirst()).getDataStickEntry((String) researchData.getSecond());
                            if (collection != null) {
                                this.recipes.addAll(collection);
                            }
                        }
                    }
                }
            } else if (this.io == IO.IN) {
                this.recipes.clear();
                for (CloudDataHatchMachine cloudHatch : VolatileData.getInstance().getDataHatches(GTWSavedData.get((ServerLevel) this.getLevel()).getWirelessHolder().getTeam(this.ownerUUID))) {
                    this.recipes.addAll(cloudHatch.recipes);
                }
            }
        }
    }

    public boolean isRecipeAvailable(@NotNull GTRecipe recipe, @NotNull Collection<IDataAccessHatch> seen) {
        if (this.io != IO.IN) {
            return false;
        }

        seen.add(this);
        Stream<RecipeCondition> conditions = recipe.conditions.stream();
        Objects.requireNonNull(ResearchCondition.class);


        return conditions.noneMatch(ResearchCondition.class::isInstance) || this.recipes.contains(recipe);
    }

    public @NotNull List<Component> getDataInfo(PortableScannerBehavior.DisplayMode mode) {
        if (mode != DisplayMode.SHOW_ALL && mode != DisplayMode.SHOW_RECIPE_INFO) {
            return new ArrayList();
        } else if (this.recipes.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<Component> list = new ArrayList();
            list.add(Component.translatable("behavior.data_item.assemblyline.title"));
            list.add(Component.empty());
            Collection<ItemStack> itemsAdded = new ObjectOpenCustomHashSet(ItemStackHashStrategy.comparingAll());

            for(GTRecipe recipe : this.recipes) {
                ItemStack stack = ((Ingredient)ItemRecipeCapability.CAP.of(((Content)recipe.getOutputContents(ItemRecipeCapability.CAP).get(0)).content)).getItems()[0];
                if (!itemsAdded.contains(stack)) {
                    itemsAdded.add(stack);
                    list.add(Component.translatable("behavior.data_item.assemblyline.data", new Object[]{stack.getDisplayName()}));
                }
            }

            return list;
        }
    }

    public boolean canShared() {
        return true;
    }

    public void addedToController(IMultiController controller) {
        this.rebuildData(controller instanceof DataBankMachine);
        super.addedToController(controller);
    }

    public GTRecipe modifyRecipe(GTRecipe recipe) {
        return super.modifyRecipe(recipe);
    }

    @Override
    public boolean isCreative() {
        return false;
    }

    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    static {
        MANAGED_FIELD_HOLDER = new ManagedFieldHolder(CloudDataHatchMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER);
    }
}
