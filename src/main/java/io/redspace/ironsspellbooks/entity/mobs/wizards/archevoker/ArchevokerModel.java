package io.redspace.ironsspellbooks.entity.mobs.wizards.archevoker;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.resources.ResourceLocation;

public class ArchevokerModel extends AbstractSpellCastingMobModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation(IronsSpellbooks.MODID,"textures/entity/archevoker.png");
    public static final ResourceLocation MODEL = new ResourceLocation(IronsSpellbooks.MODID, "geo/archevoker.geo.json");

    @Override
    public ResourceLocation getModelLocation(AbstractSpellCastingMob object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractSpellCastingMob object) {
        return TEXTURE;
    }

}