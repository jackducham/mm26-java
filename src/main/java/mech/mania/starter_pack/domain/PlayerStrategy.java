package mech.mania.starter_pack.domain;

import mech.mania.starter_pack.domain.model.GameState;
import mech.mania.starter_pack.domain.model.characters.*;
import mech.mania.starter_pack.domain.model.characters.Character;
import mech.mania.starter_pack.domain.model.board.*;
import mech.mania.starter_pack.domain.model.items.*;
import mech.mania.starter_pack.domain.memory.MemoryObject;

import java.util.List;

import mech.mania.starter_pack.domain.model.characters.CharacterDecision.DecisionType;


public class PlayerStrategy implements Strategy {
    /**
     * This MemoryObject allows you to store persistent data of the types
     * int, double/float, string, and boolean. See the MemoryObject documentation
     * (https://github.com/jackducham/mm26-infra/blob/ready-for-release-94/memory-object/API.md)
     * for details on usage.
     */
    private MemoryObject memory;

    public PlayerStrategy(MemoryObject memory){
        this.memory = memory;
    }

    /**
     * TODO: implement your strategy here! Return a CharacterDecision using either of the following constructors:
     * CharacterDecision(DecisionType decision, Position actionPosition)
     * CharacterDecision(DecisionType decision, int actionIndex)
     *
     * The default constructor makes no decision -- your player will not act in the next turn
     */
    public CharacterDecision makeDecision(String playerName, GameState gameState){
        /*
         * This API object gives you access to a few helper functions including pathfinding!
         * You'll have to reinitialize it with the new GameState and your playerName ever turn.
         */
        API api = new API(gameState, playerName);

        Player myPlayer = gameState.getAllPlayers().get(playerName);

        List<Character> enemiesHit = api.findAllEnemiesHit(myPlayer.getPosition());

        Position closestPortal = api.findClosestPortal(myPlayer.getPosition());

        List<Character> enemiesList = api.findEnemies(myPlayer.getPosition());

        List<Character> enemiesInRangeList = api.findEnemiesInRangeOfAttack(myPlayer.getPosition());

        List<Monster> monstersList = api.findMonsters(myPlayer.getPosition());

        List<Player> leaderboard = api.getLeaderboard();

        // if I'm available to attack something, attack it
        if (api.inRangeOfAttack(myPlayer.getPosition())) {
            List<Character> charactersInRange = api.findEnemies(myPlayer.getPosition());
            if (charactersInRange.size() > 0) {
                return new CharacterDecision(DecisionType.ATTACK, charactersInRange.get(0).getPosition());
            }
        }

        return new CharacterDecision();
    }
}
