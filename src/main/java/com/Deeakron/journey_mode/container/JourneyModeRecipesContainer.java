package com.Deeakron.journey_mode.container;

import com.Deeakron.journey_mode.init.JMContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class JourneyModeRecipesContainer extends Container {
    public IInventory recipeInputInventory = new Inventory(9);
    private IInventory recipeOutputInventory = new Inventory(1);
    private IInventory recipe2InputInventory = new Inventory(9);
    private IInventory recipe2OutputInventory = new Inventory(1);
    public JourneyModeRecipesContainer(final int windowId) {
        super(JMContainerTypes.JOURNEY_MODE_RECIPES.get(), windowId);

        //Recipe 1 input
        int startX = 30;
        int startY = 17;
        int slotSizePlus2 = 18;
        for(int row = 0; row < 3; ++row) {
            for(int col = 0; col < 3; ++col) {
                this.addSlot(new Slot(this.recipeInputInventory, (row * 3) + col, startX + (col * slotSizePlus2), startY + (row * slotSizePlus2)));
            }
        }
        this.addSlot(new Slot(this.recipeOutputInventory, 0, 124, 35));

        //Recipe 2 input
        int newStartX = 30;
        int newStartY = 95;
        for(int row = 0; row < 3; ++row) {
            for(int col = 0; col < 3; ++col) {
                this.addSlot(new Slot(this.recipe2InputInventory, (row * 3) + col, newStartX + (col * slotSizePlus2), newStartY + (row * slotSizePlus2)));
            }
        }
        this.addSlot(new Slot(this.recipe2OutputInventory, 0, 124, 113));


    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public JourneyModeRecipesContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId);
    }

    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.clearContainer(playerIn, playerIn.world, this.recipeInputInventory);
        this.clearContainer(playerIn, playerIn.world, this.recipeOutputInventory);
        this.clearContainer(playerIn, playerIn.world, this.recipe2InputInventory);
        this.clearContainer(playerIn, playerIn.world, this.recipe2OutputInventory);
    }

    public void insertItem(ItemStack[] items, int recipe) {
        if (recipe == 1) {
            for (int i = 0; i < 9; i++) {
                this.recipeInputInventory.setInventorySlotContents(i, items[i]);
            }
            this.recipeOutputInventory.setInventorySlotContents(0, items[9]);
        } else if (recipe == 2) {
            for (int i = 0; i < 9; i++) {
                this.recipe2InputInventory.setInventorySlotContents(i, items[i]);
            }
            this.recipe2OutputInventory.setInventorySlotContents(0, items[9]);
        }
        this.detectAndSendChanges();
    }
}
