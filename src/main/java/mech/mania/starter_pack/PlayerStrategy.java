package mech.mania.starter_pack;

import mech.mania.engine.domain.model.CharacterProtos.*;
import mech.mania.engine.domain.model.GameStateProtos.*;
import mech.mania.engine.domain.model.PlayerProtos.*;
import mech.mania.engine.domain.model.ApiProtos.*;

import java.util.List;
import java.util.logging.Logger;

public class PlayerStrategy implements Strategy {

    private static final Logger LOGGER = Logger.getLogger("PlayerStrategy");

    public PlayerDecision makeDecision(String playerName, GameState gameState){
        Player myPlayer = gameState.getPlayerNamesMap().get(playerName);
        API api = new API(gameState, playerName);

        // Pathfinding
        // was unintuitive to get player's position or the board id
        Position end = Position.newBuilder()
                .setBoardId(myPlayer.getCharacter().getPosition().getBoardId())
                .setX(0).setY(1)
                .build();
        APIPathFindingResponse pathFindingInfo = api.pathFinding(myPlayer.getCharacter().getPosition(), end);
        List<Position> path = pathFindingInfo.getPathList();

        // FindEnemies
        APIFindEnemiesResponse findEnemiesInfo = api.findEnemies();

        // FindMonsters
        APIFindMonstersResponse findMonstersInfo = api.findMonsters();

        // FindEnemiesInRange
        APIFindEnemiesInRangeResponse enemiesInRangeInfo = api.findEnemiesInRange();

        // CanBeAttacked
        APICanBeAttackedResponse canBeAttackedInfo = api.canBeAttacked();
        if (canBeAttackedInfo.getCanBeAttacked()) {
            LOGGER.info("About to be attacked?");
        }

        // LeaderBoard
        APILeaderBoardResponse leaderBoardInfo = api.leaderBoard();
        List<Player> leaderBoard = leaderBoardInfo.getLeaderBoardList();

        // FindAllEnemiesHit
        APIAllEnemiesHitResponse allEnemiesHitInfo = api.findAllEnemiesHit(myPlayer.getCharacter().getPosition());

        // FindItemsInRange
        APIItemsInRangeResponse itemsInRangeInfo = api.findItemsInRange(10);

        // Returns MOVE decision to (0, 0) on private board
        return PlayerDecision.newBuilder()
                .setDecisionType(DecisionType.MOVE)
                .setTargetPosition(Position.newBuilder()
                        .setX(0).setY(0)
                        .setBoardId(playerName)
                        .build())
                .build();
    }
}
