package com.Deeakron.journey_mode.container;

import com.Deeakron.journey_mode.init.JMMenuTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;

public class JourneyModeRecipesContainer extends AbstractContainerMenu {
    public Container recipeInputInventory = new SimpleContainer(9);
    private Container recipeOutputInventory = new SimpleContainer(1);
    private Container recipe2InputInventory = new SimpleContainer(9);
    private Container recipe2OutputInventory = new SimpleContainer(1);
    public JourneyModeRecipesContainer(final int windowId) {
        super(JMMenuTypes.JOURNEY_MODE_RECIPES.get(), windowId);

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
    public boolean stillValid(Player playerIn) {
        return true;
    }

    public JourneyModeRecipesContainer(final int windowId, final Inventory Inventory, final FriendlyByteBuf data) {
        this(windowId);
    }

    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.clearContainer(playerIn, this.recipeInputInventory);
        this.clearContainer(playerIn, this.recipeOutputInventory);
        this.clearContainer(playerIn, this.recipe2InputInventory);
        this.clearContainer(playerIn, this.recipe2OutputInventory);
    }

    public void insertItem(ItemStack[] items, int recipe) {
        if (recipe == 1) {
            for (int i = 0; i < 9; i++) {
                this.recipeInputInventory.setItem(i, items[i]);
            }
            this.recipeOutputInventory.setItem(0, items[9]);
        } else if (recipe == 2) {
            for (int i = 0; i < 9; i++) {
                this.recipe2InputInventory.setItem(i, items[i]);
            }
            this.recipe2OutputInventory.setItem(0, items[9]);
        }
        this.broadcastChanges();
    }
}
