package mech.mania.starter_pack.domain;

import mech.mania.engine.domain.model.CharacterProtos.*;
import mech.mania.engine.domain.model.GameStateProtos.*;
import mech.mania.engine.domain.model.PlayerProtos.*;

public class PlayerStrategy implements Strategy{
    public PlayerDecision makeDecision(String playerName, GameState gameState){
        Player myPlayer = gameState.getPlayerNamesMap().get(playerName);

        // Returns MOVE decision to (0, 0) on private board
        return PlayerDecision.newBuilder()
                .setDecisionType(DecisionType.MOVE)
                .setTargetPosition(Position.newBuilder()
                        .setX(0).setY(0).setBoardId(playerName)
                        .build())
                .build();
    }
}
