package mech.mania.engine.domain.model.characters;

import mech.mania.engine.domain.model.CharacterProtos;
import mech.mania.engine.domain.model.ItemProtos;
import mech.mania.engine.domain.model.items.*;

import java.util.ArrayList;
import java.util.List;

public class Monster extends Character {
    private final List<Item> drops;

    /**
     * Creates a Monster object from a given Protocol Buffer.
     * @param monsterProto the protocol buffer being copied
     */
    public Monster(CharacterProtos.Monster monsterProto) {
        super(monsterProto.getCharacter());

        drops = new ArrayList<>(monsterProto.getDropsCount());
        for (int i = 0; i < monsterProto.getDropsCount(); i++) {
            ItemProtos.Item protoItem = monsterProto.getDrops(i);
            switch(protoItem.getItemCase()) {
                case CLOTHES:
                    drops.add(i, new Clothes(protoItem.getClothes()));
                    break;
                case HAT:
                    drops.add(i, new Hat(protoItem.getHat()));
                    break;
                case SHOES:
                    drops.add(i, new Shoes(protoItem.getShoes()));
                    break;
                case WEAPON:
                    drops.add(i, new Weapon(protoItem.getWeapon()));
                    break;
                case CONSUMABLE:
                    drops.add(i, new Consumable(protoItem.getMaxStack(), protoItem.getConsumable()));
            }
        }
    }

    
}
