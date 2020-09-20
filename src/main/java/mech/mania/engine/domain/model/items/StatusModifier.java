package mech.mania.engine.domain.model.items;

import mech.mania.engine.domain.model.ItemProtos;

public class StatusModifier {
    private final int flatSpeedChange;
    private final double percentSpeedChange;
    /* flatSpeedChange is a flat bonus to the number of spaces that can be traversed in a single move action.
     * percentSpeedChange is a percentage speed modifier:
     * for example, if you can move 10 spaces (base of 5 + a flatSpeedChange of 5),
     * a percentSpeedChange of 0.1 makes that 11 i.e. (5 + 5) * (1 + 0.1).
     */
    private final int flatHealthChange;
    private final double percentHealthChange;
    /* flatHealthChange is a flat bonus to the maximum hit points of a character.
     * percentHealthChange is a percentage max HP modifier:
     * for example, if you can have 100 HP (base of 75 + a flatHealthChange of 25),
     * a percentHealthChange of 0.2 makes that 120 i.e. (75 + 25) * (1 + 0.2).
     */
    private final int flatExperienceChange;
    private final double percentExperienceChange;
    /* flatExperienceChange is a flat bonus to the experience gained per kill.
     * percentExperienceChange is a percentage EXP modifier:
     * for example, if you gain 525 XP (base kill XP of 500 + an flatExperienceChange of 25),
     * a percentExperienceChange of 0.5 makes that 788 i.e. (500 + 25) * (1 + 0.5).
     */
    private final int flatAttackChange;
    private final double percentAttackChange;
    /* flatAttackChange is a flat bonus to the attack dealt per hit.
     * percentAttackChange is a percentage damage modifier:
     * for example, if you deal 70 damage (NO BASE DAMAGE),
     * a percentAttackChange of 0.2 makes that 84 i.e. (70) * (1 + 0.2).
     */
    private final int flatDefenseChange;
    private final double percentDefenseChange;
    /* flatDefenseChange is a flat modifier to the defense per hit.
     * percentDefenseChange is a percentage damage taken modifier:
     * for example, if you have 25 defense (base defense of 0 + a flatDefenseChange of 25),
     * a percentDefenseChange of 0.2 makes that 30 i.e. (0 + 25) * (1 + 0.2).
     */
    private final int flatRegenPerTurn;
    // regenPerTurn is a flat amount of HP added to the current HP every turn.

    public StatusModifier(int speedChange, double percentSpeedChange, int healthChange, double percentHealthChange,
                          int experienceChange, double percentExperienceChange, int attackChange, double percentAttackChange,
                          int defenseChange, double percentDefenseChange, int regenPerTurn) {
        this.flatSpeedChange = speedChange;
        this.percentSpeedChange = percentSpeedChange;
        this.flatHealthChange = healthChange;
        this.percentHealthChange = percentHealthChange;
        this.flatExperienceChange = experienceChange;
        this.percentExperienceChange = percentExperienceChange;
        this.flatAttackChange = attackChange;
        this.percentAttackChange = percentAttackChange;
        this.flatDefenseChange = defenseChange;
        this.percentDefenseChange = percentDefenseChange;
        this.flatRegenPerTurn = regenPerTurn;
    }

    public StatusModifier(ItemProtos.StatusModifier statusModifierProto) {
        this.flatRegenPerTurn = statusModifierProto.getFlatRegenPerTurn();
        this.flatSpeedChange = statusModifierProto.getFlatSpeedChange();
        this.flatHealthChange = statusModifierProto.getFlatHealthChange();
        this.flatExperienceChange = statusModifierProto.getFlatExperienceChange();

        this.flatAttackChange = statusModifierProto.getFlatAttackChange();
        this.flatDefenseChange = statusModifierProto.getFlatDefenseChange();

        this.percentSpeedChange = statusModifierProto.getPercentSpeedChange();
        this.percentHealthChange = statusModifierProto.getPercentHealthChange();
        this.percentExperienceChange = statusModifierProto.getPercentExperienceChange();

        this.percentAttackChange = statusModifierProto.getPercentAttackChange();
        this.percentDefenseChange = statusModifierProto.getPercentDefenseChange();
    }

    public int getFlatSpeedChange() {
        return flatSpeedChange;
    }

    public double getPercentSpeedChange() {
        return percentSpeedChange;
    }

    public int getFlatHealthChange() {
        return flatHealthChange;
    }

    public double getPercentHealthChange() {
        return percentHealthChange;
    }

    public int getFlatExperienceChange() {
        return flatExperienceChange;
    }

    public double getPercentExperienceChange() {
        return percentExperienceChange;
    }

    public int getFlatAttackChange() {
        return flatAttackChange;
    }

    public double getPercentAttackChange() {
        return percentAttackChange;
    }

    public int getFlatDefenseChange() {
        return flatDefenseChange;
    }

    public double getPercentDefenseChange() {
        return percentDefenseChange;
    }

    public int getFlatRegenPerTurn() {
        return flatRegenPerTurn;
    }

}
