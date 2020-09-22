package mech.mania.starter_pack.domain.model.items;

import mech.mania.engine.domain.model.ItemProtos;

public class Weapon extends Wearable {
    protected int range = 0;
    protected int splashRadius = 0;
    protected int attack;

    protected TempStatusModifier onHitEffect;

    public Weapon(ItemProtos.Weapon weaponProto) {
        super(new StatusModifier(weaponProto.getStats()));
        this.range = weaponProto.getRange();
        this.splashRadius = weaponProto.getSplashRadius();
        this.onHitEffect = new TempStatusModifier(weaponProto.getOnHitEffect());
        this.attack = weaponProto.getAttack();
    }

    public int getRange() {
        return range;
    }

    public int getSplashRadius() {
        return splashRadius;
    }

    public TempStatusModifier getOnHitEffect() {
        return onHitEffect;
    }

    public int getAttack() {
        return attack;
    }

}
