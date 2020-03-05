package mech.mania;

import mech.mania.engine.server.communication.player.model.PlayerProtos.PlayerDecision;
import mech.mania.engine.server.communication.player.model.PlayerProtos.PlayerTurn;

public class PlayerStrategy implements PlayerStrategyInterface {
    @Override
    public PlayerDecision makeDecision(PlayerTurn turn) {
        return PlayerDecision.newBuilder()
                .build();
    }
}
