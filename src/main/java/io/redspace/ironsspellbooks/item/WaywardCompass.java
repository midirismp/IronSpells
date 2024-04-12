package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.util.SpellbookModCreativeTabs;
import io.redspace.ironsspellbooks.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import io.redspace.ironsspellbooks.util.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WaywardCompass extends Item {
    private static final net.minecraft.network.chat.Component description = Component.translatable("item.irons_spellbooks.wayward_compass_desc").withStyle(ChatFormatting.DARK_AQUA);
    public WaywardCompass() {
        super(new Properties().tab(SpellbookModCreativeTabs.SPELL_EQUIPMENT_TAB));
    }

    public static GlobalPos getCatacombsLocation(Entity entity, CompoundTag compoundTag) {
        if (!(entity.level.dimensionType().natural() && compoundTag.contains("CatacombsPos")))
            return null;

        return GlobalPos.of(entity.level.dimension(), NbtUtils.readBlockPos(compoundTag.getCompound("CatacombsPos")));
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        if (pLevel instanceof ServerLevel serverlevel) {
            BlockPos blockpos = serverlevel.findNearestMapFeature(ModTags.WAYWARD_COMPASS_LOCATOR, pPlayer.blockPosition(), 100, false);
            if (blockpos != null) {
                var tag = pStack.getOrCreateTag();
                tag.put("CatacombsPos", NbtUtils.writeBlockPos(blockpos));

            }

        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<net.minecraft.network.chat.Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(description);
    }
}
