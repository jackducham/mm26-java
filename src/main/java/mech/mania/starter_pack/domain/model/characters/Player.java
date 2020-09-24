package mech.mania.starter_pack.domain.model.characters;

import kotlin.Triple;
import mech.mania.engine.domain.model.CharacterProtos;
import mech.mania.engine.domain.model.ItemProtos;
import mech.mania.starter_pack.domain.model.items.*;

import static java.lang.Math.max;

public class Player extends Character {
    public static final int INVENTORY_SIZE = 16;
    private final Hat hat;
    private final Accessory accessory;
    private final Clothes clothes;
    private final Shoes shoes;
    private final Item[] inventory;


    public Player(CharacterProtos.Player playerProto) {
        super(playerProto.getCharacter());

        hat = new Hat(playerProto.getHat());
        accessory = new Accessory(playerProto.getAccessory());
        clothes = new Clothes(playerProto.getClothes());
        shoes = new Shoes(playerProto.getShoes());
        inventory = new Item[INVENTORY_SIZE];

        for (int i = 0; i < playerProto.getInventoryCount(); i++) {
            ItemProtos.Item protoItem = playerProto.getInventory(i);
            switch(protoItem.getItemCase()) {
                case CLOTHES:
                    inventory[i] = new Clothes(protoItem.getClothes());
                    break;
                case HAT:
                    inventory[i] = new Hat(protoItem.getHat());
                    break;
                case ACCESSORY:
                    inventory[i] = new Accessory(protoItem.getAccessory());
                case SHOES:
                    inventory[i] = new Shoes(protoItem.getShoes());
                    break;
                case WEAPON:
                    inventory[i] = new Weapon(protoItem.getWeapon());
                    break;
                case CONSUMABLE:
                    inventory[i] = new Consumable(protoItem.getConsumable().getMaxStack(), protoItem.getConsumable());
            }
        }
    }


    public Hat getHat() {
        return hat;
    }

    public Clothes getClothes() {
        return clothes;
    }

    public Shoes getShoes() {
        return shoes;
    }

    public int getInventorySize() {
        return INVENTORY_SIZE;
    }

    public Item[] getInventory() {
        return inventory;
    }


    @Override
    public int getSpeed() {
        int flatChange = 0;
        double percentChange = 0;

        if (hat != null) {
            flatChange += hat.getStats().getFlatSpeedChange();
            percentChange += hat.getStats().getPercentSpeedChange();
        }
        if (accessory != null) {
            flatChange += accessory.getStats().getFlatSpeedChange();
            percentChange += accessory.getStats().getPercentSpeedChange();
        }
        if (clothes != null) {
            flatChange += clothes.getStats().getFlatSpeedChange();
            percentChange += clothes.getStats().getPercentSpeedChange();
        }
        if (shoes != null) {
            flatChange += shoes.getStats().getFlatSpeedChange();
            percentChange += shoes.getStats().getPercentSpeedChange();

            if(hat != null && hat.getMagicEffect().equals(MagicEffect.SHOES_BOOST)) {
                flatChange += shoes.getStats().getFlatSpeedChange();
            }

            if (accessory != null && accessory.getMagicEffect().equals(MagicEffect.SHOES_BOOST)) {
                flatChange += shoes.getStats().getFlatSpeedChange();
            }
        }
        if (weapon != null) {
            flatChange += weapon.getStats().getFlatSpeedChange();
            percentChange += weapon.getStats().getPercentSpeedChange();
        }

        // Add active effects
        for (Triple<TempStatusModifier, String, Boolean> effect: activeEffects) {
            flatChange += effect.getFirst().getFlatSpeedChange();
            percentChange += effect.getFirst().getPercentSpeedChange();
        }

        // Make sure stat can't be negative
        flatChange = max(-baseSpeed, flatChange);
        percentChange = max(-1, percentChange);

        double speed = (baseSpeed + flatChange) * (1 + percentChange);
        return max(1, (int) speed); // speed can't be below 1
    }


    @Override
    public int getMaxHealth() {
        int flatChange = 0;
        double percentChange = 0;

        if (hat != null) {
            flatChange += hat.getStats().getFlatHealthChange();
            percentChange += hat.getStats().getPercentHealthChange();
        }
        if (accessory != null) {
            flatChange += accessory.getStats().getFlatHealthChange();
            percentChange += accessory.getStats().getPercentHealthChange();
        }
        if (clothes != null) {
            flatChange += clothes.getStats().getFlatHealthChange();
            percentChange += clothes.getStats().getPercentHealthChange();
        }
        if (shoes != null) {
            flatChange += shoes.getStats().getFlatHealthChange();
            percentChange += shoes.getStats().getPercentHealthChange();
        }
        if (weapon != null) {
            flatChange += weapon.getStats().getFlatHealthChange();
            percentChange += weapon.getStats().getPercentHealthChange();
        }

        // Add active effects
        for (Triple<TempStatusModifier, String, Boolean> effect: activeEffects) {
            flatChange += effect.getFirst().getFlatHealthChange();
            percentChange += effect.getFirst().getPercentHealthChange();
        }

        // Make sure stat can't be negative
        flatChange = max(-baseMaxHealth, flatChange);
        percentChange = max(-1, percentChange);

        double maxHealth = (baseMaxHealth + flatChange) * (1 + percentChange);
        return max(1, (int) maxHealth); // maxHealth can't be below 1
    }

    @Override
    public int getAttack() {
        int flatChange = 0;
        double percentChange = 0;

        if (hat != null) {
            flatChange += hat.getStats().getFlatAttackChange();
            percentChange += hat.getStats().getPercentAttackChange();
        }
        if (accessory != null) {
            flatChange += accessory.getStats().getFlatAttackChange();
            percentChange += accessory.getStats().getPercentAttackChange();
        }
        if (clothes != null) {
            flatChange += clothes.getStats().getFlatAttackChange();
            percentChange += clothes.getStats().getPercentAttackChange();
        }
        if (shoes != null) {
            flatChange += shoes.getStats().getFlatAttackChange();
            percentChange += shoes.getStats().getPercentAttackChange();
        }
        if (weapon != null) {
            flatChange += weapon.getStats().getFlatAttackChange();
            percentChange += weapon.getStats().getPercentAttackChange();

            if(hat != null && hat.getMagicEffect().equals(MagicEffect.WEAPON_BOOST)) {
                flatChange += (weapon.getStats().getFlatAttackChange() * 0.5);
            }

            if (accessory != null && accessory.getMagicEffect().equals(MagicEffect.WEAPON_BOOST)) {
                flatChange += (weapon.getStats().getFlatAttackChange() * 0.5);
            }
        }
        // Add active effects
        for (Triple<TempStatusModifier, String, Boolean> effect: activeEffects) {
            flatChange += effect.getFirst().getFlatAttackChange();
            percentChange += effect.getFirst().getPercentAttackChange();
        }

        // Make sure stat can't be negative
        flatChange = max(-baseAttack, flatChange);
        percentChange = max(-1, percentChange);

        double attack = (baseAttack + flatChange) * (1 + percentChange);
        return max(1, (int) attack); // Attack can't be below 1
    }


    @Override
    public int getDefense() {
        int flatChange = 0;
        double percentChange = 0;

        // Add flat wearable effects
        if (hat != null) {
            flatChange += hat.getStats().getFlatDefenseChange();
            percentChange += hat.getStats().getPercentDefenseChange();
        }
        if (accessory != null) {
            flatChange += accessory.getStats().getFlatDefenseChange();
            percentChange += accessory.getStats().getPercentDefenseChange();
        }
        if (clothes != null) {
            flatChange += clothes.getStats().getFlatDefenseChange();
            percentChange += clothes.getStats().getPercentDefenseChange();

            if(hat != null && hat.getMagicEffect().equals(MagicEffect.CLOTHES_BOOST)) {
                flatChange += clothes.getStats().getFlatDefenseChange();
            }
            if (accessory != null && accessory.getMagicEffect().equals(MagicEffect.CLOTHES_BOOST)) {
                flatChange += clothes.getStats().getFlatDefenseChange();
            }
        }
        if (shoes != null) {
            flatChange += shoes.getStats().getFlatDefenseChange();
            percentChange += shoes.getStats().getPercentDefenseChange();
        }
        if (weapon != null) {
            flatChange += weapon.getStats().getFlatDefenseChange();
            percentChange += weapon.getStats().getPercentDefenseChange();
        }

        // Add active effects
        for (Triple<TempStatusModifier, String, Boolean> effect: activeEffects) {
            flatChange += effect.getFirst().getFlatDefenseChange();
            percentChange += effect.getFirst().getPercentDefenseChange();
        }

        // Make sure stat can't be negative
        flatChange = max(-baseDefense, flatChange);
        percentChange = max(-1, percentChange);

        double defense = (baseDefense + flatChange) * (1 + percentChange);
        return (int) defense;
    }


    /**
     * @return index of first null inventory space, -1 if none
     */
    public int getFreeInventoryIndex() {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            Item item = inventory[i];
            if (item == null) {
                return i;
            }
        }
        return -1;
    }

}
