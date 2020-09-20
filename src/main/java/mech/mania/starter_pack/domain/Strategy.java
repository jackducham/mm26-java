package mech.mania.starter_pack.domain;

import mech.mania.engine.domain.model.CharacterProtos.*;
import mech.mania.engine.domain.model.GameState;

public interface Strategy {
    CharacterDecision makeDecision(String playerName, GameState gameState);
}
