package mech.mania.starter_pack.domain.model.characters;

import kotlin.Triple;
import mech.mania.engine.domain.model.CharacterProtos;
import mech.mania.starter_pack.domain.model.items.TempStatusModifier;
import mech.mania.starter_pack.domain.model.items.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.max;
import static java.lang.Math.min;

public abstract class Character {
    private final String name;

    protected final int baseSpeed;
    protected final int baseMaxHealth;
    protected final int baseAttack;
    protected final int baseDefense;

    /** Character's ongoing stats */
    protected int currentHealth;
    protected int level;
    protected int experience;

    /** Death parameters */
    protected int ticksSinceDeath;  // need access in Player to determine whether player just died
    private final boolean isDead;

    /** Position parameters */
    protected Position position;
    protected Position spawnPoint;

    /** Attack/damage parameters */
    protected Weapon weapon;

    // List of active status effects, their source Character, and an isPlayer flag
    List<Triple<TempStatusModifier, String, Boolean>> activeEffects;

    // map of attackers to amount of actual damage done
    protected Map<String, Integer> taggedPlayersDamage;

    public Character(final String name, final int baseSpeed, final int baseMaxHealth,
                     final int baseAttack, final int baseDefense, int currentHealth,
                     int experience, int ticksSinceDeath, boolean isDead, Position position,
                     Position spawnPoint, Weapon weapon, int activeEffectsTempStatusModifierCount,
                     TempStatusModifier[] activeEffectsTempStatusModifier,
                     String[] activeEffectsSource, boolean[] activeEffectsIsPlayer,
                     Map<String, Integer> taggedPlayersDamageMap) {
        this.name = name;

        this.baseSpeed = baseSpeed;
        this.baseMaxHealth = baseMaxHealth;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;

        this.currentHealth = currentHealth;
        this.experience = experience;

        this.ticksSinceDeath = ticksSinceDeath;
        this.isDead = isDead;

        this.position = position;
        this.spawnPoint = spawnPoint;

        this.weapon = weapon;

        // Build activeEffects triple
        this.activeEffects = new ArrayList<>();
        for(int i = 0; i < activeEffectsTempStatusModifierCount; i++){
            activeEffects.add(new Triple<>(
                    activeEffectsTempStatusModifier[i],
                    activeEffectsSource[i],
                    activeEffectsIsPlayer[i]
            ));
        }

        this.taggedPlayersDamage = taggedPlayersDamageMap;
    }

    /**
     * Constructor for Characters built from protos
     */
    public Character(CharacterProtos.Character character) {
        this.name = character.getName();

        this.baseSpeed = character.getBaseSpeed();
        this.baseMaxHealth = character.getBaseMaxHealth();
        this.baseAttack = character.getBaseAttack();
        this.baseDefense = character.getBaseDefense();

        this.currentHealth = character.getCurrentHealth();
        this.level = character.getLevel();
        this.experience = character.getExperience();

        this.ticksSinceDeath = character.getTicksSinceDeath();
        this.isDead = character.getIsDead();

        this.position = new Position(character.getPosition());
        this.spawnPoint = new Position(character.getSpawnPoint());

        this.weapon = new Weapon(character.getWeapon());

        // Build activeEffects triple
        this.activeEffects = new ArrayList<>();
        for(int i = 0; i < character.getActiveEffectsTempStatusModifierCount(); i++){
            activeEffects.add(new Triple<>(
                    new TempStatusModifier(character.getActiveEffectsTempStatusModifier(i)),
                    character.getActiveEffectsSource(i),
                    character.getActiveEffectsIsPlayer(i)
            ));
        }

        this.taggedPlayersDamage = character.getTaggedPlayersDamageMap();
    }


    public String getName() {
        return name;
    }

    public int getSpeed() {
        int flatChange = 0;
        double percentChange = 0;
        for (Triple<TempStatusModifier, String, Boolean> effect: activeEffects) {
            flatChange += effect.getFirst().getFlatSpeedChange();
            percentChange += effect.getFirst().getPercentSpeedChange();
        }

        // Make sure stat can't be negative
        flatChange = max(-baseSpeed, flatChange);
        percentChange = max(-1, percentChange);

        double speed = (baseSpeed + flatChange) * (1 + percentChange);
        return max(1, (int) speed); // speed can't be below 1
    }

    public int getMaxHealth() {
        int flatChange = 0;
        double percentChange = 0;
        for (Triple<TempStatusModifier, String, Boolean> effect: activeEffects) {
            flatChange += effect.getFirst().getFlatHealthChange();
            percentChange += effect.getFirst().getPercentHealthChange();
        }

        // Make sure stat can't be negative
        flatChange = max(-baseMaxHealth, flatChange);
        percentChange = max(-1, percentChange);

        double maxHealth = (baseMaxHealth + flatChange) * (1 + percentChange);
        return max(1, (int) maxHealth); // maxHealth can't be below 1
    }

    public int getExperience() {
        return experience;
    }

    public int getAttack() {
        int flatChange = 0;
        double percentChange = 0;
        for (Triple<TempStatusModifier, String, Boolean> effect: activeEffects) {
            flatChange += effect.getFirst().getFlatAttackChange();
            percentChange += effect.getFirst().getPercentAttackChange();
        }

        // Make sure stat can't be negative
        flatChange = max(-baseAttack, flatChange);
        percentChange = max(-1, percentChange);

        double attack = (baseAttack + flatChange) * (1 + percentChange);
        return max(1, (int) attack); // attack can't be below 1
    }

    public int getDefense() {
        int flatChange = 0;
        double percentChange = 0;
        for (Triple<TempStatusModifier, String, Boolean> effect: activeEffects) {
            flatChange += effect.getFirst().getFlatDefenseChange();
            percentChange += effect.getFirst().getPercentDefenseChange();
        }

        // Make sure stat can't be negative
        flatChange = max(-baseDefense, flatChange);
        percentChange = max(-1, percentChange);

        double defense = (baseDefense + flatChange) * (1 + percentChange);
        return (int) defense;
    }

    public int getCurrentHealth() {
        currentHealth = min(currentHealth, getMaxHealth());
        return currentHealth;
    }

    public int getLevel() {
        return getExperience() % 10; // @TODO: Replace with actual level equation
    }

    public int getTotalExperience() {
        return getLevel() * (getLevel() - 1) * 100 / 2 + getExperience();
    }

    public boolean isDead() {
        return isDead;
    }

    public Position getPosition() {
        return position;
    }

    public Position getSpawnPoint() {
        return spawnPoint;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public int getBaseMaxHealth() {
        return baseMaxHealth;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public int getTicksSinceDeath() {
        return ticksSinceDeath;
    }

    public List<Triple<TempStatusModifier, String, Boolean>> getActiveEffects() {
        return activeEffects;
    }

    public Map<String, Integer> getTaggedPlayersDamage() {
        return taggedPlayersDamage;
    }
}
