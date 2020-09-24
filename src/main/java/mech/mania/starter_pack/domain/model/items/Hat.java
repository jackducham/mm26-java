package mech.mania.starter_pack.domain.model.items;

import mech.mania.engine.domain.model.ItemProtos;

public class Hat extends Wearable {
    private HatEffect hatEffect;

    /**
     * Creates a Hat object based on a given Protocol Buffer.
     * @param hatProto the protocol buffer to be copied
     */
    public Hat(ItemProtos.Hat hatProto) {
        super(new StatusModifier(hatProto.getStats()));
        // TODO: copy HatEffect
    }

    /**
     * Getter for the Hat's effect.
     * @return the Hat's effect
     */
    public HatEffect getHatEffect() {
        return hatEffect;
    }

}
