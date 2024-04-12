package io.redspace.ironsspellbooks.entity.spells.fire_breath;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.spells.SchoolType;
import io.redspace.ironsspellbooks.spells.SpellType;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import io.redspace.ironsspellbooks.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FireBreathProjectile extends AbstractConeProjectile {
    public FireBreathProjectile(EntityType<? extends AbstractConeProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public FireBreathProjectile(Level level, LivingEntity entity) {
        super(EntityRegistry.FIRE_BREATH_PROJECTILE.get(), level, entity);
    }

    @Override
    public void tick() {
        if (!level.isClientSide && getOwner() != null)
            if (dealDamageActive) {
                //Set Fire Blocks
                boolean doFire = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner());

                if(doFire){
                    float range = 15 * Mth.DEG_TO_RAD;
                    for (int i = 0; i < 3; i++) {
                        Vec3 cast = getOwner().getLookAngle().normalize().xRot(level.random.nextFloat() * range * 2 - range).yRot(level.random.nextFloat() * range * 2 - range);
                        HitResult hitResult = level.clip(new ClipContext(getOwner().getEyePosition(), getOwner().getEyePosition().add(cast.scale(10)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                        if (hitResult.getType() == HitResult.Type.BLOCK) {
                            HitResult shieldResult = Utils.raycastForEntityOfClass(level, this, getOwner().getEyePosition(), hitResult.getLocation(), false, AbstractShieldEntity.class);
                            if (shieldResult.getType() == HitResult.Type.MISS) {
                                Vec3 pos = hitResult.getLocation().subtract(cast.scale(.5));
                                BlockPos blockPos = new BlockPos(pos.x, pos.y, pos.z);
                                if (level.getBlockState(blockPos).isAir())
                                    level.setBlockAndUpdate(blockPos, BaseFireBlock.getState(this.level, blockPos));
                            }

                        }
                    }
                }

            }
        super.tick();
    }


    @Override
    public void spawnParticles() {
        var owner = getOwner();
        if (!level.isClientSide || owner == null) {
            return;
        }
        Vec3 rotation = owner.getLookAngle().normalize();
        var pos = owner.position().add(rotation.scale(1.6));

        double x = pos.x;
        double y = pos.y + owner.getEyeHeight() * .9f;
        double z = pos.z;

        double speed = random.nextDouble() * .35 + .35;
        for (int i = 0; i < 10; i++) {
            double offset = .15;
            double ox = Math.random() * 2 * offset - offset;
            double oy = Math.random() * 2 * offset - offset;
            double oz = Math.random() * 2 * offset - offset;

            double angularness = .5;
            Vec3 randomVec = new Vec3(Math.random() * 2 * angularness - angularness, Math.random() * 2 * angularness - angularness, Math.random() * 2 * angularness - angularness).normalize();
            Vec3 result = (rotation.scale(3).add(randomVec)).normalize().scale(speed);
            level.addParticle(ParticleHelper.FIRE, x + ox, y + oy, z + oz, result.x, result.y, result.z);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        var entity = entityHitResult.getEntity();
        if (DamageSources.applyDamage(entity, damage, SpellType.FIRE_BREATH_SPELL.getDamageSource(this, getOwner()), SchoolType.FIRE))
            entity.setSecondsOnFire(4);
    }
}
