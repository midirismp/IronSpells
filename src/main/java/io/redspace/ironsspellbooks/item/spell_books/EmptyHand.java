package io.redspace.ironsspellbooks.item.spell_books;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.registries.AttributeRegistry;
import io.redspace.ironsspellbooks.spells.SpellRarity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.util.LazyOptional;

import java.util.UUID;

public class EmptyHand extends SpellBook {
    private final LazyOptional<Multimap<Attribute, AttributeModifier>> lazyOptional;

    public EmptyHand() {
        super(10, SpellRarity.LEGENDARY);
        lazyOptional = LazyOptional.of(this::buildMap);
    }

    private Multimap<Attribute, AttributeModifier> buildMap() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", .30, AttributeModifier.Operation.MULTIPLY_BASE));
        builder.put(AttributeRegistry.COOLDOWN_REDUCTION.get(), new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", .30, AttributeModifier.Operation.MULTIPLY_BASE));
        return builder.build();
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.lazyOptional.resolve().get() : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }
}
