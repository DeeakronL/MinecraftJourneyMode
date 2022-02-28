package com.Deeakron.journey_mode.container;

import com.Deeakron.journey_mode.*;
import com.Deeakron.journey_mode.data.AntikytheraRecipe;
import com.Deeakron.journey_mode.data.AntikytheraShapelessRecipe;
import com.Deeakron.journey_mode.data.recipebook.JMRecipeBookCategory;
import com.Deeakron.journey_mode.data.recipebook.JMRecipeBookContainer;
import com.Deeakron.journey_mode.init.JMContainerTypes;
import com.Deeakron.journey_mode.init.JMRecipeSerializerInit;
import com.Deeakron.journey_mode.init.JMSounds;
import com.Deeakron.journey_mode.init.UnobtainBlockInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import java.util.Optional;

public class UnobtainiumAntikytheraContainer extends RecipeBookContainer {
    private final CraftingInventory craftMatrix = new CraftingInventory(this, 3, 3);
    private final CraftResultInventory craftResult = new CraftResultInventory();
    private final IWorldPosCallable worldPosCallable;
    private final PlayerEntity player;
    private boolean isShaped;
    private long lastOnTake;

    public UnobtainiumAntikytheraContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, IWorldPosCallable.DUMMY);
        //journey_mode.LOGGER.info("this was the culprit");
    }

    public UnobtainiumAntikytheraContainer(int id, PlayerInventory playerInventory, final IWorldPosCallable p_i50090_3_) {
        super(JMContainerTypes.UNOBTAINIUM_ANTIKYTHERA.get(), id);
        this.worldPosCallable = p_i50090_3_;
        this.player = playerInventory.player;

        this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35) {

            public boolean isItemValid(ItemStack stack) {
                return false;
            }

            public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
                    //journey_mode.LOGGER.info(worldPosCallable);
                    //player.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.0F, 1.0F);
                    if(!player.world.isRemote){player.playSound(JMSounds.ANTIKYTHERA_CRAFT.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);}

                    //journey_mode.LOGGER.info(Sound);
                /*worldPosCallable.consume((p_216954_1_, p_216954_2_) -> {
                    long l = p_216954_1_.getGameTime();
                    if (UnobtainiumAntikytheraContainer.this.lastOnTake != l) {
                        p_216954_1_.playSound((PlayerEntity) null, p_216954_2_, JMSounds.ANTIKYTHERA_CRAFT.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                        journey_mode.LOGGER.info("insert sound here");
                        UnobtainiumAntikytheraContainer.this.lastOnTake = l;
                    }


                });*/
                    //journey_mode.LOGGER.info("taken");
                    //p_234633_1_.playEvent(1029, p_234633_2_, 0);
                
                this.onCrafting(stack);
                net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
                NonNullList<ItemStack> nonnulllist;
                if (isShaped) {
                    nonnulllist = thePlayer.world.getRecipeManager().getRecipeNonNull(JMRecipeSerializerInit.RECIPE_TYPE_ANTIKYTHERA, craftMatrix, thePlayer.world);
                } else {
                    nonnulllist = thePlayer.world.getRecipeManager().getRecipeNonNull(JMRecipeSerializerInit.RECIPE_TYPE_ANTIKYTHERA_SHAPELESS, craftMatrix, thePlayer.world);
                }
                net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
                for(int i = 0; i < nonnulllist.size(); ++i) {
                    ItemStack itemstack = craftMatrix.getStackInSlot(i);
                    ItemStack itemstack1 = nonnulllist.get(i);
                    if (!itemstack.isEmpty()) {
                        craftMatrix.decrStackSize(i, 1);
                        itemstack = craftMatrix.getStackInSlot(i);
                    }

                    if (!itemstack1.isEmpty()) {
                        if (itemstack.isEmpty()) {
                            craftMatrix.setInventorySlotContents(i, itemstack1);
                        } else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
                            itemstack1.grow(itemstack.getCount());
                            craftMatrix.setInventorySlotContents(i, itemstack1);
                        } else if (!player.inventory.addItemStackToInventory(itemstack1)) {
                            player.dropItem(itemstack1, false);
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

    public UnobtainiumAntikytheraContainer(int i, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        this(i, playerInventory);
    }

    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, UnobtainBlockInit.UNOBTAINIUM_ANTIKYTHERA.get());
    }


    public void fillStackedContents(RecipeItemHelper itemHelperIn) {
        this.craftMatrix.fillStackedContents(itemHelperIn);
    }


    public void clear() {
        this.craftMatrix.clear();
        this.craftResult.clear();
    }


    public boolean matches(IRecipe recipeIn) {
        return recipeIn.matches(this.craftMatrix, this.player.world);
    }

    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        //this.worldPosCallable.consume((p_217068_2_, p_217068_3_) -> {
        this.clearContainer(playerIn, playerIn.world, this.craftMatrix);
        //});*/
    }


    public int getOutputSlot() {
        return 0;
    }


    public int getWidth() {
        return this.craftMatrix.getWidth();
    }


    public int getHeight() {
        return this.craftMatrix.getHeight();
    }


    public int getSize() {
        return 10;
    }


    public RecipeBookCategory func_241850_m() {
        return RecipeBookCategory.CRAFTING;
    }

    public void onCraftMatrixChanged(IInventory inventoryIn) {
        //journey_mode.LOGGER.info("part 1");
        //this.worldPosCallable.consume((p_217069_1_, p_217069_2_) -> {
            //journey_mode.LOGGER.info("part 2");
            this.detectAndSendChanges();
            this.updateCraftingResult(this.windowId, this.player.world, this.player, this.craftMatrix, this.craftResult);
        //});
    }


    public void updateCraftingResult(int id, World world, PlayerEntity player, CraftingInventory inventory, CraftResultInventory inventoryResult) {
        //journey_mode.LOGGER.info("part 3");
        if (!world.isRemote) {
            //journey_mode.LOGGER.info("part 4");
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)player;
            ItemStack itemstack = ItemStack.EMPTY;
            //journey_mode.LOGGER.info(IJMRecipes.CRAFTING_ANTIKYTHERA.);
            //journey_mode.LOGGER.info(world.getServer().getRecipeManager().getRecipes(IJMRecipes.CRAFTING_ANTIKYTHERA.matches(), inventory, world));
            Optional<AntikytheraRecipe> optional = world.getServer().getRecipeManager().getRecipe(JMRecipeSerializerInit.RECIPE_TYPE_ANTIKYTHERA, inventory, world);

            //optional.get().
            //journey_mode.LOGGER.info(optional);
            //journey_mode.LOGGER.info(world.getServer().getRecipeManager().getRecipe());
            if (optional.isPresent()) {
                //journey_mode.LOGGER.info(optional.get().group + " is the group");
                ICraftingRecipe icraftingrecipe = optional.get();
                if (inventoryResult.canUseRecipe(world, serverplayerentity, icraftingrecipe)) {
                    itemstack = icraftingrecipe.getCraftingResult(inventory);
                    isShaped = true;
                }
            } else {
                //journey_mode.LOGGER.info("Second optional slot reached");
                Optional<AntikytheraShapelessRecipe> optional2 = world.getServer().getRecipeManager().getRecipe(JMRecipeSerializerInit.RECIPE_TYPE_ANTIKYTHERA_SHAPELESS, inventory, world);
                //journey_mode.LOGGER.info(world.getServer().getRecipeManager().getRecipes());
                if (optional2.isPresent()) {
                    //journey_mode.LOGGER.info(optional2.get().group + " is the group");
                    ICraftingRecipe icraftingrecipe = optional2.get();
                    if (inventoryResult.canUseRecipe(world, serverplayerentity, icraftingrecipe)) {
                        itemstack = icraftingrecipe.getCraftingResult(inventory);
                        isShaped = false;
                    }
                }
            }

            inventoryResult.setInventorySlotContents(0, itemstack);
            serverplayerentity.connection.sendPacket(new SSetSlotPacket(id, 0, itemstack));
        }
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        //journey_mode.LOGGER.info("problem?");
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 0) {
                //this.worldPosCallable.consume((p_217067_2_, p_217067_3_) -> {
                    itemstack1.getItem().onCreated(itemstack1, this.player.world, playerIn);
                //});
                if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index >= 10 && index < 46) {
                if (!this.mergeItemStack(itemstack1, 1, 10, false)) {
                    if (index < 37) {
                        if (!this.mergeItemStack(itemstack1, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.mergeItemStack(itemstack1, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);
            if (index == 0) {
                playerIn.dropItem(itemstack2, false);
            }
        }
        this.onCraftMatrixChanged(this.craftMatrix);

        return itemstack;
    }

    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        //journey_mode.LOGGER.info("what is here?");
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }


}
