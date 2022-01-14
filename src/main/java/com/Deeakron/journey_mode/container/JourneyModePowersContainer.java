package com.Deeakron.journey_mode.container;

import com.Deeakron.journey_mode.init.JMContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class JourneyModePowersContainer extends Container {
    public JourneyModePowersContainer(final int windowId, final PlayerInventory playerInventory) {
        super(JMContainerTypes.JOURNEY_MODE_POWERS.get(), windowId);

        //Main Inventory
        int startX = 8;
        int startY = 18;
        int slotSizePlus2 = 18;
        //for(int row = 0; row < 4; ++row) {
        //    for(int col = 0; col < 9; ++col) {
        //        this.addSlot(new Slot(tileEntity, (row * 9) + col, startX + (col * slotSizePlus2), startY + (row*slotSizePlus2)));
        //    }
        //}

        //Main Player Inventory
        int startPlayerInvY = startY * 5 + 12;
        for(int row = 0; row < 3; ++row) {
            for(int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, 9 + (row * 9) + col, startX + (col * slotSizePlus2), startPlayerInvY + (row * slotSizePlus2)));
            }
        }

        //Hotbar
        int hotbarY = startPlayerInvY + (startPlayerInvY/2) + 7;
        for(int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, startX + (col * slotSizePlus2), hotbarY));
        }

    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public JourneyModePowersContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, playerInventory);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(index < 36) {
                if(!this.mergeItemStack(itemstack1, 36, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if(!this.mergeItemStack(itemstack1, 0, 36, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
}
