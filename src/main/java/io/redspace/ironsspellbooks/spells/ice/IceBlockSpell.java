package io.redspace.ironsspellbooks.spells.ice;

import io.redspace.ironsspellbooks.capabilities.magic.CastTargetingData;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerMagicData;
import io.redspace.ironsspellbooks.entity.spells.ice_block.IceBlockProjectile;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.*;
import io.redspace.ironsspellbooks.util.Utils;
import net.minecraft.core.BlockPos;
import io.redspace.ironsspellbooks.util.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class IceBlockSpell extends AbstractSpell {
    public IceBlockSpell() {
        this(1);
    }

    @Override
    public List<MutableComponent> getUniqueInfo(LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getSpellPower(caster), 1)));
    }

    public static DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE )
            .setSchool(SchoolType.ICE)
            .setMaxLevel(6)
            .setCooldownSeconds(15)
            .build();

    public IceBlockSpell(int level) {
        super(SpellType.ICE_BLOCK_SPELL);
        this.level = level;
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 40;
        this.spellPowerPerLevel = 5;
        this.castTime = 30;
        this.baseManaCost = 40;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.ICE_BLOCK_CAST.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public boolean checkPreCastConditions(Level level, LivingEntity entity, PlayerMagicData playerMagicData) {
        Utils.preCastTargetHelper(level, entity, playerMagicData, getSpellType(), 48, .75f, false);
        return true;
    }

    @Override
    public void onCast(Level level, LivingEntity entity, PlayerMagicData playerMagicData) {
        Vec3 spawn = null;
        LivingEntity target = null;

        if (playerMagicData.getAdditionalCastData() instanceof CastTargetingData castTargetingData) {
            target = castTargetingData.getTarget((ServerLevel) level);
            if (target != null)
                spawn = target.position();
        }
        if (spawn == null) {
            HitResult raycast = Utils.raycastForEntity(level, entity, 32, true, .5f);
            if (raycast.getType() == HitResult.Type.ENTITY) {
                spawn = ((EntityHitResult) raycast).getEntity().position();
                if (((EntityHitResult) raycast).getEntity() instanceof LivingEntity livingEntity)
                    target = livingEntity;
            } else {
                spawn = raycast.getLocation().subtract(entity.getForward().normalize());
            }
        }

        IceBlockProjectile iceBlock = new IceBlockProjectile(level, entity, target);
        iceBlock.moveTo(raiseWithCollision(spawn, 2, level));
        iceBlock.setAirTime(target == null ? 10 : 11);
        iceBlock.setDamage(getDamage(entity));
        level.addFreshEntity(iceBlock);
        super.onCast(level, entity, playerMagicData);
    }

    private Vec3 raiseWithCollision(Vec3 start, int blocks, Level level) {
        for (int i = 0; i < blocks; i++) {
            Vec3 raised = start.add(0, 1, 0);
            if (level.getBlockState(new BlockPos(raised)).isAir())
                start = raised;
            else
                break;
        }
        return start;
    }

    private float getDamage(LivingEntity entity) {
        return this.getSpellPower(entity);
    }
}
