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

    public CharacterProtos.CharacterDecision buildProtoClassCharacterDecision(){
        CharacterProtos.CharacterDecision.Builder decisionBuilder = CharacterProtos.CharacterDecision.newBuilder();
        switch(this.decision) {
            case NONE:
                decisionBuilder.setDecisionType(CharacterProtos.DecisionType.NONE);
                break;
            case MOVE:
                decisionBuilder.setDecisionType(CharacterProtos.DecisionType.MOVE);
                break;
            case ATTACK:
                decisionBuilder.setDecisionType(CharacterProtos.DecisionType.ATTACK);
                break;
            case EQUIP:
                decisionBuilder.setDecisionType(CharacterProtos.DecisionType.EQUIP);
                break;
            case DROP:
                decisionBuilder.setDecisionType(CharacterProtos.DecisionType.DROP);
                break;
            case PICKUP:
                decisionBuilder.setDecisionType(CharacterProtos.DecisionType.PICKUP);
                break;
            case PORTAL:
                decisionBuilder.setDecisionType(CharacterProtos.DecisionType.PORTAL);
                break;
        }

        decisionBuilder.setIndex(this.index);
        if(actionPosition != null) {
            decisionBuilder.setTargetPosition(ProtoFactory.Position(actionPosition));
        }

        return decisionBuilder.build();
    }
}
