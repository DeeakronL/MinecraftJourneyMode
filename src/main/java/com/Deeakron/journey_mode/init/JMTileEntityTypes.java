package com.Deeakron.journey_mode.init;

import com.Deeakron.journey_mode.BlockEntity.UnobtainiumStarforgeBlockEntity;
import com.Deeakron.journey_mode.journey_mode;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class JMBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES,
            journey_mode.MODID);

    public static final RegistryObject<BlockEntityType<UnobtainiumStarforgeBlockEntity>> UNOBTAINIUM_STARFORGE = TILE_ENTITY_TYPES
            .register("unobtainium_starforge", () -> BlockEntityType.Builder
                    .of(UnobtainiumStarforgeBlockEntity::new, UnobtainBlockInit.UNOBTAINIUM_STARFORGE.get()).build(null));
}
