package mech.mania.starter_pack.domain.model.items;

import mech.mania.engine.domain.model.ItemProtos;

public class Accessory extends Wearable {
    private MagicEffect magicEffect;

    public Accessory(StatusModifier stats, MagicEffect magicEffect) {
        super(stats);
        switch (magicEffect) {
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
     * Creates an Accessory object based on a given Protocol Buffer.
     * @param accessoryProto the protocol buffer to be copied
     */
    public Accessory(ItemProtos.Accessory accessoryProto) {
        super(new StatusModifier(accessoryProto.getStats()));
        switch (accessoryProto.getMagicEffect()) {
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
