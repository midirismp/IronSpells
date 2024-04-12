package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, IronsSpellbooks.MODID);

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

    public static RegistryObject<SoundEvent> FORCE_IMPACT = registerSoundEvent("force_impact");
    public static RegistryObject<SoundEvent> ICE_IMPACT = registerSoundEvent("ice_impact");

    public static RegistryObject<SoundEvent> MAGIC_SPELL_REVERSE_3 = registerSoundEvent("magic_spell_reverse_3");
    public static RegistryObject<SoundEvent> ARIAL_SUMMONING_5_CUSTOM_1 = registerSoundEvent("arial_summoning_5_custom_1");
    public static RegistryObject<SoundEvent> DARK_MAGIC_BUFF_03_CUSTOM_1 = registerSoundEvent("dark_magic_buff_03_custom_1");
    public static RegistryObject<SoundEvent> DARK_SPELL_02 = registerSoundEvent("dark_spell_02");
    public static RegistryObject<SoundEvent> LIGHTNING_WOOSH_01 = registerSoundEvent("lightning_woosh_01");

    public static RegistryObject<SoundEvent> HEARTSTOP_CAST = registerSoundEvent("heartstop_cast");
    public static RegistryObject<SoundEvent> HEARTSTOP_HEARTBEAT = registerSoundEvent("spell.heartstop.heartbeat");
    public static RegistryObject<SoundEvent> LIGHTNING_LANCE_CAST = registerSoundEvent("lightning_lance_cast");
    public static RegistryObject<SoundEvent> MAGIC_ARROW_RELEASE = registerSoundEvent("magic_arrow_release");
    public static RegistryObject<SoundEvent> MAGIC_ARROW_CHARGE = registerSoundEvent("magic_arrow_charge");
    public static RegistryObject<SoundEvent> FROST_STEP = registerSoundEvent("frost_step");
    public static RegistryObject<SoundEvent> ABYSSAL_TELEPORT = registerSoundEvent("abyssal_teleport");
    public static RegistryObject<SoundEvent> ABYSSAL_SHROUD = registerSoundEvent("cast.abyssal_shroud");
    public static RegistryObject<SoundEvent> BLOOD_STEP = registerSoundEvent("cast.blood_step");
    public static RegistryObject<SoundEvent> FIRE_BREATH_LOOP = registerSoundEvent("loop.fire_breath");
    public static RegistryObject<SoundEvent> ELECTROCUTE_LOOP = registerSoundEvent("loop.electrocute");
    public static RegistryObject<SoundEvent> CONE_OF_COLD_LOOP = registerSoundEvent("loop.cone_of_cold");
    public static RegistryObject<SoundEvent> CLOUD_OF_REGEN_LOOP = registerSoundEvent("loop.cloud_of_regen");
    public static RegistryObject<SoundEvent> RAISE_DEAD_START = registerSoundEvent("cast.raise_dead.start");
    public static RegistryObject<SoundEvent> RAISE_DEAD_FINISH = registerSoundEvent("cast.raise_dead.finish");
    public static RegistryObject<SoundEvent> VOID_TENTACLES_START = registerSoundEvent("cast.void_tentacles.start");
    public static RegistryObject<SoundEvent> VOID_TENTACLES_FINISH = registerSoundEvent("cast.void_tentacles.finish");
    public static RegistryObject<SoundEvent> VOID_TENTACLES_LEAVE = registerSoundEvent("entity.void_tentacles.retreat");
    public static RegistryObject<SoundEvent> VOID_TENTACLES_AMBIENT = registerSoundEvent("entity.void_tentacles.ambient");
    public static RegistryObject<SoundEvent> ICE_BLOCK_CAST = registerSoundEvent("cast.ice_block");
    public static RegistryObject<SoundEvent> ICE_BLOCK_IMPACT = registerSoundEvent("entity.ice_block.impact");
    public static RegistryObject<SoundEvent> RAY_OF_SIPHONING = registerSoundEvent("loop.ray_of_siphoning");
    public static RegistryObject<SoundEvent> FIREBALL_START = registerSoundEvent("cast.fireball");
    public static RegistryObject<SoundEvent> ACID_ORB_CHARGE = registerSoundEvent("spell.acid_orb.charge");
    public static RegistryObject<SoundEvent> ACID_ORB_CAST = registerSoundEvent("spell.acid_orb.cast");
    public static RegistryObject<SoundEvent> ACID_ORB_IMPACT = registerSoundEvent("entity.acid_orb.impact");
    public static RegistryObject<SoundEvent> POISON_ARROW_CHARGE = registerSoundEvent("spell.poison_arrow.charge");
    public static RegistryObject<SoundEvent> POISON_ARROW_CAST = registerSoundEvent("spell.poison_arrow.cast");
    public static RegistryObject<SoundEvent> POISON_BREATH_LOOP = registerSoundEvent("spell.poison_breath.loop");
    public static RegistryObject<SoundEvent> ROOT_EMERGE = registerSoundEvent("entity.root.emerge");
    public static RegistryObject<SoundEvent> BLACK_HOLE_CHARGE = registerSoundEvent("spell.black_hole.charge");
    public static RegistryObject<SoundEvent> BLACK_HOLE_CAST = registerSoundEvent("spell.black_hole.cast");
    public static RegistryObject<SoundEvent> BLACK_HOLE_LOOP = registerSoundEvent("entity.black_hole.loop");
    public static RegistryObject<SoundEvent> POISON_SPLASH_BEGIN = registerSoundEvent("spell.poison_splash.begin");
    public static RegistryObject<SoundEvent> BLIGHT_BEGIN = registerSoundEvent("spell.blight.begin");
    public static RegistryObject<SoundEvent> SPIDER_ASPECT_CAST = registerSoundEvent("spell.spider_aspect.cast");
    public static RegistryObject<SoundEvent> BLOOD_NEEDLE_IMPACT = registerSoundEvent("entity.blood_needle.impact");
    public static RegistryObject<SoundEvent> FIRE_BOMB_CHARGE = registerSoundEvent("spell.fire_bomb.charge");
    public static RegistryObject<SoundEvent> FIRE_BOMB_CAST = registerSoundEvent("spell.fire_bomb.cast");

    public static RegistryObject<SoundEvent> DEAD_KING_SWING = registerSoundEvent("entity.dead_king.attack_swing");
    public static RegistryObject<SoundEvent> DEAD_KING_SLAM = registerSoundEvent("entity.dead_king.attack_slam");
    public static RegistryObject<SoundEvent> DEAD_KING_HIT = registerSoundEvent("entity.dead_king.attack_hit");
    public static RegistryObject<SoundEvent> DEAD_KING_RESURRECT = registerSoundEvent("entity.dead_king.resurrect");
    public static RegistryObject<SoundEvent> DEAD_KING_SPAWN = registerSoundEvent("entity.dead_king.spawn");
    public static RegistryObject<SoundEvent> DEAD_KING_FAKE_DEATH = registerSoundEvent("entity.dead_king.fake_death");
    public static RegistryObject<SoundEvent> DEAD_KING_DEATH = registerSoundEvent("entity.dead_king.death");
    public static RegistryObject<SoundEvent> DEAD_KING_HURT = registerSoundEvent("entity.dead_king.hurt");
    public static RegistryObject<SoundEvent> DEAD_KING_EXPLODE = registerSoundEvent("entity.dead_king.explode");

    public static RegistryObject<SoundEvent> FIRE_CAST = registerSoundEvent("cast.generic.fire");
    public static RegistryObject<SoundEvent> ICE_CAST = registerSoundEvent("cast.generic.ice");
    public static RegistryObject<SoundEvent> LIGHTNING_CAST = registerSoundEvent("cast.generic.lightning");
    public static RegistryObject<SoundEvent> HOLY_CAST = registerSoundEvent("cast.generic.holy");
    public static RegistryObject<SoundEvent> ENDER_CAST = registerSoundEvent("cast.generic.ender");
    public static RegistryObject<SoundEvent> BLOOD_CAST = registerSoundEvent("cast.generic.blood");
    public static RegistryObject<SoundEvent> EVOCATION_CAST = registerSoundEvent("cast.generic.evocation");
    public static RegistryObject<SoundEvent> POISON_CAST = registerSoundEvent("cast.generic.poison");


    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(IronsSpellbooks.MODID, name)));
    }
}
