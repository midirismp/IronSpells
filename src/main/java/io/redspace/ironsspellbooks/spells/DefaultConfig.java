package io.redspace.ironsspellbooks.spells;

import java.util.function.Consumer;

public class DefaultConfig {
    public SpellRarity minRarity;
    public SchoolType school;
    public int maxLevel = -1;
    public double cooldownInSeconds = -1;

    public DefaultConfig(Consumer<DefaultConfig> intialize) throws RuntimeException {
        intialize.accept(this);
        build();
    }

    public DefaultConfig() {
    }

    public DefaultConfig setMaxLevel(int i) {
        this.maxLevel = i;
        return this;
    }

    public DefaultConfig setMinRarity(SpellRarity i) {
        this.minRarity = i;
        return this;
    }

    public DefaultConfig setCooldownSeconds(double i) {
        this.cooldownInSeconds = i;
        return this;
    }

    public DefaultConfig setSchool(SchoolType school) {
        this.school = school;
        return this;
    }

    public DefaultConfig build() throws RuntimeException {
        if (!this.validate())
            throw new RuntimeException("You didn't define all config attributes!");

        return this;
    }

    private boolean validate() {
        return minRarity != null && maxLevel >= 0 && school != null && cooldownInSeconds >= 0;
    }
}

