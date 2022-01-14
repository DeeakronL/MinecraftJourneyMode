package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.tileentity.UnobtainiumStarforgeTileEntity;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class JMTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES,
            journey_mode.MODID);

    public static final RegistryObject<TileEntityType<UnobtainiumStarforgeTileEntity>> UNOBTAINIUM_STARFORGE = TILE_ENTITY_TYPES
            .register("unobtainium_starforge", () -> TileEntityType.Builder
                    .create(UnobtainiumStarforgeTileEntity::new, UnobtainBlockInit.UNOBTAINIUM_STARFORGE.get()).build(null));
}
