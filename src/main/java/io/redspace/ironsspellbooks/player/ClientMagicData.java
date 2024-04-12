package io.redspace.ironsspellbooks.player;

import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerCooldowns;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerMagicData;
import io.redspace.ironsspellbooks.capabilities.magic.ClientSpellTargetingData;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.spells.CastSource;
import io.redspace.ironsspellbooks.spells.CastType;
import io.redspace.ironsspellbooks.spells.SpellType;
import io.redspace.ironsspellbooks.util.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.UUID;

public class ClientMagicData {

    /**
     * Current Player's Synced Data
     */
    private static final PlayerMagicData playerMagicData = new PlayerMagicData();

    /**
     * Other Player's Synced Data
     */
    private static final HashMap<Integer, SyncedSpellData> playerSyncedDataLookup = new HashMap<>();
    private static final SyncedSpellData emptySyncedData = new SyncedSpellData(-999);

    /**
     * Local Targeting data
     */
    private static ClientSpellTargetingData spellTargetingData;

    public static void setTargetingData(ClientSpellTargetingData spellTargetingData) {
        ClientMagicData.spellTargetingData = spellTargetingData;
    }

    public static ClientSpellTargetingData getTargetingData() {
        if (spellTargetingData == null)
            setTargetingData(new ClientSpellTargetingData());
        return spellTargetingData;
    }

    public static void resetTargetingData() {
        spellTargetingData = null;
    }

    /**
     * Animation Data
     */
    public static HashMap<UUID, KeyframeAnimationPlayer> castingAnimationPlayerLookup = new HashMap<>();


    public static PlayerCooldowns getCooldowns() {
        return playerMagicData.getPlayerCooldowns();
    }

    public static float getCooldownPercent(SpellType spellType) {
        return playerMagicData.getPlayerCooldowns().getCooldownPercent(spellType);
    }

    public static int getPlayerMana() {
        return playerMagicData.getMana();
    }

    public static void setMana(int playerMana) {
        ClientMagicData.playerMagicData.setMana(playerMana);
    }

    public static CastType getCastType() {
        return ClientMagicData.playerMagicData.getCastType();
    }

    public static int getCastingSpellId() {
        return playerMagicData.getCastingSpellId();
    }

    public static int getCastDurationRemaining() {
        return playerMagicData.getCastDurationRemaining();
    }

    public static int getCastDuration() {
        return playerMagicData.getCastDuration();
    }

    public static boolean isCasting() {
        return playerMagicData.isCasting();
    }

    public static void handleCastDuration() {
        playerMagicData.handleCastDuration();
    }

    public static float getCastCompletionPercent() {
        return playerMagicData.getCastCompletionPercent();
    }

    public static void setClientCastState(int spellId, int spellLevel, int castDuration, CastSource castSource) {
        playerMagicData.initiateCast(spellId, spellLevel, castDuration, castSource);
    }

    public static void resetClientCastState(UUID playerUUID) {
        //Ironsspellbooks.logger.debug("resetClientCastState.1: instanceUUID:{}, playerUUID:{}", Minecraft.getInstance().player.getUUID(), playerUUID);

        if (Minecraft.getInstance().player.getUUID().equals(playerUUID)) {
            //Ironsspellbooks.logger.debug("resetClientCastState.1.1");
            playerMagicData.resetCastingState();
            resetTargetingData();
        }

        var animationPlayer = castingAnimationPlayerLookup.getOrDefault(playerUUID, null);
        if (animationPlayer != null) {
            //Ironsspellbooks.logger.debug("resetClientCastState.1.2");
            animationPlayer.stop();
        }

        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isUsingItem() && Minecraft.getInstance().player.getUUID().equals(playerUUID)) {
            //Ironsspellbooks.logger.debug("resetClientCastState.2: instanceUUID:{}, playerUUID:{}", Minecraft.getInstance().player.getUUID(), playerUUID);
            Minecraft.getInstance().player.stopUsingItem();
        }
    }

    public static SyncedSpellData getSyncedSpellData(LivingEntity livingEntity) {
        if (livingEntity instanceof Player) {
            return playerSyncedDataLookup.getOrDefault(livingEntity.getId(), emptySyncedData);
        }
        if (livingEntity instanceof AbstractSpellCastingMob abstractSpellCastingMob) {
            return abstractSpellCastingMob.getPlayerMagicData().getSyncedData();
        }
        return new SyncedSpellData(null);

    }

    public static void handlePlayerSyncedData(SyncedSpellData playerSyncedData) {
        playerSyncedDataLookup.put(playerSyncedData.getServerPlayerId(), playerSyncedData);
    }

    public static void handleAbstractCastingMobSyncedData(int entityId, SyncedSpellData syncedSpellData) {
        var level = Minecraft.getInstance().level;

        if (Log.SPELL_DEBUG) {
            IronsSpellbooks.LOGGER.debug("handleAbstractCastingMobSyncedData {}, {}, {}", level, entityId, syncedSpellData);
        }

        if (level == null) {
            return;
        }

        var entity = level.getEntity(entityId);
        if (entity instanceof AbstractSpellCastingMob abstractSpellCastingMob) {
            abstractSpellCastingMob.setSyncedSpellData(syncedSpellData);
        }
    }
}