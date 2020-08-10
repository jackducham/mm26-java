package mech.mania.starter_pack.domain;

import mech.mania.engine.domain.game.GameState;
import mech.mania.engine.domain.game.characters.CharacterDecision;

public interface Strategy {
    public CharacterDecision makeDecision(String playerName, GameState gameState);
}
