package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.util.SpellbookModCreativeTabs;
import net.minecraft.world.item.Item;

public class DragonskinItem extends Item {
    public DragonskinItem() {
        super((new Item.Properties()).tab(SpellbookModCreativeTabs.SPELL_MATERIALS_TAB));
    }

//    @Override
//    public boolean hasCustomEntity(ItemStack stack) {
//        return true;
//    }
//
//    @Nullable
//    @Override
//    public Entity createEntity(Level world, Entity entity, ItemStack itemstack) {
//        if (!world.dimensionType().respawnAnchorWorks() && !world.dimensionType().bedWorks()) {
//            entity.setNoGravity(true);
//            if (!world.isClientSide)
//                MagicManager.spawnParticles(world, ParticleHelper.UNSTABLE_ENDER, entity.getX(), entity.getY(), entity.getZ(), 100, 0, 0, 0, 2, true);
//        }
//
//        return null;
//    }
}
