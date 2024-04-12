package io.redspace.ironsspellbooks.setup;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.mobs.SummonedHorse;
import io.redspace.ironsspellbooks.entity.mobs.SummonedSkeleton;
import io.redspace.ironsspellbooks.entity.mobs.SummonedVex;
import io.redspace.ironsspellbooks.entity.mobs.SummonedZombie;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingBoss;
import io.redspace.ironsspellbooks.entity.mobs.debug_wizard.DebugWizard;
import io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid.FrozenHumanoid;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperEntity;
import io.redspace.ironsspellbooks.entity.mobs.necromancer.NecromancerEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.archevoker.ArchevokerEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cryomancer.CryomancerEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.pyromancer.PyromancerEntity;
import io.redspace.ironsspellbooks.entity.spells.root.RootEntity;
import io.redspace.ironsspellbooks.entity.spells.spectral_hammer.SpectralHammer;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import io.redspace.ironsspellbooks.entity.spells.wisp.WispEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.Utils;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = IronsSpellbooks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {
    @SubscribeEvent
    public static void onModConfigLoadingEvent(ModConfigEvent.Loading event) {
        if(event.getConfig().getType() == ModConfig.Type.SERVER){
//            SpellType.resolveSchools();
        }
    }

    @SubscribeEvent
    public static void onModConfigReloadingEvent(ModConfigEvent.Reloading event) {
 //Ironsspellbooks.logger.debug("onModConfigReloadingEvent");
        if(event.getConfig().getType() == ModConfig.Type.SERVER){
//            SpellType.resolveSchools();
        }
    }

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.DEBUG_WIZARD.get(), DebugWizard.prepareAttributes().build());
        event.put(EntityRegistry.PYROMANCER.get(), PyromancerEntity.prepareAttributes().build());
        event.put(EntityRegistry.NECROMANCER.get(), NecromancerEntity.prepareAttributes().build());
        event.put(EntityRegistry.SPECTRAL_STEED.get(), SummonedHorse.prepareAttributes().build());
        event.put(EntityRegistry.WISP.get(), WispEntity.prepareAttributes().build());
        event.put(EntityRegistry.SPECTRAL_HAMMER.get(), SpectralHammer.prepareAttributes().build());
        event.put(EntityRegistry.SUMMONED_VEX.get(), SummonedVex.createAttributes().build());
        event.put(EntityRegistry.SUMMONED_ZOMBIE.get(), SummonedZombie.createAttributes().build());
        event.put(EntityRegistry.SUMMONED_SKELETON.get(), SummonedSkeleton.createAttributes().build());
        event.put(EntityRegistry.FROZEN_HUMANOID.get(), FrozenHumanoid.prepareAttributes().build());
        event.put(EntityRegistry.SUMMONED_POLAR_BEAR.get(), PolarBear.createAttributes().build());
        event.put(EntityRegistry.DEAD_KING.get(), DeadKingBoss.prepareAttributes().build());
        event.put(EntityRegistry.DEAD_KING_CORPSE.get(), DeadKingBoss.prepareAttributes().build());
        event.put(EntityRegistry.CATACOMBS_ZOMBIE.get(), Zombie.createAttributes().build());
        event.put(EntityRegistry.MAGEHUNTER_VINDICATOR.get(), Vindicator.createAttributes().build());
        event.put(EntityRegistry.ARCHEVOKER.get(), ArchevokerEntity.prepareAttributes().build());
        event.put(EntityRegistry.KEEPER.get(), KeeperEntity.prepareAttributes().build());
        event.put(EntityRegistry.VOID_TENTACLE.get(), VoidTentacle.createLivingAttributes().build());
        event.put(EntityRegistry.CRYOMANCER.get(), CryomancerEntity.prepareAttributes().build());
        event.put(EntityRegistry.ROOT.get(), RootEntity.createLivingAttributes().build());
    }
//
//    @SubscribeEvent
//    public static void spawnPlacements(SpawnPlacementRegisterEvent event) {
//        event.register(EntityRegistry.NECROMANCER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Utils::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
//    }


}
