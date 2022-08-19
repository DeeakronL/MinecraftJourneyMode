package com.Deeakron.journey_mode.tileentity;

import com.Deeakron.journey_mode.block.UnobtainiumStarforgeBlock;
import com.Deeakron.journey_mode.container.StarforgeContainer;
import com.Deeakron.journey_mode.container.StarforgeItemHandler;
import com.Deeakron.journey_mode.data.StarforgeRecipe;
import com.Deeakron.journey_mode.init.JMRecipeSerializerInit;
import com.Deeakron.journey_mode.init.JMTileEntityTypes;
import com.Deeakron.journey_mode.init.UnobtainBlockInit;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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

public class UnobtainiumStarforgeTileEntity extends BaseContainerBlockEntity implements  MenuProvider {

    private Component customName;
    public int currentSmeltTime = 0;
    public final int maxSmeltTime = 100;
    private StarforgeItemHandler inventory;
    public int currentFuelTime = 0;
    public final int maxFuelTime = 801;

    public UnobtainiumStarforgeTileEntity(BlockEntityType<?> blockEntityTypeIn, BlockPos pos, BlockState state) {
        super(blockEntityTypeIn, pos, state);
        journey_mode.LOGGER.info("hey tile entity is real");
        this.inventory = new StarforgeItemHandler(3);
    }

    /*public UnobtainiumStarforgeTileEntity() {
        this(JMTileEntityTypes.UNOBTAINIUM_STARFORGE.get());
    }*/

    public UnobtainiumStarforgeTileEntity(BlockPos pos, BlockState state) {
        super(JMTileEntityTypes.UNOBTAINIUM_STARFORGE.get(), pos, state);
        journey_mode.LOGGER.info("hey tile entity is real?");
        this.inventory = new StarforgeItemHandler(3);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(final int windowId, final Inventory playerInv) {
        return new StarforgeContainer(windowId, playerInv, this);
    }

    public void serverTick() {
        boolean dirty = false;
        boolean itemCheck = false;
        journey_mode.LOGGER.info("i guess it really does tick the server");
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

    public Component getDefaultName() {
        return new TranslatableComponent("container." + journey_mode.MODID + ".unobtainium_starforge");
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    public Component getCustomName() {
        return this.customName;
    }

    public void load(BlockState state, CompoundTag compound) {
        super.load(compound);
        if(compound.contains("CustomName", Constants.NBT.TAG_STRING)) {
            this.customName = Component.Serializer.fromJson(compound.getString("CustomName"));
        }

        NonNullList<ItemStack> inv = NonNullList.<ItemStack>withSize(this.inventory.getSlots(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, inv);
        this.inventory.setNonNullList(inv);

        this.currentSmeltTime = compound.getInt("CurrentSmeltTime");
        this.currentFuelTime = compound.getInt("CurrentFuelTime");
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        if (this.customName != null) {
            compound.putString("CustomName", Component.Serializer.toJson(this.customName));
        }

        ContainerHelper.saveAllItems(compound, this.inventory.toNonNullList());
        compound.putInt("CurrentSmeltTime", this.currentSmeltTime);
        compound.putInt("CurrentFuelTime", this.currentFuelTime);

        return compound;
    }

    @Nullable
    public StarforgeRecipe getRecipe(ItemStack stack) {

        if (stack == null) {

            return null;
        }

        Set<Recipe<?>> recipes = findRecipesByType(JMRecipeSerializerInit.RECIPE_TYPE, this.level);
        for (Recipe<?> iRecipe : recipes) {
            StarforgeRecipe recipe = (StarforgeRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(this.inventory), this.level)) {
                return recipe;
            }
        }

        return null;
    };

    public static Set<Recipe<?>> findRecipesByType(RecipeType<?> typeIn, Level world) {
        return world != null ? world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @OnlyIn(Dist.CLIENT)
    public static Set<Recipe<?>> findRecipesByType(RecipeType<?> typeIn) {
        ClientLevel world = Minecraft.getInstance().level;
        return world != null ? world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }

    public static Set<ItemStack> getAllRecipeInputs(RecipeType<?> typeIn, Level worldIn) {
        Set<ItemStack> inputs = new HashSet<ItemStack>();
        Set<Recipe<?>> recipes = findRecipesByType(typeIn, worldIn);
        for (Recipe<?> recipe : recipes) {
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
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag nbt = new CompoundTag();
        this.save(nbt);
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, nbt);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        this.save(nbt);
        return nbt;
    }

    public void handleUpdateTag(BlockState state, CompoundTag nbt) {
        this.load(state, nbt);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }

    @Override
    public int getContainerSize() {
        return this.inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return this.inventory.extractItem(slot, amount, true);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack itemStack = this.inventory.getStackInSlot(slot);
        this.inventory.removeStackFromSlot(slot);
        return itemStack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.inventory.setStackInSlot(slot, stack);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(player);
    }

    @Override
    public void clearContent() {

    }
}
