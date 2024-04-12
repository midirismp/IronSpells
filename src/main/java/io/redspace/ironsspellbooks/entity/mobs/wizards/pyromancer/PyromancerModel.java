package io.redspace.ironsspellbooks.entity.mobs.wizards.pyromancer;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.resources.ResourceLocation;

public class PyromancerModel extends AbstractSpellCastingMobModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation(IronsSpellbooks.MODID, "textures/entity/pyromancer.png");

    @Override
    public ResourceLocation getTextureLocation(AbstractSpellCastingMob object) {
        return TEXTURE;
    }

}