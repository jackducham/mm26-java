package mech.mania.starter_pack.domain.model.characters;

import kotlin.Triple;
import mech.mania.engine.domain.model.CharacterProtos;
import mech.mania.engine.domain.model.ItemProtos;
import mech.mania.starter_pack.domain.model.items.*;

import static java.lang.Math.max;

public class Player extends Character {
    private static final int INVENTORY_SIZE = 16;
    private final Hat hat;
    private final Clothes clothes;
    private final Shoes shoes;
    private final Item[] inventory;
    private final Stats playerStats = new Stats();


    public Player(CharacterProtos.Player playerProto) {
        super(playerProto.getCharacter());

        hat = new Hat(playerProto.getHat());
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
                case SHOES:
                    inventory[i] = new Shoes(protoItem.getShoes());
                    break;
                case WEAPON:
                    inventory[i] = new Weapon(protoItem.getWeapon());
                    break;
                case CONSUMABLE:
                    inventory[i] = new Consumable(protoItem.getMaxStack(), protoItem.getConsumable());
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

        // Add flat wearable effects
        if (hat != null) {
            flatChange += hat.getStats().getFlatSpeedChange();
        }
        if (clothes != null) {
            flatChange += clothes.getStats().getFlatSpeedChange();
        }
        if (shoes != null) {
            flatChange += shoes.getStats().getFlatSpeedChange();
            if(hat != null && hat.getHatEffect().equals(HatEffect.SHOES_BOOST)) {
                flatChange += shoes.getStats().getFlatSpeedChange();
            }
        }
        if (weapon != null) {
            flatChange += weapon.getStats().getFlatSpeedChange();
        }

        // Add percent wearable effects
        if (hat != null) {
            percentChange += hat.getStats().getPercentSpeedChange();
        }
        if (clothes != null) {
            percentChange += clothes.getStats().getPercentSpeedChange();
        }
        if (shoes != null) {
            percentChange += shoes.getStats().getPercentSpeedChange();
        }
        if (weapon != null) {
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

        // Add flat wearable effects
        if (hat != null) {
            flatChange += hat.getStats().getFlatHealthChange();
        }
        if (clothes != null) {
            flatChange += clothes.getStats().getFlatHealthChange();
        }
        if (shoes != null) {
            flatChange += shoes.getStats().getFlatHealthChange();
        }
        if (weapon != null) {
            flatChange += weapon.getStats().getFlatHealthChange();
        }

        // Add percent wearable effects
        if (hat != null) {
            percentChange += hat.getStats().getPercentHealthChange();
        }
        if (clothes != null) {
            percentChange += clothes.getStats().getPercentHealthChange();
        }
        if (shoes != null) {
            percentChange += shoes.getStats().getPercentHealthChange();
        }
        if (weapon != null) {
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

        // Add flat wearable effects
        if (hat != null) {
            flatChange += hat.getStats().getFlatAttackChange();
        }
        if (clothes != null) {
            flatChange += clothes.getStats().getFlatAttackChange();
        }
        if (shoes != null) {
            flatChange += shoes.getStats().getFlatAttackChange();
        }
        if (weapon != null) {
            flatChange += weapon.getStats().getFlatAttackChange();
            if(hat != null && hat.getHatEffect().equals(HatEffect.WEAPON_BOOST)) {
                flatChange += (weapon.getStats().getFlatAttackChange() * 0.5);
            }
        }

        // Add percent wearable effects
        if (hat != null) {
            percentChange += hat.getStats().getPercentAttackChange();
        }
        if (clothes != null) {
            percentChange += clothes.getStats().getPercentAttackChange();
        }
        if (shoes != null) {
            percentChange += shoes.getStats().getPercentAttackChange();
        }
        if (weapon != null) {
            percentChange += weapon.getStats().getPercentAttackChange();
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
        }
        if (clothes != null) {
            flatChange += clothes.getStats().getFlatDefenseChange();
            if(hat != null && hat.getHatEffect().equals(HatEffect.CLOTHES_BOOST)) {
                flatChange += clothes.getStats().getFlatDefenseChange();
            }
        }
        if (shoes != null) {
            flatChange += shoes.getStats().getFlatDefenseChange();
        }
        if (weapon != null) {
            flatChange += weapon.getStats().getFlatDefenseChange();
        }

        // Add percent wearable effects
        if (hat != null) {
            percentChange += hat.getStats().getPercentDefenseChange();
        }
        if (clothes != null) {
            percentChange += clothes.getStats().getPercentDefenseChange();
        }
        if (shoes != null) {
            percentChange += shoes.getStats().getPercentDefenseChange();
        }
        if (weapon != null) {
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


    /**
     * Gets the stats object within this Player to update any extra stats.
     * @return a Stats object (Player.Stats)
     */
    public Stats getExtraStats() {
        return playerStats;
    }


    /**
     * Class of <b>extra</b> attributes that are required for infra's player
     * stat calculation
     */
    static class Stats {
        public final int monstersSlain;
        public final int deathCount;
        public final int turnsSinceJoined;

        public Stats() {
            monstersSlain = 0;
            deathCount = 0;
            turnsSinceJoined = 0;
        }

        public Stats(CharacterProtos.PlayerStats stats) {
            monstersSlain = stats.getMonstersSlain();
            deathCount = stats.getDeathCount();
            turnsSinceJoined = stats.getTurnsSinceJoined();
        }
    }

}
