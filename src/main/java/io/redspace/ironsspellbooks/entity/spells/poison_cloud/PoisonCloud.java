package io.redspace.ironsspellbooks.entity.spells.poison_cloud;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class PoisonCloud extends AoeEntity {

    public static final DamageSource DAMAGE_SOURCE = new DamageSource("poison_cloud");

    public PoisonCloud(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

    }

    public PoisonCloud(Level level) {
        this(EntityRegistry.POISON_CLOUD.get(), level);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        var damageSource = DamageSources.indirectDamageSource(DAMAGE_SOURCE, this, getOwner());
        target.hurt(damageSource, getDamage());
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 120, (int) getDamage()));
    }

    @Override
    public float getParticleCount() {
        return .15f;
    }

    @Override
    public ParticleOptions getParticle() {
        return ParticleHelper.POISON_CLOUD;
    }
}
