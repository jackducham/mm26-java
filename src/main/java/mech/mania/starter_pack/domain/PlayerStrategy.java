package mech.mania.starter_pack.domain;

import mech.mania.engine.domain.model.CharacterProtos.*;
import mech.mania.engine.domain.model.GameStateProtos.*;
import mech.mania.engine.domain.model.PlayerProtos.*;

public class PlayerStrategy implements Strategy {
    public PlayerDecision createPlayerDecision(DecisionType decision, int x, int y, String boardId, int index) {
        return PlayerDecision.newBuilder()
                .setDecisionType(decision)
                .setTargetPosition(Position.newBuilder()
                        .setX(x).setY(y).setBoardId(boardId)
                        .build())
                .setIndex(index)
                .build();
    }

    public PlayerDecision makeDecision(String playerName, GameState gameState){
        Player myPlayer = gameState.getPlayerNamesMap().get(playerName);

        // Returns MOVE decision to (0, 0) on private board
        return createPlayerDecision(DecisionType.MOVE, 0, 0, playerName,0);
    }
}
