package io.redspace.ironsspellbooks.setup;

import io.redspace.ironsspellbooks.capabilities.magic.MagicEvents;
import io.redspace.ironsspellbooks.entity.mobs.MobSyncedCastingData;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.player.CommonPlayerEvents;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.tetra.TetraActualImpl;
import io.redspace.ironsspellbooks.tetra.TetraProxy;
import io.redspace.ironsspellbooks.util.Utils;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup {

    public static void setup() {
        IEventBus bus = MinecraftForge.EVENT_BUS;

        //PLAYER
        //bus.addListener(ClientPlayerEvents::onPlayerTick); Firing for all players
        //bus.addListener(KeyMappings::onRegisterKeybinds);
//        bus.addListener(ClientPlayerEvents::onLivingEquipmentChangeEvent);
        //bus.addListener(ClientPlayerEvents::onPlayerRenderPre);
//        bus.addListener(ClientPlayerEvents::onLivingEntityUseItemEventStart);
//        bus.addListener(ClientPlayerEvents::onLivingEntityUseItemEventTick);
//        bus.addListener(ClientPlayerEvents::onLivingEntityUseItemEventFinish);

        //MANA
        bus.addGenericListener(Entity.class, MagicEvents::onAttachCapabilitiesPlayer);
        //bus.addListener(ManaEvents::onPlayerCloned);
        bus.addListener(MagicEvents::onRegisterCapabilities);
        bus.addListener(MagicEvents::onWorldTick);
        bus.addListener(CommonPlayerEvents::onPlayerRightClickItem);
        bus.addListener(CommonPlayerEvents::onUseItemStop);

        //SPELLBOOKS
        //bus.addGenericListener(ItemStack.class, SpellBookDataEvents::onAttachCapabilities);
        //bus.addListener(SpellBookDataEvents::onRegisterCapabilities);

        //SCROLLS
        //bus.addListener(ScrollDataEvents::onRegisterCapabilities);
        //bus.addGenericListener(ItemStack.class, ScrollDataEvents::onAttachCapabilitiesItemStack);

    }

    public static void init(FMLCommonSetupEvent event) {
        Messages.register();

        if (ModList.get().isLoaded("tetra")) {
            TetraProxy.PROXY = new TetraActualImpl();
        }

        EntityDataSerializers.registerSerializer(MobSyncedCastingData.MOB_SYNCED_CASTING_DATA);
        EntityDataSerializers.registerSerializer(SyncedSpellData.SYNCED_SPELL_DATA);

        SpawnPlacements.register(EntityRegistry.NECROMANCER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Utils::checkMonsterSpawnRules);

    }


}