package mech.mania.starter_pack.domain.model.characters;

import mech.mania.engine.domain.model.CharacterProtos;
import mech.mania.engine.domain.model.ItemProtos;
import mech.mania.starter_pack.domain.model.items.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Monster extends Character {
    private final int aggroRange;

    public Monster(final String name, final int baseSpeed, final int baseMaxHealth,
                   final int baseAttack, final int baseDefense, int currentHealth,
                   int experience, int ticksSinceDeath, boolean isDead, Position position,
                   Position spawnPoint, Weapon weapon, int activeEffectsTempStatusModifierCount,
                   TempStatusModifier[] activeEffectsTempStatusModifier,
                   String[] activeEffectsSource, boolean[] activeEffectsIsPlayer,
                   Map<String, Integer> taggedPlayersDamageMap, int aggroRange) {
        super(name, baseSpeed, baseMaxHealth, baseAttack, baseDefense, currentHealth,
                experience, ticksSinceDeath, isDead, position, spawnPoint, weapon,
                activeEffectsTempStatusModifierCount, activeEffectsTempStatusModifier,
                activeEffectsSource, activeEffectsIsPlayer, taggedPlayersDamageMap);
        this.aggroRange = aggroRange;
    }

    /**
     * Creates a Monster object from a given Protocol Buffer.
     * @param monsterProto the protocol buffer being copied
     */
    public Monster(CharacterProtos.Monster monsterProto) {
        super(monsterProto.getCharacter());
        aggroRange = monsterProto.getAggroRange();
    }

    public int getAggroRange() {
        return aggroRange;
    }
    
}
