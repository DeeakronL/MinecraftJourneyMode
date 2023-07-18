package com.Deeakron.journey_mode.tileentity;

import com.Deeakron.journey_mode.block.UnobtainiumStarforgeBlock;
import com.Deeakron.journey_mode.container.StarforgeContainer;
import com.Deeakron.journey_mode.container.StarforgeItemHandler;
import com.Deeakron.journey_mode.data.StarforgeRecipe;
import com.Deeakron.journey_mode.init.JMRecipeSerializerInit;
import com.Deeakron.journey_mode.init.JMTileEntityTypes;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
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
        this.inventory = new StarforgeItemHandler(3);
    }

    public UnobtainiumStarforgeTileEntity(BlockPos pos, BlockState state) {
        super(JMTileEntityTypes.UNOBTAINIUM_STARFORGE.get(), pos, state);
        this.inventory = new StarforgeItemHandler(3);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(final int windowId, final Inventory playerInv) {
        return new StarforgeContainer(windowId, playerInv, this);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, UnobtainiumStarforgeTileEntity tile) {
        boolean dirty = false;
        boolean itemCheck = false;
        try {
            itemCheck = (tile.inventory.getStackInSlot(1).isEmpty() || tile.inventory.getStackInSlot(1).getItem().getRegistryName().equals(tile.getRecipe(tile.inventory.getStackInSlot(0)).getResultItem().getItem().getRegistryName()));
        } catch (NullPointerException e) {

        }

        if (tile.level != null && !tile.level.isClientSide) {
            if (tile.currentFuelTime <= 0) {
                if (!tile.getInventory().getStackInSlot(2).isEmpty() && tile.getRecipe(tile.inventory.getStackInSlot(0)) != null) {
                    tile.level.setBlockAndUpdate(tile.getBlockPos(), tile.getBlockState().setValue(UnobtainiumStarforgeBlock.LIT, true));
                    tile.inventory.decrStackSize(2, 1);
                    tile.currentFuelTime = tile.maxFuelTime;
                } else {
                    tile.level.setBlockAndUpdate(tile.getBlockPos(), tile.getBlockState().setValue(UnobtainiumStarforgeBlock.LIT, false));
                    if(tile.currentSmeltTime > 0) {
                        tile.currentSmeltTime--;
                    }
                }
                dirty = true;
            } else {
                if (tile.getRecipe(tile.inventory.getStackInSlot(0)) != null && itemCheck) {
                    if (tile.currentSmeltTime < tile.maxSmeltTime) {
                        tile.currentSmeltTime++;
                        tile.currentFuelTime--;
                        dirty = true;
                    } else {
                        tile.currentSmeltTime = 1;
                        tile.currentFuelTime--;
                        ItemStack output = tile.getRecipe(tile.inventory.getStackInSlot(0)).getResultItem();
                        tile.inventory.insertItem(1, output.copy(), false);
                        tile.inventory.decrStackSize(0, 1);
                        dirty = true;
                    }
                } else {
                    if (tile.currentSmeltTime > 0) {
                        tile.currentSmeltTime = 0;
                    }
                    tile.currentFuelTime--;
                    dirty = true;
                }
            }
        }

        if (dirty) {
            tile.setChanged();
            tile.level.sendBlockUpdated(tile.getBlockPos(), tile.getBlockState(), tile.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public void setCustomName(Component name) {
        this.customName = name;
    }

    public Component getName() {
        return this.customName != null ? this.customName : this.getDefaultName();
    }

    public Component getDefaultName() {
        return Component.translatable("container." + journey_mode.MODID + ".unobtainium_starforge");
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    public Component getCustomName() {
        return this.customName;
    }

    public void load(CompoundTag compound) {
        super.load(compound);
        if(compound.contains("CustomName", Tag.TAG_STRING)) {
            this.customName = Component.Serializer.fromJson(compound.getString("CustomName"));
        }

        NonNullList<ItemStack> inv = NonNullList.<ItemStack>withSize(this.inventory.getSlots(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, inv);
        this.inventory.setNonNullList(inv);

        this.currentSmeltTime = compound.getInt("CurrentSmeltTime");
        this.currentFuelTime = compound.getInt("CurrentFuelTime");
    }

    public CompoundTag save(CompoundTag compound) {
        super.saveAdditional(compound);
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

        Set<Recipe<?>> recipes = findRecipesByType(StarforgeRecipe.Type.INSTANCE, this.level);
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

    //review this later in case it breaks
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        this.save(nbt);
        return nbt;
    }

    public void handleUpdateTag(BlockState state, CompoundTag nbt) {
        this.load(nbt);
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
