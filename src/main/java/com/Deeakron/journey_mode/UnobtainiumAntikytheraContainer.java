package com.Deeakron.journey_mode;

import net.minecraft.block.Blocks;
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
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

public class UnobtainiumAntikytheraContainer extends WorkbenchContainer {
    private final CraftingInventory craftMatrix = new CraftingInventory(this, 3, 3);
    private final CraftResultInventory craftResult = new CraftResultInventory();
    private final IWorldPosCallable worldPosCallable;
    private final PlayerEntity player;

    public UnobtainiumAntikytheraContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, IWorldPosCallable.DUMMY);
    }

    public UnobtainiumAntikytheraContainer(int id, PlayerInventory playerInventory, IWorldPosCallable p_i50090_3_) {
        super(id, playerInventory, p_i50090_3_);
        this.worldPosCallable = p_i50090_3_;
        this.player = playerInventory.player;
    }

    public UnobtainiumAntikytheraContainer(int i, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        this(i, playerInventory);
    }
}
