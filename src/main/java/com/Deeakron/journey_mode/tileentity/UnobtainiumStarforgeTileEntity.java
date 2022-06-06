package com.Deeakron.journey_mode.BlockEntity;

import com.Deeakron.journey_mode.block.UnobtainiumStarforgeBlock;
import com.Deeakron.journey_mode.container.StarforgeContainer;
import com.Deeakron.journey_mode.container.StarforgeItemHandler;
import com.Deeakron.journey_mode.data.StarforgeRecipe;
import com.Deeakron.journey_mode.init.JMRecipeSerializerInit;
import com.Deeakron.journey_mode.init.JMBlockEntityTypes;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateBlockEntityPacket;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UnobtainiumStarforgeBlockEntity extends BlockEntity implements TickableBlockEntity, MenuProvider {

    private Component customName;
    public int currentSmeltTime = 0;
    public final int maxSmeltTime = 100;
    private StarforgeItemHandler inventory;
    public int currentFuelTime = 0;
    public final int maxFuelTime = 801;

    public UnobtainiumStarforgeBlockEntity(BlockEntityType<?> BlockEntityTypeIn) {
        super(BlockEntityTypeIn);

        this.inventory = new StarforgeItemHandler(3);
    }

    public UnobtainiumStarforgeBlockEntity() {
        this(JMBlockEntityTypes.UNOBTAINIUM_STARFORGE.get());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(final int windowId, final Inventory playerInv, final Player playerIn) {
        return new StarforgeContainer(windowId, playerInv, this);
    }

    @Override
    public void tick() {
        boolean dirty = false;
        boolean itemCheck = false;
        try {
            itemCheck = (this.inventory.getStackInSlot(1).isEmpty() || this.inventory.getStackInSlot(1).getItem().getRegistryName().equals(this.getRecipe(this.inventory.getStackInSlot(0)).getResultItem().getItem().getRegistryName()));
        } catch (NullPointerException e) {

        }

        if (this.level != null && !this.level.isClientSide) {
            if (this.currentFuelTime <= 0) {
                if (!this.getInventory().getStackInSlot(2).isEmpty() && this.getRecipe(this.inventory.getStackInSlot(0)) != null) {
                    this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(UnobtainiumStarforgeBlock.LIT, true));
                    this.inventory.decrStackSize(2, 1);
                    this.currentFuelTime = maxFuelTime;
                } else {
                    this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(UnobtainiumStarforgeBlock.LIT, false));
                    if(this.currentSmeltTime > 0) {
                        this.currentSmeltTime--;
                    }
                }
                dirty = true;
            } else {
                if (this.getRecipe(this.inventory.getStackInSlot(0)) != null && itemCheck) {
                    if (this.currentSmeltTime < this.maxSmeltTime) {
                        this.currentSmeltTime++;
                        this.currentFuelTime--;
                        dirty = true;
                    } else {
                        this.currentSmeltTime = 1;
                        this.currentFuelTime--;
                        ItemStack output = this.getRecipe(this.inventory.getStackInSlot(0)).getResultItem();
                        this.inventory.insertItem(1, output.copy(), false);
                        this.inventory.decrStackSize(0, 1);
                        dirty = true;
                    }
                } else {
                    if (this.currentSmeltTime > 0) {
                        this.currentSmeltTime = 0;
                    }
                    this.currentFuelTime--;
                    dirty = true;
                }
            }
        }

        if (dirty) {
            this.setChanged();
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    public void setCustomName(Component name) {
        this.customName = name;
    }

    public Component getName() {
        return this.customName != null ? this.customName : this.getDefaultName();
    }

    private Component getDefaultName() {
        return new TranslationTextComponent("container." + journey_mode.MODID + ".unobtainium_starforge");
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    public Component getCustomName() {
        return this.customName;
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        if(compound.contains("CustomName", Constants.NBT.TAG_STRING)) {
            this.customName = Component.Serializer.fromJson(compound.getString("CustomName"));
        }

        NonNullList<ItemStack> inv = NonNullList.<ItemStack>withSize(this.inventory.getSlots(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, inv);
        this.inventory.setNonNullList(inv);

        this.currentSmeltTime = compound.getInt("CurrentSmeltTime");
        this.currentFuelTime = compound.getInt("CurrentFuelTime");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        if (this.customName != null) {
            compound.putString("CustomName", Component.Serializer.toJson(this.customName));
        }

        ItemStackHelper.saveAllItems(compound, this.inventory.toNonNullList());
        compound.putInt("CurrentSmeltTime", this.currentSmeltTime);
        compound.putInt("CurrentFuelTime", this.currentFuelTime);

        return compound;
    }

    @Nullable
    public StarforgeRecipe getRecipe(ItemStack stack) {

        if (stack == null) {

            return null;
        }

        Set<IRecipe<?>> recipes = findRecipesByType(JMRecipeSerializerInit.RECIPE_TYPE, this.level);
        for (IRecipe<?> iRecipe : recipes) {
            StarforgeRecipe recipe = (StarforgeRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(this.inventory), this.level)) {
                return recipe;
            }
        }

        return null;
    };

    public static Set<IRecipe<?>> findRecipesByType(RecipeType<?> typeIn, Level world) {
        return world != null ? world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @OnlyIn(Dist.CLIENT)
    public static Set<IRecipe<?>> findRecipesByType(RecipeType<?> typeIn) {
        ClientWorld world = Minecraft.getInstance().level;
        return world != null ? world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }

    public static Set<ItemStack> getAllRecipeInputs(RecipeType<?> typeIn, Level worldIn) {
        Set<ItemStack> inputs = new HashSet<ItemStack>();
        Set<IRecipe<?>> recipes = findRecipesByType(typeIn, worldIn);
        for (IRecipe<?> recipe : recipes) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            ingredients.forEach(ingredient -> {
                for(ItemStack stack : ingredient.getItems()) {
                    inputs.add(stack);
                }
            });
        }
        return inputs;
    }

    public final IItemHandlerModifiable getInventory() {
        return this.inventory;
    }

    @Nullable
    @Override
    public SUpdateBlockEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        this.save(nbt);
        return new SUpdateBlockEntityPacket(this.worldPosition, 0, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateBlockEntityPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        this.save(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        this.load(state, nbt);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }
}
