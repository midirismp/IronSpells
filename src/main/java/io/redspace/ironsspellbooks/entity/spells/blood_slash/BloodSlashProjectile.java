package io.redspace.ironsspellbooks.entity.spells.blood_slash;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerMagicData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.ShieldPart;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.spells.SchoolType;
import io.redspace.ironsspellbooks.spells.SpellType;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BloodSlashProjectile extends Projectile implements AntiMagicSusceptible {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(BloodSlashProjectile.class, EntityDataSerializers.FLOAT);
    private static final double SPEED = 1d;
    private static final int EXPIRE_TIME = 4 * 20;
    public final int animationSeed;
    private final float maxRadius;
    private EntityDimensions dimensions;
    public AABB oldBB;
    private int age;
    private float damage;
    public int animationTime;
    private List<Entity> victims;

    public BloodSlashProjectile(EntityType<? extends BloodSlashProjectile> entityType, Level level) {
        super(entityType, level);
        animationSeed = level.random.nextInt(9999);

        float initialRadius = 2;
        maxRadius = 4;
        dimensions = EntityDimensions.scalable(initialRadius, 0.5f);

        oldBB = getBoundingBox();
        victims = new ArrayList<>();
        this.setNoGravity(true);
    }

    public BloodSlashProjectile(EntityType<? extends BloodSlashProjectile> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        setOwner(shooter);
        setYRot(shooter.getYRot());
        setXRot(shooter.getXRot());
    }

    public BloodSlashProjectile(Level levelIn, LivingEntity shooter) {
        this(EntityRegistry.BLOOD_SLASH_PROJECTILE.get(), levelIn, shooter);
    }

    public void shoot(Vec3 rotation) {
        setDeltaMovement(rotation.scale(SPEED));
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    //TODO: override "doWaterSplashEffect"
    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_RADIUS, 0.5F);

    }

    public void setRadius(float newRadius) {
        if (newRadius <= maxRadius && !this.level.isClientSide) {
            this.getEntityData().set(DATA_RADIUS, Mth.clamp(newRadius, 0.0F, maxRadius));
        }
    }

    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    @Override
    public void tick() {
        super.tick();
        if (++age > EXPIRE_TIME) {
            discard();
            return;
        }
        oldBB = getBoundingBox();
        setRadius(getRadius() + 0.12f);

        if (!level.isClientSide) {
            HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
            if (hitresult.getType() == HitResult.Type.BLOCK) {
                onHitBlock((BlockHitResult) hitresult);
            }
            for (Entity entity : level.getEntities(this, this.getBoundingBox()).stream().filter(target -> canHitEntity(target) && !victims.contains(target)).collect(Collectors.toSet())) {
                damageEntity(entity);
                //IronsSpellbooks.LOGGER.info(entity.getName().getString());
                MagicManager.spawnParticles(level, ParticleHelper.BLOOD, entity.getX(), entity.getY(), entity.getZ(), 50, 0, 0, 0, .5, true);
                if (entity instanceof ShieldPart || entity instanceof AbstractShieldEntity) {
                    discard();
                    return;
                }
            }
            //spawnParticles();
        }
//        List<Entity> collisions = new ArrayList<>();
//        collisions.addAll(level.getEntities(this, this.getBoundingBox()));
//
//        collisions = collisions.stream().filter(target ->
//                target != getOwner() && target instanceof LivingEntity && !victims.contains(target)).collect(Collectors.toList());
//        for (Entity entity : collisions) {
//        }

        setPos(position().add(getDeltaMovement()));
        spawnParticles();
    }

    public EntityDimensions getDimensions(Pose p_19721_) {
        //irons_spellbooks.LOGGER.info("Accessing Blood Slash Dimensions. Age: {}", age);
        this.getBoundingBox();
        return EntityDimensions.scalable(this.getRadius() + 2.0F, 0.5F);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_19729_) {
        //irons_spellbooks.LOGGER.info("onSynchedDataUpdated");

        if (DATA_RADIUS.equals(p_19729_)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(p_19729_);
    }

    //    private void increaseSize(float increase){
//        var bbOld = this.getBoundingBox();
//        double newWidth = (bbOld.getXsize() + increase) * .5;
//        double halfHeight = bbOld.getYsize() * .5;
//        Vec3 from = bbOld.getCenter().subtract(newWidth, halfHeight, newWidth);
//        Vec3 to = bbOld.getCenter().add(newWidth, halfHeight, newWidth);
//        this.setBoundingBox(new AABB(from.x,from.y,from.z,to.x,to.y,to.z));
//    }
//    @Override
//    protected void onHit(HitResult hitresult) {
//        if (hitresult.getType() == HitResult.Type.ENTITY) {
//            onHitEntity((EntityHitResult) hitresult);
//        } else if (hitresult.getType() == HitResult.Type.BLOCK) {
//            onHitBlock((BlockHitResult) hitresult);
//        }
//        double x = hitresult.getLocation().x;
//        double y = hitresult.getLocation().y;
//        double z = hitresult.getLocation().z;
//
//        MagicManager.spawnParticles(level, ParticleHelper.BLOOD, x, y, z, 50, 0, 0, 0, .5, true);
//
//
//    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        discard();
    }

//    @Override
//    protected void onHitEntity(EntityHitResult entityHitResult) {
//        damageEntity(entityHitResult.getEntity());
//
//    }

    private void damageEntity(Entity entity) {
        if (!victims.contains(entity)) {
            var hit = DamageSources.applyDamage(entity, damage, SpellType.BLOOD_SLASH_SPELL.getDamageSource(this, getOwner()), SchoolType.BLOOD);
            if (hit && entity instanceof LivingEntity livingEntity) {
                //livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.BLOOD_SLASHED.get(), 40, 1));
                if (getOwner() instanceof LivingEntity livingOwner) {
                    livingOwner.heal(damage * .15f * DamageSources.getResist(livingEntity, SchoolType.BLOOD));
                }
            }
            victims.add(entity);
        }
    }

    //https://forge.gemwire.uk/wiki/Particles
    public void spawnParticles() {
        if (level.isClientSide) {

            float width = (float) getBoundingBox().getXsize();
            float step = .25f;
            float radians = Mth.DEG_TO_RAD * getYRot();
            float speed = .1f;
            for (int i = 0; i < width / step; i++) {
                double x = getX();
                double y = getY();
                double z = getZ();
                double offset = step * (i - width / step / 2);
                double rotX = offset * Math.cos(radians);
                double rotZ = -offset * Math.sin(radians);

                double dx = Math.random() * speed * 2 - speed;
                double dy = Math.random() * speed * 2 - speed;
                double dz = Math.random() * speed * 2 - speed;
                level.addParticle(ParticleHelper.BLOOD, false, x + rotX + dx, y + dy, z + rotZ + dz, dx, dy, dz);
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if (entity == getOwner())
            return false;
        return super.canHitEntity(entity);
    }

    @Override
    public void onAntiMagic(PlayerMagicData playerMagicData) {
        this.discard();
    }
}
