package io.redspace.ironsspellbooks.spells.lightning;

import io.redspace.ironsspellbooks.capabilities.magic.PlayerMagicData;
import io.redspace.ironsspellbooks.entity.spells.lightning_lance.LightningLanceProjectile;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.*;
import io.redspace.ironsspellbooks.util.Utils;
import io.redspace.ironsspellbooks.util.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class LightningLanceSpell extends AbstractSpell {

    @Override
    public List<MutableComponent> getUniqueInfo(LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getSpellPower(caster), 1)));
    }

    public static DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchool(SchoolType.LIGHTNING)
            .setMaxLevel(10)
            .setCooldownSeconds(8)
            .build();

    public LightningLanceSpell(int level) {
        super(SpellType.LIGHTNING_LANCE_SPELL);
        this.level = level;
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 28;
        this.spellPowerPerLevel = 3;
        this.castTime = 40;
        this.baseManaCost = 50;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
 //Ironsspellbooks.logger.debug("LightningLanceSpell.getCastStartSound");
        return Optional.of(SoundRegistry.LIGHTNING_LANCE_CAST.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.LIGHTNING_WOOSH_01.get());
    }

    @Override
    public void onServerPreCast(Level level, LivingEntity entity, @Nullable PlayerMagicData playerMagicData) {
        super.onServerPreCast(level, entity, playerMagicData);
    }

    @Override
    public void onCast(Level level, LivingEntity entity, PlayerMagicData playerMagicData) {
        LightningLanceProjectile lance = new LightningLanceProjectile(level, entity);
        lance.setPos(entity.position().add(0, entity.getEyeHeight(), 0).add(entity.getForward()));
        lance.shoot(entity.getLookAngle());
        lance.setDamage(getSpellPower(entity));
        level.addFreshEntity(lance);
        super.onCast(level, entity, playerMagicData);
    }
}
