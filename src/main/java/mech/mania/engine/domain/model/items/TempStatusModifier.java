package mech.mania.engine.domain.model.items;

import mech.mania.engine.domain.model.ItemProtos;

public class TempStatusModifier extends StatusModifier{
    private final int turnsLeft;
    private final int damagePerTurn; // flat amount of damage dealt to the character each turn,


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
