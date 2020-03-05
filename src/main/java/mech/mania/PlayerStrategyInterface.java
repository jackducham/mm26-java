package mech.mania;

import mech.mania.engine.server.communication.player.model.PlayerProtos.PlayerDecision;
import mech.mania.engine.server.communication.player.model.PlayerProtos.PlayerTurn;

public interface PlayerStrategyInterface {
    PlayerDecision makeDecision(PlayerTurn turn);
}
