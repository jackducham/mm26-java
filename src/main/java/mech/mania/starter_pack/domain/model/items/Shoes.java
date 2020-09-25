package mech.mania.starter_pack.domain.model.items;

import mech.mania.engine.domain.model.ItemProtos;

public class Shoes extends Wearable {
    /**
     * Creates a Shoes object based on a Protocol Buffer.
     * @param shoesProto the protocol buffer to be copied
     */
    public Shoes(ItemProtos.Shoes shoesProto) {
        super(new StatusModifier(shoesProto.getStats()));
        this.turnsToDeletion = shoesProto.getTurnsToDeletion();
    }

}
