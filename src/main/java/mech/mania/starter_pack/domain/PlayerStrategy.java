package mech.mania.starter_pack.domain;

import mech.mania.engine.domain.model.CharacterProtos.*;
import mech.mania.engine.domain.model.GameStateProtos.*;
import mech.mania.starter_pack.domain.memory.MemoryObject;
import mech.mania.starter_pack.domain.memory.RedisWritePolicy;

public class PlayerStrategy implements Strategy {
    /**
     * This MemoryObject allows you to store persistent data of the types
     * int, double/float, string, and boolean. See the MemoryObject documentation
     * (https://github.com/jackducham/mm26-infra/blob/master/memory-object/API-Design.md)
     * for details on usage.
     */
    //private MemoryObject memory = new MemoryObject(RedisWritePolicy.WRITETHROUGH);

    public CharacterDecision createPlayerDecision(DecisionType decision, int x, int y, String boardId, int index) {
        return CharacterDecision.newBuilder()
                .setDecisionType(decision)
                .setTargetPosition(Position.newBuilder()
                        .setX(x).setY(y).setBoardId(boardId)
                        .build())
                .setIndex(index)
                .build();
    }

    public CharacterDecision makeDecision(String playerName, GameState gameState){
        Player myPlayer = gameState.getPlayerNamesMap().get(playerName);

        // Returns MOVE decision to (0, 0) on private board
        return createPlayerDecision(DecisionType.MOVE, 0, 0, playerName,0);
    }
}
