package com.Deeakron.journey_mode.container;

import com.Deeakron.journey_mode.data.AntikytheraRecipe;
import com.Deeakron.journey_mode.data.AntikytheraShapelessRecipe;
import com.Deeakron.journey_mode.init.JMContainerTypes;
import com.Deeakron.journey_mode.init.JMRecipeSerializerInit;
import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.init.UnobtainBlockInit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.util.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.util.Optional;

import net.minecraft.world.inventory.RecipeBookMenu;

public class UnobtainiumAntikytheraContainer extends RecipeBookMenu {
    private final CraftingContainer craftMatrix = new CraftingContainer(this, 3, 3);
    private final ResultContainer craftResult = new ResultContainer();
    private final ContainerLevelAccess worldPosCallable;
    private final Player player;
    private boolean isShaped;
    private long lastOnTake;

    public UnobtainiumAntikytheraContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, ContainerLevelAccess.NULL);
    }

    public UnobtainiumAntikytheraContainer(int id, PlayerInventory playerInventory, final ContainerLevelAccess p_i50090_3_) {
        super(JMContainerTypes.UNOBTAINIUM_ANTIKYTHERA.get(), id);
        this.worldPosCallable = p_i50090_3_;
        this.player = playerInventory.player;

        this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35) {

            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
                    if(!player.level.isClientSide){player.playNotifySound(JMSounds.ANTIKYTHERA_CRAFT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);}
                this.checkTakeAchievements(stack);
                net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
                NonNullList<ItemStack> nonnulllist;
                if (isShaped) {
                    nonnulllist = thePlayer.level.getRecipeManager().getRemainingItemsFor(JMRecipeSerializerInit.RECIPE_TYPE_ANTIKYTHERA, craftMatrix, thePlayer.level);
                } else {
                    nonnulllist = thePlayer.level.getRecipeManager().getRemainingItemsFor(JMRecipeSerializerInit.RECIPE_TYPE_ANTIKYTHERA_SHAPELESS, craftMatrix, thePlayer.level);
                }
                net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
                for(int i = 0; i < nonnulllist.size(); ++i) {
                    ItemStack itemstack = craftMatrix.getItem(i);
                    ItemStack itemstack1 = nonnulllist.get(i);
                    if (!itemstack.isEmpty()) {
                        craftMatrix.removeItem(i, 1);
                        itemstack = craftMatrix.getItem(i);
                    }

                    if (!itemstack1.isEmpty()) {
                        if (itemstack.isEmpty()) {
                            craftMatrix.setItem(i, itemstack1);
                        } else if (ItemStack.isSame(itemstack, itemstack1) && ItemStack.tagMatches(itemstack, itemstack1)) {
                            itemstack1.grow(itemstack.getCount());
                            craftMatrix.setItem(i, itemstack1);
                        } else if (!player.inventory.add(itemstack1)) {
                            player.drop(itemstack1, false);
                        }
                    }
                }

                return stack;
            }

        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        for(int k = 0; k < 3; ++k) {
            for(int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for(int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
        }
    }

    public UnobtainiumAntikytheraContainer(int i, PlayerInventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(i, playerInventory);
    }

    public boolean stillValid(PlayerEntity playerIn) {
        return stillValid(this.worldPosCallable, playerIn, UnobtainBlockInit.UNOBTAINIUM_ANTIKYTHERA.get());
    }


    public void fillCraftSlotsStackedContents(RecipeItemHelper itemHelperIn) {
        this.craftMatrix.fillStackedContents(itemHelperIn);
    }


    public void clearCraftingContent() {
        this.craftMatrix.clearContent();
        this.craftResult.clearContent();
    }


    public boolean recipeMatches(IRecipe recipeIn) {
        return recipeIn.matches(this.craftMatrix, this.player.level);
    }

    public void removed(PlayerEntity playerIn) {
        super.removed(playerIn);
        this.clearContainer(playerIn, playerIn.level, this.craftMatrix);
    }


    public int getResultSlotIndex() {
        return 0;
    }


    public int getGridWidth() {
        return this.craftMatrix.getWidth();
    }


    public int getGridHeight() {
        return this.craftMatrix.getHeight();
    }


    public int getSize() {
        return 10;
    }


    public RecipeBookCategory getRecipeBookType() {
        return RecipeBookCategory.CRAFTING;
    }

    public void slotsChanged(IInventory inventoryIn) {
            this.broadcastChanges();
            this.updateCraftingResult(this.containerId, this.player.level, this.player, this.craftMatrix, this.craftResult);
    }


    public void updateCraftingResult(int id, Level world, PlayerEntity player, CraftingInventory inventory, CraftResultInventory inventoryResult) {
        if (!world.isClientSide) {
            ServerPlayer serverplayerentity = (ServerPlayer) player;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<AntikytheraRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(JMRecipeSerializerInit.RECIPE_TYPE_ANTIKYTHERA, inventory, world);

            if (optional.isPresent()) {
                ICraftingRecipe icraftingrecipe = optional.get();
                if (inventoryResult.setRecipeUsed(world, serverplayerentity, icraftingrecipe)) {
                    itemstack = icraftingrecipe.assemble(inventory);
                    isShaped = true;
                }
            } else {
                Optional<AntikytheraShapelessRecipe> optional2 = world.getServer().getRecipeManager().getRecipeFor(JMRecipeSerializerInit.RECIPE_TYPE_ANTIKYTHERA_SHAPELESS, inventory, world);
                if (optional2.isPresent()) {
                    ICraftingRecipe icraftingrecipe = optional2.get();
                    if (inventoryResult.setRecipeUsed(world, serverplayerentity, icraftingrecipe)) {
                        itemstack = icraftingrecipe.assemble(inventory);
                        isShaped = false;
                    }
                }
            }

            inventoryResult.setItem(0, itemstack);
            serverplayerentity.connection.send(new SSetSlotPacket(id, 0, itemstack));
        }
    }

    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 0) {
                    itemstack1.getItem().onCraftedBy(itemstack1, this.player.level, playerIn);
                if (!this.moveItemStackTo(itemstack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= 10 && index < 46) {
                if (!this.moveItemStackTo(itemstack1, 1, 10, false)) {
                    if (index < 37) {
                        if (!this.moveItemStackTo(itemstack1, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);
            if (index == 0) {
                playerIn.drop(itemstack2, false);
            }
        }
        this.slotsChanged(this.craftMatrix);

        return itemstack;
    }

    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
        return slotIn.container != this.craftResult && super.canTakeItemForPickAll(stack, slotIn);
    }


}
