package io.redspace.ironsspellbooks.entity.spells.blood_needle;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.SchoolType;
import io.redspace.ironsspellbooks.spells.SpellType;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;

//https://github.com/TobyNguyen710/kyomod/blob/56d3a9dc6b45f7bc5ecdb0d6de9d201cea2603f5/Mod/build/tmp/expandedArchives/forge-1.19.2-43.1.7_mapped_official_1.19.2-sources.jar_b6309abf8a7e6a853ce50598293fb2e7/net/minecraft/world/entity/projectile/ShulkerBullet.java
//https://github.com/maximumpower55/Aura/blob/1.18/src/main/java/me/maximumpower55/aura/entity/SpellProjectileEntity.java
//https://github.com/CammiePone/Arcanus/blob/1.18-dev/src/main/java/dev/cammiescorner/arcanus/common/entities/MagicMissileEntity.java#L51
//https://github.com/maximumpower55/Aura

public class BloodNeedle extends AbstractMagicProjectile {
    private static final EntityDataAccessor<Float> DATA_Z_ROT = SynchedEntityData.defineId(BloodNeedle.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_SCALE = SynchedEntityData.defineId(BloodNeedle.class, EntityDataSerializers.FLOAT);

    public BloodNeedle(EntityType<? extends BloodNeedle> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public BloodNeedle(Level levelIn, LivingEntity shooter) {
        super(EntityRegistry.BLOOD_NEEDLE.get(), levelIn);
        setOwner(shooter);
    }

    public void setZRot(float zRot) {
        if (!level.isClientSide)
            entityData.set(DATA_Z_ROT, zRot);
    }

    public void setScale(float scale) {
        if (!level.isClientSide)
            entityData.set(DATA_SCALE, scale);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_Z_ROT, 0f);
        entityData.define(DATA_SCALE, 1f);
        super.defineSynchedData();
    }

    public float getZRot() {
        return entityData.get(DATA_Z_ROT);
    }

    public float getScale() {
        return entityData.get(DATA_SCALE);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putFloat("zRot", getZRot());
        if (getScale() != 1)
            pCompound.putFloat("Scale", getScale());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        setZRot(pCompound.getFloat("zRot"));
        if (pCompound.contains("Scale"))
            setScale(pCompound.getFloat("Scale"));
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        boolean hit = DamageSources.applyDamage(entityHitResult.getEntity(), getDamage(), SpellType.BlOOD_NEEDLES_SPELL.getDamageSource(this, getOwner()), SchoolType.BLOOD);
        if (hit && entityHitResult.getEntity() instanceof LivingEntity target && getOwner() instanceof LivingEntity livingOwner) {
            livingOwner.heal(getDamage() * DamageSources.getResist(target, SchoolType.BLOOD) * .25f);
        }
        entityHitResult.getEntity().invulnerableTime = 0;

    }

    @Override
    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
        discard();
    }

    private static int soundTimestamp;

    @Override
    protected void doImpactSound(SoundEvent sound) {
        if (soundTimestamp != this.tickCount) {
            super.doImpactSound(sound);
            soundTimestamp = this.tickCount;
        }
    }

    @Override
    public void trailParticles() {

        for (int i = 0; i < 2; i++) {
            double speed = .05;
            double dx = level.random.nextDouble() * 2 * speed - speed;
            double dy = level.random.nextDouble() * 2 * speed - speed;
            double dz = level.random.nextDouble() * 2 * speed - speed;
            level.addParticle(ParticleHelper.BLOOD, this.getX() + dx, this.getY() + dy, this.getZ() + dz, dx, dy, dz);

        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level, ParticleHelper.BLOOD, x, y, z, 15, .1, .1, .1, .18, true);
    }

    @Override
    public float getSpeed() {
        return 2.5f;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.of(SoundRegistry.BLOOD_NEEDLE_IMPACT.get());
    }

    @Override
    public boolean respectsGravity() {
        return true;
    }
}
