package com.Deeakron.journey_mode.container;

import com.Deeakron.journey_mode.init.JMContainerTypes;
import com.Deeakron.journey_mode.init.UnobtainBlockInit;
import com.Deeakron.journey_mode.tileentity.UnobtainiumStarforgeTileEntity;
import com.Deeakron.journey_mode.util.FunctionalIntReferenceHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

public class StarforgeContainer extends AbstractContainerMenu {

    private UnobtainiumStarforgeTileEntity BlockEntity;
    private ContainerLevelAccess canInteractWithCallable;
    public FunctionalIntReferenceHolder currentSmeltTime;
    public FunctionalIntReferenceHolder currentFuelTime;

    public StarforgeContainer(final int windowID, final Inventory playerInv, final UnobtainiumStarforgeTileEntity tile) {
        super(JMContainerTypes.UNOBTAINIUM_STARFORGE.get(), windowID);
        this.canInteractWithCallable = ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos());
        this.BlockEntity = tile;
        final int slotSizePlus2 = 18;
        final int startX = 8;

        int hotbarY = 142;
        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(playerInv, column, startX + (column * slotSizePlus2), hotbarY));
        }

        final int startY = 84;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(playerInv, 9 + (row * 9) + column, startX + (column * slotSizePlus2), startY + (row * slotSizePlus2)));
            }
        }

        // Starforge Input
        this.addSlot(new SlotItemHandler(tile.getInventory(), 0, 56, 17));
        // Starforge Output
        this.addSlot(new StarforgeResultSlot(tile.getInventory(), 1, 116, 35));
        // Starforge Fuel
        this.addSlot(new StarforgeFuelSlot(tile.getInventory(),2, 56, 53));

        this.addDataSlot(currentSmeltTime = new FunctionalIntReferenceHolder(() -> this.BlockEntity.currentSmeltTime, value -> this.BlockEntity.currentSmeltTime = value));
        this.addDataSlot(currentFuelTime = new FunctionalIntReferenceHolder(() -> this.BlockEntity.currentFuelTime, value -> this.BlockEntity.currentFuelTime = value));
    }

    public StarforgeContainer(final int windowID, final Inventory playerInv, final FriendlyByteBuf data) {
        this(windowID, playerInv, getBlockEntity(playerInv, data));
    }

    private static UnobtainiumStarforgeTileEntity getBlockEntity(final Inventory playerInv, final FriendlyByteBuf data) {
        Objects.requireNonNull(playerInv, "plyerInv cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final BlockEntity tileAtPos = playerInv.player.level.getBlockEntity(data.readBlockPos());
        if (tileAtPos instanceof UnobtainiumStarforgeTileEntity) {
            return (UnobtainiumStarforgeTileEntity) tileAtPos;
        }
        throw new IllegalStateException("BlockEntity is not correct " + tileAtPos);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(canInteractWithCallable, playerIn, UnobtainBlockInit.UNOBTAINIUM_STARFORGE.get());
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(final Player player, final int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 37) {
                if (!this.moveItemStackTo(itemstack1, 0, 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index != 38 && index != 36) {
                if (this.BlockEntity.getRecipe(itemstack1) != null) {
                    if (!this.moveItemStackTo(itemstack1, 36, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.BlockEntity.getInventory().isItemValid(38, itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 38, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 9 && index < 36) {
                    if (!this.moveItemStackTo(itemstack1, 0, 9, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 0 && index < 9 && !this.moveItemStackTo(itemstack1, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
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

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getSmeltProgressionScaled() {
        return this.currentSmeltTime.get() != 0 && this.BlockEntity.maxSmeltTime != 0 ? this.currentSmeltTime.get() * 24 / this.BlockEntity.maxSmeltTime : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getFuelUsageScaled() {
        return this.currentFuelTime.get() != 0 && this.BlockEntity.maxFuelTime != 0 ? this.currentFuelTime.get() * 12 / this.BlockEntity.maxFuelTime : 0;
    }
}
