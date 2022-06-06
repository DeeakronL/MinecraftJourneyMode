package com.Deeakron.journey_mode.container;

import com.Deeakron.journey_mode.data.AntikytheraRecipe;
import com.Deeakron.journey_mode.data.AntikytheraShapelessRecipe;
import com.Deeakron.journey_mode.init.JMMenuTypes;
import com.Deeakron.journey_mode.init.JMRecipeSerializerInit;
import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.init.UnobtainBlockInit;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class UnobtainiumAntikytheraContainer extends RecipeBookMenu {
    private final CraftingContainer craftMatrix = new CraftingContainer(this, 3, 3);
    private final ResultContainer craftResult = new ResultContainer();
    private final ContainerLevelAccess worldPosCallable;
    private final Player player;
    private boolean isShaped;
    private long lastOnTake;

    public UnobtainiumAntikytheraContainer(int id, Inventory Inventory) {
        this(id, Inventory, ContainerLevelAccess.NULL);
    }

    public UnobtainiumAntikytheraContainer(int id, Inventory Inventory, final ContainerLevelAccess p_i50090_3_) {
        super(JMMenuTypes.UNOBTAINIUM_ANTIKYTHERA.get(), id);
        this.worldPosCallable = p_i50090_3_;
        this.player = Inventory.player;

        this.addSlot(new ResultSlot(Inventory.player, this.craftMatrix, this.craftResult, 0, 124, 35) {

            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            //onTake originally returned item stack, seems that a change was needed?
            public void onTake(Player thePlayer, ItemStack stack) {
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
                        } else if (!player.getInventory().add(itemstack1)) {
                            player.drop(itemstack1, false);
                        }
                    }
                }
            }

        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        for(int k = 0; k < 3; ++k) {
            for(int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(Inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for(int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(Inventory, l, 8 + l * 18, 142));
        }
    }

    public UnobtainiumAntikytheraContainer(int i, Inventory Inventory, FriendlyByteBuf packetBuffer) {
        this(i, Inventory);
    }

    public boolean stillValid(Player playerIn) {
        return stillValid(this.worldPosCallable, playerIn, UnobtainBlockInit.UNOBTAINIUM_ANTIKYTHERA.get());
    }


    public void fillCraftSlotsStackedContents(StackedContents itemHelperIn) {
        this.craftMatrix.fillStackedContents(itemHelperIn);
    }


    public void clearCraftingContent() {
        this.craftMatrix.clearContent();
        this.craftResult.clearContent();
    }


    public boolean recipeMatches(Recipe recipeIn) {
        return recipeIn.matches(this.craftMatrix, this.player.level);
    }

    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.clearContainer(playerIn, this.craftMatrix);
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


    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public boolean shouldMoveToInventory(int p_150635_) {
        return false;
    }

    public void slotsChanged(Inventory inventoryIn) {
            this.broadcastChanges();
            this.updateCraftingResult(this.containerId, this.player.level, this.player, this.craftMatrix, this.craftResult);
    }


    public void updateCraftingResult(int id, Level world, Player player, CraftingContainer inventory, ResultContainer inventoryResult) {
        if (!world.isClientSide) {
            ServerPlayer serverplayerentity = (ServerPlayer) player;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<AntikytheraRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(JMRecipeSerializerInit.RECIPE_TYPE_ANTIKYTHERA, inventory, world);

            if (optional.isPresent()) {
                CraftingRecipe icraftingrecipe = optional.get();
                if (inventoryResult.setRecipeUsed(world, serverplayerentity, icraftingrecipe)) {
                    itemstack = icraftingrecipe.assemble(inventory);
                    isShaped = true;
                }
            } else {
                Optional<AntikytheraShapelessRecipe> optional2 = world.getServer().getRecipeManager().getRecipeFor(JMRecipeSerializerInit.RECIPE_TYPE_ANTIKYTHERA_SHAPELESS, inventory, world);
                if (optional2.isPresent()) {
                    CraftingRecipe icraftingrecipe = optional2.get();
                    if (inventoryResult.setRecipeUsed(world, serverplayerentity, icraftingrecipe)) {
                        itemstack = icraftingrecipe.assemble(inventory);
                        isShaped = false;
                    }
                }
            }

            inventoryResult.setItem(0, itemstack);
            serverplayerentity.connection.send(new ClientboundContainerSetSlotPacket(id, 0, 0, itemstack));
        }
    }

    public ItemStack quickMoveStack(Player playerIn, int index) {
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

            slot.onTake(playerIn, itemstack1);
            if (index == 0) {
                playerIn.drop(itemstack1, false);
            }
        }
        this.slotsChanged(this.craftMatrix);

        return itemstack;
    }

    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
        return slotIn.container != this.craftResult && super.canTakeItemForPickAll(stack, slotIn);
    }


}
