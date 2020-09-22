package mech.mania.starter_pack.domain;

import mech.mania.engine.domain.model.GameState;
import mech.mania.starter_pack.domain.model.characters.CharacterDecision;

public interface Strategy {
    CharacterDecision makeDecision(String playerName, GameState gameState);
}
