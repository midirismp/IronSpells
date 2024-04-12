package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.capabilities.magic.PlayerMagicData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.spells.SchoolType;
import io.redspace.ironsspellbooks.spells.SpellType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class ExtendedWitherSkull extends WitherSkull implements AntiMagicSusceptible {
    public ExtendedWitherSkull(EntityType<? extends WitherSkull> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected float damage;

    public ExtendedWitherSkull(LivingEntity shooter, Level level, float speed, float damage) {
        super(EntityRegistry.WITHER_SKULL_PROJECTILE.get(), level);
        setOwner(shooter);

        Vec3 power = shooter.getLookAngle().normalize().scale(speed);

        this.xPower = power.x;
        this.yPower = power.y;
        this.zPower = power.z;
        this.damage = damage;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        DamageSources.applyDamage(entity, damage, SpellType.WITHER_SKULL_SPELL.getDamageSource(this, getOwner()), SchoolType.BLOOD);
 //Ironsspellbooks.logger.debug("hmm.");
    }

    @Override
    protected void onHit(HitResult hitResult) {

        if (!this.level.isClientSide) {
            float explosionRadius = 2;
            var entities = level.getEntities(this, this.getBoundingBox().inflate(explosionRadius));
            for (Entity entity : entities) {
                double distance = entity.distanceToSqr(hitResult.getLocation());
                if (distance < explosionRadius * explosionRadius  && canHitEntity(entity)) {
                    float damage = (float) (this.damage * (1 - distance / (explosionRadius * explosionRadius)));
                    DamageSources.applyDamage(entity, damage, SpellType.WITHER_SKULL_SPELL.getDamageSource(this, getOwner()), SchoolType.BLOOD);
                }
            }

            this.level.explode(this, this.getX(), this.getY(), this.getZ(), 0.0F, false, Explosion.BlockInteraction.NONE);
            this.discard();
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onAntiMagic(PlayerMagicData playerMagicData) {
        this.discard();
    }
}
