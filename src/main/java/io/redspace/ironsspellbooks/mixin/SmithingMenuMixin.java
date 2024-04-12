package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.capabilities.spell.SpellData;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.UpgradeUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//Default priority is 1000
@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin {
    private static final UpgradeRecipe fakeRecipe = new UpgradeRecipe(new ResourceLocation(""), Ingredient.of(), Ingredient.of(), ItemStack.EMPTY) {
        @Override
        public boolean matches(Container pInv, Level pLevel) {
            return true;
        }
    };

    @Shadow
    private
    UpgradeRecipe selectedRecipe;

    /*
    Necessary to wipe nbt when using shriving stone
    */
    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    public void createResult(CallbackInfo ci) {
        var menu = (SmithingMenu) (Object) this;
        var baseSlot = menu.getSlot(0);
        if (baseSlot.hasItem() && menu.getSlot(1).getItem().getItem().equals(ItemRegistry.SHRIVING_STONE.get())) {
            var resultSlot = menu.getSlot(2);
            ItemStack result = baseSlot.getItem().copy();
            if (result.is(ItemRegistry.SCROLL.get()))
                return;
            boolean flag = false;
            if (SpellData.hasSpellData(result)) {
                result.removeTagKey(SpellData.ISB_SPELL);
                flag = true;
            } else if (UpgradeUtils.isUpgraded(result)) {
                result.removeTagKey(UpgradeUtils.Upgrades);
                flag = true;
            }
            if (flag) {
                resultSlot.set(result);
                selectedRecipe = fakeRecipe;
                ci.cancel();
            }
        }
//        if (pInv.getItem(1).getItem().equals(Items.FLINT)) {
//            ItemStack result = cir.getReturnValue();
//            if (SpellData.hasSpellData(result))
//                result.removeTagKey(SpellData.ISB_SPELL);
//            else if (UpgradeUtils.isUpgraded(result))
//                result.removeTagKey(UpgradeUtils.Upgrades);
//        }
    }

}
