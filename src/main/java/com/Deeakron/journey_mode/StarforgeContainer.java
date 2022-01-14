package com.Deeakron.journey_mode;

import com.Deeakron.journey_mode.util.FunctionalIntReferenceHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

public class StarforgeContainer extends Container {

    private UnobtainiumStarforgeTileEntity tileEntity;
    private IWorldPosCallable canInteractWithCallable;
    public FunctionalIntReferenceHolder currentSmeltTime;
    public FunctionalIntReferenceHolder currentFuelTime;

    public StarforgeContainer(final int windowID, final PlayerInventory playerInv, final UnobtainiumStarforgeTileEntity tile) {
        super(JMContainerTypes.UNOBTAINIUM_STARFORGE.get(), windowID);
        this.canInteractWithCallable = IWorldPosCallable.of(tile.getWorld(), tile.getPos());
        this.tileEntity = tile;
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

        this.trackInt(currentSmeltTime = new FunctionalIntReferenceHolder(() -> this.tileEntity.currentSmeltTime, value -> this.tileEntity.currentSmeltTime = value));
        this.trackInt(currentFuelTime = new FunctionalIntReferenceHolder(() -> this.tileEntity.currentFuelTime, value -> this.tileEntity.currentFuelTime = value));
    }

    public StarforgeContainer(final int windowID, final PlayerInventory playerInv, final PacketBuffer data) {
        this(windowID, playerInv, getTileEntity(playerInv, data));
    }

    private static UnobtainiumStarforgeTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
        Objects.requireNonNull(playerInv, "plyerInv cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final TileEntity tileAtPos = playerInv.player.world.getTileEntity(data.readBlockPos());
        if (tileAtPos instanceof UnobtainiumStarforgeTileEntity) {
            return (UnobtainiumStarforgeTileEntity) tileAtPos;
        }
        throw new IllegalStateException("TileEntity is not correct " + tileAtPos);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(canInteractWithCallable, playerIn, UnobtainBlockInit.UNOBTAINIUM_STARFORGE.get());
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(final PlayerEntity player, final int index) {
        ItemStack returnStack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack slotStack = slot.getStack();
            returnStack = slotStack.copy();

            final int containerSlots = this.inventorySlots.size() - player.inventory.mainInventory.size();
            if (index < containerSlots) {
                if (!mergeItemStack(slotStack, containerSlots, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(slotStack, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (slotStack.getCount() == returnStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, slotStack);
        }
        return returnStack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getSmeltProgressionScaled() {
        return this.currentSmeltTime.get() != 0 && this.tileEntity.maxSmeltTime != 0 ? this.currentSmeltTime.get() * 24 / this.tileEntity.maxSmeltTime : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getFuelUsageScaled() {
        return this.currentFuelTime.get() != 0 && this.tileEntity.maxFuelTime != 0 ? this.currentFuelTime.get() * 12 / this.tileEntity.maxFuelTime : 0;
    }
}
