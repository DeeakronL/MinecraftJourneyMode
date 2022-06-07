package com.Deeakron.journey_mode.container;

import com.Deeakron.journey_mode.init.JMContainerTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;

public class JourneyModeResearchContainer extends AbstractContainerMenu {
    private final Container researchInventory = new SimpleContainer(1);
    public JourneyModeResearchContainer(final int windowId, final Inventory Inventory) {
        super(JMContainerTypes.JOURNEY_MODE_RESEARCH.get(), windowId);

        //Main Inventory
        int startX = 8;
        int startY = 18;
        int slotSizePlus2 = 18;
        this.addSlot(new Slot(this.researchInventory, 0, 80, 39));

        //Main Player Inventory
        int startPlayerInvY = startY * 5 + 12;
        for(int row = 0; row < 3; ++row) {
            for(int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(Inventory, 9 + (row * 9) + col, startX + (col * slotSizePlus2), startPlayerInvY + (row * slotSizePlus2)));
            }
        }

        //Hotbar
        int hotbarY = startPlayerInvY + (startPlayerInvY/2) + 7;
        for(int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(Inventory, col, startX + (col * slotSizePlus2), hotbarY));
        }

    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    public JourneyModeResearchContainer(final int windowId, final Inventory Inventory, final FriendlyByteBuf data) {
        this(windowId, Inventory);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if(slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if(index < 1) {
                if(!this.moveItemStackTo(itemstack1, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if(!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.clearContainer(playerIn, this.researchInventory);
    }
}
