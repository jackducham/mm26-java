package mech.mania.starter_pack.domain.model.items;

import mech.mania.engine.domain.model.ItemProtos;

public class TempStatusModifier extends StatusModifier{
    private final int turnsLeft;
    private final int damagePerTurn; // flat amount of damage dealt to the character each turn,

    public TempStatusModifier(int speedChange, double percentSpeedChange, int healthChange, double percentHealthChange,
                              int experienceChange, double percentExperienceChange, int attackChange, double percentAttackChange,
                              int defenseChange, double percentDefenseChange, int regenPerTurn,
                              final int turnsLeft, final int damagePerTurn) {
        super(speedChange, percentSpeedChange, healthChange, percentHealthChange, experienceChange,
              percentExperienceChange, attackChange, percentAttackChange, defenseChange, percentDefenseChange,
              regenPerTurn);
        this.turnsLeft = turnsLeft;
        this.damagePerTurn = damagePerTurn;
    }


    public TempStatusModifier(ItemProtos.TempStatusModifier tempStatusModifierProto) {
        super(
                tempStatusModifierProto.getStats().getFlatSpeedChange(),
                tempStatusModifierProto.getStats().getPercentSpeedChange(),
                tempStatusModifierProto.getStats().getFlatHealthChange(),
                tempStatusModifierProto.getStats().getPercentHealthChange(),
                tempStatusModifierProto.getStats().getFlatExperienceChange(),
                tempStatusModifierProto.getStats().getPercentExperienceChange(),
                tempStatusModifierProto.getStats().getFlatAttackChange(),
                tempStatusModifierProto.getStats().getPercentAttackChange(),
                tempStatusModifierProto.getStats().getFlatDefenseChange(),
                tempStatusModifierProto.getStats().getPercentDefenseChange(),
                tempStatusModifierProto.getStats().getFlatRegenPerTurn()
        );
        this.turnsLeft = tempStatusModifierProto.getTurnsLeft();
        this.damagePerTurn = tempStatusModifierProto.getFlatDamagePerTurn();
    }

    public int getTurnsLeft() {
        return turnsLeft;
    }

    public int getDamagePerTurn() {
        return damagePerTurn;
    }
}
