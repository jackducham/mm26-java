package mech.mania.starter_pack.domain;

import mech.mania.engine.domain.game.GameState;
import mech.mania.engine.domain.game.characters.*;
import mech.mania.engine.domain.game.board.*;
import mech.mania.engine.domain.game.items.*;

import java.util.List;

import static mech.mania.engine.domain.game.pathfinding.PathFinder.findPath;

public class PlayerStrategy implements Strategy{
    public CharacterDecision makeDecision(String playerName, GameState gameState){
        Player myPlayer = gameState.getPlayer(playerName);
        return new CharacterDecision(CharacterDecision.decisionTypes.MOVE, new Position(0, 0, playerName));
//
//        // Target adjacent space to the east
//        Position target = myPlayer.getPosition();
//        target.setX(target.getX() + 1);
//
//        // Check that move is possible
//        List<Position> path = findPath(gameState, myPlayer.getPosition(), target);
//        if(path.isEmpty() || path.size() > myPlayer.getSpeed()){
//            // Move west instead
//            target.setX(target.getX() - 2);
//        }
//
//        return new CharacterDecision(CharacterDecision.decisionTypes.MOVE, target);
    }
}
