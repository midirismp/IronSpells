package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.Utils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class HealingAoe extends AoeEntity {

    public HealingAoe(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

    }

    public HealingAoe(Level level) {
        this(EntityRegistry.HEALING_AOE.get(), level);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        //var owner = getOwner();
        //IronsSpellbooks.LOGGER.debug("HealingAoe apply effect: target: {} owner: {} should heal: {}",target.getName().getString(),owner==null?null:owner.getName().getString(),owner==null?false: Utils.shouldHealEntity((LivingEntity) owner,target));
        if (getOwner() instanceof LivingEntity owner && Utils.shouldHealEntity(owner, target)) {
            target.heal(getDamage());
        }
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return !pTarget.isSpectator() && pTarget.isAlive() && pTarget.isPickable();
    }

    @Override
    public float getParticleCount() {
        return .15f;
    }

    @Override
    public void ambientParticles() {
        int color = PotionUtils.getColor(Potion.byName("healing"));
        double d0 = (double) (color >> 16 & 255) / 255.0D;
        double d1 = (double) (color >> 8 & 255) / 255.0D;
        double d2 = (double) (color >> 0 & 255) / 255.0D;

        if (!level.isClientSide)
            return;

        float f = getParticleCount();
        f = Mth.clamp(f * getRadius(), f / 4, f * 10);
        for (int i = 0; i < f; i++) {
            if (f - i < 1 && random.nextFloat() > f - i)
                return;
            var r = getRadius();
            Vec3 pos;
            if (isCircular()) {
                float distance = (1 - this.random.nextFloat() * this.random.nextFloat()) * r;
                pos = new Vec3(0, 0, distance).yRot(this.random.nextFloat() * 360);
            } else {
                pos = new Vec3(
                        Utils.getRandomScaled(r * .85f),
                        .2f,
                        Utils.getRandomScaled(r * .85f)
                );
            }
            level.addParticle(getParticle(), getX() + pos.x, getY() + pos.y + particleYOffset(), getZ() + pos.z, d0, d1, d2);
        }
    }

    @Override
    public ParticleOptions getParticle() {
        return ParticleTypes.ENTITY_EFFECT;
    }
}
