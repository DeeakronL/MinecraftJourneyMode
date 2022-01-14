package com.Deeakron.journey_mode.tileentity;

import com.Deeakron.journey_mode.block.UnobtainiumStarforgeBlock;
import com.Deeakron.journey_mode.container.StarforgeContainer;
import com.Deeakron.journey_mode.container.StarforgeItemHandler;
import com.Deeakron.journey_mode.data.StarforgeRecipe;
import com.Deeakron.journey_mode.init.JMRecipeSerializerInit;
import com.Deeakron.journey_mode.init.JMTileEntityTypes;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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

public class UnobtainiumStarforgeTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    private ITextComponent customName;
    public int currentSmeltTime = 0;
    public final int maxSmeltTime = 100;
    private StarforgeItemHandler inventory;
    public int currentFuelTime = 0;
    public final int maxFuelTime = 801;

    public UnobtainiumStarforgeTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);

        this.inventory = new StarforgeItemHandler(3);
    }

    public UnobtainiumStarforgeTileEntity() {
        this(JMTileEntityTypes.UNOBTAINIUM_STARFORGE.get());
    }

    @Nullable
    @Override
    public Container createMenu(final int windowId, final PlayerInventory playerInv, final PlayerEntity playerIn) {
        return new StarforgeContainer(windowId, playerInv, this);
    }

    @Override
    public void tick() {
        boolean dirty = false;
        boolean itemCheck = false;
        try {
            itemCheck = (this.inventory.getStackInSlot(1).isEmpty() || this.inventory.getStackInSlot(1).getItem().getRegistryName().equals(this.getRecipe(this.inventory.getStackInSlot(0)).getRecipeOutput().getItem().getRegistryName()));
        } catch (NullPointerException e) {

        }

        if (this.world != null && !this.world.isRemote) {
            if (this.currentFuelTime <= 0) {
                if (!this.getInventory().getStackInSlot(2).isEmpty() && this.getRecipe(this.inventory.getStackInSlot(0)) != null) {
                    this.world.setBlockState(this.getPos(), this.getBlockState().with(UnobtainiumStarforgeBlock.LIT, true));
                    this.inventory.decrStackSize(2, 1);
                    this.currentFuelTime = maxFuelTime;
                } else {
                    this.world.setBlockState(this.getPos(), this.getBlockState().with(UnobtainiumStarforgeBlock.LIT, false));
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
                        ItemStack output = this.getRecipe(this.inventory.getStackInSlot(0)).getRecipeOutput();
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
            this.markDirty();
            this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    public void setCustomName(ITextComponent name) {
        this.customName = name;
    }

    public ITextComponent getName() {
        return this.customName != null ? this.customName : this.getDefaultName();
    }

    private ITextComponent getDefaultName() {
        return new TranslationTextComponent("container." + journey_mode.MODID + ".unobtainium_starforge");
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.getName();
    }

    @Nullable
    public ITextComponent getCustomName() {
        return this.customName;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        if(compound.contains("CustomName", Constants.NBT.TAG_STRING)) {
            this.customName = ITextComponent.Serializer.getComponentFromJson(compound.getString("CustomName"));
        }

        NonNullList<ItemStack> inv = NonNullList.<ItemStack>withSize(this.inventory.getSlots(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, inv);
        this.inventory.setNonNullList(inv);

        this.currentSmeltTime = compound.getInt("CurrentSmeltTime");
        this.currentFuelTime = compound.getInt("CurrentFuelTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (this.customName != null) {
            compound.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
        }

        ItemStackHelper.saveAllItems(compound, this.inventory.toNonNullList());
        compound.putInt("CurrentSmeltTime", this.currentSmeltTime);
        compound.putInt("CurrentFuelTime", this.currentFuelTime);

        return compound;
    }

    @Nullable
    private StarforgeRecipe getRecipe(ItemStack stack) {
        if (stack == null) {
            return null;
        }

        Set<IRecipe<?>> recipes = findRecipesByType(JMRecipeSerializerInit.RECIPE_TYPE, this.world);
        for (IRecipe<?> iRecipe : recipes) {
            StarforgeRecipe recipe = (StarforgeRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(this.inventory), this.world)) {
                return recipe;
            }
        }

        return null;
    };

    public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn, World world) {
        return world != null ? world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @OnlyIn(Dist.CLIENT)
    public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn) {
        ClientWorld world = Minecraft.getInstance().world;
        return world != null ? world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }

    public static Set<ItemStack> getAllRecipeInputs(IRecipeType<?> typeIn, World worldIn) {
        Set<ItemStack> inputs = new HashSet<ItemStack>();
        Set<IRecipe<?>> recipes = findRecipesByType(typeIn, worldIn);
        for (IRecipe<?> recipe : recipes) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            ingredients.forEach(ingredient -> {
                for(ItemStack stack : ingredient.getMatchingStacks()) {
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
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        return new SUpdateTileEntityPacket(this.pos, 0, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(this.getBlockState(), pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        this.read(state, nbt);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }
}
