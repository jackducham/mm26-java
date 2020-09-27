package mech.mania.starter_pack.domain.model.characters;

import kotlin.Triple;
import mech.mania.engine.domain.model.CharacterProtos;
import mech.mania.engine.domain.model.ItemProtos;
import mech.mania.starter_pack.domain.model.items.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.max;

public class Player extends Character {
    public static final int INVENTORY_SIZE = 16;
    private final Hat hat;
    private final Accessory accessory;
    private final Clothes clothes;
    private final Shoes shoes;
    private final List<Item> inventory;

    public Player(final String name, final int baseSpeed, final int baseMaxHealth,
                   final int baseAttack, final int baseDefense, int currentHealth,
                   int experience, int ticksSinceDeath, boolean isDead, Position position,
                   Position spawnPoint, Weapon weapon, int activeEffectsTempStatusModifierCount,
                   TempStatusModifier[] activeEffectsTempStatusModifier,
                   String[] activeEffectsSource, boolean[] activeEffectsIsPlayer,
                   Map<String, Integer> taggedPlayersDamageMap,
                   final Hat hat, final Accessory accessory, final Clothes clothes,
                   final Shoes shoes, final List<Item> inventory) {
        super(name, baseSpeed, baseMaxHealth, baseAttack, baseDefense, currentHealth,
                experience, ticksSinceDeath, isDead, position, spawnPoint, weapon,
                activeEffectsTempStatusModifierCount, activeEffectsTempStatusModifier,
                activeEffectsSource, activeEffectsIsPlayer, taggedPlayersDamageMap);
        this.hat = hat;
        this.accessory = accessory;
        this.clothes = clothes;
        this.shoes = shoes;
        this.inventory = inventory;
    }


    public Player(CharacterProtos.Player playerProto) {
        super(playerProto.getCharacter());

        hat = new Hat(playerProto.getHat());
        accessory = new Accessory(playerProto.getAccessory());
        clothes = new Clothes(playerProto.getClothes());
        shoes = new Shoes(playerProto.getShoes());
        inventory = new ArrayList<>();

        for (int i = 0; i < playerProto.getInventoryCount(); i++) {
            ItemProtos.Item protoItem = playerProto.getInventory(i);
            switch(protoItem.getItemCase()) {
                case CLOTHES:
                    inventory.add(new Clothes(protoItem.getClothes()));
                    break;
                case HAT:
                    inventory.add(new Hat(protoItem.getHat()));
                    break;
                case ACCESSORY:
                    inventory.add(new Accessory(protoItem.getAccessory()));
                    break;
                case SHOES:
                    inventory.add(new Shoes(protoItem.getShoes()));
                    break;
                case WEAPON:
                    inventory.add(new Weapon(protoItem.getWeapon()));
                    break;
                case CONSUMABLE:
                    inventory.add(new Consumable(protoItem.getConsumable()));
                    break;
            }
        }
    }


    public Hat getHat() {
        return hat;
    }

    public Accessory getAccessory() {
        return accessory;
    }

    public Clothes getClothes() {
        return clothes;
    }

    public Shoes getShoes() {
        return shoes;
    }

    public int getMaxInventorySize() {
        return INVENTORY_SIZE;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * Checks if either hat or accessory has the chosen MagicEffect
     */
    public boolean hasMagicEffect(MagicEffect effect){
        return (hat != null && hat.getMagicEffect() == effect)
                || (accessory != null && accessory.getMagicEffect() == effect);
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
            if (hasMagicEffect(MagicEffect.SHOES_BOOST)) {
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
            if (hasMagicEffect(MagicEffect.WEAPON_BOOST)) {
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

            if(hasMagicEffect(MagicEffect.CLOTHES_BOOST)) {
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
     * @return index of first available inventory space, -1 if none
     */
    public int getFreeInventoryIndex() {
        if(inventory.size() < INVENTORY_SIZE){
            return inventory.size();
        }
        return -1;
    }

    /**
     * @return index of first filled inventory space, -1 if none
     */
    public int getFirstInventoryIndex() {
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            if (item != null) {
                return i;
            }
        }
        return -1;
    }

}
