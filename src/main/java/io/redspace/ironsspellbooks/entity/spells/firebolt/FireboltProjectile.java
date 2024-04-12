package io.redspace.ironsspellbooks.entity.spells.firebolt;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.spells.SchoolType;
import io.redspace.ironsspellbooks.spells.SpellType;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import io.redspace.ironsspellbooks.util.Utils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

//https://github.com/TobyNguyen710/kyomod/blob/56d3a9dc6b45f7bc5ecdb0d6de9d201cea2603f5/Mod/build/tmp/expandedArchives/forge-1.19.2-43.1.7_mapped_official_1.19.2-sources.jar_b6309abf8a7e6a853ce50598293fb2e7/net/minecraft/world/entity/projectile/ShulkerBullet.java
//https://github.com/maximumpower55/Aura/blob/1.18/src/main/java/me/maximumpower55/aura/entity/SpellProjectileEntity.java
//https://github.com/CammiePone/Arcanus/blob/1.18-dev/src/main/java/dev/cammiescorner/arcanus/common/entities/MagicMissileEntity.java#L51
//https://github.com/maximumpower55/Aura

public class FireboltProjectile extends AbstractMagicProjectile {
    public FireboltProjectile(EntityType<? extends FireboltProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public FireboltProjectile(EntityType<? extends FireboltProjectile> entityType, Level levelIn, LivingEntity shooter) {
        super(entityType, levelIn);
        setOwner(shooter);
    }

    public FireboltProjectile(Level levelIn, LivingEntity shooter) {
        this(EntityRegistry.FIREBOLT_PROJECTILE.get(), levelIn, shooter);
    }

    @Override
    public float getSpeed() {
        return 1.75f;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.of(SoundEvents.FIREWORK_ROCKET_BLAST);
    }

    @Override
    protected void doImpactSound(SoundEvent sound) {
        level.playSound(null, getX(), getY(), getZ(), sound, SoundSource.NEUTRAL, 2, 1.2f + level.random.nextFloat() * .2f);

    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        kill();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        var target = entityHitResult.getEntity();
        if (DamageSources.applyDamage(target, getDamage(), SpellType.FIREBOLT_SPELL.getDamageSource(this, getOwner()), SchoolType.FIRE)) {
            target.setSecondsOnFire(3);
            kill();
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level, ParticleTypes.LAVA, x, y, z, 5, .1, .1, .1, .25, true);
    }

    @Override
    public void trailParticles() {

        for (int i = 0; i < 1; i++) {
            float yHeading = -((float) (Mth.atan2(getDeltaMovement().z, getDeltaMovement().x) * (double) (180F / (float) Math.PI)) + 90.0F);
            //float xHeading = -((float) (Mth.atan2(getDeltaMovement().horizontalDistance(), getDeltaMovement().y) * (double) (180F / (float) Math.PI)) - 90.0F);
            float radius = .25f;
            int steps = 6;
            for (int j = 0; j < steps; j++) {
                float offset = (1f / steps) * i;
                double radians = ((age + offset) / 7.5f) * 360 * Mth.DEG_TO_RAD;
                Vec3 swirl = new Vec3(Math.cos(radians) * radius, Math.sin(radians) * radius, 0).yRot(yHeading * Mth.DEG_TO_RAD);
                double x = getX() + swirl.x;
                double y = getY() + swirl.y + getBbHeight() / 2;
                double z = getZ() + swirl.z;
                Vec3 jitter = Utils.getRandomVec3(.05f);
                level.addParticle(ParticleHelper.EMBERS, x, y, z, jitter.x, jitter.y, jitter.z);
            }
            //level.addParticle(ParticleTypes.SMOKE, getX(), getY(), getZ(), 0, 0, 0);

        }
    }

}
