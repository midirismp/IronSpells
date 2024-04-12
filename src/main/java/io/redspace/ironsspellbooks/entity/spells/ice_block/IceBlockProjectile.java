package io.redspace.ironsspellbooks.entity.spells.ice_block;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.SchoolType;
import io.redspace.ironsspellbooks.spells.SpellType;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import io.redspace.ironsspellbooks.util.Utils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class IceBlockProjectile extends AbstractMagicProjectile implements IAnimatable {

    private UUID targetUUID;
    private Entity cachedTarget;
    private List<Entity> victims;

    public IceBlockProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        victims = new ArrayList<>();

    }

    public IceBlockProjectile(Level pLevel, LivingEntity owner, LivingEntity target) {
        this(EntityRegistry.ICE_BLOCK_PROJECTILE.get(), pLevel);
        this.setOwner(owner);
        this.setTarget(target);
    }

    int airTime;

    public void setAirTime(int airTimeInTicks) {
        airTime = airTimeInTicks;
    }

    public void setTarget(@Nullable Entity pOwner) {
        if (pOwner != null) {
            this.targetUUID = pOwner.getUUID();
            this.cachedTarget = pOwner;
        }

    }

    @Nullable
    public Entity getTarget() {
        if (this.cachedTarget != null && !this.cachedTarget.isRemoved()) {
            return this.cachedTarget;
        } else if (this.targetUUID != null && this.level instanceof ServerLevel) {
            this.cachedTarget = ((ServerLevel) this.level).getEntity(this.targetUUID);
            return this.cachedTarget;
        } else {
            return null;
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.targetUUID != null) {
            pCompound.putUUID("Target", this.targetUUID);
        }
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.hasUUID("Target")) {
            this.targetUUID = pCompound.getUUID("Target");
        }
    }

    @Override
    public void trailParticles() {
        for (int i = 0; i < 1; i++) {
            Vec3 random = new Vec3(
                    Utils.getRandomScaled(this.getBbWidth() * .5f),
                    0,
                    Utils.getRandomScaled(this.getBbWidth() * .5f)
            );
            level.addParticle(ParticleTypes.SNOWFLAKE, getX() + random.x, getY(), getZ() + random.z, 0, -.05, 0);
        }
    }

    private void doFallingDamage(Entity target) {
        if (level.isClientSide)
            return;
        if (!canHitEntity(target) || victims.contains(target))
            return;
        boolean flag = DamageSources.applyDamage(target, getDamage() / 2, SpellType.ICE_BLOCK_SPELL.getDamageSource(this, getOwner()), SchoolType.ICE);
        if (flag) {
            if(target.canFreeze())
                target.setTicksFrozen(200);
            victims.add(target);
            target.invulnerableTime = 0;
        }
 //Ironsspellbooks.logger.debug("IceBlockProjectile.doFallingDamage: {}", target.getName().getString());

    }

    private void doImpactDamage() {
        float explosionRadius = 3.5f;
        level.getEntities(this, this.getBoundingBox().inflate(explosionRadius)).forEach((entity) -> {
            if (canHitEntity(entity)) {
                double distance = entity.distanceToSqr(position());
                if (distance < explosionRadius * explosionRadius) {
                    double p = (1 - Math.pow(Math.sqrt(distance) / (explosionRadius), 3));
                    float damage = (float) (this.damage * p);
 //Ironsspellbooks.logger.debug("IceBlockProjectile.doImpactDamage distance: {} p: {}", Math.sqrt(distance), p);

                    if (DamageSources.applyDamage(entity, damage, SpellType.ICE_BLOCK_SPELL.getDamageSource(this, getOwner()), SchoolType.ICE)  && entity.canFreeze())
                        entity.setTicksFrozen(200);
                }
            }

        });
    }

    @Override
    public void tick() {
//        IronsSpellbooks.LOGGER.debug("IceBlockProjectileRotation: X:{} Y:{}", getXRot(), getYRot());
//        this.setYRot((float)(Mth.atan2(getDeltaMovement().x, getDeltaMovement().z) * (double)(180F / (float)Math.PI)));
//        this.setXRot(0);
//        this.yRotO = this.getYRot();
//        this.xRotO = this.getXRot();
        baseTick();
        xOld = getX();
        yOld = getY();
        zOld = getZ();
        yRotO = getYRot();
        xRotO = getXRot();
        if (!level.isClientSide) {
            if (airTime <= 0) {
                //Falling
                if (isOnGround()) {
                    doImpactDamage();
                    playSound(SoundRegistry.ICE_BLOCK_IMPACT.get(), 2.5f, .8f + random.nextFloat() * .4f);
                    impactParticles(getX(), getY(), getZ());
                    discard();
                } else {
                    level.getEntities(this, getBoundingBox().inflate(0.35)).forEach(this::doFallingDamage);
                }
            }
            if (airTime-- > 0) {
                boolean tooHigh = false;
                this.setDeltaMovement(getDeltaMovement().multiply(.95f, .75f, .95f));
                if (getTarget() != null) {
                    var target = getTarget();

                    Vec3 diff = target.position().subtract(this.position());
                    if (diff.horizontalDistanceSqr() > 1) {
                        this.setDeltaMovement(getDeltaMovement().add(diff.multiply(1, 0, 1).normalize().scale(.02f)));
                    }
                    if (this.getY() - target.getY() > 3.5)
                        tooHigh = true;

                } else {
                    if (airTime % 3 == 0) {
                        HitResult ground = Utils.raycastForBlock(level, position(), position().subtract(0, 3.5, 0), ClipContext.Fluid.ANY);
                        if (ground.getType() == HitResult.Type.MISS) {
                            tooHigh = true;
                        } else if (Math.abs(position().y - ground.getLocation().y) < 4) {
                        }
                    }

                }
                if (tooHigh)
                    this.setDeltaMovement(getDeltaMovement().add(0, -.005, 0));
                else
                    this.setDeltaMovement(getDeltaMovement().add(0, .01, 0));

                if (airTime == 0)
                    this.setDeltaMovement(0, 0.5, 0);
            } else {
                this.setDeltaMovement(0, getDeltaMovement().y - .15, 0);
            }
        } else {
            trailParticles();
        }

        move(MoverType.SELF, getDeltaMovement());



    }

    @Override
    public void setXRot(float pXRot) {
//        IronsSpellbooks.LOGGER.debug("IceBlockProjectile: Something is trying to set my x rot! Ignoring.");
        //super.setXRot(pXRot);
    }

    @Override
    public void setYRot(float pYRot) {
//        super.setYRot(pYRot);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return pTarget != getOwner() && super.canHitEntity(pTarget);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level, ParticleTypes.SNOWFLAKE, x, y, z, 50, .8, .1, .8, 0.2, false);
        MagicManager.spawnParticles(level, ParticleHelper.SNOWFLAKE, x, y, z, 25, .5, .1, .5, 0.3, false);
    }

    @Override
    public float getSpeed() {
        return 0;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.empty();
    }

//    private final AnimationBuilder form = new AnimationBuilder().addAnimation("form", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
//
//    private PlayState predicate(AnimationEvent animationEvent) {
//        //IronsSpellbooks.LOGGER.debug("hello?");
//        if (animationEvent.getController().getAnimationState() == AnimationState.Stopped)
//            animationEvent.getController().setAnimation(form);
//        return PlayState.CONTINUE;
//    }

    public void registerControllers(AnimationData data) {
//        data.addAnimationController(new AnimationController(this, "ice_block_animations", 0, this::predicate));
    }

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
