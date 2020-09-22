package mech.mania.starter_pack.domain.model.characters;

import mech.mania.engine.domain.model.CharacterProtos;

public class CharacterDecision {
    public enum decisionTypes {
        NONE, MOVE, ATTACK, EQUIP, DROP, PICKUP, PORTAL
    }
    private final decisionTypes decision;
    private final Position actionPosition;
    private final int index;

    public CharacterDecision(decisionTypes decision, Position actionPosition) {
        this.decision = decision;
        this.actionPosition = actionPosition;
        index = -1;
    }

    public CharacterDecision(decisionTypes decision, Position actionPosition, int actionIndex) {
        this.decision = decision;
        this.actionPosition = actionPosition;
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
        decisionBuilder.setTargetPosition(this.actionPosition.buildProtoClass());

        return decisionBuilder.build();
    }
}
