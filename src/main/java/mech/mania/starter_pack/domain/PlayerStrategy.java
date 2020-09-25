package mech.mania.starter_pack.domain;

import mech.mania.starter_pack.domain.model.GameState;
import mech.mania.starter_pack.domain.model.characters.*;
import mech.mania.starter_pack.domain.memory.MemoryObject;

public class PlayerStrategy implements Strategy {
    /**
     * This MemoryObject allows you to store persistent data of the types
     * int, double/float, string, and boolean. See the MemoryObject documentation
     * (https://github.com/jackducham/mm26-infra/blob/ready-for-release-94/memory-object/API.md)
     * for details on usage.
     */
    private MemoryObject memory = new MemoryObject();

    /**
     * This API object gives you access to a few helper functions including pathfinding!
     * You'll have to reinitialize it with the new GameState and your playerName every turn.
     */
    private API api;

    /**
     * TODO: implement your strategy here! Return a CharacterDecision using either of the following constructors:
     * CharacterDecision(decisionTypes decision, Position actionPosition)
     * CharacterDecision(decisionTypes decision, int actionIndex)
     *
     * The default constructor makes no decision -- your player will not act in the next turn
     */
    public CharacterDecision makeDecision(String playerName, GameState gameState){
        api = new API(gameState, playerName);

        Player myPlayer = gameState.getAllPlayers().get(playerName);

        return new CharacterDecision();
    }
}
