package mech.mania.starter_pack.domain.model.items;

import mech.mania.engine.domain.model.ItemProtos;

public class Clothes extends Wearable {

    public Clothes(StatusModifier stats) {
        super(stats);
    }

    /**
     * Creates a Clothes object based on a Protocol Buffer.
     * @param clothesProto the protocol buffer to be copied
     */
    public Clothes(ItemProtos.Clothes clothesProto) {
        super(new StatusModifier(clothesProto.getStats()));
        this.turnsToDeletion = clothesProto.getTurnsToDeletion();
    }
}
