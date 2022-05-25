package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.tileentity.UnobtainiumStarforgeTileEntity;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class JMTileEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES,
            journey_mode.MODID);

    public static final RegistryObject<BlockEntityType<UnobtainiumStarforgeTileEntity>> UNOBTAINIUM_STARFORGE = TILE_ENTITY_TYPES
            .register("unobtainium_starforge", () -> BlockEntityType.Builder
                    .of(UnobtainiumStarforgeTileEntity::new, UnobtainBlockInit.UNOBTAINIUM_STARFORGE.get()).build(null));
}
