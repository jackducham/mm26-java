package mech.mania.starter_pack.domain.model.characters;

import mech.mania.engine.domain.model.CharacterProtos;
import mech.mania.engine.domain.model.ProtoFactory;

public class CharacterDecision {
    public enum decisionTypes {
        NONE, MOVE, ATTACK, EQUIP, DROP, PICKUP, PORTAL
    }
    private final decisionTypes decision;
    private final Position actionPosition;
    private final int index;

    /**
     * Creates a decision to do nothing
     */
    public CharacterDecision(){
        this.decision = decisionTypes.NONE;
        this.actionPosition = null;
        this.index = -1;
    }

    /**
     * Creates a decision with a specified action position
     * @param decision Should be one of MOVE or ATTACK
     * @param actionPosition The position at which to apply the decision
     */
    public CharacterDecision(decisionTypes decision, Position actionPosition) {
        this.decision = decision;
        this.actionPosition = actionPosition;
        this.index = -1;
    }

    /**
     * Creates a decision with a specified index
     * @param decision Should be one of EQUIP, DROP, PICKUP, or PORTAL
     * @param actionIndex The index (of inventory, tile item list, or board portal list)
     */
    public CharacterDecision(decisionTypes decision, int actionIndex) {
        this.decision = decision;
        this.actionPosition = null;
        this.index = actionIndex;
    }

    public decisionTypes getDecision() {
        return decision;
    }

    public Position getActionPosition() {
        return actionPosition;
    }

    public int getIndex() {
        return index;
    }
}
