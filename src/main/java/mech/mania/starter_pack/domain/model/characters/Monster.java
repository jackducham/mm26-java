package mech.mania.starter_pack.domain.model.characters;

import mech.mania.engine.domain.model.CharacterProtos;
import mech.mania.engine.domain.model.ItemProtos;
import mech.mania.starter_pack.domain.model.items.*;

import java.util.ArrayList;
import java.util.List;

public class Monster extends Character {
    private final int aggroRange;

    /**
     * Creates a Monster object from a given Protocol Buffer.
     * @param monsterProto the protocol buffer being copied
     */
    public Monster(CharacterProtos.Monster monsterProto) {
        super(monsterProto.getCharacter());
        aggroRange = monsterProto.getAggroRange();
    }

    public int getAggroRange() {
        return aggroRange;
    }
    
}
