package io.redspace.ironsspellbooks.spells.evocation;

import io.redspace.ironsspellbooks.capabilities.magic.PlayerMagicData;
import io.redspace.ironsspellbooks.entity.spells.creeper_head.CreeperHeadProjectile;
import io.redspace.ironsspellbooks.spells.*;
import io.redspace.ironsspellbooks.util.Utils;
import io.redspace.ironsspellbooks.util.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class LobCreeperSpell extends AbstractSpell {
    public LobCreeperSpell() {
        this(1);
    }

    @Override
    public List<MutableComponent> getUniqueInfo(LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(caster), 1))
        );
    }

    public static DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchool(SchoolType.EVOCATION)
            .setMaxLevel(10)
            .setCooldownSeconds(2)
            .build();

    public LobCreeperSpell(int level) {
        super(SpellType.LOB_CREEPER_SPELL);
        this.level = level;
        this.manaCostPerLevel = 2;
        this.baseSpellPower = 24;
        this.spellPowerPerLevel = 3;
        this.castTime = 0;
        this.baseManaCost = 20;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.CREEPER_HURT);
    }

    @Override
    public void onCast(Level level, LivingEntity entity, PlayerMagicData playerMagicData) {
        float speed = (6 + this.level) * .15f;
        float damage = getDamage(entity);
        CreeperHeadProjectile head = new CreeperHeadProjectile(entity, level, speed, damage);
        Vec3 spawn = entity.getEyePosition().add(entity.getForward());
        head.moveTo(spawn.x, spawn.y - head.getBoundingBox().getYsize() / 2, spawn.z, entity.getYRot() + 180, entity.getXRot());
        level.addFreshEntity(head);
        super.onCast(level, entity, playerMagicData);
    }

    private float getDamage(LivingEntity entity) {
        return this.getSpellPower(entity) * .5f;
    }
}
