package mech.mania.starter_pack.domain;

import mech.mania.engine.domain.model.CharacterProtos.CharacterDecision;
import mech.mania.engine.domain.model.GameState;
import mech.mania.engine.domain.model.characters.Player;
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

    /**
     * TODO: implement your strategy here! Make sure to return a CharacterDecision using either of the following
     * constructors:
     * CharacterDecision(decisionTypes decision, Position actionPosition)
     * CharacterDecision(decisionTypes decision, Position actionPosition, int inventoryIndex)
     */
    public CharacterDecision makeDecision(String playerName, GameState gameState){
        Player myPlayer = gameState.getAllPlayers().get(playerName);

        return null;
    }
}
