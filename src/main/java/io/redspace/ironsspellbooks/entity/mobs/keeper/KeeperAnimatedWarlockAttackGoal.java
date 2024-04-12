package io.redspace.ironsspellbooks.entity.mobs.keeper;

import io.redspace.ironsspellbooks.entity.mobs.goals.AttackAnimationData;
import io.redspace.ironsspellbooks.entity.mobs.goals.WarlockAttackGoal;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class KeeperAnimatedWarlockAttackGoal extends WarlockAttackGoal {
    final KeeperEntity keeper;

    public KeeperAnimatedWarlockAttackGoal(KeeperEntity abstractSpellCastingMob, double pSpeedModifier, int minAttackInterval, int maxAttackInterval, float meleeRange) {
        super(abstractSpellCastingMob, pSpeedModifier, minAttackInterval, maxAttackInterval, meleeRange);
        keeper = abstractSpellCastingMob;
        currentAttack = keeper.getNextAttackType();
        this.meleeBias = 1f;
        this.wantsToMelee = true;
    }

    int meleeAnimTimer = -1;
    public KeeperEntity.AttackType currentAttack;
    private boolean hasLunged;
    private boolean hasHitLunge;
    //measured from blockbench
    Map<KeeperEntity.AttackType, AttackAnimationData> attackAnimations = Map.of(
            KeeperEntity.AttackType.Double_Slash, new AttackAnimationData(28, 12, 21),
            KeeperEntity.AttackType.Lunge, new AttackAnimationData(43, 34, 35, 36, 37),
            KeeperEntity.AttackType.Slash_Stab, new AttackAnimationData(38, 13, 33),
            KeeperEntity.AttackType.Triple_Slash, new AttackAnimationData(41, 11, 21, 35)
    );

    @Override
    protected void handleAttackLogic(double distanceSquared) {
        //Handling Animation hit frames
        float distance = Mth.sqrt((float) distanceSquared);
        //mob.lookAt(target, 300, 300);

        if (meleeAnimTimer > 0) {
            forceFaceTarget();
            //mob.setYBodyRot(0);
            //IronsSpellbooks.LOGGER.debug("EntityRot: {}\nHeadRot:{}\nBodyRot:{}", mob.getYRot(), mob.getYHeadRot(), mob.yBodyRot);
            meleeAnimTimer--;
            //IronsSpellbooks.LOGGER.debug("KeeperAnimatedAttackGoal.handleLogic Frame: {} | Should attack: {}", meleeAnimTimer, attackAnimations.get(currentAttack).isHitFrame(meleeAnimTimer));
            if (attackAnimations.get(currentAttack).isHitFrame(meleeAnimTimer)) {
                if (currentAttack == KeeperEntity.AttackType.Lunge) {
                    if (!hasLunged) {
                        Vec3 lunge = target.position().subtract(mob.position()).normalize().multiply(2.4, .5, 2.4).add(0, 0.15, 0);
                        mob.push(lunge.x, lunge.y, lunge.z);
                        hasLunged = true;
                    }
                    if (!hasHitLunge && distance <= meleeRange * .6f) {
                        this.mob.doHurtTarget(target);
                        target.knockback(1, Mth.sin(mob.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(mob.getYRot() * ((float) Math.PI / 180F)));

                        hasHitLunge = true;
                    }
                } else {
                    //mob.lookAt(target, 300, 300);
                    Vec3 lunge = target.position().subtract(mob.position()).normalize().scale(.55f)/*.add(0, 0.2, 0)*/;
                    mob.push(lunge.x, lunge.y, lunge.z);
                    if (distance <= meleeRange) {
                        this.mob.doHurtTarget(target);
                        target.invulnerableTime = 0;
                    }
                }

            }
        } else if (meleeAnimTimer == 0) {
            resetAttackTimer(distanceSquared);
            keeper.randomizeNextAttack();
            meleeAnimTimer = -1;
        } else {
            //Handling attack delay
            //this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            mob.lookAt(target, 15, 15);
            if (distance < meleeRange * (keeper.getNextAttackType() == KeeperEntity.AttackType.Lunge ? 3 : 1)) {
                //IronsSpellbooks.LOGGER.debug("KeeperAnimatedAttackGoal.handleLogic: IN RANGE");
                if (--this.attackTime == 0) {
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    doMeleeAction();
                } else if (this.attackTime < 0) {
                    resetAttackTimer(distanceSquared);

                }
            } else if (--this.attackTime < 0) {
                //Always keep the clock running, eventually he'll lunge to close distance. Otherwise he can be kited incredibly easily
                resetAttackTimer(distanceSquared);
                keeper.randomizeNextAttack();
            }
        }
    }


    private void forceFaceTarget() {

        if (hasLunged)
            return;
        double d0 = target.getX() - mob.getX();
        double d1 = target.getZ() - mob.getZ();
        float yRot = (float) (Mth.atan2(d1, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
        mob.setYBodyRot(yRot);
        mob.setYHeadRot(yRot);
        mob.setYRot(yRot);
    }

    @Override
    protected void doMeleeAction() {
        //anim duration
        currentAttack = keeper.getNextAttackType();
        meleeAnimTimer = attackAnimations.get(currentAttack).lengthInTicks;
        hasLunged = false;
        hasHitLunge = false;

    }

    @Override
    protected void doMovement(double distanceSquared) {
        if (distanceSquared > meleeRange * meleeRange) {
            if (isFlying)
                this.mob.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), this.speedModifier * 1.3f);
            else
                this.mob.getNavigation().moveTo(this.target, this.speedModifier * 1.3f);
        }
    }
}
