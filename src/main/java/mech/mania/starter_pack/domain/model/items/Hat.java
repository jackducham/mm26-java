package mech.mania.starter_pack.domain.model.items;

import mech.mania.engine.domain.model.ItemProtos;

public class Hat extends Wearable {
    private MagicEffect magicEffect;

    /**
     * Creates a Hat object based on a given Protocol Buffer.
     * @param hatProto the protocol buffer to be copied
     */
    public Hat(ItemProtos.Hat hatProto) {
        super(new StatusModifier(hatProto.getStats()));
        switch (hatProto.getMagicEffect()) {
            case SHOES_BOOST:
                this.magicEffect = MagicEffect.SHOES_BOOST;
                break;
            case WEAPON_BOOST:
                this.magicEffect = MagicEffect.WEAPON_BOOST;
                break;
            case CLOTHES_BOOST:
                this.magicEffect = MagicEffect.CLOTHES_BOOST;
                break;
            case STACKING_BONUS:
                this.magicEffect = MagicEffect.STACKING_BONUS;
                break;
            case TRIPLED_ON_HIT:
                this.magicEffect = MagicEffect.TRIPLED_ON_HIT;
                break;
            case LINGERING_POTIONS:
                this.magicEffect = MagicEffect.LINGERING_POTIONS;
                break;
            case NONE:
                this.magicEffect = MagicEffect.NONE;
                break;
        }
    }

    /**
     * Getter for the Hat's effect.
     * @return the Hat's effect
     */
    public MagicEffect getMagicEffect() {
        return magicEffect;
    }

}
